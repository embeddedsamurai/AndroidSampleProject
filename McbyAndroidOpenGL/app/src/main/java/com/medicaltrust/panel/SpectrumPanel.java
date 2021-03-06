/*****************************************************************/
/* Digital Signal Proccessing Program                            */
/*                     programming by embedded.samurai           */
/*****************************************************************/

package com.medicaltrust.panel;

import java.util.*;

import com.medicaltrust.manager.FilterManager;
import com.medicaltrust.mcby.MyAndroidGraphics;
import com.medicaltrust.mcby.R;

import android.graphics.Color;

public class SpectrumPanel extends GraphPanel{
	
	//ナイキスト周波数
	public double nyquist;
	//描画用スペクトルのバッファ
	private double[] drawBuffer = new double[PNT_WIDTH];
	
	//描画用スペクトルのバッファ
	private double[] drawBuffer2 = new double[PNT_WIDTH];

        // 横軸
        private double[] drawBufferX = new double[PNT_WIDTH];

	//振幅スペクトルの色
	private static final int WAVE_COLOR = Color.MAGENTA;
	//パネルに表示する情報
	private String infoLabel ="";

        // Y OFFSET
        private static final double OFFSET_Y = 10.0;
	
        MyAndroidGraphics mc;


	/**
	 * コンストラクタ
	 * @param sample サンプリングレート
	 */
	public SpectrumPanel(MyAndroidGraphics mc,double sample) {
		this.mc = mc;
		this.nyquist = sample /2.;
		
		mc.changeUserWindow(0,nyquist,-10,PNT_HEIGHT+OFFSET_Y,2);
		init();
	}
	
	/**
	 * 初期化
	 */
	public void init(){
		for(int i = 0; i < drawBuffer.length ; i++){
			drawBuffer[i] = 0.0;
			drawBuffer2[i] = 0.0;
		}		
	}
	

	/**
	 * 描画メソッド
	 * @param g Graphicsオブジェクト
	 */
	public void paintComponent(double df) {
		drawBackground();
		drawWave(df);
		drawLabel();
	}
	
	/**
	 * 背景を描画する
	 * @param g Graphicsオブジェクト
	 */
	private void drawBackground(){
		
		double[]  bminx={0},bmaxx={0},bminy={0},bmaxy={0};
		mc.getUserWindow(bminx,bmaxx,bminy,bmaxy,2);
		
		//if(DEBUG2) System.out.println("2 minx"+minx[0]+"maxx"+maxx[0]+"miny"+miny[0]+"maxy"+maxy[0]);
		double minx=bminx[0];
		double maxx=bmaxx[0];
		double miny=bminy[0];
		double maxy=bmaxy[0]-OFFSET_Y;
		//System.out.println("2 minx"+minx+"maxx"+maxx+"miny"+miny+"maxy"+maxy);
		
		double userXLength= maxx - minx;
		double userYLength= maxy - miny;

		double userTickX = userXLength / 50.;
		double userTickY = userYLength / 50.;
		
		double userMemoriX = userXLength / 100.;
		double userMemoriY = userYLength / 100.;
		
		//fill background
		mc.setColor(BG_COLOR);				
		mc.fillRect(minx,miny,maxx,maxy, 2);
		
		//draw ground line
		mc.setColor(BG_LINE_COLOR);
		mc.drawLine(minx,0,maxx,0,2);
	
		//mc.setFont(BG_FONT);
		
		
		//目盛り線 横
		int j=0;
		for(double i = minx; i < maxx; i += userTickX){
			
			if(j == 5){
				double k=((int)(i*10))/10.;
				mc.setColor(BG_STR_COLOR);
				mc.drawLine(i, -userMemoriY, i,userMemoriY,2);
				mc.drawString(R.drawable.chars16, k + "", i, -userMemoriY*4,2);
				mc.setColor(BG_LINE_COLOR);
				j=0;
			}else{
				mc.drawLine(i, -userMemoriY, i, userMemoriY,2);	
			}
			j++;
		}
		
		//目盛り線 縦	
		j=0;
		for(double i = miny*10; i <= maxy*10 ; i += userTickY*10){
			
			double ii=i/10;
			if(j == 5){
				double k=((int)(ii*10))/10.;
				mc.setColor(BG_STR_COLOR);
				mc.drawLine(0,ii,userMemoriX,ii,2);				
				mc.drawString(R.drawable.chars16, k +"", userMemoriX, ii,2);
				mc.setColor(BG_LINE_COLOR);
				j=0;
			}else{				
				mc.drawLine(0,ii,userMemoriX,ii,2);	
			}
			j++;
		}
	}
	
