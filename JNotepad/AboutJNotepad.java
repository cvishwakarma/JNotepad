/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JNotepad;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author DELL
 */
public class AboutJNotepad extends JDialog {

    public AboutJNotepad() {
        setTitle("About JNotepad");
        JPanel basic = new JPanel();
        basic.setLayout(new BoxLayout(basic, BoxLayout.Y_AXIS));
        add(basic);
        JPanel topPanel = new JPanel(new BorderLayout(0, 0));
        topPanel.setMaximumSize(new Dimension(450, 0));
        JLabel hint = new JLabel("JNotepad");
        hint.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));
        topPanel.add(hint);
        ImageIcon icon = new ImageIcon("JNotepad/paint.png");
        JLabel label = new JLabel(icon);
        label.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topPanel.add(label, BorderLayout.EAST);
        JSeparator separator = new JSeparator();
        separator.setForeground(Color.gray);
        topPanel.add(separator, BorderLayout.SOUTH);
        basic.add(topPanel);
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15,
                25));
        JTextPane pane = new JTextPane();
        pane.setContentType("text/html");
        String text = "<p><b>Welcome to JNotepad</b></p>" 
                +"<p>"
                + "JNotepad is a simple text editor for java and html files. "
                + "JNotepad is developed by using swing gui components."
                + "</p>";
        pane.setText(text);
        pane.setEditable(false);
        textPanel.add(pane);
        basic.add(textPanel);
        setSize(new Dimension(350, 250));
        setResizable(false);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
