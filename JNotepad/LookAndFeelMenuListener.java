/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JNotepad;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 *
 * @author DELL
 */


public class LookAndFeelMenuListener implements ActionListener
{
String classname;
Component jf;

LookAndFeelMenuListener(String cln,Component jf)
{
this.jf=jf;
classname=new String(cln);
}

public void actionPerformed(ActionEvent ev)
{
try
{
UIManager.setLookAndFeel(classname);
SwingUtilities.updateComponentTreeUI(jf);
}
catch(Exception e){System.out.println(e);}
}

}


