import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.awt.image.BufferedImage;
import java.net.*;
import java.io.*;
import java.util.Calendar;
import javax.imageio.ImageIO;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEditSupport;


import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class NetDrawClient extends JComponent {
    
    
    public NetDrawClient(String title, int width, int height, int graphicsWidth, int graphicsHeight) {
        
        
        frame = new JFrame(title);

        // Setup menus        
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        JMenu menu1 = new JMenu("Server");
        menu1.setMnemonic(KeyEvent.VK_F);
        menu1.getAccessibleContext().setAccessibleDescription("The only menu in this program that has menu items");
        menuBar.add(menu1);

        JMenuItem menuItem1 = new JMenuItem("Connect", KeyEvent.VK_C);
        menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.ALT_MASK));
        menuItem1.getAccessibleContext().setAccessibleDescription("Connect to a NetDraw Server");
        menu1.add(menuItem1);

        JMenuItem menuItem2 = new JMenuItem("Disconnect", KeyEvent.VK_D);
        menuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
        menuItem2.getAccessibleContext().setAccessibleDescription("Disconnect from the NetDraw Server");
        menu1.add(menuItem2);
        
        JMenuItem menuItem4 = new JMenuItem("Save", KeyEvent.VK_S);
        menuItem4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem4.getAccessibleContext().setAccessibleDescription("Save image");
        menu1.add(menuItem4);
        
        JMenuItem menuItem5 = new JMenuItem("Open", KeyEvent.VK_O);
        menuItem5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        menuItem5.getAccessibleContext().setAccessibleDescription("Save image");
        menu1.add(menuItem5);

        JMenuItem menuItem3 = new JMenuItem("Exit", KeyEvent.VK_X);
        menuItem3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.ALT_MASK));
        menuItem3.getAccessibleContext().setAccessibleDescription("Exit the NetDraw Client");
        menu1.add(menuItem3);
        
        JMenu menu2 = new JMenu("Drawing");
        menu2.setMnemonic(KeyEvent.VK_R);
        menu2.getAccessibleContext().setAccessibleDescription("Drawing options");
        menuBar.add(menu2);
        
        JMenuItem menuItem1_1 = new JMenuItem("Clear", KeyEvent.VK_L);
        menuItem1_1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ActionEvent.ALT_MASK));
        menuItem1_1.getAccessibleContext().setAccessibleDescription("Clear the drawing window for all users");
        menu2.add(menuItem1_1);
        
        JMenuItem menuItem1_2 = new JMenuItem("Color", KeyEvent.VK_S);
        menuItem1_2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));
        menuItem1_2.getAccessibleContext().setAccessibleDescription("Clear the drawing window for all users");
        menu2.add(menuItem1_2);
        
        JMenuItem menuItem1_3 = new JMenuItem("New layer", KeyEvent.VK_U);
        menuItem1_3.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, ActionEvent.ALT_MASK));
        menuItem1_3.getAccessibleContext().setAccessibleDescription("Clear the drawing window for all users");
        menu2.add(menuItem1_3);
        
        JMenuItem menuItem1_4 = new JMenuItem("Delete layer", KeyEvent.VK_D);
        menuItem1_4.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D, ActionEvent.ALT_MASK));
        menuItem1_4.getAccessibleContext().setAccessibleDescription("Clear the drawing window for all users");
        menu2.add(menuItem1_4);
        
        JMenuItem menuItem1_5 = new JMenuItem("Show layer", KeyEvent.VK_H);
        menuItem1_5.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.ALT_MASK));
        menuItem1_5.getAccessibleContext().setAccessibleDescription("Clear the drawing window for all users");
        menu2.add(menuItem1_5);
        
        JMenuItem menuItem1_6 = new JMenuItem("Undo", KeyEvent.VK_Z);
        menuItem1_6.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, ActionEvent.ALT_MASK));
        menuItem1_6.getAccessibleContext().setAccessibleDescription("Clear the drawing window for all users");
        menu2.add(menuItem1_6);
        // End of menu setup.
        

        // Set up drop down boxes
        lineStyle.setRenderer(new ComboBoxCellRenderer(lineStyle));
        lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(0), "Freehand"));
        lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(1), "Line"));
        lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(2), "Box"));
        lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(3), "Oval"));
        lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(4), "Text"));
        lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(5), "Pseudo-UML"));
        lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(6), "Clear Area"));
        lineStyle.addItem(new ComboBoxItem(ImageStore.getImage(2), "Box Area"));
        
        lineColor.setRenderer(new ComboBoxCellRenderer(lineColor));
        lineColor.addItem(ImageStore.getColorItem("#000000"));
        lineColor.addItem(ImageStore.getColorItem("#999999"));
        lineColor.addItem(ImageStore.getColorItem("#ffffff"));
        // cont
        lineColor.addItem(ImageStore.getColorItem("#990000"));
        lineColor.addItem(ImageStore.getColorItem("#009900"));
        lineColor.addItem(ImageStore.getColorItem("#000099"));
        lineColor.addItem(ImageStore.getColorItem("#999900"));
        lineColor.addItem(ImageStore.getColorItem("#990099"));
        lineColor.addItem(ImageStore.getColorItem("#009999"));
        // cont
        lineColor.addItem(ImageStore.getColorItem("#ff0000"));
        lineColor.addItem(ImageStore.getColorItem("#00ff00"));
        lineColor.addItem(ImageStore.getColorItem("#0000ff"));
        lineColor.addItem(ImageStore.getColorItem("#ffff00"));
        lineColor.addItem(ImageStore.getColorItem("#ff00ff"));
        lineColor.addItem(ImageStore.getColorItem("#00ffff"));
        //lineColor.addItem(ImageStore.getColorItem("#"+color.getRGB()));
        //lineColor.addItem(ImageStore.getColorItem(color.getRGB() & 0x00ffffff));

        lineThickness.setRenderer(new ComboBoxCellRenderer(lineThickness));
        lineThickness.addItem(ImageStore.getLineThicknessItem("0.5"));
        lineThickness.addItem(ImageStore.getLineThicknessItem("1"));
        lineThickness.addItem(ImageStore.getLineThicknessItem("2"));
        lineThickness.addItem(ImageStore.getLineThicknessItem("4"));
        lineThickness.addItem(ImageStore.getLineThicknessItem("8"));

        // Set default JComboBox values.
        lineStyle.setSelectedIndex(0);
        lineColor.setSelectedIndex(5);
        lineThickness.setSelectedIndex(0);

        //msgTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        msgScrollPane.getViewport().setView(msgTextArea);
        msgTextArea.setEditable(false);
        msgTextArea.setLineWrap(true);

        image = new NetDrawImage(bwriter, graphicsWidth, graphicsHeight, lineStyle, lineColor, lineThickness, antiAliasCheckBox, filledCheckBox);
        graphicsScrollPane.getViewport().setView(image);
        graphicsScrollPane.setPreferredSize(new Dimension(640, 240));
        image.setEnabled(false);

        try {
            
        InetAddress addr = InetAddress.getLocalHost();

        // Get IP Address
        byte[] ipAddr = addr.getAddress();

        // Get hostname
        String hostname = addr.getHostName();
        String hostname2 = addr.getCanonicalHostName();
        
        InetAddress   in  = InetAddress.getLocalHost();
         InetAddress[] all = InetAddress.getAllByName(in.getHostName());
        for (int i=0; i<all.length; i++) {
            System.out.println("  address = " + all[i]);
            }
        
        JOptionPane.showMessageDialog(frame, ipAddr);
        JOptionPane.showMessageDialog(frame, hostname);
        JOptionPane.showMessageDialog(frame, hostname2);
        
            while (userName == null || userName.trim().equals("")) {
                userName = JOptionPane.showInputDialog(null, "Please enter your user name:", "NetDraw Client", JOptionPane.QUESTION_MESSAGE).trim();
            }
        }
        catch (Exception e) {
            System.exit(0);
        }
        
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel(userName));
        controlPanel.add(inputText);
        inputText.setEnabled(false);

        final JPanel graphicsToolBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        graphicsToolBarPanel.add(lineStyle);
        graphicsToolBarPanel.add(lineColor);
        graphicsToolBarPanel.add(lineThickness);
        graphicsToolBarPanel.add(antiAliasCheckBox);
        graphicsToolBarPanel.add(filledCheckBox);
        JToolBar graphicsToolBar = new JToolBar();
        graphicsToolBar.add(graphicsToolBarPanel);
        graphicsToolBar.add(UndoManagerHelper.getUndoAction ( manager ));
        graphicsToolBar.add(UndoManagerHelper.getRedoAction ( manager ));

        JPanel graphicsOptions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        graphicsOptions.add(graphicsToolBar);
        

        UndoableDrawingPanel drawingPanel = new UndoableDrawingPanel () ;
 
        
        final JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, graphicsScrollPane, msgScrollPane);
        splitPane.setOneTouchExpandable(true);
    
        ///
