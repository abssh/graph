package jmain;

import components.click.DragObject;
import components.click.SelectObject;
import components.graph.Hoverable;
import components.ui.MenuButton;
import components.ui.MenuPanel;
import components.graph.Vertex;
import components.graph.Edge;
import components.graph.GraphHandler;

import components.ui.SelectedMenu;
import styles.Palette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainPanel extends JPanel implements Runnable, MouseListener, MouseMotionListener {
    public static final short NORMAL_MODE;
    public static final short ADD_VERTEX_MODE;
    public static final short ADD_EDGE_MODE;
    public static final short PLAY_MODE;

    static {
        NORMAL_MODE = 0;
        ADD_VERTEX_MODE = 1;
        ADD_EDGE_MODE = 2;
        PLAY_MODE = 3;
    }

    public GraphHandler graph;
    public short currentMode;
    public DragObject dragObject;
    public SelectObject selectObject;
    public SelectedMenu selectedMenu;

    MainPanel() {
        init();
        Thread thread = new Thread(this);
        thread.start();
        graph = new GraphHandler();
        currentMode = NORMAL_MODE;
        dragObject = new DragObject();
        selectObject = new SelectObject();
    }

    private void init() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 10));
        this.setBackground(Palette.emptyBackground);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        MenuPanel menuPanel = new MenuPanel(this);
        this.add(menuPanel);
        this.validate();
    }

    private void removeSelectedMenu(){
        if (selectedMenu != null) {
            this.remove(this.selectedMenu);
            selectedMenu = null;
        }
    }

    private void draw(Graphics2D g2){
        if (dragObject.isDragging && dragObject.dragEnd != null){
            g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.setColor(Palette.guidEdge);
            g2.drawLine(dragObject.dragStart.x, dragObject.dragStart.y, dragObject.dragEnd.x, dragObject.dragEnd.y);
        }
        graph.draw(g2);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Image image = createImage(this.getWidth(), this.getHeight());
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        draw(g2);
        g.drawImage(image, 0, 0, this);
        g2.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        Point point = mouseEvent.getPoint();
        if (PLAY_MODE == currentMode) {
            int index = graph.findIndexWithPoint(point.x, point.y);
            if (index != -1) {
                graph.startIndex = index;
                this.currentMode = NORMAL_MODE;
                MenuButton.selectedIndex = -1;
                graph.getVertex(index).setAsStart();
                graph.animate();
            }
            return;
        }

        if (NORMAL_MODE == currentMode) {
            Vertex v = graph.findVertexWithPoint(point.x, point.y);
            Edge e = graph.findEdgeWithPoint(point.x, point.y);
            this.removeSelectedMenu();
            if (v != null) {
                selectObject.toggleSelect(v);
                if (selectObject.isSelected) {
                    this.selectedMenu = new SelectedMenu(v.getName(),
                            (ActionListener) actionEvent -> {
                                graph.deleteVertex(v);
                                this.remove(this.selectedMenu);
                                this.selectedMenu = null;
                            });
                    this.add(selectedMenu);
                }
            } else if (e != null) {
               selectObject.toggleSelect(e);
               if (selectObject.isSelected) {
                    this.selectedMenu = new SelectedMenu(e.getName(),
                            (ActionListener) actionEvent -> {
                                graph.deleteEdge(e);
                                this.remove(this.selectedMenu);
                                this.selectedMenu = null;
                            });
                    this.add(selectedMenu);
               }
            } else {
                selectObject.setUnSelect();
            }
            return;
        }

        if (ADD_VERTEX_MODE == currentMode) {
            if (graph.findVertexWithPoint(point.x, point.y) == null) {
                graph.addVertex(point.x, point.y);
            }
            return;
        }
        if (ADD_EDGE_MODE == currentMode) {
            return;
        }

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        Point lastPress = mouseEvent.getPoint();
        if (ADD_EDGE_MODE == currentMode) {
            Vertex v = graph.findVertexWithPoint(lastPress.x, lastPress.y);
            if (v != null) {
                dragObject.setStartObject(v);
                dragObject.setStartPoint(lastPress);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        Point point = mouseEvent.getPoint();
        if (ADD_EDGE_MODE == currentMode) {
            dragObject.end();
            Hoverable start = dragObject.startObject;
            Hoverable end = dragObject.endObject;
            if (start != null && end != null) {
                Vertex sv = (Vertex) start;
                Vertex ev = (Vertex) end;
                if (sv.getId() != ev.getId()) {
                    if (graph.getEdge(sv, ev) == null){
                        graph.addEdge(sv, ev);
                    }
                }
            }
        }

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        Point point = mouseEvent.getPoint();
        if (ADD_EDGE_MODE == currentMode) {
            Vertex v = graph.findVertexWithPoint(point.x, point.y);
            if (v != null) {
                dragObject.setEndObject(v);
            } else {
                dragObject.unSetEndObject();
            }
            dragObject.setEndPoint(point);
        }
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        Point point = mouseEvent.getPoint();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 30.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        while(true){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1){
                updateUI();
                delta = 0;
            }
        }
    }
}
