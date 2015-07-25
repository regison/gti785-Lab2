package com.client.filemanager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import android.util.Base64;

public class FileManager {
	//We have a list of server for a client
	private ArrayList<File> files;
	
	//Lets make a singleton of this class. per client there is only one server manager
/*	private static FileManager instance = null;
	
	public static FileManager getInstance(){
		if (instance == null)
			instance = new FileManager();
		
		return instance;
	}*/
	
	public FileManager (){
		files =  new ArrayList<File>();		
	}	

	public void setFiles(ArrayList<File> fileList)
	{
		files = fileList;
	}
	
	public void setFilesFromUnencodedString(String fileList)
	{
		String[] list = fileList.split(",");
		
		for(int i=0; i < list.length; i++)
		{
			files.add(new File(list[i]));
		}
	}
	
	public void setFilesFromEncodedString(String fileList) throws UnsupportedEncodingException
	{
		setFilesFromUnencodedString(DecodeString(fileList));
	}
	
	public void AddFile(String file)
	{
		files.add(new File(file));
	}
	
	public void AddFile(File file)
	{
		files.add(file);
	}
	
	public ArrayList<File> getFiles(){
		return files;
	}
	
	private String DecodeString(String files) throws UnsupportedEncodingException
	{
		byte[] octets = Base64.decode(files, Base64.URL_SAFE);
		return new String(octets, "UTF-8");
	}
	
	public File getFileAt(int position){
		return files.get(position);
	}
}
