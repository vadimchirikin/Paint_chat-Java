import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class NetDrawImage extends JComponent implements MouseListener, MouseMotionListener {
    
    public NetDrawImage()  {
       
           setBackground(Color.gray); // Background color of applet, shows
                                              // in borders and between components.
           setLayout(new BorderLayout(3,3)); // Layout manager for applet.
          
           NetDrawImage canvas = new NetDrawImage(); // The drawing area.
           add(canvas,BorderLayout.CENTER);
          
           Panel buttonBar = new Panel(); // A panel to hold the buttons.
           buttonBar.setBackground(Color.lightGray);
           add(buttonBar, BorderLayout.SOUTH);
          
           Panel choiceBar = new Panel(); // A panel to hole the pop-up menus
           choiceBar.setBackground(Color.lightGray);
           add(choiceBar, BorderLayout.NORTH);
          
           Button fill = new Button("Set Background"); // The first button.
          // fill.addActionListener(canvas);
           fill.setBackground(Color.lightGray);
           buttonBar.add(fill);
          
           Button clear = new Button("Clear"); // The second button.
           //clear.addActionListener(canvas);
           clear.setBackground(Color.lightGray);
           buttonBar.add(clear);
          
           Button undo = new Button("Undo"); // Third button. 
            //undo.addActionListener(canvas); 
            undo.setBackground(Color.lightGray); 
            buttonBar.add(undo);
          
           Choice choice = new Choice(); // The pop-up menu of colors.
           choice.addItem("Black");
           choice.addItem("Red");
           choice.addItem("Green");
           choice.addItem("Blue");
           choice.addItem("Cyan");
           choice.addItem("Magenta");
           choice.addItem("Yellow");
           choice.addItem("White");
           choice.setBackground(Color.white);
           choiceBar.add(choice);
          
           Choice choice2 = new Choice(); // The pop-up menu of shapes.
           choice2.addItem("Curve");
           choice2.addItem("Straight Line");
           choice2.addItem("Rectangle");
           choice2.addItem("Oval");
           choice2.addItem("RoundRect");
           choice2.addItem("Filled Rectangle");
           choice2.addItem("Filled Oval");
           choice2.addItem("Filled RoundRect");
           choice2.setBackground(Color.white);
           choiceBar.add(choice2);
          
           Choice choice3 = new Choice(); // The pop-up menu of symmetry styles.
           choice3.add("No symmetry");
           choice3.add("2-way symmetry");
           choice3.add("4-way symmetry");
           choice3.add("8-way symmetry");
           choice3.setBackground(Color.white);
           choiceBar.add(choice3);
          
           canvas.colorChoice = choice; // Canvas needs access to Choice menus,
           canvas.figureChoice = choice2; // so it can check it to find out what
           canvas.symmetryChoice = choice3; // color, shape, and symmetry to use.
          
//           addWindowListener( new WindowAdapter() {
//                   public void windowClosing(WindowEvent evt) {
//                           // If user clicks the window's close box, close
//                           // the window.  Call System.exit(0) to end
//                           // the program.
//                      dispose();
//                      System.exit(0);
//                   }
//                }
//             );
          
           //pack(); // Use the frame's preferred size;
           setLocation(40,60); // Put the frame at (40,60) on the screen.
           show(); // Make the window visible.
          
        } // end constructor
    
       
//        public static void main(String[] args) {
//              // If this class is run as a stand-along program, then
//              // it just opens a frame and ends.  (The frame remains
//              // open until the user closes it.)
//           new KaleidaPaintFrame();
//        }
     
    
    // } // end class KaleidaPaintFrame
    
    private final static int 
                    BLACK = 0,
                    RED = 1, // Some constants to make
                    GREEN = 2, // the code more readable.
                    BLUE = 3, // These numbers code for
                    CYAN = 4, // the differnet drawing colors.
                    MAGENTA = 5,
                    YELLOW = 6,
                    WHITE = 7;
    
        Choice colorChoice; // A Choice object, containing the possible drawing
                             // colors, which must be created by the applet.
                            
        private final static int
                   CURVE = 0,
                   LINE = 1,
                   RECT = 2, // Some constants that code
                   OVAL = 3, // for the different types of
                   ROUNDRECT = 4, // figure the program can draw.
                   FILLED_RECT = 5,
                   FILLED_OVAL = 6,
                   FILLED_ROUNDRECT = 7;
                  
        Choice figureChoice; // A Choice object containing the possible figures.
       
        private final static int
                   NO_SYMMETRY = 0, // Some constants that code for 
                   SYMMETRY_2 = 1, // the different symmetry styles.
                   SYMMETRY_4 = 2,        
                   SYMMETRY_8 = 3;
                  
        Choice symmetryChoice; // A Choice object containing the possible 
                                // symmetry styles.
                            
    
    Image OSC; // The off-screen canvas (created in setupOSC()).
       
        int widthOfOSC, heightOfOSC; // Current width and height of OSC.  These
                                      // are checked against the size of the applet,
                                      // to detect any change in the applet's size.
                                      // If the size has changed, a new OSC is created.
                                      // The picture in the off-screen canvas is lost
                                      // when that happens.
                                     
        Image undoBuffer; // Another buffer for saving a copy of OSC to 
                            // implement the undo operation.  The OSC is copied 
                            // to the undoBuffer at the start of a draw operation. 
                            // If the user clicks Undo, the buffers are swapped.
       
    
        /* The following variables are used when the user is sketching a
           curve while dragging a mouse.  */
    
        private int prevX, prevY; // The previous location of the mouse.
       
        private int startX, startY; // The starting position of the mouse.
                                      // (Not used for drawing curves.)
       
        private boolean dragging; // This is set to true when the user is drawing.
       
        private int figure; // What type of figure is being drawn.  This is
                               // specified by the figureChoice menu.
                              
        private int symmetry; // What type of symmetry style is being used.  This is
                               // specified by the symmetryChoice menu.
       
        private Graphics graphicsForDrawing; // A graphics context for the applet.
       
        private Graphics offscreenGraphics; // A graphics context for the off-screen canvas.
    
         private void setupOSC() {
             // This method is responsible for creating the off-screen canvas. 
             // It should be called before using the OSC.  It will make a new OSC if
             // the size of the canvas changes.  Also make an undo buffer.
            
           if (OSC == null || widthOfOSC != getSize().width || heightOfOSC != getSize().height) {
                  // Create the OSC, or make a new one if canvas size has changed.
                 
              OSC = null; // (If OSC & undoBuffer already exist, this frees up the memory.)
              undoBuffer = null;
             
              OSC = createImage(getSize().width, getSize().height);
              widthOfOSC = getSize().width;
              heightOfOSC = getSize().height;
              Graphics OSG = OSC.getGraphics(); // Graphics context for drawing to OSC.
              OSG.setColor(getBackground());
              OSG.fillRect(0, 0, widthOfOSC, heightOfOSC);
              OSG.dispose();
             
              undoBuffer = createImage(widthOfOSC, heightOfOSC); 
               OSG = undoBuffer.getGraphics(); // Graphics context for drawing to the undoBuffer. 
               OSG.setColor(getBackground()); 
               OSG.fillRect(0, 0, widthOfOSC, heightOfOSC); 
               OSG.dispose();
             
           }
          
        } // end setupOSC()
         
          private Graphics getOSG() {
              // Return a graphics context for drawing to the off-screen canvas.
              // A new canvas is created if necessary.  Note that the graphics
              // context should not be kept for any length of time, in case the
              // size of the canvas changes.
            setupOSC();
            return OSC.getGraphics();
        }
    
    
        private void clearOSC() {
              // Fill the off-screen canvas with the background color.
              // (First, save the current image in the undo buffer.)
            Graphics undoGr = undoBuffer.getGraphics(); 
             undoGr.drawImage(OSC, 0, 0, null); 
             undoGr.dispose(); 
            Graphics OSG = OSC.getGraphics();
            OSG.setColor(getBackground());
            OSG.fillRect(0, 0, widthOfOSC, heightOfOSC);
            OSG.dispose();
        }
    
        public void update(Graphics g) {
              // Redefine update so it doesn't clear the canvas before calling paint().
           paint(g);
        }
        
        public void paint(Graphics g) {
             // Just copy the off-screen canvas to the screen.
           setupOSC();
           g.drawImage(OSC, 0, 0, this);
        }
        
         private void putFigure(Graphics g, int kind,
                                   int x1, int y1, int x2, int y2, boolean outlineOnly) {
              // Draws a figure with corners at (x1,y1) and (x2,y2).  The 
              // parameter "kind" codes for the type of figure to draw.  If the
              // figure is LINE, a line is drawn between the points.  For the
              // other shapes, we need the top-left corner, the width, and the
              // height of the shape.  We have to figure out whether x1 or x2
              // is the left edge of the shape and compute the width accordingly.
              // Similarly for y1 and y2.  If outlineOnly is true, then filled shapes
              // are drawn in outline only.
           if (kind == LINE)
              g.drawLine(x1, y1, x2, y2);
           else {
              int x, y, w, h; // Top-left corner, width, and height.
              if (x2 >= x1) { // x1 is left edge
                 x = x1;
                 w = x2 - x1;
              }
              else { // x2 is left edge
                 x = x2;
                 w = x1 - x2;
              }
              if (y2 >= y1) { // y1 is top edge
                 y = y1;
                 h = y2 - y1;
              }
              else { // y2 is top edge.
                 y = y2;
                 h = y1 - y2;
              }
              switch (kind) { // Draw the appropriate figure.
                 case RECT:
                    g.drawRect(x, y, w, h);
                    break;
                 case OVAL:
                    g.drawOval(x, y, w, h);
                    break;
                 case ROUNDRECT:
                    g.drawRoundRect(x, y, w, h, 20, 20);
                    break;
                 case FILLED_RECT:
                    if (outlineOnly)
                       g.drawRect(x, y, w, h);
                    else 
                       g.fillRect(x, y, w, h);
                    break;
                 case FILLED_OVAL:
                    if (outlineOnly)
                       g.drawOval(x, y, w, h);
                    else 
                    g.fillOval(x, y, w, h);
                    break;
                 case FILLED_ROUNDRECT:
                    if (outlineOnly)
                       g.drawRoundRect(x, y, w, h, 20, 20);
                    else 
                    g.fillRoundRect(x, y, w, h, 20, 20);
                    break;
              }
           }
        }
       
         private Color getCurrentColor() {
                 // Check the colorChoice menu to find the currently
                 // selected color, and return the appropriate color
                 // object.
           int currentColor = colorChoice.getSelectedIndex();
           switch (currentColor) {
              case BLACK:
                 return Color.black;
              case RED:
                 return Color.red;
              case GREEN:
                 return Color.green;
              case BLUE:
                 return Color.blue;
              case CYAN:
                 return Color.cyan;
              case MAGENTA:
                 return Color.magenta;
              case YELLOW:
                 return Color.yellow;
              default:
                 return Color.white;
           }
        }
       
        private void putMultiFigure(Graphics g, int kind, int sym,
                                   int x1, int y1, int x2, int y2, boolean outlineOnly) {
              // Draws a figure with corners at (x1,y1) and (x2,y2) and possibly
              // some of its reflections.  The reflections that are drawn depend
              // on the sym parameter, which has one of the values NO_SYMMETRY,
              // SYMMETRY_2, SYMMETRY_4, or SYMMETRY_8.  The
              // parameter "kind" codes for the type of figure to draw.
          
           int width = getSize().width;
           int height = getSize().height;
          
           putFigure(g, kind, x1, y1, x2, y2, outlineOnly);
          
           if (sym >= SYMMETRY_2) { // Draw the horizontal reflection.
              putFigure(g, kind, width - x1, y1, width - x2, y2, outlineOnly);
           }
    
           if (sym >= SYMMETRY_4) { // Draw the two vertical reflections.
              putFigure(g, kind, x1, height - y1, x2, height - y2, outlineOnly);
              putFigure(g, kind, width - x1, height - y1, width - x2, height - y2, outlineOnly);
           }
          
           if (sym == SYMMETRY_8) { // Draw the four diagonal reflections.
              int a1 = (int)( ((double)y1 / height) * width );
              int b1 = (int)( ((double)x1 / width) * height );
              int a2 = (int)( ((double)y2 / height) * width );
              int b2 = (int)( ((double)x2 / width) * height );
              putFigure(g, kind, a1, b1, a2, b2, outlineOnly);
              putFigure(g, kind, width - a1, b1, width - a2, b2, outlineOnly);
              putFigure(g, kind, a1, height - b1, a2, height - b2, outlineOnly);
              putFigure(g, kind, width - a1, height - b1, width - a2, height - b2, outlineOnly);
           }
    
        } // end putMultiFigure
        
        
        
        
    
    public NetDrawImage(BufferedWriter bwriter, int width, int height, JComboBox lineStyle, JComboBox lineColor, JComboBox lineThickness, JCheckBox antiAliasCheckBox, JCheckBox filledCheckBox) {
        this.bwriter = bwriter;
        this.lineStyle = lineStyle;
        this.lineColor = lineColor;
        this.lineThickness = lineThickness;
        this.antiAliasCheckBox = antiAliasCheckBox;
        this.filledCheckBox = filledCheckBox;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        this.setSize(width, height);
        this.setPreferredSize(new Dimension(width, height));
        this.setMinimumSize(new Dimension(width, height));
        this.setBackground(Color.white);
        this.setOpaque(true);
        
        imageGraphics = (Graphics2D)image.getGraphics();
        imageGraphics.setColor(Color.white);
        imageGraphics.fillRect(0, 0, width-1, height-1);
        
        this.updateUI();

        this.addMouseMotionListener(this);
        this.addMouseListener(this);

    }
    
    // Draw on both the JComponent and the BufferedImage.
    // This making both drawing and redraw events quicker!
    public void mouseDragged(MouseEvent e) {
        if (enabled) {
            e.consume();
            int newX = e.getX();
            int newY = e.getY();
            String style = (String)lineStyle.getSelectedItem().toString();
            if (style.equals("Freehand")) {
                drawBufferedLine(lastX, lastY, newX, newY, currentColor, currentStroke);
                try {
                    sendLine("line " + lastX + " " + lastY + " " + newX + " " + newY + " " + currentColor.getRGB() + " " + currentStroke.getLineWidth());
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
                lastX = newX;
                lastY = newY;
            }
            else if (style.equals("Line")) {
                paintComponent(this.getGraphics());
                this.drawClientLine(lastX, lastY, newX, newY);
            }
            else if (style.equals("Box")) {
                paintComponent(this.getGraphics());
                this.drawClientBox(lastX, lastY, newX-lastX, newY-lastY, filledCheckBox.isSelected());
            }
            else if (style.equals("Oval")) {
                paintComponent(this.getGraphics());
                this.drawClientOval((lastX+newX)/2 - (newX-lastX), (lastY+newY)/2 - (newY-lastY), newX-lastX, newY-lastY, filledCheckBox.isSelected());
            }
            else if (style.equals("Text")) {
                Graphics g = this.getGraphics();
                paintComponent(g);
                g.drawString("Text...", newX, newY);
            }
            else if (style.equals("Pseudo-UML")) {
                paintComponent(this.getGraphics());
                this.drawClientBox(lastX, lastY, newX-lastX, newY-lastY, false);
                if (newY > lastY + 20) {
                    this.drawClientLine(lastX, lastY+20, newX, lastY+20);
                }
            }
            else if (style.equals("Clear Area")) {
                paintComponent(this.getGraphics());
                currentColor = Color.yellow;
                this.drawClientBox(lastX, lastY, newX-lastX, newY-lastY, false);
            }
            else if (style.equals("Box Area")) {
                paintComponent(this.getGraphics());
                this.drawClientBox(lastX, lastY, newX-lastX, newY-lastY, filledCheckBox.isSelected());
            }
        }
        
        
         // Called whenever the user moves the mouse while a mouse button
       // is down.  If the user is drawing a curve, draw a segment of
        // the curve on both the screen and the off-screen canvas.
     // If the user is drawing a shape, erase the previous shape
      // by redrawing it (in XOR mode), then draw the new shape.
    
            if (dragging == false)
               return; // Nothing to do because the user isn't drawing.
              
            int x = e.getX(); // x-coordinate of mouse.
            int y = e.getY(); // y=coordinate of mouse.
           
            if (figure == CURVE) {
                   // Draw the line on the applet and on the off-screen canvas.
               putMultiFigure(graphicsForDrawing, LINE, symmetry, prevX, prevY, x, y, false); 
               putMultiFigure(offscreenGraphics, LINE, symmetry, prevX, prevY, x, y, false);
            }
            else {  
                  // Erase previous figure and draw a new one using the new mouse position.
               putMultiFigure(graphicsForDrawing, figure, symmetry, 
                                                      startX, startY, prevX, prevY, true);
               putMultiFigure(graphicsForDrawing, figure, symmetry, 
                                                      startX, startY, x, y, true);
            }
           
            prevX = x; // Save coords for the next call to mouseDragged or mouseReleased.
            prevY = y;
            
    }// end mouseDragged()
    
    public Dimension getMinimumSize() {
           return new Dimension(250,200);
        }
       
       
        public Dimension getPreferredSize() {
            return new Dimension(320,320);
        }
    
    public void drawBufferedLine(int x1, int y1, int x2, int y2, Color color, Stroke stroke) {
        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        if (antiAliasCheckBox.isSelected()) {
            componentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        else {
            componentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        componentGraphics.setStroke(stroke);
        componentGraphics.setColor(color);
        componentGraphics.drawLine(x1, y1, x2, y2);
        imageGraphics.setStroke(stroke);
        imageGraphics.setColor(color);
        imageGraphics.drawLine(x1, y1, x2, y2);
    }
    
    public void drawBufferedBox(int x1, int y1, int width, int height, Color color, Stroke stroke, boolean filled) {
        // No need to anti-alias this really...
        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        componentGraphics.setStroke(stroke);
        componentGraphics.setColor(color);
        imageGraphics.setStroke(stroke);
        imageGraphics.setColor(color);
        
        if (filled) {
            componentGraphics.fillRect(x1, y1, width, height);
            imageGraphics.fillRect(x1, y1, width, height);
        }
        else {
            componentGraphics.drawRect(x1, y1, width, height);
            imageGraphics.drawRect(x1, y1, width, height);
        }
    }
    
//    public void drawBufferedBox(int x1, int y1, int width, int height, Color color, Stroke stroke, boolean filled) {
//        // No need to anti-alias this really...
//        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
//        componentGraphics.setStroke(stroke);
//        componentGraphics.setColor(color);
//        imageGraphics.setStroke(stroke);
//        imageGraphics.setColor(color);
//        
//        if (filled) {
//            componentGraphics.fillRect(x1, y1, width, height);
//            imageGraphics.fillRect(x1, y1, width, height);
//        }
//        else {
//            componentGraphics.drawRect(x1, y1, width, height);
//            imageGraphics.drawRect(x1, y1, width, height);
//        }
//    }
    
    public void drawBufferedOval(int x1, int y1, int width, int height, Color color, Stroke stroke, boolean filled) {
        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        if (antiAliasCheckBox.isSelected()) {
            componentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        else {
            componentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        componentGraphics.setStroke(stroke);
        componentGraphics.setColor(color);
        imageGraphics.setStroke(stroke);
        imageGraphics.setColor(color);
        
        if (filled) {
            componentGraphics.fillOval(x1, y1, width, height);
            imageGraphics.fillOval(x1, y1, width, height);
        }
        else {
            componentGraphics.drawOval(x1, y1, width, height);
            imageGraphics.drawOval(x1, y1, width, height);
        }
    }
    
    public void drawBufferedText(int x, int y, Color color, String text) {
        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        if (antiAliasCheckBox.isSelected()) {
            componentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }
        else {
            componentGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        componentGraphics.setColor(color);
        componentGraphics.drawString(text, x, y);
        imageGraphics.setColor(color);
        imageGraphics.drawString(text, x, y);
    }
    
    public void drawClientLine(int x1, int y1, int x2, int y2) {
        // Note that this is not antialiased
        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        componentGraphics.setColor(currentColor);
        componentGraphics.setStroke(currentStroke);
        componentGraphics.drawLine(x1, y1, x2, y2);
    }
    
    public void drawClientBox(int x1, int y1, int width, int height, boolean filled) {
        // Note that this is not antialiased
        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        componentGraphics.setColor(currentColor);
        componentGraphics.setStroke(currentStroke);
        if (filled) {
            componentGraphics.fillRect(x1, y1, width, height);
        }
        else {
            componentGraphics.drawRect(x1, y1, width, height);
        }
    }
    
    public void drawClientOval(int x1, int y1, int width, int height, boolean filled) {
        // Note that this is not antialiased
        Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
        componentGraphics.setColor(currentColor);
        componentGraphics.setStroke(currentStroke);
        if (filled) {
            componentGraphics.fillOval(x1, y1, width, height);
        }
        else {
            componentGraphics.drawOval(x1, y1, width, height);
        }
    }
    
    public void clearGraphics() {
        //Graphics g = image.getGraphics();
        //g.fillRect(0, 0, image.getWidth()-1, image.getWidth()-1);
        imageGraphics.setColor(Color.white);
        imageGraphics.fillRect(0, 0, image.getWidth()-1, image.getWidth()-1);
        repaint();
    }
    
    public void setBufferedWriter(BufferedWriter bwriter) {
        this.bwriter = bwriter;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void mousePressed(MouseEvent e) {
        if (enabled) {
            currentColor = Color.decode((String)lineColor.getSelectedItem().toString());
            currentStroke = new BasicStroke(Float.parseFloat((String)lineThickness.getSelectedItem().toString()));
            e.consume();
            lastX = e.getX();
            lastY = e.getY();
            String style = (String)lineStyle.getSelectedItem().toString();
            if (style.equals("Freehand")) {
                this.drawBufferedLine(lastX, lastY, lastX, lastY, currentColor, currentStroke);
                try {
                    sendLine("line " + lastX + " " + lastY + " " + lastX + " " + lastY + " " + currentColor.getRGB() + " " + currentStroke.getLineWidth());
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
            }
            else if (style.equals("Line")) {
                // Shouldn't need to do anything here?
            }
            else if (style.equals("Box")) {
                // Shouldn't need to do anything here?
            }
            else if (style.equals("Oval")) {
                // No need to do anything
            }
            else if (style.equals("Text")) {
                Graphics g = this.getGraphics();
                g.drawString("Text...", lastX, lastY);
            }
            else if (style.equals("Pseudo-UML")) {
                
            }
            else if (style.equals("Clear Area")) {
                
            }
        }
        
    // This is called when the user presses the mouse on the
   // canvas.  This begins a draw operation in which the user
  // sketches a curve or draws a shape.
               
           if (dragging == true) // Ignore mouse presses that occur
               return; // when user is already drawing a curve.
                                  // (This can happen if the user presses
                                  // two mouse buttons at the same time.)
    
           prevX = startX = e.getX(); // Save mouse coordinates.
           prevY = startY = e.getY();
          
           figure = figureChoice.getSelectedIndex(); // Type of figure being drawn.
          
           symmetry = symmetryChoice.getSelectedIndex(); // Symmetry style for drawing.
    
           graphicsForDrawing = getGraphics(); // For drawing on the screen.
           graphicsForDrawing.setColor(getCurrentColor());
    
           offscreenGraphics = getOSG(); // For drawing on the canvas.
           offscreenGraphics.setColor(getCurrentColor());
    
           // Copy the current image to the undo buffer. 
          
            Graphics undoGr = undoBuffer.getGraphics();   
            undoGr.drawImage(OSC, 0, 0, null); 
            undoGr.dispose();
    
           if (figure != CURVE) {
                  // Shapes are drawn in XOR mode so they can be erased by redrawing.
                  // Curves are drawn directly to both the screen and to the OSC.
              graphicsForDrawing.setXORMode(getBackground());
              putMultiFigure(graphicsForDrawing, figure, symmetry, 
                                               startX, startY, startX, startY, true);
           }
          
           dragging = true; // Start drawing.
          
    } // end mousePressed()
    
    public void mouseReleased(MouseEvent e) {
        if (enabled) {
            e.consume();
            int newX = e.getX();
            int newY = e.getY();
            String style = (String)lineStyle.getSelectedItem().toString();
            if (style.equals("Freehand")) {
                // No need to do anything
            }
            else if (style.equals("Line")) {
                this.drawBufferedLine(lastX, lastY, newX, newY, currentColor, currentStroke);
                try {
                    sendLine("line " + lastX + " " + lastY + " " + newX + " " + newY + " " + currentColor.getRGB() + " " + currentStroke.getLineWidth());
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
            }
            else if (style.equals("Box")) {
                this.drawBufferedBox(lastX, lastY, newX-lastX, newY-lastY, currentColor, currentStroke, filledCheckBox.isSelected());
                try {
                    sendLine("box " + lastX + " " + lastY + " " + (newX-lastX) + " " + (newY-lastY) + " " + currentColor.getRGB() + " " + currentStroke.getLineWidth() + " " + filledCheckBox.isSelected());
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
            }
            else if (style.equals("Oval")) {
                this.drawBufferedOval((lastX+newX)/2 - (newX-lastX), (lastY+newY)/2 - (newY-lastY), newX-lastX, newY-lastY, currentColor, currentStroke, filledCheckBox.isSelected());
                try {
                    sendLine("oval " + ((lastX+newX)/2 - (newX-lastX)) + " " + ((lastY+newY)/2 - (newY-lastY)) + " " + (newX-lastX) + " " + (newY-lastY) + " " + currentColor.getRGB() + " " + currentStroke.getLineWidth() + " " + filledCheckBox.isSelected());
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
            }
            else if (style.equals("Text")) {
                Graphics2D g = (Graphics2D)this.getGraphics();
                String text = null;
                try {
                    text = JOptionPane.showInputDialog(null, "Please enter your text to add to the drawing view:", "Drawing Text", JOptionPane.QUESTION_MESSAGE).trim();
                }
                catch (Exception any) {
                    paintComponent(g);
                    return;
                }
                if (text == null || text == "") {
                    paintComponent(g);
                    return;
                }
                paintComponent(g);
                this.drawBufferedText(newX, newY, currentColor, text);
                try {
                    sendLine("text " + newX + " " + newY + " " + currentColor.getRGB() + " " + text);
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
                antiAliasCheckBox.requestFocus();
            }
            else if (style.equals("Pseudo-UML")) {
                this.drawBufferedBox(lastX, lastY, newX-lastX, newY-lastY, currentColor, currentStroke, filledCheckBox.isSelected());
                if (newY > lastY + 20) {
                    this.drawBufferedLine(lastX, lastY+20, newX, lastY+20, currentColor, currentStroke);
                }
                try {
                    sendLine("uml " + lastX + " " + lastY + " " + (newX-lastX) + " " + (newY-lastY) + " " + currentColor.getRGB() + " " + currentStroke.getLineWidth());
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
            }
            else if (style.equals("Clear Area")) {
                this.drawBufferedBox(lastX, lastY, newX-lastX, newY-lastY, Color.white, currentStroke, true);
                try {
                    sendLine("box " + lastX + " " + lastY + " " + (newX-lastX) + " " + (newY-lastY) + " " + Color.white.getRGB() + " " + currentStroke.getLineWidth()+ " " + "true");
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
                repaint();
            }
            else if (style.equals("Box Area")) {
                this.drawBufferedBox(lastX, lastY, newX-lastX, newY-lastY, currentColor, currentStroke, filledCheckBox.isSelected());
                try {
                    sendLine("box " + lastX + " " + lastY + " " + (newX-lastX) + " " + (newY-lastY) + " " + currentColor.getRGB() + " " + currentStroke.getLineWidth() + " " + filledCheckBox.isSelected());
                }
                catch (IOException ie) {
                    this.setEnabled(false);
                }
            }
        }
        
     // Called whenever the user releases the mouse button.
    // If the use was drawing a shape, we make the shape
   // permanent by drawing it in paintMode on both the screen
   // and the off-screen canvas (but only if the mouse is not
    // at its starting location.) Then get rid of the graphics
   // contexts that we were using while dragging.
        
            if (dragging == false)
               return; // Nothing to do because the user isn't drawing.
            dragging = false;
            if (figure != CURVE) {
                   // Erase the last XOR mode shape by redrawing it in XOR mode.
                   // Then, if the mouse is not back where it started from,
                   // Draw the final shape on both the screen and the off-screen
                   // canvas in paintMode.
               putMultiFigure(graphicsForDrawing, figure, symmetry, 
                                                       startX, startY, prevX, prevY, true);
               if (startX != prevX || startY != prevY) {
                  graphicsForDrawing.setPaintMode();
                  putMultiFigure(graphicsForDrawing, figure, symmetry, 
                                                            startX, startY, prevX, prevY, false);
                  putMultiFigure(offscreenGraphics, figure, symmetry, 
                                                            startX, startY, prevX, prevY, false);
               }
            }
            graphicsForDrawing.dispose();
            offscreenGraphics.dispose();
            graphicsForDrawing = null;
            offscreenGraphics = null;
            
    } //end mouseReleased()
    
    
    public void mouseMoved(MouseEvent e) {
    }
    public void mouseEntered(MouseEvent e) {
    }
    public void mouseExited(MouseEvent e) {
    }
    public void mouseClicked(MouseEvent e) {
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.drawImage(image, null, null);
    }
    
    private synchronized void sendLine(String line) throws IOException {
        bwriter.write(line);
        bwriter.newLine();
        bwriter.flush();
    }
    
    //public void save(BufferedImage image, String ext) {
    public void save(String userdir, String fileName) {
        //String fileName = "savingAnImage";
        File file = new File( userdir + "\\" + fileName + "." + "JPG");
        try {
            //Graphics2D componentGraphics = (Graphics2D)this.getGraphics();
            //ImageIO.write(image, ext, file);  // ignore returned boolean
            ImageIO.write(image, "JPG", file);
        } catch(IOException e) {
//            System.out.println("Write error for " + file.getPath() +
//                               ": " + e.getMessage());
        }
    }
    
         public void load() {
        
        

        try {
            String fileName = "java";
        File file = new File(fileName + "." + "gif");
        //image.g
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new File("."));
        int r = chooser.showOpenDialog(this);
        File f = chooser.getSelectedFile();
        
        String name = f.getName();
        String suffix = name.substring(name.lastIndexOf('.')+1);
        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
        ImageReader reader = iter.next();
        ImageInputStream imageIn = ImageIO.createImageInputStream(f);
        reader.setInput(imageIn);
        //image = reader.read(0);
        
        imageGraphics.drawImage(reader.read(0), null, r, r);

        //repaint();
            //ImageIO.read("java.gif");  // ignore returned boolean
        } catch(Exception e) {
//            System.out.println("Write error for " + file.getPath() +
//                               ": " + e.getMessage());
        }
    }
    
  
     public void undo() {

        try {
//            String fileName = "java";
//        File file = new File(fileName + "." + "gif");
//        //image.g
//        JFileChooser chooser = new JFileChooser();
//        chooser.setCurrentDirectory(new File("."));
//        int r = chooser.showOpenDialog(this);
//        File f = chooser.getSelectedFile();
//        
//        String name = f.getName();
//        String suffix = name.substring(name.lastIndexOf('.')+1);
//        Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
//        ImageReader reader = iter.next();
//        ImageInputStream imageIn = ImageIO.createImageInputStream(f);
//        reader.setInput(imageIn);
//        //image = reader.read(0);
//        
//        imageGraphics.drawImage(reader.read(0), null, r, r);

      // Swap the off-screen canvas with the undoBuffer and repaint. 
         Image temp = OSC; 
         OSC = undoBuffer; 
         undoBuffer = temp; 
         repaint(); 
                
        
         
                
        //repaint();
            //ImageIO.read("java.gif");  // ignore returned boolean
        } catch(Exception e) {

        }
    }
                 
     
    private BufferedImage image;
    private BufferedWriter bwriter = null;
    private JComboBox lineStyle = null;
    private JComboBox lineColor = null;
    private JComboBox lineThickness = null;
    private JCheckBox antiAliasCheckBox = null;
    private JCheckBox filledCheckBox = null;
    private boolean mouseDown = false;
    private int lastX = 0;
    private int lastY = 0;
    
    private Color currentColor = null;
    private BasicStroke currentStroke = null;
    
    private Graphics2D imageGraphics = null;
    
    private boolean enabled = false;
}