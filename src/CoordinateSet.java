import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by martinpettersson on 19/11/15.
 */
public class CoordinateSet {
    private ArrayList<Coordinate> coordinates;
    private int xMax, yMax;

    public CoordinateSet() {
        coordinates = handleInput();
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

    private ArrayList<Coordinate> handleInput() {
        String input = getInput();
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
        return coordinates;
    }

    private String getInput() {
        return "-226, 190, ST-971\n" +
                "71, 1202, ST-605\n" +
                "143, 1348, ST-984\n" +
                "657, 1283, ST-1435\n" +
                "195, 1072, STO-737\n" +
                "677, -178, ST-844\n" +
                "-142, 66, ST-1184\n" +
                "200, 1270, STO-1063\n" +
                "164, 414, ST-856\n" +
                "386, 1237, ST-1394\n" +
                "485, 1456, ST-1205\n" +
                "276, 998, ST-1016";
    }
}

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
