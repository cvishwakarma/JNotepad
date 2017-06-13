/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JNotepad;

/**
 *
 * @author DELL
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class FontDemo extends JFrame {

    FontChooser dialog = null;
    JTextArea ta;
    JButton fontButton;

    FontDemo() {
        super("Font");

        ta = new JTextArea(7, 20);
        fontButton = new JButton("Set Font");

        ActionListener ac = (ActionEvent ev) -> {
            if (dialog == null) {
                dialog = new FontChooser(ta.getFont());
            }
            if (dialog.showDialog(FontDemo.this, "Choose a font")) {
                FontDemo.this.ta.setFont(dialog.createFont());
            }
        };
        fontButton.addActionListener(ac);

        add(ta, BorderLayout.CENTER);
        add(fontButton, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50, 50, 400, 400);
        ta.append("Hello dear. how r u?");
        ta.append("\n\n A quick brown fox jumps over the lazy dog.");
        ta.append("\n\n0123456789");
        ta.append("\n~!@#$%^&*()_+|?><");
        setVisible(true);
    }

    public static void main(String[] args) {
        new FontDemo();
    }

}
