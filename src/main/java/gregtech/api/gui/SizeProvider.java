package gregtech.api.gui;

/**
 * Provides GUI and screen sizes for aligning of widgets
 * according to the screen configuration and relative coordinates
 */
public interface SizeProvider {

    /**
     * @return current screen width
     */
    int getScreenWidth();

    /**
     * @return current screen height
     */
    int getScreenHeight();

    /**
     * @return width of the GUI the widget is located in
     * if the widget is located in the sub interface, then width
     * and height will be the sub interface's holder dimensions
     */
    int getWidth();

    /**
     * @return height of the GUI the widget is located in
     * if the widget is located in the sub interface, then height
     * and width will be the sub interface's holder dimensions
     */
    int getHeight();

    int getGuiLeft();

    int getGuiTop();
}
