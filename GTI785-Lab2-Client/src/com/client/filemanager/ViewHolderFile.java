package com.client.filemanager;

import android.widget.TextView;

public class ViewHolderFile {

	private TextView fileName;
	private TextView filePath;
	private TextView fileSize;
	
	public TextView getFileName() {
		return fileName;
	}
	public void setFileName(TextView fileName) {
		this.fileName = fileName;
	}
	public TextView getFilePath() {
		return filePath;
	}
	public void setFilePath(TextView filePath) {
		this.filePath = filePath;
	}
	public TextView getFileSize() {
		return fileSize;
	}
	public void setFileSize(TextView fileSize) {
		this.fileSize = fileSize;
	}
	

}
