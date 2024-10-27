package jmain;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainFrame {
    private JFrame frame;
    static Dimension screenSize;

    static {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    }

    private MainPanel mainPanel;

    public MainFrame() throws IOException {
        frameInit();
        mainPanel = new MainPanel();
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.validate();
    }

    private void frameInit() {
        frame = new JFrame();
        frame.setSize(screenSize);
        frame.setTitle("Graph Viewer");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
