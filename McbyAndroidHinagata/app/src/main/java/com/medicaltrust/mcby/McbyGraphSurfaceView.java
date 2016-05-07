package com.medicaltrust.mcby;

import com.medicaltrust.manager.FilterManager;
import com.medicaltrust.manager.TitleManager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;


public class McbyGraphSurfaceView extends SurfaceView implements SurfaceHolder.Callback,Runnable{

	/** For Debug */
	public static final boolean DEBUG=false;
	public static final boolean DEBUG2=true;

	/** ループ内でのスリープ時間*/
	private static final int SLEEP_TIME = 1;
	private static int sleepTime=SLEEP_TIME;

	/** 画面のサイズ */
	//public static final int SCREEN_W=700,SCREEN_H=400;

	/** タイトルモード */
	public static final int TITLE_MODE = 0;
	/** 計算モード   */
	public static final int CALC_MODE = 1;
	/** 終了モード   */
	public static final int EXIT_MODE = 2;
	
	/** フレーム */
	public static final int FRAME=10;

	public static int AQUA   =(  0<<16)+(255<<8)+255;
    public static int BLACK  =(  0<<16)+(  0<<8)+  0;
    public static int BLUE   =(  0<<16)+(  0<<8)+255;
    public static int FUCHSIA=(255<<16)+(  0<<8)+255;
    public static int GRAY   =(128<<16)+(128<<8)+128;
    public static int GREEN  =(  0<<16)+(128<<8)+  0;
    public static int LIME   =(  0<<16)+(255<<8)+  0;
    public static int MAROON =(128<<16)+(  0<<8)+  0;
    public static int NAVY   =(  0<<16)+(  0<<8)+128;
    public static int OLIVE  =(128<<16)+(128<<8)+  0;
    public static int PURPLE =(128<<16)+(  0<<8)+128;
    public static int RED    =(255<<16)+(  0<<8)+  0;
    public static int SILVER =(192<<16)+(192<<8)+192;
    public static int TEAL   =(  0<<16)+(128<<8)+128;
    public static int WHITE  =(255<<16)+(255<<8)+255;
    public static int YELLOW =(255<<16)+(255<<8)+  0;
    
	//入力波形の色
	private static final int INPUT_COLOR = RED;
	//出力波形の色
	private static final int OUTPUT_COLOR = BLUE;
	
	//描画バッファ 512ポイント
	private double[] input  = new double[512 ];
	private double[] output = new double[512 ];
	
	//周波数などを表示	
	private String infoLabel = "";
	

	//サーフェイスビュー
    private SurfaceHolder holder;//サーフェイスホルダー
    private Thread        thread;//スレッド
	
   
    public MyAndroidGraphics          mc;           //グラフィックス
    private Bitmap[]     image=new Bitmap[3];       //画像
    
    private Button btn1,btn2;
    
    //サンプリング周期 1/250=0.004
	//public double stime=0.004;
	public double alltime=0.004*input.length;
	public TitleManager titleManager;
	public FilterManager filterManager;
	/** 状態 */
	public static int mode_state;
	/** ひとつ前の状態 */
	public int back_mode_state;
	
	private int width;
    private int height;
    
	//コンストラクタ
    public McbyGraphSurfaceView(Context context) {
        super(context);

      //サーフェイスホルダーの生成
        holder=getHolder();
        holder.setFixedSize(getWidth(),getHeight());
        
        
        
        holder.addCallback(this);
        
        //フォーカス指定
        //ここをONにしないとタッチキーやボールが有効にならない
        setFocusable(true);     
        setFocusableInTouchMode(true);
      
    }
	
  //サーフェイスの生成
    public void surfaceCreated(SurfaceHolder holder) {
        
    	width=getWidth();
        height=getHeight();
        //System.out.println("widht="+width);
        //System.out.println("Height="+height);
        
        //グラフィックスの取得
        mc=new MyAndroidGraphics(holder,width,height);
          
      	//minx maxx miny maxy
  		mc.setViewport(0, 1, 0.6, 1);
  		mc.setViewport(0, 1, 0.2, 0.6);
  		mc.setViewport(0, 1,   0, 0.2);
  		
  		//ユーザ座標系の設定 
  		//minx maxx miny maxy
  		mc.setUserWindow(0,200,0,100);
  		mc.setUserWindow(0,200,0,100);
  		mc.setUserWindow(0,100,0,20);
      	
  		titleManager  = new TitleManager(mc);
  		filterManager = new FilterManager(mc);
  		
  		
        //スレッドの開始
        thread=new Thread(this);
        thread.start();
    }

