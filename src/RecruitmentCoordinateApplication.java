import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.Timer;

public class RecruitmentCoordinateApplication {
    private final static int RELOAD_TIME = 30000;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        final CoordinatePanel coordinatePanel = new CoordinatePanel();



        //System.out.println("Created GUI on EDT? " + SwingUtilities.isEventDispatchThread());
        final JFrame frame = new JFrame("Recruitment Test");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        JButton reloadButton = new JButton("Reload Coordinates");
        JButton aboutButton = new JButton("About");
        final JButton timerButton = new JButton("Disable Automatic Reload");
        frame.add(coordinatePanel);

        // Button Panel
        final JPanel buttonPanel = new JPanel();
        frame.add(buttonPanel, BorderLayout.NORTH);
        buttonPanel.setPreferredSize(new Dimension(frame.getWidth(), 40));

        // status label begin
        final JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        frame.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 16));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        final JLabel statusLabel = new JLabel("status");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        // status label end

        final Timer timer = new Timer(RELOAD_TIME, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        statusLabel.setText("Communicating with web server... (automatic reload)");
                        coordinatePanel.updateSet();
                        coordinatePanel.repaint();
                        return true;
                    }

                    protected void done() {
                        statusLabel.setText("Updated coordinates automatically at " + LocalDateTime.now());
                    }
                };
                worker.execute();
            }
        });

        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
                    @Override
                    protected Boolean doInBackground() throws Exception {
                        statusLabel.setText("Communicating with web server...");
                        coordinatePanel.updateSet();
                        coordinatePanel.repaint();
                        return true;
                    }
                    protected void done() {
                        statusLabel.setText("Updated coordinates at " + LocalDateTime.now());
                    }
                };
                worker.execute();
            }
        });

        aboutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Created by Martin Pettersson\nkontakt@martinpettersson.se");
            }
        });

        timerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop();
                statusLabel.setText("Automatic reload disabled");
            }
        });


        buttonPanel.add(reloadButton);
        buttonPanel.add(timerButton);
        buttonPanel.add(aboutButton);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        coordinatePanel.updateSet();
        timer.start();

    }
}

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

    private void handleCoordinates() {
        xMin = Math.abs(set.getxMin());
        yMin = Math.abs(set.getyMin());
        xCoordinateScale = ((double) SCREENWIDTH - 60) / (set.getxMax() + xMin);
        yCoordinateScale = ((double) SCREENHEIGHT - 115) / (set.getyMax() + yMin);
        coordinates = set.getCoordinates();
    }

    public Dimension getPreferredSize() {
        return new Dimension(SCREENWIDTH,SCREENHEIGHT);
    }

    public void updateSet() {
        set = new CoordinateSet();
    }

    public void paintComponent(Graphics g) {
        handleCoordinates();
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