//        UndoableDrawingPanel drawingPanel = new UndoableDrawingPanel () ; 
//
//     UndoManager manager = new UndoManager () ; 
//     drawingPanel.addUndoableEditListener ( manager ) ; 
//
//     JToolBar toolbar = new JToolBar () ; 
//     toolbar.add ( UndoManagerHelper.getUndoAction ( manager )) ; 
//     toolbar.add ( UndoManagerHelper.getRedoAction ( manager )) ;
        ///
        
        //layers = new JLayeredPane();
        
        
        
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(graphicsOptions, BorderLayout.NORTH);
        //pane.add(splitPane, BorderLayout.CENTER);
        pane.add(controlPanel, BorderLayout.SOUTH);
        
        //Create and set up the content pane.
//        JComponent newContentPane = new NetDrawClient(title, width, height, graphicsWidth, graphicsHeight);
//        newContentPane.setOpaque(true); //content panes must be opaque
//        frame.setContentPane(newContentPane);
        
        //layers.add(graphicsToolBarPanel,  new Integer(1));
        //layers.add(graphicsOptions,  new Integer(2));
        //layers.add(controlPanel,  new Integer(3));
        splitPane.setBackground(Color.RED);
        splitPane.setBounds(10,10,1024,768);//(150, 150, 200, 200);
        layers.add(splitPane,  new Integer(1));
        layers.setOpaque(true);
        
        frame.getContentPane().add(layers, BorderLayout.CENTER);
        
        //layers.add(pane);
         
        
        //pane.add(drawingPanel, BorderLayout.CENTER);
       
        
        
        drawingPanel.addUndoableEditListener ( manager ) ; 

