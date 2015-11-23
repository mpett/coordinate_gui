import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

/**
 * CoordinateSet
 * Downloads coordinates from a web server and stores them in a coordinate data structure.
 *
 * @author Martin Pettersson
 */
public class CoordinateSet {
    private static final String WEB_SERVER_URL = "http://daily.digpro.se/bios/servlet/bios.servlets.web.RecruitmentTestServlet";
    private static final String CHARACTER_ENCODING = "ISO-8859-1";
    private ArrayList<Coordinate> coordinates;
    private int xMax, yMax, xMin, yMin;

    public CoordinateSet() {
        coordinates = processInput();
    }

    public ArrayList<Coordinate> getCoordinates() {
        return coordinates;
    }

    public int getxMax() {
        return xMax;
    }

    public int getyMax() {
        return yMax;
    }

    public int getxMin() {
        return xMin;
    }

    public int getyMin() {
        return yMin;
    }

    /**
     * Process the input string and stores it as coordinates.
     * @return List of coordinates
     */
    private ArrayList<Coordinate> processInput() {
        String input = getInputFromServer();
        String[] splitInput = input.split("\n");
        ArrayList<Coordinate> coordinates = new ArrayList<Coordinate>();
        ArrayList<Integer> xCoords = new ArrayList<Integer>();
        ArrayList<Integer> yCoords = new ArrayList<Integer>();

        for (String inputLine : splitInput) {
            String[] splitInputLine = inputLine.split(", ");
            int xCoordinate = Integer.parseInt(splitInputLine[0]);
            int yCoordinate = Integer.parseInt(splitInputLine[1]);
            String coordinateName = splitInputLine[2];
            Coordinate coordinate = new Coordinate(xCoordinate, yCoordinate, coordinateName);
            xCoords.add(xCoordinate);
            yCoords.add(yCoordinate);
            coordinates.add(coordinate);
        }

        Collections.sort(xCoords);
        Collections.sort(yCoords);
        xMax = xCoords.get(xCoords.size()-1);
        yMax = yCoords.get(yCoords.size()-1);
        xMin = xCoords.get(0);
        yMin = yCoords.get(0);
        return coordinates;
    }

    /**
     * Communicates with web server and returns an input string with the desired character encoding.
     * Removes all comments in the form of '#'.
     * @return Input String
     */
    private String getInputFromServer() {
        URL url;
        InputStream is = null;
        BufferedReader br;
        String line;
        String returnData = "";

        try {
            url = new URL(WEB_SERVER_URL);
            is = url.openStream();
            br = new BufferedReader(new InputStreamReader(is, CHARACTER_ENCODING));
            while ((line = br.readLine()) != null) {
                if (line.contains("#"))
                    continue;
                returnData += line + "\n";
            }
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException ioe) {
                 // Caught an IO exception
            }
        }
        return returnData;
    }
}

/**
 * Class for storing coordinates, takes x,y-positions and a name.
 */
class Coordinate {
    private int xPosition, yPosition;
    private String coordinateName;

    public Coordinate(int xPosition, int yPosition, String coordinateName) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.coordinateName = coordinateName;
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public String getCoordinateName() {
        return coordinateName;
    }

    public String toString() {
        return xPosition + " " + yPosition + " " + coordinateName;
    }
}
