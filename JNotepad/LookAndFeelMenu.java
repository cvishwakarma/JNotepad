/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JNotepad;

import java.awt.Component;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author DELL
 */
public class LookAndFeelMenu
{

public static void createLookAndFeelMenuItem(JMenu jmenu,Component cmp)
{
final UIManager.LookAndFeelInfo[] infos=UIManager.getInstalledLookAndFeels();

JRadioButtonMenuItem rbm[]=new JRadioButtonMenuItem[infos.length];
ButtonGroup bg=new ButtonGroup();
JMenu tmp=new JMenu("Change Look and Feel");
tmp.setMnemonic('C');
for(int i=0; i<infos.length; i++)
{
rbm[i]=new JRadioButtonMenuItem(infos[i].getName());
rbm[i].setMnemonic(infos[i].getName().charAt(0));
tmp.add(rbm[i]);
bg.add(rbm[i]);
rbm[i].addActionListener((ActionListener) new LookAndFeelMenuListener(infos[i].getClassName(),cmp));
}

rbm[0].setSelected(true);
jmenu.add(tmp);

}

}