//        pane.add ( toolbar, BorderLayout.NORTH ) ; 
//        pane.add ( drawingPanel, BorderLayout.EAST ) ;
        
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (socket != null) {
                    try {
                        bwriter.write("quit");
                        bwriter.newLine();
                        bwriter.flush();
                        socket.close();
                    }
                    catch (IOException ie) {
                        // Do nothing.
                    }
                    socket = null;
                }
                System.exit(0);
            }
        });

        // Input Text
        inputText.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message = inputText.getText();
                try {
                    bwriter.write("msg " + userName + " " + message);
                    bwriter.newLine();
                    bwriter.flush();
                    inputText.setText("");
                }
                catch (IOException ie) {
                    image.setEnabled(false);
                    inputText.setEnabled(false);
                    inputText.setText("*** Disconnected ***");
                }
            }
        });
        
        // Connect
        menuItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                ////
                
                
                
                ////
                
                String serverHostname = JOptionPane.showInputDialog("Server address:");
                if (serverHostname == null) {
                    return;
                }
                
                String password = JOptionPane.showInputDialog("Enter password:");
                if (password == null) {
                    return;
                }
                
                //JOptionPane.showMessageDialog(frame, Integer.toString(35));
                        
                        
                try {

                    //conect to database
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        JOptionPane.showMessageDialog(frame, Integer.toString(35));
                        Connection con = null;
                        
                        con = DriverManager.getConnection("jdbc:mysql://"+serverHostname+"/paint?" + "user=root&password=");
                        

                        Statement stmt = null;
                        ResultSet rs = null;
                        stmt = (Statement) con.createStatement();
                        //String query = "SELECT * FROM USERS";
                        //String query = "SELECT * FROM USERS WHERE login='111' and passwd='111'";
                        String query = "SELECT * FROM USERS WHERE login='"+userName+"' and passwd='"+password+"'";
                        rs = stmt.executeQuery(query);
                        ResultSetMetaData meta = (ResultSetMetaData) rs.getMetaData();
                        //int num=meta.getColumnCount();
                        
                        //String num2=meta.getColumnName(2); 
                        //String num3=meta.getColumnName(3); 
                        
                        //JOptionPane.showMessageDialog(frame, Integer.toString(num));
                        //JOptionPane.showMessageDialog(frame, query);
                        //JOptionPane.showMessageDialog(frame, num2);//show column name 'login'
                        //JOptionPane.showMessageDialog(frame, num3);//show column name 'passwd'
//                        JOptionPane.showMessageDialog(f,meta.getColumnTypeName(1)); //начинается с первой
//                        JOptionPane.showMessageDialog(f,meta.getColumnLabel(3));
                        if(rs.next()) {
                String str = rs.getString("login");
                int n = rs.getInt("passwd");
                JOptionPane.showMessageDialog(frame, str);
                JOptionPane.showMessageDialog(frame, n);
                //this.jTextArea1.append(Integer.toString(n)+"   ");
                //this.jTextArea1.append(str+"\r\n");
            }
                        else
                        {
                            JOptionPane.showMessageDialog(frame, "Acsess denide!");
                            frame.dispose();
                        }
                        
                    socket = new Socket(serverHostname, 1333);
                    breader = new BufferedReader (new InputStreamReader(socket.getInputStream()));
                    bwriter = new BufferedWriter (new OutputStreamWriter(socket.getOutputStream()));
                    bwriter.write("join " + userName);
                    bwriter.newLine();
                    bwriter.flush();
                    image.setBufferedWriter(bwriter);
                    InputThread inputThread = new InputThread(breader, msgTextArea, msgScrollPane, inputText, image);
                    inputThread.start();
                    inputText.setText("");
                    inputText.setEnabled(true);
                    inputText.requestFocus();
                    image.setEnabled(true);
                    image.clearGraphics();
                }
                catch (IOException ie) {
                    JOptionPane.showMessageDialog(null, "Could not connect to " + serverHostname + ":1333", "Cannot connect", JOptionPane.ERROR_MESSAGE); 
                }
                catch (SQLException ex) {
                        Logger.getLogger(NetDrawClient.class.getName()).log(Level.SEVERE, null, ex);
                    } 
                catch (InstantiationException ex) {
                        Logger.getLogger(NetDrawClient.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(NetDrawClient.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(NetDrawClient.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
        });
        
        // Disconnect
        menuItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (socket != null) {
                    try {
                        bwriter.write("quit");
                        bwriter.newLine();
                        bwriter.flush();
                        socket.close();
                    }
                    catch (IOException ie) {
                        // Do nothing.
                    }
                    socket = null;
                }
            }
        });

        // Save
        menuItem4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                //get instance of Calendar class
                Calendar now = Calendar.getInstance();
                
                 //display full date time
    System.out.println(
                now.get(Calendar.HOUR_OF_DAY)
                + ":"
                + now.get(Calendar.MINUTE)
                + ":"
                + now.get(Calendar.SECOND)
                + "."
                + now.get(Calendar.MILLISECOND) 
                );        
                 //display full date time
