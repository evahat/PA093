package geometry;

import geometry.algorithms.AlgorithmRegistry;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

public final class GeometryPlayground {
    private static final Color INK = new Color(35, 43, 40);
    private static final Color MUTED = new Color(104, 111, 106);
    private static final Color ACCENT = new Color(220, 104, 65);

    private final JLabel pointCount = new JLabel("0 points");
    private final GeometryCanvas canvas = new GeometryCanvas(new Runnable() {
        @Override
        public void run() {
            updatePointCount();
        }
    });

    private GeometryPlayground() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GeometryPlayground().show();
            }
        });
    }

    private void show() {
        JFrame frame = new JFrame("Geometry Playground");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(760, 560));
        frame.setLayout(new BorderLayout());
        frame.add(createHeader(), BorderLayout.NORTH);
        frame.add(createWorkspace(), BorderLayout.CENTER);
        frame.add(createFooter(), BorderLayout.SOUTH);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(INK);
        header.setBorder(new EmptyBorder(17, 22, 17, 22));

        JLabel title = new JLabel("Geometry Playground");
        title.setForeground(Color.WHITE);
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 21));
        JLabel subtitle = new JLabel("A workbench for 2D algorithms");
        subtitle.setForeground(new Color(196, 203, 197));
        subtitle.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));

        JPanel titles = new JPanel(new BorderLayout(0, 3));
        titles.setOpaque(false);
        titles.add(title, BorderLayout.NORTH);
        titles.add(subtitle, BorderLayout.SOUTH);
        header.add(titles, BorderLayout.WEST);

        pointCount.setForeground(Color.WHITE);
        pointCount.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13));
        header.add(pointCount, BorderLayout.EAST);
        return header;
    }

    private JPanel createWorkspace() {
        JPanel workspace = new JPanel(new BorderLayout(0, 12));
        workspace.setBackground(new Color(244, 243, 238));
        workspace.setBorder(new EmptyBorder(14, 18, 12, 18));
        workspace.add(createToolbar(), BorderLayout.NORTH);
        canvas.setBorder(BorderFactory.createLineBorder(new Color(214, 212, 204)));
        workspace.add(canvas, BorderLayout.CENTER);
        return workspace;
    }

    private JPanel createToolbar() {
        JPanel toolbar = new JPanel();
        toolbar.setBackground(new Color(244, 243, 238));
        toolbar.setLayout(new javax.swing.BoxLayout(toolbar, javax.swing.BoxLayout.X_AXIS));

        JLabel algorithmLabel = new JLabel("ALGORITHM");
        algorithmLabel.setForeground(MUTED);
        algorithmLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        algorithmLabel.setBorder(new EmptyBorder(0, 0, 0, 8));
        toolbar.add(algorithmLabel);

        final JComboBox<GeometryAlgorithm> algorithms = new JComboBox<GeometryAlgorithm>();
        List<GeometryAlgorithm> defaults = AlgorithmRegistry.createDefaults();
        for (GeometryAlgorithm algorithm : defaults) {
            algorithms.addItem(algorithm);
        }
        algorithms.setRenderer(new javax.swing.DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(javax.swing.JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof GeometryAlgorithm) {
                    setText(((GeometryAlgorithm) value).name());
                }
                return this;
            }
        });
        algorithms.addActionListener((ActionEvent event) -> canvas.setAlgorithm(
                (GeometryAlgorithm) algorithms.getSelectedItem()));
        algorithms.setMaximumSize(new Dimension(190, 32));
        toolbar.add(algorithms);
        toolbar.add(javax.swing.Box.createHorizontalStrut(18));

        JLabel randomLabel = new JLabel("POINTS");
        randomLabel.setForeground(MUTED);
        randomLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 10));
        randomLabel.setBorder(new EmptyBorder(0, 0, 0, 8));
        toolbar.add(randomLabel);

        final JSpinner count = new JSpinner(new SpinnerNumberModel(30, 0, 500, 5));
        count.setMaximumSize(new Dimension(72, 32));
        toolbar.add(count);

        JButton randomButton = new JButton("Random points");
        randomButton.addActionListener((ActionEvent event) ->
                canvas.generateRandomPoints(((Number) count.getValue()).intValue()));
        toolbar.add(javax.swing.Box.createHorizontalStrut(8));
        toolbar.add(randomButton);

        JButton clearButton = new JButton("Clear");
        clearButton.setForeground(ACCENT.darker());
        clearButton.addActionListener((ActionEvent event) -> canvas.clearPoints());
        toolbar.add(javax.swing.Box.createHorizontalStrut(6));
        toolbar.add(clearButton);
        toolbar.add(javax.swing.Box.createHorizontalGlue());
        return toolbar;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(new Color(244, 243, 238));
        footer.setBorder(new EmptyBorder(0, 20, 12, 20));
        JLabel controls = new JLabel("LEFT CLICK add / drag move     RIGHT CLICK delete");
        controls.setForeground(MUTED);
        controls.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        footer.add(controls, BorderLayout.WEST);
        JLabel coordinates = new JLabel("Cartesian coordinates", SwingConstants.RIGHT);
        coordinates.setForeground(MUTED);
        coordinates.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 11));
        footer.add(coordinates, BorderLayout.EAST);
        return footer;
    }

    private void updatePointCount() {
        int count = canvas.getPointCount();
        pointCount.setText(count + (count == 1 ? " point" : " points"));
    }
}