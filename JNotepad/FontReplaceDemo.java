/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JNotepad;

import java.awt.BorderLayout;
import java.awt.event.*;
import javax.swing.*;

/**
 *
 * @author DELL
 */
class FindReplaceDemo extends JFrame {

    FindDialog dialog = null;
    JTextArea ta;
    JButton findButton, replaceButton;

    FindReplaceDemo() {
        super("Find Demo");

        ta = new JTextArea(7, 20);
        findButton = new JButton("Find text");

        ActionListener ac1;
        ac1 = (ActionEvent ev) -> {
            if (dialog == null) {
                dialog = new FindDialog(FindReplaceDemo.this.ta);
            }
            dialog.showDialog(FindReplaceDemo.this, true);//find
        };
        findButton.addActionListener(ac1);

        replaceButton = new JButton("Replace text");

        ActionListener ac2 = (ActionEvent ev) -> {
            if (dialog == null) {
                dialog = new FindDialog(FindReplaceDemo.this.ta);
            }
            dialog.showDialog(FindReplaceDemo.this, false);//find
        };
        replaceButton.addActionListener(ac2);

        add(ta, BorderLayout.CENTER);
        add(replaceButton, BorderLayout.NORTH);
        add(findButton, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(50, 50, 400, 400);
        ta.append("Hello dear. how r u?");
        ta.append("\nhey i said Hello and not hello or Hel or hello.");
        ta.append("\nWell do u know what is the meaning of Hello");
        ta.append("\n Hello is no hello but it is Hello");
        ta.setCaretPosition(0);
        setVisible(true);
    }

    public static void main(String[] args) {
        FindReplaceDemo findReplaceDemo = new FindReplaceDemo();
    }

}
