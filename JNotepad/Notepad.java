/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JNotepad;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.text.BadLocationException;

/**
 *
 * @author DELL
 */
public class Notepad implements ActionListener, MenuConstants, Printable {

    JFrame f;
    JTextArea ta;
    JLabel statusBar;
    JToolBar jtb;

    String fontType = "Agency FB " ;
    int fontStyle =Font.PLAIN, fontSize=10;

    private String fileName = "Untitled";
    private boolean saved = true;
    String applicationName = "Javapad";

    String searchString, replaceString;
    int lastSearchIndex;
    Font font = null;
    FileOperationHandler fileHandler;
    FontChooser fontDialog = null;
    FindDialog findDialog = null, findReplaceDialog = null;
    JColorChooser bcolorChooser = null;
    JColorChooser fcolorChooser = null;
    JDialog backgroundDialog = null;
    JDialog foregroundDialog = null;
    JMenuItem cutItem, copyItem, deleteItem, findItem, findNextItem, replaceItem, gotoItem, selectAllItem;

    /**
     * *************************
     */
    Notepad() {
        f = new JFrame(fileName + " - " + applicationName);
        ta = new JTextArea(30, 60);
        jtb = new JToolBar();
        statusBar = new JLabel("||       Ln 1, Col 1  ", JLabel.RIGHT);
        f.add(new JScrollPane(ta), BorderLayout.CENTER);
        f.add(jtb, BorderLayout.NORTH);
        f.add(statusBar, BorderLayout.SOUTH);
        f.add(new JLabel("  "), BorderLayout.EAST);
        f.add(new JLabel("  "), BorderLayout.WEST);
        createMenuBar(f);
        createToolBar(jtb, f);
        f.setSize(350, 350);
        f.pack();
        f.setLocation(100, 50);
        f.setVisible(true);
        f.setLocation(150, 50);
        f.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        fileHandler = new FileOperationHandler(this);

        ta.addCaretListener((CaretEvent e) -> {
            int lineNumber = 0, column = 0, pos = 0;
            try {
                pos = ta.getCaretPosition();
                lineNumber = ta.getLineOfOffset(pos);
                column = pos - ta.getLineStartOffset(lineNumber);
            } catch (Exception excp) {
            }
            if (ta.getText().length() == 0) {
                lineNumber = 0;
                column = 0;
            }
            statusBar.setText("||       Ln " + (lineNumber + 1) + ", Col " + (column + 1));
        });

        DocumentListener myListener = new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                fileHandler.saved = false;
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fileHandler.saved = false;
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                fileHandler.saved = false;
            }
        };
        ta.getDocument().addDocumentListener(myListener);

        WindowListener frameClose = new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                if (fileHandler.confirmSave()) {
                    System.exit(0);
                }
            }
        };
        f.addWindowListener(frameClose);

    }

    void goTo() {
        int lineNumber = 0;
        try {
            lineNumber = ta.getLineOfOffset(ta.getCaretPosition()) + 1;
            String tempStr = JOptionPane.showInputDialog(f, "Enter Line Number:", "" + lineNumber);
            if (tempStr == null) {
                return;
            }
            lineNumber = Integer.parseInt(tempStr);
            ta.setCaretPosition(ta.getLineStartOffset(lineNumber - 1));
        } catch (BadLocationException | NumberFormatException e) {
        }
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        String cmdText = ev.getActionCommand();

        switch (cmdText) {
            case fileNew:
                fileHandler.newFile();
                break;
            case fileOpen:
                fileHandler.openFile();
                updateFont();
                break;
            case fileSave:
                fileHandler.saveThisFile();
                break;
            case fileSaveAs:
                fileHandler.saveAsFile();
                break;
            case fileExit:
                if (fileHandler.confirmSave()) {
                    System.exit(0);
                }
                break;
            case filePrint:
                //print
                try {
                    PrinterJob printerJob = PrinterJob.getPrinterJob();
                    printerJob.setPrintable(this);
                    if (!printerJob.printDialog()) {
                        return;
                    }
                    printerJob.print();
                } catch (Exception e) {
                }
                break;
            case editCut:
                ta.cut();
                break;
            case editCopy:
                ta.copy();
                break;
            case editPaste:
                ta.paste();
                break;
            case editDelete:
                ta.replaceSelection("");
                break;
            case editFind:
                if (Notepad.this.ta.getText().length() == 0) {
                    return;	// text box have no text
                }
                if (findReplaceDialog == null) {
                    findReplaceDialog = new FindDialog(Notepad.this.ta);
                }
                findReplaceDialog.showDialog(Notepad.this.f, true);//find
                break;
            case editFindNext:
                if (Notepad.this.ta.getText().length() == 0) {
                    return;	// text box have no text
                }
                if (findReplaceDialog == null) {
                    statusBar.setText("Nothing to search for, use Find option of Edit Menu first !!!!");
                } else {
                    findReplaceDialog.findNextWithSelection();
                }
                break;
            case editReplace:
                if (Notepad.this.ta.getText().length() == 0) {
                    return;	// text box have no text
                }
                if (findReplaceDialog == null) {
                    findReplaceDialog = new FindDialog(Notepad.this.ta);
                }
                findReplaceDialog.showDialog(Notepad.this.f, false);//replace
                break;
            case editGoTo:
                if (Notepad.this.ta.getText().length() == 0) {
                    return;	// text box have no text
                }
                goTo();
                break;
            case editSelectAll:
                ta.selectAll();
                break;
            case editTimeDate:
                ta.insert(new Date().toString(), ta.getSelectionStart());
                break;
            case formatWordWrap: {
                JCheckBoxMenuItem temp = (JCheckBoxMenuItem) ev.getSource();
                ta.setLineWrap(temp.isSelected());
                break;
            }
            case formatFont:
                if (fontDialog == null) {
                    fontDialog = new FontChooser(ta.getFont());
                }
                if (fontDialog.showDialog(Notepad.this.f, "Choose a font")) {
                    Notepad.this.ta.setFont(fontDialog.createFont());
                }
                break;
            case formatForeground:
                showForegroundColorDialog();
                break;
            case formatBackground:
                showBackgroundColorDialog();
                break;
            case viewStatusBar: {
                JCheckBoxMenuItem temp = (JCheckBoxMenuItem) ev.getSource();
                statusBar.setVisible(temp.isSelected());
                break;
            }
            case helpAboutNotepad:
                AboutJNotepad about = new AboutJNotepad();
                about.show();
                about.setBackground(Color.GRAY);
                break;
            default:
                statusBar.setText("This " + cmdText + " command is yet to be implemented");
                break;
        }
    }

    void showBackgroundColorDialog() {
        if (bcolorChooser == null) {
            bcolorChooser = new JColorChooser();
        }
        if (backgroundDialog == null) {
            backgroundDialog = JColorChooser.createDialog(Notepad.this.f,
                    formatBackground,
                    false,
                    bcolorChooser, (ActionEvent evvv) -> {
                        Notepad.this.ta.setBackground(bcolorChooser.getColor());
                    },
                    null);
        }

        backgroundDialog.setVisible(true);
    }

    void showForegroundColorDialog() {
        if (fcolorChooser == null) {
            fcolorChooser = new JColorChooser();
        }
        if (foregroundDialog == null) {
            foregroundDialog = JColorChooser.createDialog(Notepad.this.f,
                    formatForeground,
                    false,
                    fcolorChooser, (ActionEvent evvv) -> {
                        Notepad.this.ta.setForeground(fcolorChooser.getColor());
                    },
                    null);
        }

        foregroundDialog.setVisible(true);
    }

    JMenuItem createMenuItem(String s, int key, JMenu toMenu, ActionListener al) {
        JMenuItem temp = new JMenuItem(s, key);
        temp.addActionListener(al);
        toMenu.add(temp);

        return temp;
    }

    JMenuItem createMenuItem(String s, int key, JMenu toMenu, int aclKey, ActionListener al) {
        JMenuItem temp = new JMenuItem(s, key);
        temp.addActionListener(al);
        temp.setAccelerator(KeyStroke.getKeyStroke(aclKey, ActionEvent.CTRL_MASK));
        toMenu.add(temp);

        return temp;
    }

    JCheckBoxMenuItem createCheckBoxMenuItem(String s, int key, JMenu toMenu, ActionListener al) {
        JCheckBoxMenuItem temp = new JCheckBoxMenuItem(s);
        temp.setMnemonic(key);
        temp.addActionListener(al);
        temp.setSelected(false);
        toMenu.add(temp);

        return temp;
    }

    JMenu createMenu(String s, int key, JMenuBar toMenuBar) {
        JMenu temp = new JMenu(s);
        temp.setMnemonic(key);
        toMenuBar.add(temp);
        return temp;
    }

    void createMenuBar(JFrame f) {
        JMenuBar mb = new JMenuBar();
        JMenuItem temp;

        JMenu fileMenu = createMenu(fileText, KeyEvent.VK_F, mb);
        JMenu editMenu = createMenu(editText, KeyEvent.VK_E, mb);
        JMenu formatMenu = createMenu(formatText, KeyEvent.VK_O, mb);
        JMenu viewMenu = createMenu(viewText, KeyEvent.VK_V, mb);
        JMenu helpMenu = createMenu(helpText, KeyEvent.VK_H, mb);

        createMenuItem(fileNew, KeyEvent.VK_N, fileMenu, KeyEvent.VK_N, this);
        createMenuItem(fileOpen, KeyEvent.VK_O, fileMenu, KeyEvent.VK_O, this);
        createMenuItem(fileSave, KeyEvent.VK_S, fileMenu, KeyEvent.VK_S, this);
        createMenuItem(fileSaveAs, KeyEvent.VK_A, fileMenu, this);
        fileMenu.addSeparator();
        temp = createMenuItem(filePageSetup, KeyEvent.VK_U, fileMenu, this);
        temp.setEnabled(false);
        createMenuItem(filePrint, KeyEvent.VK_P, fileMenu, KeyEvent.VK_P, this);
        fileMenu.addSeparator();
        createMenuItem(fileExit, KeyEvent.VK_X, fileMenu, this);

        temp = createMenuItem(editUndo, KeyEvent.VK_U, editMenu, KeyEvent.VK_Z, this);
        temp.setEnabled(false);
        editMenu.addSeparator();
        cutItem = createMenuItem(editCut, KeyEvent.VK_T, editMenu, KeyEvent.VK_X, this);
        copyItem = createMenuItem(editCopy, KeyEvent.VK_C, editMenu, KeyEvent.VK_C, this);
        createMenuItem(editPaste, KeyEvent.VK_P, editMenu, KeyEvent.VK_V, this);
        deleteItem = createMenuItem(editDelete, KeyEvent.VK_L, editMenu, this);
        deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        editMenu.addSeparator();
        findItem = createMenuItem(editFind, KeyEvent.VK_F, editMenu, KeyEvent.VK_F, this);
        findNextItem = createMenuItem(editFindNext, KeyEvent.VK_N, editMenu, this);
        findNextItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        replaceItem = createMenuItem(editReplace, KeyEvent.VK_R, editMenu, KeyEvent.VK_H, this);
        gotoItem = createMenuItem(editGoTo, KeyEvent.VK_G, editMenu, KeyEvent.VK_G, this);
        editMenu.addSeparator();
        selectAllItem = createMenuItem(editSelectAll, KeyEvent.VK_A, editMenu, KeyEvent.VK_A, this);
        createMenuItem(editTimeDate, KeyEvent.VK_D, editMenu, this).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));

        createCheckBoxMenuItem(formatWordWrap, KeyEvent.VK_W, formatMenu, this);

        createMenuItem(formatFont, KeyEvent.VK_F, formatMenu, this);
        formatMenu.addSeparator();
        createMenuItem(formatForeground, KeyEvent.VK_T, formatMenu, this);
        createMenuItem(formatBackground, KeyEvent.VK_P, formatMenu, this);

        createCheckBoxMenuItem(viewStatusBar, KeyEvent.VK_S, viewMenu, this).setSelected(true);

        LookAndFeelMenu.createLookAndFeelMenuItem(viewMenu, this.f);

        temp = createMenuItem(helpHelpTopic, KeyEvent.VK_H, helpMenu, this);
        temp.setEnabled(false);
        helpMenu.addSeparator();
        createMenuItem(helpAboutNotepad, KeyEvent.VK_A, helpMenu, this);

        MenuListener editMenuListener = new MenuListener() {
            @Override
            public void menuSelected(MenuEvent evvvv) {
                if (Notepad.this.ta.getText().length() == 0) {
                    findItem.setEnabled(false);
                    findNextItem.setEnabled(false);
                    replaceItem.setEnabled(false);
                    selectAllItem.setEnabled(false);
                    gotoItem.setEnabled(false);
                } else {
                    findItem.setEnabled(true);
                    findNextItem.setEnabled(true);
                    replaceItem.setEnabled(true);
                    selectAllItem.setEnabled(true);
                    gotoItem.setEnabled(true);
                }
                if (Notepad.this.ta.getSelectionStart() == ta.getSelectionEnd()) {
                    cutItem.setEnabled(false);
                    copyItem.setEnabled(false);
                    deleteItem.setEnabled(false);
                } else {
                    cutItem.setEnabled(true);
                    copyItem.setEnabled(true);
                    deleteItem.setEnabled(true);
                }
            }

            @Override
            public void menuDeselected(MenuEvent evvvv) {
            }

            @Override
            public void menuCanceled(MenuEvent evvvv) {
            }
        };
        editMenu.addMenuListener(editMenuListener);
        f.setJMenuBar(mb);
    }

    public static void main(String[] s) {
        Notepad notepad = new Notepad();
    }

    private void createToolBar(JToolBar jtb, JFrame f) {
        JButton tbBtn[] = new JButton[14];
        String toolTipTextBtn[] = {
            "New File", "Opne File", "Save File", "Save As File", "Cut", "Copy", "Paste", "Undo", "Redo", "Find", "Find And Replace",
            "Print", "Zoom In", "Zoom Out"};
        ImageIcon imgIcon[] = {new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/NewFile.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/File.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/Save.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/SaveAs.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/Cut.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/Copy.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/Paste.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/Undo.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/Redo.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/Find.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/FindAndReplace.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/Print.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/zoomIn.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/zoomOut.png"))
        };

        JButton tbStyleBtn[] = new JButton[5];
        ImageIcon imgStyleIcon[] = {
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/Bold.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/Italic.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/Underline.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/TextColor.png")),
            new ImageIcon(ClassLoader.getSystemResource("JNotepad/Images/BackColor.png"))

        };

        String[] fontNames
                = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getAvailableFontFamilyNames();
        String[] fontSizes = new String[30];
        for (int j = 0; j < 30; j++) {
            fontSizes[j] = new String(10 + j * 2 + "");
        }

        FontChooser fontChooser = new FontChooser(ta.getFont());
        JComboBox fontTypeCmbBox = new JComboBox(fontNames);
        JComboBox fontSizeCmbBox = new JComboBox(fontSizes);
        fontTypeCmbBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent ie) {
                String s = (String) fontTypeCmbBox.getSelectedItem();
                setFontType(s);
                ta.setFont(new Font(s, getFontStyle(), getFontSize()));
                updateFont();
            }

        });

        for (int i = 0; i < tbBtn.length; i++) {
            tbBtn[i] = new JButton(imgIcon[i]);
            jtb.add(tbBtn[i]);
            tbBtn[i].addActionListener(this);
            tbBtn[i].setToolTipText(toolTipTextBtn[i]);

        }

        fontSizeCmbBox.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent ie) {
                String s = (String) fontSizeCmbBox.getSelectedItem();
                int fs = Integer.parseInt(s);
                setFontSize(fs);
                ta.setFont(new Font(getFontType(), getFontStyle(), fs));
                updateFont();
            }

        });
        jtb.add(fontTypeCmbBox);
        jtb.add(fontSizeCmbBox);
        for (int i = 0; i < tbStyleBtn.length; i++) {
            tbStyleBtn[i] = new JButton(imgStyleIcon[i]);
            jtb.add(tbStyleBtn[i]);
            tbStyleBtn[i].addActionListener(this);

        }
        //font
        //bold
        tbStyleBtn[0].addActionListener((ActionEvent ae) -> {
            ta.setFont(new Font(getFontType(), Font.BOLD, getFontSize()));

        });
        tbStyleBtn[1].addActionListener((ActionEvent ae) -> {
            //italic
            ta.setFont(new Font(getFontType(), Font.ITALIC, getFontSize()));

        });
        tbStyleBtn[2].addActionListener((ActionEvent ae) -> {
            //underline

        });
        tbStyleBtn[3].addActionListener((ActionEvent ae) -> {
            //font color
            showForegroundColorDialog();
        });
        tbStyleBtn[4].addActionListener((ActionEvent ae) -> {
            //back color
            showBackgroundColorDialog();

        });

        //toolbar buttons
        tbBtn[0].addActionListener((ActionEvent ae) -> {
            //new file
            fileHandler.newFile();
        });
        tbBtn[1].addActionListener((ActionEvent ae) -> {
            //open file
            fileHandler.openFile();
            updateFont();

        });
        tbBtn[2].addActionListener((ActionEvent ae) -> {
            //save file
            fileHandler.saveThisFile();
        });
        tbBtn[3].addActionListener((ActionEvent ae) -> {
            //save as
            fileHandler.saveAsFile();
        });
        tbBtn[4].addActionListener((ActionEvent ae) -> {
            //cut
            ta.cut();
        });

        tbBtn[5].addActionListener((ActionEvent ae) -> {
            //copy
            ta.copy();

        });
        tbBtn[6].addActionListener((ActionEvent ae) -> {
            //paste
            ta.paste();
        });
        tbBtn[7].addActionListener((ActionEvent ae) -> {
            //undo

        });
        tbBtn[8].addActionListener((ActionEvent ae) -> {
            //redo

        });
        tbBtn[9].addActionListener((ActionEvent ae) -> {
            //find
            if (Notepad.this.ta.getText().length() == 0) {
                return;	// text box have no text
            }
            if (findDialog == null) {
                findDialog = new FindDialog(Notepad.this.ta);
            }
            findDialog.showDialog(Notepad.this.f, true);
        });

        tbBtn[10].addActionListener((ActionEvent ae) -> {
            //replace
            if (Notepad.this.ta.getText().length() == 0) {
                return;	// text box have no text
            }
            if (findReplaceDialog == null) {
                findReplaceDialog = new FindDialog(Notepad.this.ta);
            }
            findReplaceDialog.showDialog(Notepad.this.f, false);

        });
        tbBtn[11].addActionListener((ActionEvent ae) -> {
            //replace
            try {
                PrinterJob printerJob = PrinterJob.getPrinterJob();
                printerJob.setPrintable(this);
                if (!printerJob.printDialog()) {
                    return;
                }
                printerJob.print();
            } catch (HeadlessException | PrinterException e) {
            }

        });
        tbBtn[12].addActionListener((ActionEvent ae) -> {
            //zoom in

            int fontsize = this.getFontSize();
            if (tbBtn[12].isVisible()) {
                ta.setFont(new Font(getFontType(), getFontStyle(), fontsize));
                fontSize = fontSize + 8;

            }

        });
        tbBtn[13].addActionListener((ActionEvent ae) -> {
            //zoom out
            int fontsize = this.getFontSize();

            if (tbBtn[12].isVisible()) {
                ta.setFont(new Font(getFontType(), getFontStyle(), fontsize));
                fontSize = fontSize - 8;

            }

        });

        f.add(jtb, BorderLayout.NORTH);
    }

    private int getFontStyle() {
        return this.fontStyle;
    }

    private int getFontSize() {
        return this.fontSize;
    }

    private String getFontType() {
        return this.fontType;
    }

    private void setFontStyle(int fontstyle) {
        this.fontStyle = fontStyle;
    }

    private void setFontSize(int fontsize) {
        this.fontSize = fontsize;
    }

    private void setFontType(String fonttype) {
        this.fontType = fonttype;
    }

    public void updateFont() {
        ta.setFont(new Font(getFontType(), getFontStyle(), getFontSize()));
    }

    @Override
    public int print(Graphics grphcs, PageFormat pf, int i) throws PrinterException {
        return 0;
    }

}
