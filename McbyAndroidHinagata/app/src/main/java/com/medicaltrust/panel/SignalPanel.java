/*****************************************************************/
/* Digital Signal Proccessing Program                            */
/*                     programming by embedded.samurai           */
/*****************************************************************/
package com.medicaltrust.panel;


import java.util.*;

import com.medicaltrust.mcby.MyAndroidGraphics;

import android.graphics.Color;
import android.util.Log;

public class SignalPanel extends GraphPanel{

	/** For Debug */
	public static final boolean DEBUG=true;

	//入力波形の色
	private static final int INPUT_COLOR = Color.RED;
	//出力波形の色
	private static final int OUTPUT_COLOR = Color.BLUE;
	
	//描画バッファ 512ポイント
	private double[] input  = new double[PNT_WIDTH ];
	private double[] output = new double[PNT_HEIGHT ];
	
	//周波数などを表示	
	private String infoLabel = "";

	MyAndroidGraphics mc;

	//サンプリング周期 1/250=0.004
	//public double stime=0.004;
	public double alltime=0.004*input.length;
	/**
	  * コンストラクタ
	  */
	public SignalPanel(MyAndroidGraphics mc) {
		this.mc=mc;
		alltime=0.004*input.length;
		mc.changeUserWindow(0,alltime,-1,1,1);
		init();
	}	
	
	public void init(){
		//波形描画バッファを初期化する
		for(int i = 0; i < input.length; i++)input[i] = 0;
		for(int i = 0; i < output.length; i++)output[i] = 0;
		
	}
	
	/**
	 * 描画用メソッド
	 */
	public void paintComponent(double dt) {
		//フォントの設定
		//mc.setFont(FONT);
		Log.d("SignalPannel","paintComponent(dt)");
		
		//背景の描画
		drawBackground();
		//波形の描画
		drawWave(dt);
		//ラベルの描画
		drawInfoLabel();
	}	
	
	/**
	 * 背景を描画
	 * @param g Graphicsオブジェクト
	 */
	private void drawBackground(){
		
		double[]  bminx={0},bmaxx={0},bminy={0},bmaxy={0};
		mc.getUserWindow(bminx,bmaxx,bminy,bmaxy,1);
		
		//if(DEBUG2) System.out.println("2 minx"+minx[0]+"maxx"+maxx[0]+"miny"+miny[0]+"maxy"+maxy[0]);
		double minx=bminx[0];
		double maxx=bmaxx[0];
		double miny=bminy[0];
		double maxy=bmaxy[0];
		//System.out.println("2 minx"+minx+"maxx"+maxx+"miny"+miny+"maxy"+maxy);
		
		double userXLength= maxx - minx;
		double userYLength= maxy - miny;

		double userTickX = userXLength / 50.;
		double userTickY = userYLength / 50.;
		
		double userMemoriX = userXLength / 100.;
		double userMemoriY = userYLength / 100.;
		
		//fill background
		mc.setColor(BG_COLOR);				
		mc.fillRect(minx,miny,maxx,maxy, 1);
		
		//draw ground line
		mc.setColor(BG_LINE_COLOR);
		mc.drawLine(minx,0,maxx,0,1);
	
		//mc.setFont(BG_FONT);
		
		
		//目盛り線 横
		int j=0;
		for(double i = minx; i < maxx; i += userTickX){
			
			if(j == 5){
				double k=((int)(i*100))/100.;
				mc.setColor(BG_STR_COLOR);
				mc.drawLine(i, userMemoriY, i, -userMemoriY,1);
				mc.drawString(k + "", i, -userMemoriY*4,1);
				mc.setColor(BG_LINE_COLOR);
				j=0;
			}else{
				mc.drawLine(i, userMemoriY, i, -userMemoriY,1);	
			}
			j++;
		}
		
		//目盛り線 縦	
		j=0;
		for(double i = miny*10; i <= maxy*10 ; i += userTickY*10){
			
			double ii=i/10;
			if(j == 5){
				double k=((int)(ii*100))/100.;
				mc.setColor(BG_STR_COLOR);
				mc.drawLine(0,ii,userMemoriX,ii,1);				
				mc.drawString( k +"", userMemoriX, ii,1);
				mc.setColor(BG_LINE_COLOR);
				j=0;
			}else{				
				mc.drawLine(0,ii,userMemoriX,ii,1);	
			}
			j++;
		}
	}
	
	/**
	 * 入力信号を描画する
	 * @param val
	 */
	public void putInput(double val){	
		for(int i = 0; i < input.length-1 ; i++) input[i] = input[i+1];

		//set new value
		input[input.length-1] = val;
	}
	
	
	/**
	 * 出力信号を描画する
	 * @param val
	 */
	public void putOutput(double val){
		for(int i = 0; i < output.length - 1 ; i++) output[i] = output[i+1];
	
		output[input.length-1] = val;
	}
	
	
	
	/**
	 * 波形を描画
	 * @param g Graphicsオブジェクト
	 */
	private void drawWave(double dt){
		
		mc.setColor(INPUT_COLOR);
		//入力信号を描画
		for(int i = 0; i < input.length - 1 ; i++){
			mc.drawLine(i*dt, input[i],(i + 1)*dt, input[i + 1],1);
		}		
		
		mc.setColor(OUTPUT_COLOR);
		//出力信号を描画
		for(int i = 0; i < output.length - 1; i++){
			mc.drawLine(i*dt, output[i],(i + 1)*dt,output[i + 1],1);
		}
	}
	
	/**
	 * 周波数などの情報を描画 
	 * @param g Graphicsオブジェクト
	 */
	private void drawInfoLabel(){
		//mc.setFont(FONT);
		mc.setColor(BG_STR_COLOR);
	
		mc.drawString(infoLabel,1,0.8,1);
	}
	
	/**
	 * 周波数などの情報を変更 
	 * @param sample サンプリングレート
	 * @param freq　　　周波数
	 */
	public void setInfoLabel(double freq,double sample){
		this.infoLabel = "Sample:" + sample + "Hz" + " Freq:" + freq + "Hz";
	}		
	
	/**
	 * X方向の拡大縮小率を変更
	 * @param value
	 */
	public void setXGain(int value){
		
	}
}
