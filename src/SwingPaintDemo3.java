import com.sun.codemodel.internal.*;

import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.Timer;

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
        JButton nextButton = new JButton("Reload Coordinates");
        JButton aboutButton = new JButton("About");
        final JButton timerButton = new JButton("Disable Automatic Reload");

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



        Timer timer = new Timer(50000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panel.updateSet();
                panel.repaint();
            }
        });
//        timer.start();

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        panel.updateSet();
                        panel.repaint();
                        return null;
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
                JOptionPane.showMessageDialog(frame, "En produkt av Martin Pettersson\nkontakt@martinpettersson.se");
            }
        });

        timerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.err.println("timer stopped");
            }
        });

        panel.add(nextButton);
        panel.add(aboutButton);
        panel.add(timerButton);
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
    private boolean update = false;

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
