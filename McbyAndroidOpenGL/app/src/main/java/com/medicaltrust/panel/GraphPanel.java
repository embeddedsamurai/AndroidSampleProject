/*****************************************************************/
/* Digital Signal Proccessing Program                            */
/*                     programming by embedded.samurai           */
/*****************************************************************/
package com.medicaltrust.panel;

import java.util.*;

import android.graphics.Color;

abstract public class GraphPanel{
	
	//データポイント数 
	protected static final int PNT_WIDTH = 512;
	//暫定的な高さ     
	protected static int PNT_HEIGHT = 200;
	
	//背景色
	protected static final int BG_COLOR = Color.WHITE;
	//目盛り線の色
	protected static final int BG_LINE_COLOR = Color.LTGRAY;
	//目盛りの字の色
	protected static final int BG_STR_COLOR = Color.BLACK;
	
	public GraphPanel() {
		//パネルの大きさを設定
	
	}
	
}