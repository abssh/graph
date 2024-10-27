package components.graph;

import styles.Palette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Edge extends Rectangle implements Selectable{
    static final public Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    static final private short DRAW_DOWN = 0;
    static final private short DRAW_UP = 1;
    static final private short DRAW_LEFT = 2;
    static final private short DRAW_RIGHT = 3;

    private static int counter = 0;
    private final int id;

    private int strokeSize;
    public Vertex v1;
    public Vertex v2;
    public int weight;
    public short drawVDirection;
    public short drawHDirection;
    public Color color;


    public Edge(Vertex v1, Vertex v2) {
        super(Math.min(v1.getCenter().x, v2.getCenter().x), Math.min(v1.getCenter().y, v2.getCenter().y),
                Math.abs(v1.getCenter().x - v2.getCenter().x), Math.abs(v1.getCenter().y - v2.getCenter().y));
        this.id = counter++;
        this.weight = 0;
        this.v1 = v1;
        this.v2 = v2;
        this.strokeSize = 8;
        this.color = Palette.unSelectedEdge;

        setDrawVDirection();
        setDrawHDirection();
    }

    private void setDrawVDirection() {
        int cy = this.y + height / 2;
        if (cy < screenSize.height/2) {
            this.drawVDirection = DRAW_DOWN;
        } else if (cy >= screenSize.height/2) {
            this.drawVDirection = DRAW_UP;
        }
    }

    private void setDrawHDirection() {
        int cx = this.x + width / 2;
        if (cx < screenSize.width/2) {
            this.drawHDirection = DRAW_RIGHT;
        } else if (cx >= screenSize.width/2) {
            this.drawHDirection = DRAW_LEFT;
        }
    }

    public void draw(Graphics2D g) {
        Point p1 = v1.getCenter();
        Point p2 = v2.getCenter();
        g.setColor(this.color);
        g.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        Point vec = new Point(p2.x - p1.x, p2.y - p1.y);
        double vecLength = Math.sqrt((vec.x * vec.x) + (vec.y * vec.y));
        double d1 = (vecLength / 2 - 25) / vecLength;
        double d2 = (vecLength / 2 + 25)/ vecLength;

        Point vec1 = new Point((int) (p1.x + vec.x * (d1)), (int) (p1.y + vec.y * (d1)));
        Point vec2 = new Point((int) (p1.x + vec.x * (d2)), (int) (p1.y + vec.y * (d2)));

        g.drawLine(p1.x, p1.y, vec1.x, vec1.y);

        g.drawLine(vec2.x, vec2.y, p2.x, p2.y);



        int cx = this.x + width/2;
        int cy = this.y + height/2;
        g.setFont(new Font("QuickSand", Font.BOLD, 25));
        FontMetrics fontMetrics = g.getFontMetrics();
        g.setColor(Palette.graphText);

        if (drawVDirection == DRAW_DOWN) {
            g.drawString(String.valueOf(this.weight), cx - (fontMetrics.stringWidth(String.valueOf(this.weight))/2),
                    cy + fontMetrics.getHeight() - fontMetrics.getAscent());
        }
        else if (drawVDirection == DRAW_UP){
            g.drawString(String.valueOf(this.weight), cx - (fontMetrics.stringWidth(String.valueOf(this.weight))/2),
                    cy - fontMetrics.getHeight() + fontMetrics.getAscent() + fontMetrics.getDescent() * 2);
        }
    }

    private double slope() {
        return (double) height / (double) width;
    }

    public double delta(int x, int y) {
        Point p1 = v1.getCenter();
        double c = p1.y - slope() * p1.x;

        return Math.abs((slope() * x) - y + c) / Math.sqrt(1 + slope() * slope());
    }

    public boolean edgeContains(int x, int y) {
        if (this.contains(x, y)) {
            return this.delta(x, y) < 20;
        }
        return false;
    }

    public String getName() {
        return "E"+this.id;
    }

    public Vertex other(Vertex v) {
        if (this.v1.getId() == v.getId()) {
            return this.v2;
        } else if (this.v2.getId() == v.getId()) {
            return this.v1;
        }
        return null;
    }

    public void setAsSelected(){
        this.color = Palette.selectedEdge;
    }
    public void setAsUnSelected(){
        this.color = Palette.unSelectedEdge;
    }

    public void askWeight(){
        JFrame frame = new JFrame("Edge Weight");
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel cPanel = new JPanel();
        JPanel sPanel = new JPanel();
        frame.add(cPanel, BorderLayout.CENTER);
        frame.add(sPanel, BorderLayout.SOUTH);

        cPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 20));
        sPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        cPanel.add(new JLabel());
        cPanel.add(new JLabel("weight"));

        JTextField nameField = new JTextField(10);
        nameField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.getKeyCode()==10) {
                    try {
                        weight = Integer.parseInt(nameField.getText());
                        frame.dispose();
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        nameField.setText(String.valueOf(this.weight));
        cPanel.add(nameField);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            try {
                this.weight = Integer.parseInt(nameField.getText());
                frame.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> frame.dispose());
        sPanel.add(okButton);
        sPanel.add(cancelButton);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void Select() {
        this.strokeSize = 7;
    }

    @Override
    public void unSelect() {
        this.strokeSize = 5;
    }
}
