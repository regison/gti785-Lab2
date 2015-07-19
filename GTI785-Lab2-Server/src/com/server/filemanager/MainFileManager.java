package com.server.filemanager;

import java.util.ArrayList;

import android.app.Activity;



public class MainFileManager {
	
	private Activity serverActivity = null;
	
	private ArrayList<File> fileList;
	
	
	public MainFileManager(Activity serverActivity){
		this.serverActivity = serverActivity;
		fileList = new  ArrayList<File>();
	}
	
	public ArrayList<File> getFileList(){
		return fileList;
	}
	
	public File getFile(int position){
	
		return getFileList().get(position);
	}

	
}
