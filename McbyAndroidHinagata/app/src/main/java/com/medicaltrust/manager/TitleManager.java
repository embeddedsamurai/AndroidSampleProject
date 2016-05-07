package com.medicaltrust.manager;
/*****************************************************************/
/* Digital Signal Proccessing Program                            */
/*                     programming by embedded.samurai           */
/*****************************************************************/


import java.util.*;

import android.graphics.Color;

import com.medicaltrust.mcby.McbyGraphSurfaceView;
import com.medicaltrust.mcby.MyAndroidGraphics;

public class TitleManager{
	
	/** For Debug */
	public static final boolean DEBUG=true;

	/** Applet コピー用 */
	private McbyGraphSurfaceView  mApplet;
	private MyAndroidGraphics mc;

	/** メニューのインデックスをインクリメント */
	private static final int INCREMENT_MENU_INDEX = 0;
	
	/** メニューのインデックスをデクリメント */
	private static final int DECREMENT_MENU_INDEX = 1;
	
	
	//private Key key;
	/**
	 * コンストラクタ
	 */
	public TitleManager( MyAndroidGraphics mc) {

		
		this.mc = mc;
		
	

	}
	
	/**
	 * メイン処理
	 */
	public void process(){
		//キー処理
		//key();
		touchKey();
		//リクエストの処理
		doRequest();
		//描画
		draw();		
	}
	
		
	/**
		* キー処理
		*/
	private void key(){
		/*
		if(key.isKeyPressed(Key.KEY_RIGHT)){
			if(DEBUG) System.out.println("右");
		}else if(key.isKeyPressed(Key.KEY_LEFT)){
			if(DEBUG) System.out.println("左");
		}else if(key.isKeyPressed(Key.KEY_0)){
			if(DEBUG) System.out.println("0");
		}else if(key.isKeyPressed(Key.KEY_ENTER)){
			FFTGraph.setMode(FFTGraph.CALC_MODE);
		}
		*/
	}
	
	/**
	 * タッチキー
	 */
	private void touchKey(){
		McbyGraphSurfaceView.setMode(McbyGraphSurfaceView.CALC_MODE);
		
	}
	
	
	/**
		* キューの中のリクエストを処理する
		*/
	private void doRequest(){
	}
	
	
	/**
		* 描画処理
		*/
	private void draw() {
		
		int   red=0;
		int green=0;
		int  blue=0;
    
		int color = Color.WHITE;
		mc.lock();
		mc.setColor(color);  
	    mc.AllClear();
		
		color = Color.BLACK;
		mc.setColor(color);  
		
		mc.drawRawString("Mcby Test Program",240/2-20,320/2);
		mc.drawRawString("(c)2010 MedicalTrust",240/2-20,320/2+20);
		mc.unlock(true);
	}
	
	
}
