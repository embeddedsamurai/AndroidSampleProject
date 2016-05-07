package com.medicaltrust.mcby;

import com.medicaltrust.manager.FilterManager;
import com.medicaltrust.manager.TitleManager;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.content.res.Resources;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.opengl.GLSurfaceView;

import android.util.Log;

import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Button;
// import android.view.SurfaceHolder;
// import android.view.SurfaceView;

public class McbyGraphRenderer implements GLSurfaceView.Renderer {
// public class McbyGraphSurfaceView
//   extends SurfaceView implements SurfaceHolder.Callback, Runnable {

//  private static final long SLEEP_TIME = 1;
  private static final long SLEEP_TIME = 33;

  //public static final int SCREEN_W=700,SCREEN_H=400;

  private Context context;

  public static enum Mode { Title, Calc, Exit }

  private Mode modeState;
  private Mode backModeState;

  /*
  public static final int TITLE_MODE = 0;
  public static final int CALC_MODE = 1;
  public static final int EXIT_MODE = 2;
  public static int mode_state;
  public int backModeState;
  */

  private double[] input  = new double[512];
  private double[] output = new double[512];
	
  // private SurfaceHolder holder;
  // private Thread thread;
  
  private long startTime;
  private long pastTime;

  private int width;
  private int height;

  // String to display?
  // private String infoLabel = "";
  // private Bitmap[] image = new Bitmap[3];
  // private Button btn1, btn2;
    
  // sampling rate 1/250=0.004
  // public double stime = 0.004;
  // public double alltime = 0.004*input.length;

  public TitleManager titleManager;
  public FilterManager filterManager;

  public MyAndroidGraphics mc;

  /* USELESS 
     public static final int FRAME = 10;

     public static int AQUA    = (  0<<16)+(255<<8)+255;
     public static int BLACK   = (  0<<16)+(  0<<8)+  0;
     public static int BLUE    = (  0<<16)+(  0<<8)+255;
     public static int FUCHSIA = (255<<16)+(  0<<8)+255;
     public static int GRAY    = (128<<16)+(128<<8)+128;
     public static int GREEN   = (  0<<16)+(128<<8)+  0;
     public static int LIME    = (  0<<16)+(255<<8)+  0;
     public static int MAROON  = (128<<16)+(  0<<8)+  0;
     public static int NAVY    = (  0<<16)+(  0<<8)+128;
     public static int OLIVE   = (128<<16)+(128<<8)+  0;
     public static int PURPLE  = (128<<16)+(  0<<8)+128;
     public static int RED     = (255<<16)+(  0<<8)+  0;
     public static int SILVER  = (192<<16)+(192<<8)+192;
     public static int TEAL    = (  0<<16)+(128<<8)+128;
     public static int WHITE   = (255<<16)+(255<<8)+255;
     public static int YELLOW  = (255<<16)+(255<<8)+  0;

     private static final int INPUT_COLOR = RED;
     private static final int OUTPUT_COLOR = BLUE;
  */
	
  public McbyGraphRenderer (Context c) {

    context = c;
    
    /*
    holder=getHolder();
    holder.setFixedSize(getWidth(),getHeight());
    holder.addCallback(this);
    */

    /*
    setFocusable(true);     
    setFocusableInTouchMode(true);
    */
  }

  /* OLD USELESS METHOD
  public void onDrawFrame(GL10 gl) {
    mc = new MyAndroidGraphics(GL);
  }
  */

  // onSurfaceChanged <-> surfaceCreated
  public void onSurfaceChanged(GL10 gl, int w, int h) {

    mc = new MyAndroidGraphics(gl, w, h);

    mc.setViewport(0, 1, 0.6, 1);
    mc.setViewport(0, 1, 0.2, 0.6);
    mc.setViewport(0, 1,   0, 0.2);
  		
    mc.setUserWindow(0, 200, 0, 100);
    mc.setUserWindow(0, 200, 0, 100);
    mc.setUserWindow(0, 100, 0, 20);

    Resources res = context.getResources();
    try {
      mc.setTexture(R.drawable.chars16,
                    loadTexture(gl, res, R.drawable.chars16, "chars"));
    } catch (IOException e) {
      Log.e("OnSurfaceChanged", "Failed to load texture: "+e);
    }
      	
    mc.setFontSize(12);

    titleManager = new TitleManager(mc, this);
    filterManager = new FilterManager(mc);

    // onDrawFrame is the alternative to thread.
    // thread = new Thread(this);
    // thread.start();

    modeState = Mode.Title;
    backModeState = modeState;
    
    //setFocusable(true);
    //setFocusableInTouchMode(true);
    //Key.init();

    startTime = System.currentTimeMillis();
    // long startTime = System.currentTimeMillis();

    pastTime = 0;
    // spent time for calculation
    // long pastTime = 0;
    	
    SetCalcWindow();
  }