//    System.out.println("Current full date time is : " + 
//                (now.get(Calendar.MONTH) + 1)
//                + "-"
//                + now.get(Calendar.DATE)
//                + "-"
//                + now.get(Calendar.YEAR)
//                + " "
//                + now.get(Calendar.HOUR_OF_DAY)
//                + ":"
//                + now.get(Calendar.MINUTE)
//                + ":"
//                + now.get(Calendar.SECOND)
//                + "."
//                + now.get(Calendar.MILLISECOND) 
//                );        
    
                // Convert Image to BufferedImage if required.
                //BufferedImage image2 = toBufferedImage(image.);
                //save(image2, "jpg");
    
    String FileName = 
            now.get(Calendar.HOUR_OF_DAY)+"-"+ 
            now.get(Calendar.MINUTE)+ "-"+ 
            now.get(Calendar.SECOND)+ "-"+ 
            now.get(Calendar.MILLISECOND);
                
    String image_directory = "users_images" + "\\" + userName;
                //createDirectory("users_images");
                createDirectory("users_images" + "\\" + userName);
                image.save(image_directory, FileName);
                
//                if (socket != null) {
//                    try {
//                        bwriter.write("quit");
//                        bwriter.newLine();
//                        bwriter.flush();
//                        socket.close();
//                    }
//                    catch (IOException ie) {
//                        // Do nothing.
//                    }
//                    socket = null;
//                }
//                System.exit(0);
            }
        });
        
        // Clear
        menuItem1_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    bwriter.write("save image " + userName);
                    bwriter.newLine();
                    bwriter.flush();
                }
                catch (Exception ie) {
                    
                }
                if (image != null ) {
                    image.clearGraphics();
                }
            }
        });
        
       
        
        //load
        menuItem5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                
                image.load();
