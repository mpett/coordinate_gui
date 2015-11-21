import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Timer;

public class SwingPaintDemo3 {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        final MyPanel panel = new MyPanel();
        panel.updateSet();
        System.out.println("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
        JFrame frame = new JFrame("Digpro Recruitment Test - Martin Pettersson");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.add(panel);
        JButton nextButton = new JButton("NEXT");

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.updateSet();
                panel.repaint();
            }
        });

        panel.add(nextButton);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
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
    private CoordinateSet set;

    private ArrayList<Coordinate> coordinates;

    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    private void handleCoordinates() {
        xMin = Math.abs(set.getxMin());
        yMin = Math.abs(set.getyMin());
        xCoordinateScale = (double) SCREENWIDTH / (set.getxMax() + xMin);
        yCoordinateScale = (double) SCREENHEIGHT / (set.getyMax() + yMin);
        coordinates = set.getCoordinates();
    }

    public Dimension getPreferredSize() {
        return new Dimension(SCREENWIDTH + 300,SCREENHEIGHT + 100);
    }

    public void updateSet() {
        System.err.println("Updating coordinates");
        set = new CoordinateSet();
        System.err.println("Done");
    }

    public void paintComponent(Graphics g) {
        handleCoordinates();
        for (Coordinate coordinate : coordinates) {
            double xCoord = (coordinate.getxPosition() + xMin) * xCoordinateScale;
            double yCoord = (coordinate.getyPosition() + yMin) * yCoordinateScale;
            g.setColor(Color.RED);
            g.fillRect((int) xCoord,(int) yCoord,squareWidth,squareHeight);
            g.setColor(Color.BLACK);
            g.drawRect((int) xCoord,(int) yCoord, squareWidth, squareHeight);
        }
    }
}