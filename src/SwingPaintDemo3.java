import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
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
        final JFrame frame = new JFrame("Recruitment Test");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.add(panel);
        JButton nextButton = new JButton("NEXT");
        JButton aboutButton = new JButton("ABOUT");
        final JTextArea ta = new JTextArea("Communicating with web server...");
        ta.setVisible(false);

        // status label begin
        final JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        frame.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        final JLabel statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        final JLabel updateLabel = new JLabel("update");
        // status label end

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                statusLabel.setText("Updated coordinates");
                statusLabel.repaint();
                panel.updateSet();
                panel.repaint();
            }
        });

        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "En produkt av Martin Pettersson\nkontakt@martinpettersson.se");
            }
        });



        panel.add(nextButton);
        panel.add(aboutButton);
        panel.add(ta);
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
            g.drawString(coordinate.getCoordinateName(),(int) xCoord, (int) yCoord);
            g.setColor(Color.BLACK);
            g.drawRect((int) xCoord,(int) yCoord, squareWidth, squareHeight);
        }

    }
}