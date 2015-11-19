import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;

public class SwingPaintDemo3 {
    public static void main(String[] args) {
        new CoordinateSet();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
        JFrame frame = new JFrame("Digpro Recruitment Test - Martin Pettersson");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900,700);
        frame.add(new MyPanel());
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo( null );
    }
}

class MyPanel extends JPanel {
    private final int SCREENWIDTH = 800;
    private final int SCREENHEIGHT = 600;

    private int squareWidth = 20;
    private int squareHeight = 20;
    private double xCoordinateScale;
    private double yCoordinateScale;
    private int xMin, yMin;

    private ArrayList<Coordinate> coordinates;
    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        CoordinateSet set = new CoordinateSet();
        xMin = Math.abs(set.getxMin());
        yMin = Math.abs(set.getyMin());
        xCoordinateScale = (double) SCREENWIDTH / (set.getxMax() + xMin);
        yCoordinateScale = (double) SCREENHEIGHT / (set.getyMax() + yMin);
        coordinates = set.getCoordinates();
    }

    public Dimension getPreferredSize() {
        return new Dimension(SCREENWIDTH + 300,SCREENHEIGHT + 100);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Coordinate coordinate : coordinates) {
            double xCoord = (coordinate.getxPosition() + xMin) * xCoordinateScale;
            double yCoord = (coordinate.getyPosition() + yMin) * yCoordinateScale;
            System.err.println(xCoord + " " + yCoord);
            g.setColor(Color.RED);
            g.fillRect((int) xCoord,(int) yCoord,squareWidth,squareHeight);
            g.setColor(Color.BLACK);
            g.drawRect((int) xCoord,(int) yCoord, squareWidth, squareHeight);
        }
    }
}