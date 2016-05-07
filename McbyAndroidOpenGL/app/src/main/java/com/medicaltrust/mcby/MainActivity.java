package com.medicaltrust.mcby;

import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{
	
  public static final String CLASSNAME = "MainActivity";
  public static final int MSG_CONTROL = 0x0001;
  public static final int MSG_CONTROL_ACK = (MSG_CONTROL  | 0x8000);

  public ImageButton imgbtn;
  public Bitmap bitmap;
	
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
		
    bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title);
    imgbtn = (ImageButton)findViewById(R.id.ImageButton01);
    imgbtn.setImageBitmap(bitmap);
    imgbtn.setOnClickListener(this);
  }
	
  public void onClick(View view){
    Intent intent = new Intent(MainActivity.this, McbyActivity.class);
    startActivityForResult(intent, MSG_CONTROL);
  }
	
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == MSG_CONTROL) {
      if (resultCode == MSG_CONTROL_ACK) {
        //TextView textView = (TextView)findViewById(R.id.TextView01);
        //textView.setText(data.getCharSequenceExtra("TEXT"));
      }
    }
  }
	
}