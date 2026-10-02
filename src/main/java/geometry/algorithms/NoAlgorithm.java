package geometry.algorithms;

import geometry.GeometryAlgorithm;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.List;

public final class NoAlgorithm implements GeometryAlgorithm {
    @Override
    public String name() {
        return "None";
    }

    @Override
    public void draw(List<Point2D.Double> points, Graphics2D graphics) {
    }
}