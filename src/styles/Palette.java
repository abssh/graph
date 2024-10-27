package styles;

import java.awt.*;


public class Palette {
    public  static final Color clouds = new Color(236, 240, 241);

    public static final Color emptyBackground = new Color(248, 248, 255);
    public static final Color unSelectedVertex = new Color(0, 0, 46);
    public static final Color unSelectedEdge = new Color(0, 0, 148, 255);
    public static final Color selectedVertex = new Color(151, 219, 104);
    public static final Color selectedEdge = new Color(44, 153, 8);
    public static final Color startVertex = new Color(102, 102, 99);

    public static final Color graphText = new Color(128, 128, 128);

    public static final Color guidEdge = new Color(226, 62, 81, 90);

    public static final Color selectMenuBorder = new Color(57, 112, 248);
    public static final Color selectMenuShadow = new Color(30, 96, 102);
    public static final Color delete = new Color(191, 39, 39);


    // MenuButtons
    public static final ButtonColors menuButtonUnSelected =
            new ButtonColors(new Color(110, 86, 248), new Color(87, 60, 250), new Color(113, 97, 196));

    public static final ButtonColors menuButtonSelected = new ButtonColors(
            new Color(2, 134, 74), new Color(6, 107, 6), new Color(0, 173, 95)
    );

}
