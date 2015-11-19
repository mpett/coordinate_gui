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
        JFrame frame = new JFrame("Demo Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800,600);
        frame.add(new MyPanel());
        frame.pack();
        frame.setVisible(true);
    }
}

class MyPanel extends JPanel {
    private final int SCREENWIDTH = 800;
    private final int SCREENHEIGHT = 600;

    private int squareWidth = 20;
    private int squareHeight = 20;
    private double xCoordinateScale;
    private double yCoordinateScale;

    private ArrayList<Coordinate> coordinates;
    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
        CoordinateSet set = new CoordinateSet();
        xCoordinateScale = (double) SCREENWIDTH / set.getxMax();
        yCoordinateScale = (double) SCREENHEIGHT / set.getyMax();
        System.err.println(yCoordinateScale);
        coordinates = set.getCoordinates();
    }

    public Dimension getPreferredSize() {
        return new Dimension(800,600);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Coordinate coordinate : coordinates) {
            double xCoord = coordinate.getxPosition() * xCoordinateScale;
            double yCoord = coordinate.getyPosition() * yCoordinateScale;
            System.err.println(xCoord + " " + yCoord);
            g.setColor(Color.RED);
            g.fillRect((int) xCoord,(int) yCoord,squareWidth,squareHeight);
            g.setColor(Color.BLACK);
            g.drawRect((int) xCoord, (int) yCoord, squareWidth, squareHeight);
        }
    }
}