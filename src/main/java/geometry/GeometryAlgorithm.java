package geometry;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.List;

public interface GeometryAlgorithm {
    String name();

    void draw(List<Point2D.Double> points, Graphics2D graphics);
}