//                if (socket != null) {
//                    try {
//                        bwriter.write("quit");
//                        bwriter.newLine();
//                        bwriter.flush();
//                        socket.close();
//                    }
//                    catch (IOException ie) {
//                        // Do nothing.
//                    }
//                    socket = null;
//                }
            }
        });
        
        // Color
        menuItem1_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //System.out.println("#"+color.toString());
                   Color color = JColorChooser.showDialog(frame, "Color, please", Color.red);
                   //int rgb = getRGB(color);
                   //color.decode(color);\
                   String rgb = Integer.toHexString(color.getRGB());
                   rgb = rgb.substring(2, rgb.length());
                    
                   System.out.println("#"+rgb);
                   lineColor.addItem(ImageStore.getColorItem("#" + rgb));
                   
                   //if(color!=null) frame.getContentPane().setBackground(color);
                try {
                   
                }
                catch (Exception ie) {
                    
                    //color.getRGB();
                    //if(color!=null) frame.getContentPane().setBackground(color);
                }
            }
        });
        
        // New layer
        menuItem1_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
//                splitPane.setOpaque(!splitPane.isOpaque());
//                layers.repaint();
                
                
                
                //image.undo();
                try {
                    //down = new JPanel();
                    down.setBackground(Color.WHITE);
                    down.setBounds(10,10,640, 480);
                    //down.setOpaque(!down.isOpaque());
                    //down.setOpaque(true);
                    layers.add(down, new Integer(5));
                    layers.repaint();
                }
                catch (Exception ie) {
                    
                    //color.getRGB();
                    //if(color!=null) frame.getContentPane().setBackground(color);
                }
            }
        });
        
      // Delete layer
        menuItem1_4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
//                splitPane.setOpaque(!splitPane.isOpaque());
//                layers.repaint();
                
                
                
                //image.undo();
                try {
                    
                   // down.setOpaque(!down.isOpaque());
                    layers.moveToBack(down);
                    layers.repaint();
                    //layers.moveToBack(down);
                    //layers.;
                }
                catch (Exception ie) {
                    
                    //color.getRGB();
                    //if(color!=null) frame.getContentPane().setBackground(color);
                }
            }
        });
        
              // Show layer
        menuItem1_5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