  public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) { }

  /*
  public void surfaceCreated(SurfaceHolder holder) {
        
    width=getWidth();
    height=getHeight();
    //System.out.println("widht="+width);
    //System.out.println("Height="+height);
        
    mc=new MyAndroidGraphics(holder,width,height);
          
    // minx maxx miny maxy
    mc.setViewport(0, 1, 0.6, 1);
    mc.setViewport(0, 1, 0.2, 0.6);
    mc.setViewport(0, 1,   0, 0.2);
  		
    // user coorinate system
    //minx maxx miny maxy
    mc.setUserWindow(0,200,0,100);
    mc.setUserWindow(0,200,0,100);
    mc.setUserWindow(0,100,0,20);
      	
    titleManager = new TitleManager(mc);
    filterManager = new FilterManager(mc);
  		
    thread = new Thread(this);
    thread.start();
  }
  public void surfaceDestroyed(SurfaceHolder holder) {
    thread = null;
  }
  public void surfaceChanged(SurfaceHolder holder,
                             int format,int w,int h) {
  }   
  */

  public void onDrawFrame (GL10 gl) {

    switch (modeState) {
    case Title: 
      titleManager.process(); break;
    case Calc:
      filterManager.process(); break;
    }
      
    // if mode was changed
    if (backModeState != modeState){
      switch (modeState) {
      case Calc:
        setCalcWindow();
      default:
        backModeState = modeState;
      }
    }

    pastTime = System.currentTimeMillis() - startTime;					
    if (pastTime < SLEEP_TIME){
      try {
        Thread.sleep(SLEEP_TIME+5 - pastTime);
      } catch(Exception e) { }
    }
    startTime = System.currentTimeMillis();
  }

  /*
  public void run() {  

    mode_state = TITLE_MODE;
    back_mode_state = mode_state;
    
    //setFocusable(true);     
    //setFocusableInTouchMode(true);

    //Key.init();

    long startTime = System.currentTimeMillis();

    // spent time for calculation
    long pastTime = 0;
    	
    SetCalcWindow();
    	
    while (thread!=null) {
            
      //key.registKeyEvent();					
					
      if(mode_state == TITLE_MODE) {

        titleManager.process();

      } else if(mode_state == CALC_MODE) {

        filterManager.process();
        Log.d("TEST","fileManager");

      }	

      // if mode was changed
      if(back_mode_state != mode_state){
        if(mode_state == CALC_MODE )
          SetCalcWindow();
        back_mode_state = mode_state;
      }

      // repaint() calls paint(g)
			
      pastTime = System.currentTimeMillis() - startTime;					
      if(pastTime < sleepTime){
        try{
          Thread.sleep(sleepTime+5 - pastTime);
        }catch(Exception e){}
      }	
      startTime = System.currentTimeMillis();
    }
        
  }
  */

  // Load texture
  static private int loadTexture (GL10 gl, Resources res, int id, String name)
    throws IOException {
    int texture = GraphicUtils.loadTexture(gl, res, id);
    if (texture == 0) throw new IOException(name);
    return texture;
  }
  
  private void SetCalcWindow () { setCalcWindow(); }
  private void setCalcWindow () { 
    	
    double[]  bminx={40}, bmaxx={50}, bminy={5}, bmaxy={10};
    // mc.getWindowPosition(bminx, bmaxx, bminy, bmaxy, 3);
    	
    // if(DEBUG2)
    //   System.out.println("2 minx"+bminx[0]+"maxx"+bmaxx[0]
    //                      +"miny"+bminy[0]+"maxy"+bmaxy[0]);
    bminx[0] = 50;
    bmaxx[0] = 60;
    bminy[0] = 5;
    bmaxy[0] = 10;
    	
    // mc.getWindowPosition(bminx, bmaxx, bminy, bmaxy, 3);
  }

  /*==================================================*/
  /*
  private boolean keyDown;
  private boolean touchDown;
  private static int ballX = 0;
  private static int ballY = 0;
  private static int ballAction = -999;
	
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    int touchAction = event.getAction();
    if (touchAction == MotionEvent.ACTION_DOWN) {
      keyDown=true;
    } else if (touchAction == MotionEvent.ACTION_UP ||
               touchAction == MotionEvent.ACTION_CANCEL) {
      keyDown=false;
    }
    return true;
  }
    
  @Override
  public boolean onKeyDown(int keyCode,KeyEvent event){
    if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER) touchDown = true;
    	
    return super.onKeyDown(keyCode, event);
  }

  @Override
  public boolean onKeyUp(int keyCode,KeyEvent event){
    if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER) touchDown = false;
    	
    return super.onKeyUp(keyCode, event);
  }
  
  int ballCount = 5;
  static int backBallX = 0;
  
  @Override
  public boolean onTrackballEvent(MotionEvent event){
    	
    ballAction = event.getAction();
    if(ballAction == MotionEvent.ACTION_MOVE){
      ballX = (int)(event.getX()*100);
      ballY = (int)(event.getY()*100);
			
      if(ballX > backBallX) ballCount++;
      if(ballX < backBallX) ballCount--;
        	
      if(ballCount <= 1) ballCount = 1;
      if(ballCount >= 100) ballCount = 100;
	    
      filterManager.setFrequency((double)ballCount);
			
      backBallX = ballX;
    }
    //filterManager.setDisplay(ballX);
    //}
    return true;
  }
  */
  /*==================================================*/
  public void setMode (Mode m) {
    modeState = m;
  }
}
