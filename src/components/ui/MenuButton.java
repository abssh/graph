package components.ui;

import styles.ButtonColors;
import styles.Palette;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class MenuButton extends JButton implements MouseListener {
    public static int selectedIndex = -1;
    static int counter = 0;
    final private int id;

    ButtonColors buttonColors;
    int diameter;
    Color currentColor;
    BufferedImage image;

    public MenuButton(int size, String toolTipText, BufferedImage image) {
        super();
        id = counter++;
        init();
        this.setToolTipText(toolTipText);
        this.image = image;
        this.diameter = size;
        this.setPreferredSize(new Dimension(diameter, diameter));
        this.setMaximumSize(new Dimension(diameter, diameter));
    }

    private void init() {
        this.setFocusable(false);
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.buttonColors = Palette.menuButtonUnSelected;
        this.currentColor = buttonColors.normal;

        this.addMouseListener(this);
    }

    private void secretCheck(){
        if (!isButtonSelected() && buttonColors == Palette.menuButtonSelected){
            sync();
            this.currentColor = buttonColors.normal;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        secretCheck();
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        int w = diameter;
        int h = diameter;

        g2.setColor(Palette.unSelectedVertex);

        g2.setColor(currentColor);
        g2.fillOval(0, 0, w, h);
        g2.drawImage(image, 23, 23, w-46, h-46, this);
        g2.dispose();
    }

    private boolean isButtonSelected(){
        return selectedIndex == this.id;
    }

    private void sync(){
        if (isButtonSelected()){
            this.buttonColors = Palette.menuButtonSelected;
        } else {
            this.buttonColors = Palette.menuButtonUnSelected;
        }
    }


    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        this.currentColor = buttonColors.pressed;
        if (isButtonSelected()){
            MenuButton.selectedIndex = -1;
        } else {
            MenuButton.selectedIndex = this.id;
        }
        sync();
        ((JPanel) this.getParent()).updateUI();

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        int x = mouseEvent.getX();
        int y = mouseEvent.getY();

        if (this.contains(x, y)){
            this.currentColor = buttonColors.hover;
        } else {
            this.currentColor = buttonColors.normal;
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
        this.currentColor = buttonColors.hover;
        this.getToolkit().sync();
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
        this.currentColor = buttonColors.normal;
        this.getToolkit().sync();
    }
}
