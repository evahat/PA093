package geometry.algorithms;

import geometry.GeometryAlgorithm;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public final class GiftWrappingAlgorithm implements GeometryAlgorithm {
    @Override
    public String name() {
        return "Gift wrapping (Jarvis march)";
    }

    @Override
    public void draw(List<Point2D.Double> points, Graphics2D graphics) {
        if (points.size() < 2) {
            return;
        }

        List<Point2D.Double> hull = findHull(points);
        graphics.setColor(new Color(46, 132, 111));
        graphics.setStroke(new BasicStroke(2.5f));
        for (int index = 0; index < hull.size(); index++) {
            Point2D.Double current = hull.get(index);
            Point2D.Double next = hull.get((index + 1) % hull.size());
            graphics.drawLine((int) Math.round(current.x), (int) Math.round(current.y),
                    (int) Math.round(next.x), (int) Math.round(next.y));
        }
    }

    private List<Point2D.Double> findHull(List<Point2D.Double> points) {
        List<Point2D.Double> hull = new ArrayList<Point2D.Double>();
        int startIndex = 0;
        for (int index = 1; index < points.size(); index++) {
            Point2D.Double candidate = points.get(index);
            Point2D.Double start = points.get(startIndex);
            if (candidate.x < start.x || (candidate.x == start.x && candidate.y < start.y)) {
                startIndex = index;
            }
        }

        int currentIndex = startIndex;
        do {
            Point2D.Double current = points.get(currentIndex);
            hull.add(current);
            int nextIndex = (currentIndex + 1) % points.size();
            for (int index = 0; index < points.size(); index++) {
                if (index == currentIndex) {
                    continue;
                }
                Point2D.Double next = points.get(nextIndex);
                Point2D.Double candidate = points.get(index);
                double turn = cross(current, next, candidate);
                if (turn < 0.0 || (turn == 0.0 && current.distanceSq(candidate) > current.distanceSq(next))) {
                    nextIndex = index;
                }
            }
            currentIndex = nextIndex;
        } while (currentIndex != startIndex && hull.size() <= points.size());
        return hull;
    }

    private double cross(Point2D.Double origin, Point2D.Double first, Point2D.Double second) {
        return (first.x - origin.x) * (second.y - origin.y)
                - (first.y - origin.y) * (second.x - origin.x);
    }
}