	/**
	 * 波形を描画する
	 * @param g
	 */
	private void drawWave(double df){

          for (int i = 0; i < drawBuffer.length; i++)
            drawBufferX[i] = i * df;
		
		//スペクトルを描画	
		//System.out.printf("dt="+df);
          if (FilterManager.fftmode == FilterManager.SET_DFT) {
            mc.setColor(Color.GREEN);
            mc.drawPolyline(drawBufferX, drawBuffer2, drawBuffer.length-1, 2);
          } else if (FilterManager.fftmode == FilterManager.SET_FFT) {
            mc.setColor(WAVE_COLOR);
            mc.drawPolyline(drawBufferX, drawBuffer, drawBuffer.length-1, 2);
          } else {
            mc.setColor(Color.GREEN);
            mc.drawPolyline(drawBufferX, drawBuffer2, drawBuffer.length-1, 2);
            mc.setColor(WAVE_COLOR);
            mc.drawPolyline(drawBufferX, drawBuffer, drawBuffer.length-1, 2);
          }

          /*
		for(int i = 0; i < drawBuffer.length - 1 ; i++){
			if(FilterManager.fftmode == FilterManager.SET_DFT){
				mc.setColor(Color.GREEN);
				mc.drawLine(i*df, drawBuffer2[i],(i + 1)*df, drawBuffer2[i + 1],2);
			}else if(FilterManager.fftmode == FilterManager.SET_FFT){
				mc.setColor(WAVE_COLOR);
				mc.drawLine(i*df, drawBuffer[i],(i + 1)*df, drawBuffer[i + 1],2);
			}else{
				mc.setColor(Color.GREEN);
				mc.drawLine(i*df, drawBuffer2[i],(i + 1)*df, drawBuffer2[i + 1],2);
				mc.setColor(WAVE_COLOR);
				mc.drawLine(i*df, drawBuffer[i],(i + 1)*df, drawBuffer[i + 1],2);
			}
		}
          */
	}
	
	/**
	 * ラベルの描画
	 * @param g Graphicsオブジェクト
	 */
	private void drawLabel(){
		//mc.back.setFont(FONT);
		mc.setColor(BG_STR_COLOR);
		mc.drawString(R.drawable.chars16, infoLabel,100,200,2);
	}
	
	/**
	 * スペクトルを更新
	 * @param val
	 */
	public void setSpectrum(double val[]){		
			
		double max = val[0];
		
		//X軸方向のマッピング
		for(int i = 0; i < drawBuffer.length ; i++){
			drawBuffer[i] = val[i];
			if(max < drawBuffer[i])max = drawBuffer[i];
		}
		
		//MaxをPanelの高さとして正規化
		double ygain = (double)PNT_HEIGHT /(double) max;
		
		for(int i = 0; i < drawBuffer.length ; i++){
			drawBuffer[i] = ygain*drawBuffer[i];
		}
	}
	
	public void setSpectrum2(double val[]){		
		
		double max = val[0];
		
		//X軸方向のマッピング
		for(int i = 0; i < drawBuffer2.length ; i++){
			drawBuffer2[i] = val[i];
			if(max < drawBuffer2[i])max = drawBuffer2[i];
		}
		
		//MaxをPanelの高さとして正規化
		double ygain = (double)PNT_HEIGHT /(double) max;
		
		for(int i = 0; i < drawBuffer2.length ; i++){
			drawBuffer2[i] = ygain*drawBuffer2[i];
		}
	}
	
	/**
	 * 精度Nの表示
	 * @param n
	 */
	public void setInfoLabel(double n){
		this.infoLabel ="N:" + n;			
	}
	
 
}
