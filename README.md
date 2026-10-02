# Geometry Playground

A small Java Swing workbench for experimenting with 2D geometry algorithms. It uses only the Java standard library and keeps algorithm drawing separate from the interactive point canvas.

## Requirements

- JDK 9 or newer to build with the commands below
- Java 8 or newer to run

## Run

From the project folder in PowerShell:

```powershell
$sources = Get-ChildItem src\main\java -Recurse -Filter *.java | ForEach-Object FullName
javac --release 8 -d out $sources
java -cp out geometry.GeometryPlayground
```

## Controls

- Left-click an empty area to add a point.
- Left-click and drag a point to move it.
- Right-click near a point to delete it.
- Use **Random points** to replace the scene with a random set.
- Use **Clear** to remove every point.
- Choose an algorithm to draw its result over the points.

Coordinates passed to algorithms use a Cartesian system: the origin is at the center of the canvas, positive x points right, and positive y points up. Distances are in canvas pixels.

## Add an algorithm

1. Add a class that implements `GeometryAlgorithm` and draw its result in `draw`.
2. Add an instance to `AlgorithmRegistry.createDefaults()`.

The canvas passes algorithms an unmodifiable snapshot of the current points. Algorithm graphics use the same Cartesian coordinate system as the points. See `GiftWrappingAlgorithm` for a complete example.