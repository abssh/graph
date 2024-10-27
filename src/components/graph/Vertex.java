package components.graph;

import styles.Palette;

import java.awt.*;
import java.io.Serializable;


public class Vertex extends Rectangle implements Hoverable, Selectable {
    static private int counter = 0;

    static final public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    static final private short DRAW_DOWN = 0;
    static final private short DRAW_UP = 1;
    static final private short DRAW_LEFT = 2;
    static final private short DRAW_RIGHT = 3;

    public short drawVDirection;
    public short drawHDirection;

    private int id;
    public Color currentColor;
    public int diameter;
    public boolean drawBorder = false;
    public int cost;
    public String routes;

    public Vertex(int x, int y, int diameter, Color c) {
        super(x-(diameter/2)-2, y-(diameter/2)-2, diameter+4, diameter+4);
        this.diameter = diameter;
        currentColor = Palette.unSelectedVertex;
        id = Vertex.counter++;
        cost = Integer.MAX_VALUE;
        routes = "";
        setDrawVDirection();
        setDrawHDirection();
    }

    private void setDrawVDirection() {
        int cy = this.y + (this.diameter / 2) + 2;
        if (cy < screenSize.height/2) {
            this.drawVDirection = DRAW_DOWN;
        } else if (cy >= screenSize.height/2) {
            this.drawVDirection = DRAW_UP;
        }
    }

    private void setDrawHDirection() {
        int cx = this.x + (this.diameter / 2) + 2;
        if (cx < screenSize.width/2) {
            this.drawHDirection = DRAW_RIGHT;
        } else if (cx >= screenSize.width/2) {
            this.drawHDirection = DRAW_LEFT;
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "V" + id;
    }

    public int getCost(Vertex v){
        return v.cost;
    }

    public Point getCenter(){
        return new Point(x+(diameter/2)+2 , y+(diameter/2)+2);
    }

    public void drawVertex(Graphics2D g) {
        if (drawBorder) {
            g.setColor(Palette.unSelectedEdge);
            g.fillOval(x, y, diameter, diameter);
        }
        g.setColor(currentColor);
        g.fillOval(x + 2 , y + 2, diameter - 4,  diameter - 4);

        g.setFont(new Font("Quicksand", Font.BOLD, 20));
        g.setColor(Palette.graphText);
        FontMetrics fontMetrics = g.getFontMetrics();
        String cost = this.cost == Integer.MAX_VALUE? "∞" : String.valueOf(this.cost);
        String title = this.getName() +"| " + this.routes + ": " + cost;
        int x2 = 0, y2 = 0;

        int padding = 15;

        if (drawVDirection == DRAW_DOWN) {
            if (drawHDirection == DRAW_RIGHT) {
                x2 = x + diameter + padding;
                y2 = y + diameter + padding + fontMetrics.getHeight() - fontMetrics.getAscent();
            }
            if (drawHDirection == DRAW_LEFT) {
                x2 = x - padding - fontMetrics.stringWidth(title);
                y2 = y + diameter + padding + fontMetrics.getHeight() - fontMetrics.getAscent();
            }

        } else if (drawVDirection == DRAW_UP) {
            if (drawHDirection == DRAW_RIGHT) {
                x2 = x + diameter + padding;
                y2 = y + fontMetrics.getDescent() - padding;
            }
            if (drawHDirection == DRAW_LEFT) {
                x2 = x - fontMetrics.stringWidth(title) - padding;
                y2 = y + fontMetrics.getDescent() - padding;
            }
        }

        g.drawString(title, x2, y2);
    }

    public void setAsStart(){
        this.currentColor = Palette.startVertex;
        this.cost = 0;
        this.routes = this.getName();
    }

    public void setAsSelected(){
        this.currentColor = Palette.selectedVertex;
    }
    public void setAsUnSelected(){
        this.currentColor = Palette.unSelectedVertex;
    }

    public void reset(){
        cost = Integer.MAX_VALUE;
        routes = "";
    }

    @Override
    public boolean doHover(int x, int y) {
        return this.contains(x, y);
    }

    @Override
    public void hover() {
        drawBorder = true;
    }

    @Override
    public void unHover() {
        drawBorder = false;
    }

    @Override
    public void Select() {
        drawBorder = true;
    }

    @Override
    public void unSelect() {
        drawBorder = false;
    }
}
