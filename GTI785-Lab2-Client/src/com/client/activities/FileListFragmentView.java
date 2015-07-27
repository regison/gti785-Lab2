package com.client.activities;

import com.client.gti785_lab2.R;
import com.client.servermanager.ServerObject;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FileListFragmentView extends Fragment  {
		
	 @Override 
	   public View onCreateView(LayoutInflater inflater,
	      ViewGroup container, Bundle savedInstanceState) {
	       
	      // Inflate the layout for this fragment 
	         
	      return inflater.inflate(
	              R.layout.fragment_files, container, false);
	      }
}
