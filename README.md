# Custom JFrame with Draggable Title Bar and Resizable, Rounded Frame

## Overview
- [Usage](#Usage)
    - [Constructor](#constructor)
    - [Inner Classes](#inner-classes)
    - [Methods](#methods)
- [Customization](#customization)
- [Examples](#examples)
- [License](#license)

This Java class provides a custom JFrame implementation with the following features:
- Draggable title bar for repositioning the frame on the screen.
- Resizable frame with rounded corners.
- Maximize, restore, and minimize controls in the title bar.

## Usage

### Constructor

```java
new CusFrame(boolean sizable, double divisorPhi, boolean visible, double borderRadius, double resizeBorderSize)
```
- 'sizable': Set to true if the frame is resizable, false otherwise.
- 'divisorPhi': Divisor for calculating initial frame dimensions based on the golden ratio.
- 'visible': Set to true if the frame should be visible, false otherwise.
- 'borderRadius': Border radius for rounding frame corners.
- 'resizeBorderSize': Size of the resize border for dragging to resize the frame.

### Inner Classes
#### **ResizeMouseAdapter**
* Handles mouse events for resizing the frame.

#### **TitleBarMouseAdapter**
* Handles mouse events for the draggable title bar.

#### **TitleBarPanel**
* Inner class representing the title bar panel of the custom fame. It includes the frame title, minimize, maximize, and close buttons, allowing user interactions.

### Methods

`roundCorners(int width, int height)`

Rounds the corners of the frame to create a visually pleasing effect.

`getSCREEN_WIDTH(), getSCREEN_HEIGHT()`

Gets the screen width and height of the default toolkit.

`getFrameWidth(), getFrameHeight()`

Gets the current width and height of the frame.

`getPHI()`

Gets the golden ratio (PHI) constant.

`getTitleBarHeight()`

Gets the height of the title bar.

`setTitleBarBackground(Color color), setTitleBarForeground(Color color)`

Sets the background and foreground color of the title bar.

`getTitleBarBackground(), getTitleBarForeground()`

Gets the background and foreground color of the title bar.

`setTitleBarText(String text), getTitleBarText()`

Sets and gets the text of the title bar.

`invertTitleBarButtons()`

Inverts the icons of the title bar buttons (from black to white).

`getTitleBarButtonBorder(), setTitleBarButtonBorder(Border border)`

Gets and sets the border of the title bar buttons.

`getTitleBar()`

Gets the title bar panel.

### Customization
You can customize the appearance and behavior of the frame, title bar, and buttons by modifying the respective methods and properties.

## Examples
```java
// Create a resizable frame with default settings
CusFrame customFrame = new CusFrame(true, 1.618, true, 10, 5);

// Customize the title bar
customFrame.setTitleBarBackground(Color.BLUE);
customFrame.setTitleBarForeground(Color.WHITE);
customFrame.setTitleBarButtonBorder(new LineBorder(Color.WHITE, 2));

// Set the frame title
customFrame.setTitleBarText("My Custom Frame");

// Invert title bar button icons
customFrame.invertTitleBarButtons();

```

## License

This project is licensed under the terms of the [MIT License](LICENSE.md).
