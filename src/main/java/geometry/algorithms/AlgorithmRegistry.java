package geometry.algorithms;

import geometry.GeometryAlgorithm;
import java.util.ArrayList;
import java.util.List;

public final class AlgorithmRegistry {
    private AlgorithmRegistry() {
    }

    public static List<GeometryAlgorithm> createDefaults() {
        List<GeometryAlgorithm> algorithms = new ArrayList<GeometryAlgorithm>();
        algorithms.add(new NoAlgorithm());
        algorithms.add(new GiftWrappingAlgorithm());
        return algorithms;
    }
}