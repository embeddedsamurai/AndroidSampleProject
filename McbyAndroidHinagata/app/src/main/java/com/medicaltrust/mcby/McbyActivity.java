package com.medicaltrust.mcby;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class McbyActivity extends Activity{

	public static final String CLASSNAME = "McbyActivity";
	McbyGraphSurfaceView mcbyview;
	
	//(1)はじめに呼ばれる
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mcbyview = new McbyGraphSurfaceView(this);
		setContentView(mcbyview);
	}
	 
	//(2)開始時に呼ばれる
	@Override
	public void onStart(){
		super.onStart();
		Toast.makeText(this, "onStart", Toast.LENGTH_LONG).show();
		Log.d(CLASSNAME,"onStart");
	}
	
	
	//(3)ユーザの操作受付を開始したときに呼ばれる
	@Override
	public void onResume() {
		super.onResume();
		Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
		Log.d(CLASSNAME,"onResume");
	}
	
	//(4)ユーザの操作受付を中断したときに呼ばれる
	@Override
	public void onPause() {
		super.onPause();
		Toast.makeText(this, "OnPause", Toast.LENGTH_LONG).show();
		Log.d(CLASSNAME,"OnPause");
	}
	    
	//(5)アクティブティの表示を中断したときに呼ばれる（アクティブティは生きている)
	@Override
	public void onStop(){
		super.onStop();
	     Log.d(CLASSNAME,"OnStop");
	     Toast.makeText(this, "OnStop", Toast.LENGTH_LONG).show();
	}
	
	//(6)アクティブティが死んだときに呼ばれる
	@Override
	public void onDestroy(){
		super.onDestroy();
	     Log.d(CLASSNAME ,"onDestroy");
	     Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show();
	}
	    
}
