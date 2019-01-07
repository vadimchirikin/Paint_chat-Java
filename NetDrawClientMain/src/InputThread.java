import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.BasicStroke;

public class InputThread extends Thread {

    public InputThread(BufferedReader breader, JTextArea textArea, JScrollPane scrollPane, JTextField inputText, NetDrawImage image) {
        this.breader = breader;
        this.textArea = textArea;
        this.scrollPane = scrollPane;
        this.inputText = inputText;
        this.image = image;
    }

    public void run() {
        boolean running = true;
        try {
            while (running) {
                String input = breader.readLine();
                if (input == null) {
                    break;
                }
                StringTokenizer tokenizer = new StringTokenizer(input);
                if (tokenizer.countTokens() < 2) {
                    continue;
                }
                String type = tokenizer.nextToken();
                if (type.equals("msg")) {
                    String newText = "<" + tokenizer.nextToken() + ">";
                    while (tokenizer.hasMoreTokens()) {
                        newText = newText + " " + tokenizer.nextToken();
                    }
                    textArea.append(newText + "\r\n");
                    textArea.setCaretPosition(textArea.getText().length());
                }
                else if (type.equals("alert")) {
                    String newText = "*";
                    while (tokenizer.hasMoreTokens()) {
                        newText = newText + " " + tokenizer.nextToken();
                    }
                    textArea.append(newText + "\r\n");
                    textArea.setCaretPosition(textArea.getText().length());
                }
                else if (type.equals("clear")) {
                    String by = tokenizer.nextToken();
                    textArea.append("* Drawing cleared by " + by + ".\r\n");
                    textArea.setCaretPosition(textArea.getText().length());
                    image.clearGraphics();
                }
                else if (type.equals("line")) {
                    try {
                        int x1 = Integer.parseInt(tokenizer.nextToken());
                        int y1 = Integer.parseInt(tokenizer.nextToken());
                        int x2 = Integer.parseInt(tokenizer.nextToken());
                        int y2 = Integer.parseInt(tokenizer.nextToken());
                        Color color = new Color(Integer.parseInt(tokenizer.nextToken()));
                        BasicStroke stroke = new BasicStroke(Float.parseFloat(tokenizer.nextToken()));
                        image.drawBufferedLine(x1, y1, x2, y2, color, stroke);
                    }
                    catch (Exception e) {
                        System.out.println("May have lost a Line packet: " + input);
                    }
                }
                else if (type.equals("box")) {
                    try {
                        int x = Integer.parseInt(tokenizer.nextToken());
                        int y = Integer.parseInt(tokenizer.nextToken());
                        int width = Integer.parseInt(tokenizer.nextToken());
                        int height = Integer.parseInt(tokenizer.nextToken());
                        Color color = new Color(Integer.parseInt(tokenizer.nextToken()));
                        BasicStroke stroke = new BasicStroke(Float.parseFloat(tokenizer.nextToken()));
                        boolean filled = Boolean.valueOf(tokenizer.nextToken()).booleanValue();
                        image.drawBufferedBox(x, y, width, height, color, stroke, filled);
                    }
                    catch (Exception e) {
                        System.out.println("May have lost a Box packet: " + input);
                    }
                    image.repaint();
                }
                else if (type.equals("oval")) {
                    try {
                        int x = Integer.parseInt(tokenizer.nextToken());
                        int y = Integer.parseInt(tokenizer.nextToken());
                        int width = Integer.parseInt(tokenizer.nextToken());
                        int height = Integer.parseInt(tokenizer.nextToken());
                        Color color = new Color(Integer.parseInt(tokenizer.nextToken()));
                        BasicStroke stroke = new BasicStroke(Float.parseFloat(tokenizer.nextToken()));
                        boolean filled = Boolean.valueOf(tokenizer.nextToken()).booleanValue();
                        image.drawBufferedOval(x, y, width, height, color, stroke, filled);
                    }
                    catch (Exception e) {
                        System.out.println("May have lost an Oval packet: " + input);
                    }
                }
                else if (type.equals("uml")) {
                    try {
                        int x = Integer.parseInt(tokenizer.nextToken());
                        int y = Integer.parseInt(tokenizer.nextToken());
                        int width = Integer.parseInt(tokenizer.nextToken());
                        int height = Integer.parseInt(tokenizer.nextToken());
                        Color color = new Color(Integer.parseInt(tokenizer.nextToken()));
                        BasicStroke stroke = new BasicStroke(Float.parseFloat(tokenizer.nextToken()));
                        image.drawBufferedBox(x, y, width, height, color, stroke, false);
                        if (height > 20) {
                            image.drawBufferedLine(x, y+20, x+width, y+20, color, stroke);
                        }
                    }
                    catch (Exception e) {
                        System.out.println("May have lost a UML packet: " + input);
                    }
                }
                else if (type.equals("text")) {
                    try {
                        int x = Integer.parseInt(tokenizer.nextToken());
                        int y = Integer.parseInt(tokenizer.nextToken());
                        Color color = new Color(Integer.parseInt(tokenizer.nextToken()));
                        String text = tokenizer.nextToken();
                        while (tokenizer.hasMoreTokens()) {
                            text = text + " " + tokenizer.nextToken();
                        }
                        image.drawBufferedText(x, y, color, text);
                    }
                    catch (Exception e) {
                        System.out.println("May have lost a Text packet: " + input);
                    }
                }
                
            }
        }
        catch (IOException e) {
            image.setEnabled(false);
            inputText.setEnabled(false);
            inputText.setText("*** Disconnected ***");
        }
    }

    private BufferedReader breader;
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JTextField inputText;
    private NetDrawImage image;

}