import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * RecruitmentCoordinateApplication
 * Builds a Graphical User Interface that displays coordinates, buttons and a status panel.
 *
 * @author Martin Pettersson
 */
public class RecruitmentCoordinateApplication {
    private final static int RELOAD_TIME = 30000;
    private final static int SCREEN_WIDTH = 800;
    private final static int SCREEN_HEIGHT = 600;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    /**
     * Create and display all GUI elements
     */
    private static void createAndShowGUI() {
        // Create a coordinate panel and a main frame
        final CoordinatePanel coordinatePanel = new CoordinatePanel();
        final JFrame frame = new JFrame("Recruitment Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        frame.add(coordinatePanel);

        // Create buttons
        JButton reloadButton = new JButton("Reload Coordinates");
        JButton aboutButton = new JButton("About");
        final JButton timerButton = new JButton("Disable Automatic Reload");

        // Create button panel at the top of the frame
        final JPanel buttonPanel = new JPanel();
        frame.add(buttonPanel, BorderLayout.NORTH);
        buttonPanel.setPreferredSize(new Dimension(frame.getWidth(), 40));

        // Create status panel and label at the bottom of the frame
        final JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        frame.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        final JLabel statusLabel = new JLabel("Status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);

        // Initialize automatic reload timer on a separate thread
        final Timer timer = new Timer(RELOAD_TIME, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        statusLabel.setText("Communicating with web server... (automatic reload)");
                        coordinatePanel.updateSet();
                        frame.revalidate();
                        frame.repaint();
                        return true;
                    }
                    protected void done() {
                        statusLabel.setText("Updated coordinates automatically at " + LocalDateTime.now());
                    }
                };
                worker.execute();
            }
        });

        // Listener for automatic reload timer button
        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        statusLabel.setText("Communicating with web server...");
                        coordinatePanel.updateSet();
                        frame.revalidate();
                        frame.repaint();
                        return true;
                    }
                    protected void done() {
                        statusLabel.setText("Updated coordinates at " + LocalDateTime.now());
                    }
                };
                worker.execute();
            }
        });

        // Listener for about button
        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Created by Martin Pettersson\nkontakt@martinpettersson.se");
            }
        });

        // Listener for disable automatic reload button
        timerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                statusLabel.setText("Automatic reload disabled");
            }
        });

        // Add buttons to button panel
        buttonPanel.add(reloadButton);
        buttonPanel.add(timerButton);
        buttonPanel.add(aboutButton);

        // Set frame visibility, update coordinates (on startup) and start automatic reload timer
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        coordinatePanel.updateSet();
        timer.start();
    }
}

/**
 * JPanel which downloads coordinates through a CoordinateSet and draws them as rectangles
 * along with their name
 */
class CoordinatePanel extends JPanel {
    private final int SCREENWIDTH = 800;
    private final int SCREENHEIGHT = 600;
    private int squareWidth = 20;
    private int squareHeight = 20;
    private double xCoordinateScale, yCoordinateScale;
    private int xMin, yMin;
    private CoordinateSet set;
    private ArrayList<Coordinate> coordinates;

    public CoordinatePanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));
    }

    /**
     * Scale coordinates so they fit the panel properly
     */
    private void scaleCoordinates() {
        coordinates = set.getCoordinates();
        xMin = Math.abs(set.getxMin());
        yMin = Math.abs(set.getyMin());
        xCoordinateScale = ((double) SCREENWIDTH - 60) / (set.getxMax() + xMin);
        yCoordinateScale = ((double) SCREENHEIGHT - 115) / (set.getyMax() + yMin);
    }

    /**
     * Download a new set of coordinates
     */
    public void updateSet() {
        set = new CoordinateSet();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(SCREENWIDTH,SCREENHEIGHT);
    }

    @Override
    public void paintComponent(Graphics g) {
        scaleCoordinates();
        for (Coordinate coordinate : coordinates) {
            double xCoord = (coordinate.getxPosition() + xMin) * xCoordinateScale;
            double yCoord = (coordinate.getyPosition() + yMin) * yCoordinateScale;
            g.setColor(Color.DARK_GRAY);
            g.fillRect((int) xCoord,(int) yCoord,squareWidth,squareHeight);
            g.drawString(coordinate.getCoordinateName(),(int) xCoord, (int) yCoord + squareHeight + 15);
            g.setColor(Color.BLACK);
            g.drawRect((int) xCoord,(int) yCoord, squareWidth, squareHeight);
        }
    }
}