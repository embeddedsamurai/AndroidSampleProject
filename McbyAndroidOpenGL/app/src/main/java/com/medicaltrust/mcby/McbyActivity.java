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

public class McbyActivity extends Activity {
  public static final String CLASSNAME = "McbyActivity";
  
  // private McbyGraphSurfaceView mcbyview;
	
  @Override
  public void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    McbyGraphSurfaceView mcbyview = new McbyGraphSurfaceView(this);
    mcbyview.setRenderer(new McbyGraphRenderer(this));
    // mcbyview = new McbyGraphSurfaceView(this);
    setContentView(mcbyview);
  }
	 
  @Override
  public void onStart(){
    super.onStart();
    Toast.makeText(this, "onStart", Toast.LENGTH_LONG).show();
    Log.d(CLASSNAME, "onStart");
  }
	
  @Override
  public void onResume() {
    super.onResume();
    Toast.makeText(this, "onResume", Toast.LENGTH_LONG).show();
    Log.d(CLASSNAME, "onResume");
  }
	
  @Override
  public void onPause() {
    super.onPause();
    Toast.makeText(this, "OnPause", Toast.LENGTH_LONG).show();
    MyAndroidGraphics.deleteAllTextures();
    Log.d(CLASSNAME, "OnPause");
  }
	    
  @Override
  public void onStop(){
    super.onStop();
    Log.d(CLASSNAME, "OnStop");
    Toast.makeText(this, "OnStop", Toast.LENGTH_LONG).show();
  }
	
  @Override
  public void onDestroy(){
    super.onDestroy();
    Log.d(CLASSNAME, "onDestroy");
    Toast.makeText(this, "onDestroy", Toast.LENGTH_LONG).show();
  }
}
