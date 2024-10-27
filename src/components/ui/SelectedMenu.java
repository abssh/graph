package components.ui;

import styles.Palette;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class SelectedMenu extends JPanel {

    public SelectedMenu(String title, ActionListener onDelete) {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setBorder(new BevelBorder(BevelBorder.RAISED, Palette.selectMenuBorder, Palette.selectMenuShadow));
        this.setBackground(Palette.clouds);
        this.setAlignmentX(CENTER_ALIGNMENT);

        // label
        JLabel titleLabel = new JLabel(title);
        titleLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, titleLabel.getPreferredSize().height));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        // titleLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Palette.selectMenuBorder, Palette.selectMenuShadow));

        // button
        JButton deleteButton = new JButton("Delete");
        deleteButton.setForeground(Palette.delete);
        deleteButton.setToolTipText("Delete selected item");
        deleteButton.setContentAreaFilled(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setFocusPainted(false);
        deleteButton.addActionListener(onDelete);

        this.add(titleLabel);
        this.add(deleteButton);
    }


}
