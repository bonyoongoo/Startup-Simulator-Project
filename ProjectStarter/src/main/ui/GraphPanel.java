package ui;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class GraphPanel extends JPanel {
    private List<Integer> userBaseHistory;
    private int graphWidth;
    private int graphHeight;
    private int maxUsers;
    private int minUsers;
    private int userRange;

    // EFFECTS: initializes graph
    public GraphPanel(List<Integer> userBaseHistory) {
        this.userBaseHistory = userBaseHistory;
        setPreferredSize(new Dimension(300, 300));
        setBorder(BorderFactory.createTitledBorder("User Growth Graph"));
    }

    // EFFECTS: updates data for graph
    public void updateData(List<Integer> newData) {
        this.userBaseHistory = newData;
        repaint();
    }

    // EFFECTS: draws user base growth line chart
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        if (shouldShowEmptyGraphMessage()) {
            g.drawString("Complete at least one feature to see the graph", 10, 20);
            return;
        }

        drawGraph(g);
    }

    // EFFECTS: determines if empty graph message should be shown
    private boolean shouldShowEmptyGraphMessage() {
        return userBaseHistory == null || userBaseHistory.size() < 2;
    }

    // MODIFIES: this
    // EFFECTS: draws all the graph components
    private void drawGraph(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        int padding = 50;

        setupGraphDimensions(width, height, padding);
        drawGraphComponents(g, width, height, padding);
    }

    // EFFECTS: sets graph dimensions and user metrics
    private void setupGraphDimensions(int width, int height, int padding) {
        graphWidth = width - 2 * padding;
        graphHeight = height - 2 * padding;
        maxUsers = Collections.max(userBaseHistory);
        minUsers = Collections.min(userBaseHistory);
        userRange = Math.max(maxUsers - minUsers, 1);
    }

    // EFFECTS: draws axes, graph lines, and labels
    private void drawGraphComponents(Graphics g, int width, int height, int padding) {
        Graphics2D g2d = (Graphics2D) g;

        drawAxes(g, width, height, padding);
        drawGraphLine(g2d, height, padding);
        drawGraphLabels(g, width, height, padding);
    }

    // EFFECTS: draws two lines representing graph axes
    private void drawAxes(Graphics g, int width, int height, int padding) {
        g.drawLine(padding, height - padding, width - padding, height - padding);
        g.drawLine(padding, height - padding, padding, padding);
    }

    // EFFECTS: draws connected line segments with data points
    private void drawGraphLine(Graphics2D g2d, int height, int padding) {
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.BLUE);

        int pointCount = userBaseHistory.size();
        int prevX = padding;
        int prevY = calculateY(height, padding, userBaseHistory.get(0));

        for (int i = 1; i < pointCount; i++) {
            int x = calculateX(padding, i, pointCount);
            int y = calculateY(height, padding, userBaseHistory.get(i));

            drawLineSegment(g2d, prevX, prevY, x, y);
            drawDataPoint(g2d, x, y);

            if (shouldLabelPoint(i, pointCount)) {
                drawValueLabel(g2d, x, y, userBaseHistory.get(i));
            }

            prevX = x;
            prevY = y;
        }
    }

    // EFFECTS: returns calculated x coordinate
    private int calculateX(int padding, int index, int pointCount) {
        return padding + (index * graphWidth) / (pointCount - 1);
    }

    // EFFECTS: returns calculated y coordinate
    private int calculateY(int height, int padding, int value) {
        return height - padding - (value - minUsers) * graphHeight / userRange;
    }

    // EFFECTS: draws a single line segment between two points
    private void drawLineSegment(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        g2d.drawLine(x1, y1, x2, y2);
    }

    // EFFECTS: draws a filled oval representing data point
    private void drawDataPoint(Graphics2D g2d, int x, int y) {
        g2d.fillOval(x - 3, y - 3, 6, 6);
    }

    // EFFECTS: returns true if point meets labeling criteria
    private boolean shouldLabelPoint(int index, int pointCount) {
        return index % Math.max(1, pointCount / 10) == 0 || index == pointCount - 1;
    }

    // EFFECTS: draws text representation of the value
    private void drawValueLabel(Graphics2D g2d, int x, int y, int value) {
        g2d.setColor(Color.RED);
        g2d.drawString(String.valueOf(value), x - 10, y - 10);
        g2d.setColor(Color.BLUE);
    }

    // EFFECTS: draws multiple text labels
    private void drawGraphLabels(Graphics g, int width, int height, int padding) {
        g.setColor(Color.BLACK);
        g.drawString("User Growth Over Time", 10, 25);
        g.drawString("Time →", width - padding, height - padding + 20);
        g.drawString("Users ↑", padding - 40, padding - 10);
        g.drawString(String.valueOf(minUsers), padding - 30, height - padding + 5);
        g.drawString(String.valueOf(maxUsers), padding - 30, padding + 5);
    }

}
