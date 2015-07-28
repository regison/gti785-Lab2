package com.client.filemanager;

import java.util.ArrayList;

import com.client.gti785_lab2.R;
import com.client.servermanager.ServerObject;



import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class FileAdapter extends ArrayAdapter<File> {

	private ArrayList<File> filesFromServer = null;
	private LayoutInflater inflater = null;
	private Activity context;
	
	public FileAdapter(Activity context, ServerObject srv ) {
		
		super(context, R.layout.file_on_device, srv.getServerFiles());
		
		this.filesFromServer = (ArrayList<File>) srv.getServerFiles();
		this.context = context;
		// TODO Auto-generated constructor stub
		

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// to fix when adding a new server
		View fileView = convertView;		

		VIewHolderFile viewHolder;

		if (convertView == null) {
			fileView = inflater.inflate(R.layout.file_on_device, parent, false);

			viewHolder = new VIewHolderFile();
			viewHolder.setFilePath((TextView) fileView
					.findViewById(R.id.filepath));
			viewHolder.setFileName((TextView) fileView
					.findViewById(R.id.filename));
			viewHolder.setFileSize((TextView) fileView
					.findViewById(R.id.fileSize));
			viewHolder.getFileName().setText(filesFromServer.get(position).getName()); 
			viewHolder.getFilePath().setText(filesFromServer.get(position).getFilePath());
			viewHolder.getFileSize().setText(String.valueOf( filesFromServer.get(position).getSize() ) + " KB");

			

			fileView.setTag(viewHolder);
		} else {
			viewHolder = (VIewHolderFile) fileView.getTag();
		}
	
		return fileView;
	}

}
