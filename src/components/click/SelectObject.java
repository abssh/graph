package components.click;

import components.graph.Selectable;

import javax.swing.*;

public class SelectObject {

    public Selectable selectable;
    public boolean isSelected;
    public SelectObject() {
        selectable = null;
        isSelected = false;
    }


    public void toggleSelect(Selectable select) {
        if (select != null) {
            if (isSelected && selectable == select) {
                isSelected = false;
                selectable.unSelect();
                selectable = null;
            } else if (!isSelected) {
                isSelected = true;
                selectable = select;
                this.selectable.Select();
            } else if (isSelected && selectable != select) {
                selectable.unSelect();
                selectable = select;
                this.selectable.Select();
            }
        }
    }

    public void setUnSelect() {
        this.isSelected = false;
        if (selectable != null) {
            selectable.unSelect();
        }
    }


}
