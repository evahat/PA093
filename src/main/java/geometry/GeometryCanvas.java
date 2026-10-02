package geometry;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

public final class GeometryCanvas extends JPanel {
    private static final int GRID_SPACING = 40;
    private static final int POINT_RADIUS = 6;
    private static final double HIT_RADIUS = 13.0;

    private final List<Point2D.Double> points = new ArrayList<Point2D.Double>();
    private final Runnable onPointsChanged;
    private GeometryAlgorithm algorithm;
    private int draggedPoint = -1;

    public GeometryCanvas(Runnable onPointsChanged) {
        this.onPointsChanged = onPointsChanged;
        setBackground(new Color(250, 249, 245));
        setPreferredSize(new Dimension(900, 560));
        setMinimumSize(new Dimension(360, 280));
        setFocusable(true);

        MouseAdapter mouse = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                requestFocusInWindow();
                int hit = findPoint(event.getX(), event.getY());
                if (event.getButton() == MouseEvent.BUTTON3) {
                    if (hit >= 0) {
                        points.remove(hit);
                        onPointsChanged.run();
                        repaint();
                    }
                    return;
                }
                if (event.getButton() == MouseEvent.BUTTON1) {
                    draggedPoint = hit;
                    if (draggedPoint < 0) {
                        points.add(toWorld(event.getX(), event.getY()));
                        draggedPoint = points.size() - 1;
                        onPointsChanged.run();
                    }
                    repaint();
                }
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                if (draggedPoint >= 0) {
                    points.set(draggedPoint, toWorld(event.getX(), event.getY()));
                    onPointsChanged.run();
                    repaint();
                }
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                draggedPoint = -1;
            }
        };
        addMouseListener(mouse);
        addMouseMotionListener(mouse);
    }

    public int getPointCount() {
        return points.size();
    }

    public void setAlgorithm(GeometryAlgorithm algorithm) {
        this.algorithm = algorithm;
        repaint();
    }

    public void clearPoints() {
        points.clear();
        onPointsChanged.run();
        repaint();
    }

    public void generateRandomPoints(int count) {
        points.clear();
        Random random = new Random();
        int halfWidth = Math.max(1, getWidth() / 2 - 28);
        int halfHeight = Math.max(1, getHeight() / 2 - 28);
        for (int index = 0; index < count; index++) {
            double x = random.nextInt(halfWidth * 2 + 1) - halfWidth;
            double y = random.nextInt(halfHeight * 2 + 1) - halfHeight;
            points.add(new Point2D.Double(x, y));
        }
        onPointsChanged.run();
        repaint();
    }

    private Point2D.Double toWorld(int screenX, int screenY) {
        return new Point2D.Double(screenX - getWidth() / 2.0, getHeight() / 2.0 - screenY);
    }

    private int findPoint(int screenX, int screenY) {
        double closestDistance = HIT_RADIUS * HIT_RADIUS;
        int closestIndex = -1;
        for (int index = 0; index < points.size(); index++) {
            Point2D.Double point = points.get(index);
            double dx = screenX - (getWidth() / 2.0 + point.x);
            double dy = screenY - (getHeight() / 2.0 - point.y);
            double distance = dx * dx + dy * dy;
            if (distance <= closestDistance) {
                closestDistance = distance;
                closestIndex = index;
            }
        }
        return closestIndex;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        Graphics2D screen = (Graphics2D) graphics.create();
        screen.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(screen);

        Graphics2D world = (Graphics2D) screen.create();
        world.translate(getWidth() / 2.0, getHeight() / 2.0);
        world.scale(1.0, -1.0);
        if (algorithm != null) {
            List<Point2D.Double> snapshot = new ArrayList<Point2D.Double>();
            for (Point2D.Double point : points) {
                snapshot.add((Point2D.Double) point.clone());
            }
            algorithm.draw(Collections.unmodifiableList(snapshot), world);
        }
        drawPoints(world);
        world.dispose();
        drawCoordinateLabels(screen);
        screen.dispose();
    }

    private void drawGrid(Graphics2D graphics) {
        graphics.setColor(new Color(232, 230, 223));
        graphics.setStroke(new BasicStroke(1.0f));
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        for (int x = centerX % GRID_SPACING; x < getWidth(); x += GRID_SPACING) {
            graphics.drawLine(x, 0, x, getHeight());
        }
        for (int y = centerY % GRID_SPACING; y < getHeight(); y += GRID_SPACING) {
            graphics.drawLine(0, y, getWidth(), y);
        }
        graphics.setColor(new Color(90, 96, 93));
        graphics.setStroke(new BasicStroke(1.5f));
        graphics.drawLine(0, centerY, getWidth(), centerY);
        graphics.drawLine(centerX, 0, centerX, getHeight());
    }

    private void drawPoints(Graphics2D graphics) {
        for (Point2D.Double point : points) {
            int x = (int) Math.round(point.x);
            int y = (int) Math.round(point.y);
            graphics.setColor(new Color(220, 104, 65));
            graphics.fillOval(x - POINT_RADIUS, y - POINT_RADIUS, POINT_RADIUS * 2, POINT_RADIUS * 2);
            graphics.setColor(new Color(255, 255, 255));
            graphics.setStroke(new BasicStroke(1.5f));
            graphics.drawOval(x - POINT_RADIUS, y - POINT_RADIUS, POINT_RADIUS * 2, POINT_RADIUS * 2);
        }
    }

    private void drawCoordinateLabels(Graphics2D graphics) {
        graphics.setColor(new Color(104, 109, 105));
        graphics.drawString("0, 0", getWidth() / 2 + 7, getHeight() / 2 - 7);
        graphics.drawString("x", getWidth() - 16, getHeight() / 2 - 8);
        graphics.drawString("y", getWidth() / 2 + 8, 16);
    }
}