    //サーフェイスの終了
    public void surfaceDestroyed(SurfaceHolder holder) {
        thread=null;
    }

    //サーフェイスの変更
    public void surfaceChanged(SurfaceHolder holder,
        int format,int w,int h) {
    }   
      
    //スレッドの処理
    public void run() {  
    	
    	
    	
  		
  		//最初はタイトルを表示
  		mode_state = TITLE_MODE;
  		back_mode_state = mode_state;       
        //フォーカスの指定
        //setFocusable(true);     
        //setFocusableInTouchMode(true);
        
        
    	// キーの初期化
    	//Key.init();
    	//スリープ時間
    	long startTime = System.currentTimeMillis();
    	//計算にかかった時間
    	long pastTime = 0;
    	
    	SetCalcWindow();
    	
        while (thread!=null) {
            
        	//キーを更新
			//key.registKeyEvent();					
					
			//キー、計算、描画の各種処理
			if(mode_state == TITLE_MODE){//タイトルモード
				titleManager.process();
			}else if(mode_state == CALC_MODE){//計算モード
				
				
				filterManager.process();
				
				Log.d("TEST","fileManager");
			}	

			if(back_mode_state != mode_state){//モードの変更がないかどうかチェック。
				//今の状態を取っておく
				if(mode_state == CALC_MODE ){
					SetCalcWindow();
				}
				back_mode_state = mode_state;
			}
			
			// repaint()でpaint(g)の呼び出し,初めて描画が更新される
			
        	//計算にかかった時間
    		pastTime = System.currentTimeMillis() - startTime;					
    				
    		if(pastTime < sleepTime){
    			//休止
    			try{
    				Thread.sleep(sleepTime+5 - pastTime);
    			}catch(Exception e){}
    		}	
    		startTime = System.currentTimeMillis();
        }
        
    }

    public void SetCalcWindow(){
    	
    	//ボタン関係準備
    	double[]  bminx={40},bmaxx={50},bminy={5},bmaxy={10};
    	mc.getWindowPosition(bminx,bmaxx,bminy,bmaxy,3);
    	
    	
    	//if(DEBUG2) System.out.println("2 minx"+bminx[0]+"maxx"+bmaxx[0]+"miny"+bminy[0]+"maxy"+bmaxy[0]);
    	bminx[0]=50;
    	bmaxx[0]=60;
    	bminy[0]=5;
    	bmaxy[0]=10;
    	
    	mc.getWindowPosition(bminx,bmaxx,bminy,bmaxy,3);
    	
    	
    }

   
	
    

	/**
	 * モードを変更する
	 * 実際に反映されるのは、ループ内での次の状態のチェック時
	 * 
	 * @param mode
	 */
	public static void setMode(int mode){
		mode_state = mode;
	}
	
	
	//////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////
	private boolean              keyDown;           //キーダウン
	private boolean              touchDown;         //タッチダウン
	private static int ballX=0;
	private static int ballY=0;
	private static int ballAction=-999;
	
    //タッチイベントの処理
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int touchAction=event.getAction();
        if (touchAction==MotionEvent.ACTION_DOWN) {
            keyDown=true;
        } else if (touchAction==MotionEvent.ACTION_UP ||
            touchAction==MotionEvent.ACTION_CANCEL) {
            keyDown=false;
        }
        return true;
    }
    
    //キーダウンイベントの処理
    @Override
    public boolean onKeyDown(int keyCode,KeyEvent event){
    	if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER) touchDown=true;
    	
    	return super.onKeyDown(keyCode, event);
    	
    	
    }
  //キーダウンイベントの処理
    @Override
    public boolean onKeyUp(int keyCode,KeyEvent event){
    	if(keyCode==KeyEvent.KEYCODE_DPAD_CENTER) touchDown=false;
    	
    	return super.onKeyUp(keyCode, event);
    	
    	
    }
    int ballCount=5;
    static int backBallX=0;
    @Override
    public boolean onTrackballEvent(MotionEvent event){
    	
    	ballAction=event.getAction();
    	if(ballAction==MotionEvent.ACTION_MOVE){
    		ballX=(int)(event.getX()*100);
        	ballY=(int)(event.getY()*100);
			
        	if(ballX > backBallX) ballCount++;
        	if(ballX < backBallX) ballCount--;
        	
			if(ballCount <= 1) ballCount=1;
			if(ballCount >= 100) ballCount=100;
	    
			filterManager.setFrequency((double)ballCount);
			
			backBallX=ballX;
    	}
	    //filterManager.setDisplay(ballX);
		//}
    	return true;
    }
}