//                splitPane.setOpaque(!splitPane.isOpaque());
//                layers.repaint();
                
                
                
                //image.undo();
                try {
                    
                   // down.setOpaque(!down.isOpaque());
                    layers.moveToFront(down);
                    layers.repaint();
                    //layers.moveToBack(down);
                    //layers.;
                }
                catch (Exception ie) {
                    
                    //color.getRGB();
                    //if(color!=null) frame.getContentPane().setBackground(color);
                }
            }
        });
        
      // Undo
        menuItem1_6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               
//                splitPane.setOpaque(!splitPane.isOpaque());
//                layers.repaint();
                
                
                image.undo();
                
                try {
//                    String command = e.getActionCommand();
//           if (command.equals("Undo")) {
//                  // Clear the off-screen canvas to current background color,
//                  // then call repaint() so the screen is also cleared.
//              image.undo();
//              //repaint();
//           }
//                    //image.undo();
//                   // down.setOpaque(!down.isOpaque());
////                    layers.moveToFront(down);
////                    layers.repaint();
//                    //layers.moveToBack(down);
//                    //layers.;
               }
                catch (Exception ie) {
                    
                    //color.getRGB();
                    //if(color!=null) frame.getContentPane().setBackground(color);
                }
            }
        });
        
        frame.setSize(width, height);
        //frame.setResizable(false);
        
        SwingUtilities.updateComponentTreeUI(this);
        frame.setVisible(true);
        
          
        
    }
    
    private JFrame frame;
    private JTextArea msgTextArea = new JTextArea();
    private JTextField inputText = new JTextField("*** Not Connected ***", 40);
    private JScrollPane graphicsScrollPane = new JScrollPane();
    private JScrollPane msgScrollPane = new JScrollPane();
    private NetDrawImage netDrawImage = null;
    private NetDrawImage image;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private JMenuItem menuItem3;
    private JMenuItem menuItem4;
    private JMenuItem menuItem5;
    private JComboBox lineStyle = new JComboBox(); // (new Object[] {"Freehand", "Line", "Box", "Oval", "Text", "Pseudo-UML", "Clear Area"});
    private JComboBox lineColor = new JComboBox(); // (new Object[] {"#000000", "#00ff00", "#0000ff"});
    private JComboBox lineThickness = new JComboBox(); //(new Object[] {"1.0", "2.0", "4.0"});
    private JCheckBox antiAliasCheckBox = new JCheckBox("Anti-alias", true);
    private JCheckBox filledCheckBox = new JCheckBox("filled", false);

    private Socket socket = null;
    private BufferedReader breader = null;
    private BufferedWriter bwriter = null;

    private String userName = null;
    private Color color = null;
    
    private UndoManager manager = new UndoManager ();
    
    private JLayeredPane layers = new JLayeredPane();
    private JPanel up, graphicsToolBarPanel;
    private JPanel down = new JPanel();
    private JButton toggleOpaque;

    private static void save(BufferedImage image, String ext) {
        String fileName = "savingAnImage";
        File file = new File(fileName + "." + ext);
        try {
            ImageIO.write(image, ext, file);  // ignore returned boolean
        } catch(IOException e) {
            System.out.println("Write error for " + file.getPath() +
                               ": " + e.getMessage());
        }
    }
    
     private static BufferedImage toBufferedImage(Image src) {
        int w = src.getWidth(null);
        int h = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_RGB;  // other options
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return dest;
    }
     
     private void createDirectory(String directoryName)
    {
      //File myDir = new File( "/home/fundir" );
        
      File theDir = new File(directoryName);
      try
      {
          // if the directory does not exist, create it
          if (!theDir.exists())
          {
            //System.out.println("creating directory: " + directoryName);
            theDir.mkdir();
          }
      }
      catch(Exception e) {
            
        }
    }
 
}


