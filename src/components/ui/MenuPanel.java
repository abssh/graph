package components.ui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import jmain.MainPanel;
import styles.Palette;

public class MenuPanel extends JPanel {
    BoxLayout layout;
    MainPanel mainPanel;

    public MenuPanel(MainPanel mainPanel) {
        this.mainPanel = mainPanel;
        this.layout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setPreferredSize(new Dimension(120, 390));
        this.setLayout(layout);
        createUI();
    }


    public void createUI(){

        // 2
        BufferedImage addVertexImage = null;
        try{
            addVertexImage = ImageIO.read(new File(Objects.requireNonNull(getClass().getResource("/add_vertex.png")).getFile()));
        } catch (IOException | NullPointerException ex) {
            addVertexImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }

        MenuButton addVertexButton = new MenuButton(80, "add vertex", addVertexImage);
        addVertexButton.addActionListener(e -> {
            if (mainPanel.currentMode == MainPanel.ADD_VERTEX_MODE){
                mainPanel.currentMode = MainPanel.NORMAL_MODE;
            } else {
                mainPanel.currentMode = MainPanel.ADD_VERTEX_MODE;
            }
        });
        this.add(addVertexButton);

        this.add(Box.createRigidArea(new Dimension(10, 20)));

        // 2
        BufferedImage addEdgeImage = null;
        try {
            addEdgeImage = ImageIO.read(new File(Objects.requireNonNull(getClass().getResource("/draw_edges.png")).getFile()));
        } catch (IOException | NullPointerException ex) {
            addEdgeImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }

        MenuButton addEdgeButton = new MenuButton(80, "add edge", addEdgeImage);
        addEdgeButton.addActionListener(e -> {
            if (mainPanel.currentMode == MainPanel.ADD_EDGE_MODE){
                mainPanel.currentMode = MainPanel.NORMAL_MODE;
            } else {
                mainPanel.currentMode = MainPanel.ADD_EDGE_MODE;
            }
        });
        this.add(addEdgeButton);

        this.add(Box.createRigidArea(new Dimension(10, 20)));

        //
        BufferedImage playImage = null;
        try {
            playImage = ImageIO.read(new File(Objects.requireNonNull(getClass().getResource("/play.png")).getFile()));
        } catch (IOException | NullPointerException ex) {
            playImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        MenuButton playButton = new MenuButton(80, "< play >", playImage);
        playButton.addActionListener(e -> {
            if (mainPanel.currentMode == MainPanel.PLAY_MODE){
                mainPanel.currentMode = MainPanel.NORMAL_MODE;
            } else {
                mainPanel.currentMode = MainPanel.PLAY_MODE;
            }
        });
        this.add(playButton);

        this.add(Box.createRigidArea(new Dimension(10, 20)));

        //
        BufferedImage resetImage = null;
        try {
            resetImage = ImageIO.read(new File(Objects.requireNonNull(getClass().getResource("/reset.png")).getFile()));
        } catch (IOException | NullPointerException ex) {
            resetImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        }
        MenuButton resetButton = new MenuButton(80, "< reset >", resetImage);
        resetButton.addActionListener(e -> {
            mainPanel.graph.reset();
            MenuButton.selectedIndex = -1;
        });
        this.add(resetButton);

        this.validate();
    }
}