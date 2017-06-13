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
import java.awt.event.*;
import javax.swing.*;

public class FileFilterDemo extends JFrame {

    JLabel fileLbl;
    JButton chooserFileBtn;

    JFileChooser chooser;

    FileFilterDemo() {
        super("File Filter Demo");
        fileLbl = new JLabel("No file is choosed yet");
        chooserFileBtn = new JButton("Choose file");

        chooserFileBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {

                if (FileFilterDemo.this.chooser == null) {
                    chooser = new JFileChooser();
                }
                chooser.addChoosableFileFilter(new MyFileFilter(".java", "Java Source Files(*.java)"));
                chooser.addChoosableFileFilter(new MyFileFilter(".xml", "Java Source Files(*.xml)"));
                chooser.addChoosableFileFilter(new MyFileFilter(".html", "Java Source Files(*.html)"));
                chooser.addChoosableFileFilter(new MyFileFilter(".txt", "Text Files(*.txt)"));
                if (chooser.showDialog(FileFilterDemo.this, "Select this") == JFileChooser.APPROVE_OPTION) {
                    FileFilterDemo.this.fileLbl.setText(chooser.getSelectedFile().getPath());
                }
            }

        });

        add(fileLbl, "Center");
        add(chooserFileBtn, "South");

        setSize(300, 300);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        FileFilterDemo ffd = new FileFilterDemo();
        ffd.setVisible(true);
    }
}