class UndoableDrawingPanel extends JPanel { 
   UndoableEditSupport undoableEditSupport = new UndoableEditSupport ( this ) ; 

   Polygon polygon = new Polygon () ; 

   public UndoableDrawingPanel () { 
     MouseListener mouseListener = new MouseAdapter () { 
       public void mouseReleased ( MouseEvent mouseEvent ) { 
         undoableEditSupport.postEdit ( new UndoableDrawEdit ( 
             UndoableDrawingPanel. this )) ; 
         polygon.addPoint ( mouseEvent.getX () , mouseEvent.getY ()) ; 
         repaint () ; 
       } 
     } ; 
     addMouseListener ( mouseListener ) ; 
   } 

   public void addUndoableEditListener ( 
       UndoableEditListener undoableEditListener ) { 
     undoableEditSupport.addUndoableEditListener ( undoableEditListener ) ; 
   } 

   public void removeUndoableEditListener ( 
       UndoableEditListener undoableEditListener ) { 
     undoableEditSupport.removeUndoableEditListener ( undoableEditListener ) ; 
   } 

   public void setPolygon ( Polygon newValue ) { 
     polygon = newValue; 
     repaint () ; 
   } 

   public Polygon getPolygon () { 
     Polygon returnValue; 
     if ( polygon.npoints == 0 ) { 
       returnValue = new Polygon () ; 
     } else { 
       returnValue = new Polygon ( polygon.xpoints, polygon.ypoints, 
           polygon.npoints ) ; 
     } 
     return returnValue; 
   } 

   protected void paintComponent ( Graphics g ) { 
     super .paintComponent ( g ) ; 
     g.drawPolygon ( polygon ) ; 
   } 
} 

class UndoManagerHelper { 

   public static Action getUndoAction ( UndoManager manager, String label ) { 
     return new UndoAction ( manager, label ) ; 
   } 

   public static Action getUndoAction ( UndoManager manager ) { 
     return new UndoAction ( manager, "Undo" ) ; 
   } 

   public static Action getRedoAction ( UndoManager manager, String label ) { 
     return new RedoAction ( manager, label ) ; 
   } 

   public static Action getRedoAction ( UndoManager manager ) { 
     return new RedoAction ( manager, "Redo" ) ; 
   } 

   private abstract static class UndoRedoAction extends AbstractAction { 
     UndoManager undoManager = new UndoManager () ; 

     String errorMessage = "Cannot undo" ; 

     String errorTitle = "Undo Problem" ; 

     protected UndoRedoAction ( UndoManager manager, String name ) { 
       super ( name ) ; 
       undoManager = manager; 
     } 

     public void setErrorMessage ( String newValue ) { 
       errorMessage = newValue; 
     } 

     public void setErrorTitle ( String newValue ) { 
       errorTitle = newValue; 
     } 

     protected void showMessage ( Object source ) { 
       if ( source instanceof Component ) { 
         JOptionPane.showMessageDialog (( Component ) source, errorMessage, 
             errorTitle, JOptionPane.WARNING_MESSAGE ) ; 
       } else { 
         System.err.println ( errorMessage ) ; 
       } 
     } 
   } 

   public static class UndoAction extends UndoRedoAction { 
     public UndoAction ( UndoManager manager, String name ) { 
       super ( manager, name ) ; 
       setErrorMessage ( "Cannot undo" ) ; 
       setErrorTitle ( "Undo Problem" ) ; 
     } 

     public void actionPerformed ( ActionEvent actionEvent ) { 
       try { 
         undoManager.undo () ; 
       } catch ( CannotUndoException cannotUndoException ) { 
         showMessage ( actionEvent.getSource ()) ; 
       } 
     } 
   } 

   public static class RedoAction extends UndoRedoAction { 
     String errorMessage = "Cannot redo" ; 

