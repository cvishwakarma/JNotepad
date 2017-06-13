/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package JNotepad;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author DELL
 */
public final class MyFileFilter extends FileFilter
{
private String extension;
private String description;

public MyFileFilter()
{
setExtension(null);
setDescription(null);
}

public MyFileFilter(final String ext, final String desc)
{
setExtension(ext);
setDescription(desc);
}

@Override
public boolean accept(File f)
{
final String filename=f.getName();

if(	f.isDirectory() || 
	extension==null || 
	filename.toUpperCase()
	.endsWith(extension.toUpperCase()))
	return true;
return false;

}

@Override
public String getDescription()
{
return description;
}

public void setDescription(String desc)
{
if(desc==null)
	description="All Files(*.*)";
else
	description=desc;
}

public void setExtension(String ext)
{
if(ext==null)
	{extension=null;  return;}

extension=ext.toLowerCase();
if(!ext.startsWith("."))
	extension="."+extension;
}
}
