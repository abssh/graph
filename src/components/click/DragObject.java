package components.click;

import components.graph.Hoverable;

import java.awt.*;

public class DragObject {

    public boolean isDragging;
    public Point dragStart;
    public Point dragEnd;
    public Hoverable startObject;
    public Hoverable endObject;

    public DragObject() {
        isDragging = false;
        dragStart = null;
        dragEnd = null;
        startObject = null;
        endObject = null;
    }

    public void setStartObject(Hoverable startObject) {
        this.startObject = startObject;
    }

    public void setEndObject(Hoverable endObject) {
        this.endObject = endObject;
        if (endObject != null) {
            this.endObject.hover();
        }
    }
    public void unSetEndObject() {
        if (endObject != null) {
            endObject.unHover();
            endObject = null;
        }
    }

    public void setStartPoint(Point startPoint) {
        isDragging = true;
        dragEnd = null;
        dragStart = startPoint;
        if (startObject != null) {
            startObject.hover();
        }


    }

    public void setEndPoint(Point endPoint) {
        if (isDragging) {
            dragEnd = endPoint;
        }

    }

    public void end() {
        isDragging = false;
        if (endObject != null) {
            endObject.unHover();
        }
        if (startObject != null) {
            startObject.unHover();
        }
    }

}