     String errorTitle = "Redo Problem" ; 

     public RedoAction ( UndoManager manager, String name ) { 
       super ( manager, name ) ; 
       setErrorMessage ( "Cannot redo" ) ; 
       setErrorTitle ( "Redo Problem" ) ; 
     } 

     public void actionPerformed ( ActionEvent actionEvent ) { 
       try { 
         undoManager.redo () ; 
       } catch ( CannotRedoException cannotRedoException ) { 
         showMessage ( actionEvent.getSource ()) ; 
       } 
     } 
   } 

} 

class UndoableDrawEdit extends AbstractUndoableEdit { 
   UndoableDrawingPanel panel; 

   Polygon polygon, savedPolygon; 

   public UndoableDrawEdit ( UndoableDrawingPanel panel ) { 
     this .panel = panel; 
     polygon = panel.getPolygon () ; 
   } 

   public String getPresentationName () { 
     return "Polygon of size " + polygon.npoints; 
   } 

   public void redo () throws CannotRedoException { 
     super .redo () ; 
     if ( savedPolygon == null ) { 
       // Should never get here, as super() doesn't permit redoing 
       throw new CannotRedoException () ; 
     } else { 
       panel.setPolygon ( savedPolygon ) ; 
       savedPolygon = null ; 
     } 
   } 

   public void undo () throws CannotUndoException { 
     super .undo () ; 
     savedPolygon = panel.getPolygon () ; 
     panel.setPolygon ( polygon ) ; 
   } 
} 


//With JDK1.2, Sun introduces a new package called JIMI (available for download at their Web site. With this package, it's easy to convert a Java Image to a JPEG image file.
//
//double w = 200.0;
//double h = 200.0;
//BufferedImage image = new BufferedImage(
//   (int)w,(int)h,BufferedImage.TYPE_INT_RGB);
//
//Graphics2D g = (Graphics2D)image.getGraphics();
//g.drawLine(0,0,w,h);
//
//try {
//   File f = new File("myimage.jpg");
//   JimiRasterImage jrf = Jimi.createRasterImage(image.getSource());
//   Jimi.putImage("image/jpeg",jrf,new FileOutputStream(f));
//   }
//catch (JimiException je) {
//   je.printStackTrace();}
//Another way is to use the undocumented com.sun.image.codec.jpeg package.
////  [JDK1.2]
////  img is a Java Image
////
//BufferedImage bimg = null;
//int w = img.getWidth(null);
//int h = img.getHeight(null);
//int [] pixels = new int[w * h];
//PixelGrabber pg = new PixelGrabber(img,0,0,w,h,pixels,0,w);
//try { 
//  pg.grabPixels(); 
//  } 
//catch(InterruptedException ie) { 
//  ie.printStackTrace();
//  }
//
//bimg = new BufferedImage(w,h,BufferedImage.TYPE_INT_RGB);
//bimg.setRGB(0,0,w,h,pixels,0,w);
//
//// Encode as a JPEG
//FileOutputStream fos = new FileOutputStream("out.jpg");
//JPEGImageEncoder jpeg = JPEGCodec.createJPEGEncoder(fos);
//jpeg.encode(bimg);
//fos.close();
//Since JDK1.4.2, javax.imageio.ImageIO lets you save and restore Images to disk in a platform independent format. "png" and "jpeg" format are supported. With ImageIO, instead of Image you use BufferedImage which is a subclass of Image.
//
//import java.io.*;
//import javax.imageio.*;
//import java.awt.image.*;
// 
//public class FileOperations {
//    
//    public static BufferedImage readImageFromFile(File file) 
//       throws IOException
//    {
//        return ImageIO.read(file);
//    }
// 
//    public static void writeImageToJPG
//       (File file,BufferedImage bufferedImage) 
//          throws IOException
//    {
//        ImageIO.write(bufferedImage,"jpg",file);
//    }
//}