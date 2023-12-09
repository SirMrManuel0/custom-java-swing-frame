/**
 * Custom JFrame class that provides a resizable, rounded frame with a draggable title bar.
 * The frame can be maximized, restored, and minimized using custom controls in the title bar.
 */
package Customs;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class CusFrame extends JFrame implements ActionListener, ComponentListener, WindowStateListener {

    // Golden ratio constant
    protected final double PHI =  1.618033988749895;
    // Screen dimensions
    protected final int SCREEN_WIDTH;
    protected final int SCREEN_HEIGHT;
    // Frame dimensions
    protected int frameHeight;
    protected int frameWidth;
    // Resize border size and border radius
    protected final double RESIZE_BORDER_SIZE;
    protected double borderRadius;

    // Title bar panel
    protected TitleBarPanel titleBar = new TitleBarPanel();

    /**
     * Constructs a custom frame.
     *
     * @param sizable         true if the frame is resizable, false otherwise.
     * @param divisorPhi      the divisor for calculating initial frame dimensions based on the golden ratio.
     * @param visible         true if the frame should be visible, false otherwise.
     * @param borderRadius    the border radius for rounding frame corners.
     * @param resizeBorderSize the size of the resize border for dragging to resize the frame.
     */
    public CusFrame(boolean sizable, double divisorPhi, boolean visible, double borderRadius, double resizeBorderSize){
        setResizable(sizable);
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        SCREEN_WIDTH = screenSize.width;
        SCREEN_HEIGHT = screenSize.height;
        this.borderRadius = borderRadius;
        this.RESIZE_BORDER_SIZE = resizeBorderSize;

        frameHeight = (int) Math.round(SCREEN_WIDTH / divisorPhi);
        frameWidth = (int) Math.round(frameHeight * PHI);
        setSize(new Dimension(frameWidth, frameHeight));

        ResizeMouseAdapter resizeAdapter = new ResizeMouseAdapter();
        addMouseListener(resizeAdapter);
        addMouseMotionListener(resizeAdapter);



        addComponentListener(this);
        addWindowStateListener(this);
        setUndecorated(true);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(titleBar, BorderLayout.NORTH);
        roundCorners(frameWidth, frameHeight);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(visible);

    }

    /**
     * Inner class that handles mouse events for resizing the frame.
     */
    protected class ResizeMouseAdapter extends MouseAdapter {
        Point initialLocation;
        /**
         * Invoked when a mouse button is pressed.
         *
         * @param e The MouseEvent when the button is pressed.
         */
        public void mousePressed(MouseEvent e) {
            initialLocation = e.getPoint();
        }

        /**
         * Invoked when the mouse is dragged.
         *
         * @param e The MouseEvent when the mouse is dragged.
         */
        public void mouseDragged(MouseEvent e) {
            int deltaX = e.getX() - initialLocation.x;
            int deltaY = e.getY() - initialLocation.y;
            Point[] frameLocation = getFrameLocation();


            // Check if dragging the right upper corner of the frame
            if (isNearRightEdge(initialLocation) && isNearTopEdge(initialLocation)){
                // Resize the frame by changing its height and width
                CusFrame.this.setSize(getFrameWidth() + deltaX, getFrameHeight() - deltaY);
                roundCorners(getFrameWidth() + deltaX, getFrameHeight() - deltaY);
                frameWidth += deltaX;
                initialLocation.x += deltaX;
                frameHeight -= deltaY;
                CusFrame.this.setLocation(frameLocation[0].x, frameLocation[0].y + deltaY);
            }
            // Check if dragging the right lower corner of the frame
            else if (isNearRightEdge(initialLocation) && isNearBottomEdge(initialLocation)){
                // Resize the frame by changing its height and width
                CusFrame.this.setSize(getFrameWidth() + deltaX, getFrameHeight() + deltaY);
                roundCorners(getFrameWidth() + deltaX, getFrameHeight() + deltaY);
                frameWidth += deltaX;
                frameHeight += deltaY;
                initialLocation.x += deltaX;
                initialLocation.y += deltaY;
            }
            // Check if dragging the left lower corner of the frame
            else if (isNearLeftEdge(initialLocation) && isNearBottomEdge(initialLocation)){
                // Resize the frame by changing its height and width
                CusFrame.this.setSize(getFrameWidth() - deltaX, getFrameHeight() + deltaY);
                roundCorners(getFrameWidth() - deltaX, getFrameHeight() + deltaY);
                frameWidth -= deltaX;
                frameHeight += deltaY;
                initialLocation.y += deltaY;
                CusFrame.this.setLocation(frameLocation[0].x + deltaX, frameLocation[0].y);
            }
            // Check if dragging the left upper corner of the frame
            else if (isNearLeftEdge(initialLocation) && isNearTopEdge(initialLocation)){
                // Resize the frame by changing its height and width
                CusFrame.this.setSize(getFrameWidth() - deltaX, getFrameHeight() - deltaY);
                roundCorners(getFrameWidth() - deltaX, getFrameHeight() - deltaY);
                frameHeight -= deltaY;
                frameWidth -= deltaX;
                CusFrame.this.setLocation(frameLocation[0].x + deltaX, frameLocation[0].y + deltaY);
            }
            // Check if dragging near the top edge of the frame
            else if (isNearTopEdge(initialLocation)){
                // Resize the frame by changing its height
                CusFrame.this.setSize(getFrameWidth(), getFrameHeight() - deltaY);
                roundCorners(getFrameWidth(), getFrameHeight() - deltaY);
                frameHeight -= deltaY;
                CusFrame.this.setLocation(frameLocation[0].x, frameLocation[0].y + deltaY);
            }
            // Check if dragging near the bottom edge of the frame
            else if (isNearBottomEdge(initialLocation)){
                // Resize the frame by changing its height
                CusFrame.this.setSize(getFrameWidth(), getFrameHeight() + deltaY);
                roundCorners(getFrameWidth(), getFrameHeight() + deltaY);
                frameHeight += deltaY;
                initialLocation.y += deltaY;
            }
            // Check if dragging near the right edge of the frame
            else if (isNearRightEdge(initialLocation)){
                // Resize the frame by changing its width
                CusFrame.this.setSize(getFrameWidth() + deltaX, getFrameHeight());
                roundCorners(getFrameWidth() + deltaX, getFrameHeight());
                frameWidth += deltaX;
                initialLocation.x += deltaX;
            }
            // Check if dragging near the left edge of the frame
            else if (isNearLeftEdge(initialLocation)){
                // Resize the frame by changing its width
                CusFrame.this.setSize(getFrameWidth() - deltaX, getFrameHeight());
                roundCorners(getFrameWidth() - deltaX, getFrameHeight());
                frameWidth -= deltaX;
                CusFrame.this.setLocation(frameLocation[0].x + deltaX, frameLocation[0].y);
            }

            // Ensure a minimum frame size
            if (getFrameWidth() <= 100 && getFrameHeight() <= 30){
                CusFrame.this.setSize(100,30);
                roundCorners(100,30);
            } else if (getFrameWidth() <= 20){
                CusFrame.this.setSize(100, getFrameHeight());
                roundCorners(100, getFrameHeight());
            } else if (getFrameHeight() <= 20){
                CusFrame.this.setSize(getFrameWidth(), 30);
                roundCorners(getFrameWidth(), 30);
            }

        }
        public void mouseMoved(MouseEvent e) {
            setCursorForLocation(e.getPoint());
        }

        /**
         * Sets the cursor based on the location of the mouse.
         *
         * @param location The Point representing the current mouse position.
         */
        protected void setCursorForLocation(Point location) {
            if ((isNearLeftEdge(location) && isNearTopEdge(location)) || (isNearBottomEdge(location) && isNearRightEdge(location))){
                setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
            }
            else if ((isNearTopEdge(location) && isNearRightEdge(location)) || (isNearBottomEdge(location) && isNearLeftEdge(location))){
                setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
            }
            else if (isNearTopEdge(location) || isNearBottomEdge(location)) {
                setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
            } else if (isNearRightEdge(location) || isNearLeftEdge(location)) {
                setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }

    /**
     * Rounds the corners of the frame to create a visually pleasing effect.
     *
     * @param width  The width of the frame.
     * @param height The height of the frame.
     */
    protected void roundCorners(int width, int height){
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Double(0, 0, width, height, borderRadius, borderRadius);
        setShape(roundedRectangle);
    }

    /**
     * Gets the screen width of the default toolkit.
     *
     * @return The screen width.
     */
    public int getSCREEN_WIDTH() {return SCREEN_WIDTH;}
    /**
     * Gets the screen height of the default toolkit.
     *
     * @return The screen height.
     */
    public int getSCREEN_HEIGHT() { return SCREEN_HEIGHT;}
    /**
     * Gets the current width of the frame.
     *
     * @return The width of the frame.
     */
    public int getFrameWidth() {return frameWidth; }
    /**
     * Gets the current height of the frame.
     *
     * @return The height of the frame.
     */
    public int getFrameHeight() { return frameHeight;}
    /**
     * Gets the golden ratio (PHI) constant.
     *
     * @return The golden ratio constant.
     */
    public double getPHI() {return PHI;}
    /**
     * Gets the height of the title bar.
     *
     * @return The height of the title bar.
     */
    public int getTitleBarHeight() {return 25;}
    /**
     * Sets the background color of the title bar.
     *
     * @param color The color to set as the background.
     */
    public void setTitleBarBackground(Color color) {titleBar.setBackground(color);}
    /**
     * Sets the foreground color of the title bar.
     *
     * @param color The color to set as the foreground.
     */
    public void setTitleBarForeground(Color color) {titleBar.setForeground(color);}
    /**
     * Gets the foreground color of the title bar.
     *
     * @return The foreground color of the title bar.
     */
    public Color getTitleBarForeground() {return titleBar.getForeground();}
    /**
     * Gets the background color of the title bar.
     *
     * @return The background color of the title bar.
     */
    public Color getTitleBarBackground() {return titleBar.getBackground();}
    /**
     * Sets the text of the title bar.
     *
     * @param text The text to set in the title bar.
     */
    public void setTitleBarText(String text) { titleBar.setText(text);}
    /**
     * Gets the text of the title bar.
     *
     * @return The text of the title bar.
     */
    public String getTitleBarText() { return titleBar.getText(); }
    /**
     * Inverts the icons of the title bar buttons.
     */
    public void invertTitleBarButtons() {titleBar.invertButtonIcon();}
    /**
     * Gets the border of the title bar buttons.
     *
     * @return The border of the title bar buttons.
     */
    public Border getTitleBarButtonBorder() {return titleBar.getButtonBorder();}
    /**
     * Sets the border of the title bar buttons.
     *
     * @param border The border to set for the title bar buttons.
     */
    public void setTitleBarButtonBorder(Border border) {titleBar.setButtonBorder(border);}
    /**
     * Gets the title bar panel.
     *
     * @return The title bar panel.
     */
    public JPanel getTitleBar() {return titleBar;}



    /**
     * Inner class that handles mouse events for the draggable title bar of the custom frame.
     * It allows users to click and drag the frame to reposition it on the screen.
     */
    protected class TitleBarMouseAdapter extends MouseAdapter{
        protected int mouseX, mouseY;

        /**
         * Invoked when a mouse button is pressed.
         *
         * @param e The MouseEvent when the button is pressed.
         */
        @Override
        public void mousePressed(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }
        /**
         * Invoked when the mouse is dragged.
         *
         * @param e The MouseEvent when the mouse is dragged.
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            int deltaX = e.getX() - mouseX;
            int deltaY = e.getY() - mouseY;
            CusFrame.this.setLocation(CusFrame.this.getX() + deltaX, CusFrame.this.getY() + deltaY);
        }
    }

    /**
     * Inner class representing the title bar panel of the custom frame.
     * It includes the frame title, minimize, maximize, and close buttons, allowing user interactions.
     */
    protected class TitleBarPanel extends JPanel implements WindowStateListener, ActionListener{
        protected JLabel label;
        protected JButton minimizeButton;
        protected JButton maximizeButton;
        protected JButton closeButton;
        protected ImageIcon minimizeIcon;
        protected ImageIcon maximizeIcon;
        protected ImageIcon closeIcon;
        protected  ImageIcon shrinkIcon;
        protected Border buttonBorder;
        protected int[] beforeMax;
        protected boolean isMax = false;
        protected boolean isInversed = false;

        /**
         * Constructor: Initializes the title bar components, sets up button actions, and configures the appearance of the title bar.
         */
        public TitleBarPanel() {
            setLayout(new TitleBarLayout());
            label = new JLabel("CusFrame");
            minimizeIcon = new ImageIcon("icons/minimize.png");
            minimizeIcon = new ImageIcon(scaleImage(minimizeIcon.getImage(), 20,20));
            maximizeIcon = new ImageIcon("icons/maximize.png");
            maximizeIcon = new ImageIcon(scaleImage(maximizeIcon.getImage(), 20,20));
            closeIcon = new ImageIcon("icons/close.png");
            closeIcon = new ImageIcon(scaleImage(closeIcon.getImage(), 20,20));
            shrinkIcon = new ImageIcon("icons/shrink.png");
            shrinkIcon = new ImageIcon(scaleImage(shrinkIcon.getImage(), 20,20));
            minimizeButton = new JButton(minimizeIcon);
            maximizeButton = new JButton(maximizeIcon);
            closeButton = new JButton(closeIcon);
            buttonBorder = new LineBorder(Color.BLACK, 2);

            minimizeButton.setPreferredSize(new Dimension(20, 20));
            maximizeButton.setPreferredSize(new Dimension(20, 20));
            closeButton.setPreferredSize(new Dimension(20, 20));

            minimizeButton.setBackground(null);
            maximizeButton.setBackground(null);
            closeButton.setBackground(null);

            minimizeButton.setBorder(buttonBorder);
            maximizeButton.setBorder(buttonBorder);
            closeButton.setBorder(buttonBorder);

            minimizeButton.addActionListener(new ActionListener() {
                /**
                 * Invoked when the minimize button is clicked.
                 *
                 * @param e The ActionEvent when the button is clicked.
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    ((Frame)getTopLevelAncestor()).setExtendedState(JFrame.ICONIFIED);
                }
            });

            maximizeButton.addActionListener(new ActionListener() {
                /**
                 * Invoked when the maximize button is clicked.
                 *
                 * @param e The ActionEvent when the button is clicked.
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    toggleMaximizeFrame();
                }
            });

            closeButton.addActionListener(new ActionListener() {
                /**
                 * Invoked when the close button is clicked.
                 *
                 * @param e The ActionEvent when the button is clicked.
                 */
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            setPreferredSize(new Dimension(frameWidth, 25));
            setBackground(new Color(255,0,255));
            TitleBarMouseAdapter titleBarMouseAdapter = new TitleBarMouseAdapter();
            addMouseListener(titleBarMouseAdapter);
            addMouseMotionListener(titleBarMouseAdapter);

            add(label);
            add(minimizeButton);
            if (CusFrame.this.isResizable()){
                add(maximizeButton);
            }
            add(closeButton);
        }

        /**
         * Toggles between maximizing and restoring the frame. Updates button icon accordingly.
         */
        protected void toggleMaximizeFrame() {
            Frame frame = (Frame) getTopLevelAncestor();
            int state = frame.getExtendedState();

            if (!isMax) {
                Point[] frameLocation = getFrameLocation();
                isMax = true;
                maximizeButton.setIcon(shrinkIcon);
                // Frame is not maximized, maximize it
                frame.setExtendedState(Frame.NORMAL);  // Restore first to get correct frame size
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                beforeMax = new int[]{getFrameWidth(), getFrameHeight(), frameLocation[0].x, frameLocation[0].y};
                frame.setSize(screenSize);
                roundCorners(screenSize.width, screenSize.height);
                frame.setLocation(0, 0);
            } else {
                maximizeButton.setIcon(maximizeIcon);
                isMax = false;
                // Frame is maximized, restore it
                frame.setExtendedState(Frame.NORMAL);
                frame.setSize(beforeMax[0], beforeMax[1]);
                roundCorners(beforeMax[0], beforeMax[1]);
                frame.setLocation(beforeMax[2], beforeMax[3]);
            }

        }

        public void invertButtonIcon(){
            if (isInversed) {
                minimizeIcon = new ImageIcon("icons/minimizeInverted.png");
                minimizeIcon = new ImageIcon(scaleImage(minimizeIcon.getImage(), 20,20));
                maximizeIcon = new ImageIcon("icons/maximizeInverted.png");
                maximizeIcon = new ImageIcon(scaleImage(maximizeIcon.getImage(), 20,20));
                closeIcon = new ImageIcon("icons/closeInverted.png");
                closeIcon = new ImageIcon(scaleImage(closeIcon.getImage(), 20,20));
                shrinkIcon = new ImageIcon("icons/shrinkInverted.png");
                shrinkIcon = new ImageIcon(scaleImage(shrinkIcon.getImage(), 20,20));
            } else {
                minimizeIcon = new ImageIcon("icons/minimize.png");
                minimizeIcon = new ImageIcon(scaleImage(minimizeIcon.getImage(), 20,20));
                maximizeIcon = new ImageIcon("icons/maximize.png");
                maximizeIcon = new ImageIcon(scaleImage(maximizeIcon.getImage(), 20,20));
                closeIcon = new ImageIcon("icons/close.png");
                closeIcon = new ImageIcon(scaleImage(closeIcon.getImage(), 20,20));
                shrinkIcon = new ImageIcon("icons/shrink.png");
                shrinkIcon = new ImageIcon(scaleImage(shrinkIcon.getImage(), 20,20));
            }

            minimizeButton.setIcon(minimizeIcon);
            maximizeButton.setIcon(maximizeIcon);
            closeButton.setIcon(closeIcon);
        }

        /**
         * Inner Class: TitleBarLayout
         * Implements the LayoutManager interface for custom layout of title bar components.
         */
        protected class TitleBarLayout implements LayoutManager {
            @Override
            public void addLayoutComponent(String name, Component comp) {}

            @Override
            public void removeLayoutComponent(Component comp) {}

            /**
             * Returns the preferred size of the layout.
             *
             * @param parent The container being laid out.
             * @return The preferred size of the layout.
             */
            @Override
            public Dimension preferredLayoutSize(Container parent) {
                return new Dimension(0, 0);
            }

            /**
             * Returns the minimum size of the layout.
             *
             * @param parent The container being laid out.
             * @return The minimum size of the layout.
             */
            @Override
            public Dimension minimumLayoutSize(Container parent) {
                return new Dimension(0, 0);
            }

            /**
             * Custom layout for title bar components.
             *
             * @param parent The container being laid out.
             */
            @Override
            public void layoutContainer(Container parent) {
                int width = parent.getWidth();
                int height = parent.getHeight();

                int labelWidth = label.getPreferredSize().width;
                int labelX = (width - labelWidth) / 2;

                label.setBounds(labelX, 0, labelWidth, height);

                int buttonX = width-10;
                int buttonWidth = minimizeButton.getPreferredSize().width;

                buttonX -= buttonWidth+5;
                closeButton.setBounds(buttonX, 2, buttonWidth, 20);

                buttonX -= buttonWidth+5;
                maximizeButton.setBounds(buttonX, 2, buttonWidth, 20);

                buttonX -= buttonWidth+5;
                minimizeButton.setBounds(buttonX, 2, buttonWidth, 20);
            }
        }

        /**
         * Scales the given image to the specified width and height.
         *
         * @param image  The original image to be scaled.
         * @param width  The target width of the scaled image.
         * @param height The target height of the scaled image.
         * @return The scaled image.
         */
        protected Image scaleImage(Image image, int width, int height) {
            BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = scaledImage.createGraphics();
            graphics.drawImage(image, 0, 0, width, height, null);
            graphics.dispose();
            return scaledImage;
        }
        /**
         * Sets the text of the title bar label.
         *
         * @param text The text to be set.
         */
        public void setText(String text){label.setText(text);}
        /**
         * Retrieves the text of the title bar label.
         *
         * @return The text of the title bar label.
         */
        public String getText(){return label.getText();}
        /**
         * Sets the border for the minimize, maximize, and close buttons.
         *
         * @param border The border to be set.
         */
        public void setButtonBorder(Border border){
            buttonBorder = border;
            minimizeButton.setBorder(buttonBorder);
            maximizeButton.setBorder(buttonBorder);
            closeButton.setBorder(buttonBorder);
        }
        /**
         * Retrieves the border of the minimize, maximize, and close buttons.
         *
         * @return The border of the minimize, maximize, and close buttons.
         */
        public Border getButtonBorder(){return buttonBorder;}


        @Override
        public void windowStateChanged(WindowEvent e) {

        }

        @Override
        public void actionPerformed(ActionEvent e) {

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {}

    @Override
    public void componentResized(ComponentEvent e) {
        frameHeight = getHeight();
        frameWidth = getWidth();
    }

    @Override
    public void componentMoved(ComponentEvent e) {}

    @Override
    public void componentShown(ComponentEvent e) {}

    @Override
    public void componentHidden(ComponentEvent e) {}

    @Override
    public void windowStateChanged(WindowEvent e) {
        frameHeight = getHeight();
        frameWidth = getWidth();
    }

    /**
     * Retrieves an array of Points representing the frame's location and its corners.
     *
     * @return An array of Points representing the frame's location and its corners.
     */
    public  Point[] getFrameLocation(){
        return new Point[]{
                getLocation(),
                new Point(getLocation().x+getFrameWidth(),getLocation().y),
                new Point(getLocation().x+getFrameWidth(),getLocation().y+getFrameHeight()),
                new Point(getLocation().x, getLocation().y+getFrameHeight())
        };
    }

    /**
     * Checks if the specified Point is near the right edge of the frame.
     *
     * @param point The Point to check.
     * @return True if the Point is near the right edge, otherwise false.
     */
    protected boolean isNearRightEdge(Point point) {
        return point.x <= getFrameWidth() && point.x >= getFrameWidth() - RESIZE_BORDER_SIZE;
    }

    /**
     * Checks if the specified Point is near the left edge of the frame.
     *
     * @param point The Point to check.
     * @return True if the Point is near the left edge, otherwise false.
     */
    protected boolean isNearLeftEdge(Point point) {
        return point.x >= 0 && point.x <= RESIZE_BORDER_SIZE;
    }

    /**
     * Checks if the specified Point is near the top edge of the frame.
     *
     * @param point The Point to check.
     * @return True if the Point is near the top edge, otherwise false.
     */
    protected boolean isNearTopEdge(Point point) {
        return point.y >= 28 && point.y <= RESIZE_BORDER_SIZE + 30;
    }

    /**
     * Checks if the specified Point is near the bottom edge of the frame.
     *
     * @param point The Point to check.
     * @return True if the Point is near the bottom edge, otherwise false.
     */
    protected boolean isNearBottomEdge(Point point) {
        return point.y <= getFrameHeight() && point.y >= getFrameHeight() - RESIZE_BORDER_SIZE;
    }

}
