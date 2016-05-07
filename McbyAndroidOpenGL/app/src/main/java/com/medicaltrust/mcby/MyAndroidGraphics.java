/******************************************************************/
/* MyAndroidGraphics                                              */
/*                     2002  Base programming by Masaki Aono      */
/*                     2010       programming by embedded.samurai */
/*                     2012       Programming by kikei            */
/******************************************************************/


package com.medicaltrust.mcby;

import android.graphics.Bitmap;
import android.graphics.Canvas;
/*
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.graphics.RectF;
*/

import android.opengl.GLU;
import android.util.Log;
import javax.microedition.khronos.opengles.GL10;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/*
  ユーザ座標系：ユーザから見た世界の座標系で任意の大きさをもつ。
                ビューポート内で表示が完結する。
  ビュー座標系：ビューポートの設定。
                ビューポートの位置関係や大きさを示す。
                この時、端末の画面全体を [0.0-1.0, 0.0-1.0] とするので、
                setViewportの際もこれを逸脱しないよう与える。
  
  draw関数を呼ぶ際にビューポートのIDを指定する。
  この時の引数はユーザ座標系で表現され、関数内でビューポート内の座標に変換。
  ビューポート内の座標は [0.0-1.0, 0.0-1.0] としている。(カメラと視界)

  文字のサイズだけ例外でユーザ座標系に関わらず実際の画面のピクセル数に対応。

  使い方：
  1. setViewportとsetUserWindowでビューポートを作成する。
     同じ順番で設定したものどうしが対応し、始めのビューポートがID:1になる。
     ID:0は画面全体を含むビューポートでコンストラクタで自動的に作られる。
  2. draw関数でビューポートIDと共に呼び出す。
  3. 必要に応じてchangeUserWindowによって
     任意のビューポートのユーザ座標を処理中に変更することができる。

  draw関数を呼んだ後、使ったビューポートIDは保存されている。
  以下の関数では保存されたビューポートIDを使っている。

  getX, getY: 位置をユーザ座標からビューポート内の座標に変換。
  getSizeX, getSizeY: 大きさをユーザ座標系からビューポート内の大きさに変換。
*/
                  
public class MyAndroidGraphics {
  final static private String TAG = "MyAndroidGraphics";

  private static Hashtable<Integer, Integer> textures =
    new Hashtable<Integer, Integer>();
  /*
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
  */
  
  // boundings of user coordinate system
  protected double[] userMinX;
  protected double[] userMaxX;
  protected double[] userMinY;
  protected double[] userMaxY;
	
  // boundings of viewport
  protected double[] viewMinX;
  protected double[] viewMaxX;
  protected double[] viewMinY;
  protected double[] viewMaxY;
	
  final static int DEFAULT_VIEWPORT_MAX = 256;
  //  final static int DEFAULT_WINDOW_SIZE = 240;
  final static int DEFAULT_FONT_SIZE = 12;
  final static private float CLEAR_COLOR_R = 1.0f;
  final static private float CLEAR_COLOR_G = 1.0f;
  final static private float CLEAR_COLOR_B = 1.0f;
  
  protected int viewportMax = DEFAULT_VIEWPORT_MAX;
  protected int viewportNum = 0; // current
  protected int userWinMax = 10; // max
  protected int userWinNum = 0; // current
	
  // final static int DefaultWindowSize = 240;
  protected int windowWidth;
  protected int windowHeight;

  private int currentTextSize = DEFAULT_FONT_SIZE;

  private float currentColorR = CLEAR_COLOR_R;
  private float currentColorG = CLEAR_COLOR_G;
  private float currentColorB = CLEAR_COLOR_B;

  private int currentViewport;

  private double currentViewMinX = 0.0;
  private double currentViewMinY = 0.0;
  private double currentViewMaxX = 1.0;
  private double currentViewMaxY = 1.0;
  private double currentViewWidth = currentViewMaxX - currentViewMinX;
  private double currentViewHeight = currentViewMaxY - currentViewMinY;
  
  private double currentUserMinX = -100.0;
  private double currentUserMinY = -100.0;
  private double currentUserMaxX = 100.0;
  private double currentUserMaxY = 100.0;
  private double currentUserWidth = currentUserMaxX - currentUserMinX;
  private double currentUserHeight = currentUserMaxY - currentUserMinY;

  // view port clicked
  public int stViewPt=0;
  public int endViewPt=0;

  //  private Rect drawArea;

  static public  GL10 gl;
  
  /*
  // for MoveTo(x,y) and LineTo(x,y)
  protected double lastx = 0;
  protected double lasty = 0;
  */

  /*
  private SurfaceHolder holder;
  private Paint paint;
  public Canvas canvas;
  */

 /*
  public MyAndroidGraphics (GL10 g) {
    gl = gl;
    newAndroidGraphics(windowWidth, windowHeight);
  }
  */
  public MyAndroidGraphics(GL10 g, int width, int height)
  {
    gl = g;
    newAndroidGraphics(width, height);
  }
  
  public void newAndroidGraphics(int width, int height) {
    /*
    this.holder = holder;
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTextSize(12);
    paint.setColor(Color.rgb(0,0,0));
    
    drawArea = new Rect(0, 0, width, height);
    */
		
    windowWidth  = width;
    windowHeight = height;
    
    createViewport(viewportMax);
    createUserWindow(5);
}
  
  private void createViewport (int max){
    viewportMax = max;
    viewMinX = new double[viewportMax];
    viewMaxX = new double[viewportMax];
    viewMinY = new double[viewportMax];
    viewMaxY = new double[viewportMax];
    viewMinX[0] = viewMinY[0] = 0.0;
    viewMaxX[0] = viewMaxY[0] = 1.0;
    viewportNum = 1;
  }
	
  private void createUserWindow (int max) {
    userWinMax = max;
    userMinX = new double[userWinMax];
    userMaxX = new double[userWinMax];
    userMinY = new double[userWinMax];
    userMaxY = new double[userWinMax];
    userMinX[0] = userMinY[0] = 0;
    userMaxX[0] = windowWidth;
    userMaxY[0] = windowHeight;
    userWinNum = 1; // define current index in user coordinate as 1
  }
  
  /*
  public MyAndroidGraphics (SurfaceHolder holder){
		
    this.holder = holder;
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTextSize(12);
    paint.setColor(Color.rgb(0,0,0));
    drawArea = new Rect(0, 0, windowWidth, windowHeight);
		
    viewportMax = DefaultViewportMax;
		 
    windowWidth = DefaultWindowSize;
    windowHeight = DefaultWindowSize;
		
    createViewport(DefaultViewportMax);
    createUserWindow(5);
  }
  public MyAndroidGraphics(SurfaceHolder holder,int width,int height)
  {
    this.holder = holder;
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setTextSize(12);
    paint.setColor(Color.rgb(0,0,0));
    drawArea = new Rect(0,0,width,height);
		
    viewportMax = DefaultViewportMax;

    windowWidth  = width;
    windowHeight = height;
    createViewport(DefaultViewportMax);
    createUserWindow(5);
	  
    //System.out.println("windowWidth"+windowWidth+"windowHeight"+windowHeight);
  }
  */
  
  // viewport methods
  
  public void setViewport(double minx, double maxx, double miny, double maxy) {
    Log.d("setViewport",
          "["+viewportNum+"] = ("+minx+":"+maxx+","+miny+":"+maxy+")");
    viewMinX[viewportNum] = minx;
    viewMaxX[viewportNum] = maxx;
    viewMinY[viewportNum] = miny;
    viewMaxY[viewportNum] = maxy;
    viewportNum++;
  }
    
	
  public void setViewport2(double minx, double maxx,
                           double miny, double maxy) {
    viewMinX[viewportNum] = minx;
    viewMaxX[viewportNum] = maxx;
    viewMinY[viewportNum] = miny;
    viewMaxY[viewportNum] = maxy;
    viewportNum++;
    setClip(minx, miny, maxx, maxy, true); // clipping
  }
	
  public void resetViewport(){
    viewMinX[0] = viewMinY[0] = 0.0;
    viewMaxX[0] = viewMaxY[0] = 1.0;
    viewportNum = 1;
  }

  // user window methods
  
  public void setUserWindow(double minx, double maxx, double miny, double maxy){
    Log.d("setUserWindow",
          "["+userWinNum+"] = ("+minx+":"+maxx+","+miny+":"+maxy+")");
    userMinX[userWinNum] = minx;
    userMaxX[userWinNum] = maxx;
    userMinY[userWinNum] = miny;
    userMaxY[userWinNum] = maxy;
    userWinNum++;
  }
	
  public int getUserWindow (double[] minx, double[] maxx,
                            double[] miny, double[] maxy, int num){
    if(userWinNum <= num) return 0;

    minx[0] = userMinX[num]; 
    maxx[0] = userMaxX[num]; 
    miny[0] = userMinY[num]; 
    maxy[0] = userMaxY[num]; 

    //System.out.println("1 minx"+minx+"maxx"+maxx+"miny"+miny+"maxy"+maxy);
    return num;
  }
  public void changeUserWindow(double minx, double maxx,
                               double miny, double maxy, int num) {
    userMinX[num] = minx;
    userMaxX[num] = maxx;
    userMinY[num] = miny;
    userMaxY[num] = maxy;
  }
	
  public void setWindow(int width, int height) {
    windowWidth  = width;
    windowHeight = height;
  }
    
  public int getWidth() {
    return windowWidth;
  }
  public int getHeight(){
    return windowHeight;
  }

  /*
  public int getWindowPosition (double[] minx, double[] maxx,
                                double[] miny, double[] maxy, int num){
    // convert to Java AWT
    minx[0] = getX(minx[0],num);
    miny[0] = getY(miny[0],num);
    maxx[0] = getX(maxx[0],num);
    maxy[0] = getY(maxy[0],num);
		
    return 0;
  }
  */

  public static void setTexture(int id, int tex) {
    if (textures.containsKey(id))
      Log.w(TAG, "Texture("+id+") is already load.");
    textures.put(id, tex);
  }
  public static int getTexture(int id) {
    if (textures.containsKey(id))
      return textures.get(id);
    Log.e(TAG, "Texture("+id+") has not been load.");
    return 0;
  }
  public static final void deleteAllTextures() {
    List<Integer> keys = new ArrayList<Integer>(textures.keySet());
    for (Integer resId : keys) {
      int[] texId = new int[1];
      texId[0] = textures.get(resId);
      gl.glDeleteTextures(1, texId, 0);
    }
  }
	
  /*==================================================
    map user to WIN/Java
   ==================================================*/
  // Dimension
  public double getDimensionX (double w) {
    return windowWidth * currentViewWidth * w / currentUserWidth;
  }
  public double getDimensionY (double h) {
    return windowHeight * currentViewHeight * h / currentUserHeight;
  }
  /*
  public int getDimensionX(double w,int num){
    double x = viewMaxx[num] - viewMinx[num];
    x *= windowWidth * w / (userMaxx[num]-userMinx[num]);
    return ((int)Math.abs(x));
  }
  public int getDimensionY(double h,int num){
    double y = viewMaxy[num] - viewMiny[num];
    y *= windowHeight * h / (userMaxy[num]-userMiny[num]);
    return ((int)Math.abs(y));
  }
  */

  /*
  public int getX(double x, int num) {
    double xx = viewX(x, num); // mapping user to viewport
    int ix = getIntX(xx); // mapping viewport to Java
    return ix;
  }
  public int getY(double y, int num) {
    double yy = viewY(y, num);
    int iy = getIntY(yy);
    return iy;
  }
  public double viewX(double x,int num) {
    double s = (x - userMinx[num]) / (userMaxx[num] - userMinx[num]);
    double t = viewMinx[num] + s * (viewMaxx[num] - viewMinx[num]);
    //System.out.println("userMinx["+num+"]="+userMinx[num]);
    //System.out.println("userMaxx["+num+"]="+userMaxx[num]);
    //System.out.println("viewMinx["+num+"]="+viewMinx[num]);
    //System.out.println("viewMaxx["+num+"]="+viewMaxx[num]);
    //System.out.println("t"+t);
    return t;
  }

  public double viewY(double y,int num)
  {
    double s = (y - userMiny[num])/(userMaxy[num] - userMiny[num]);
    double t = viewMiny[num] +
      s * (viewMaxy[num] - viewMiny[num]);

    return t;
  }

  // mapping viewport to Java AWT
  public int getIntX(double x){
    return (int)(windowWidth * x);
  }
  public int getIntY(double y){
    return (int)(windowHeight * (1-y));
  }
  */
  
  public void activateViewport(int viewId) {
    if (viewId == currentViewport) return;
    currentViewport = viewId;
    
    currentViewMaxX = viewMaxX[viewId];
    currentViewMinX = viewMinX[viewId];
    currentViewWidth = Math.abs(currentViewMaxX - currentViewMinX);

    currentViewMaxY = viewMaxY[viewId];
    currentViewMinY = viewMinY[viewId];
    currentViewHeight = Math.abs(currentViewMaxY - currentViewMinY);

    currentUserMaxX = userMaxX[viewId];
    currentUserMinX = userMinX[viewId];
    currentUserWidth = Math.abs(currentUserMaxX - currentUserMinX);

    currentUserMaxY = userMaxY[viewId];
    currentUserMinY = userMinY[viewId];
    currentUserHeight = Math.abs(currentUserMaxY - currentUserMinY);

    gl.glViewport((int)(windowWidth  * currentViewMinX),
                  (int)(windowHeight * currentViewMinY),
                  (int)(windowWidth  * currentViewWidth),
                  (int)(windowHeight * currentViewHeight));
    
    gl.glMatrixMode(gl.GL_PROJECTION);
    gl.glLoadIdentity();
    gl.glOrthof(-0.5f, 0.5f, -0.5f, 0.5f, 1.0f, 10.0f);
  }

  // returns gl's x by ux where ux is user's x in viewport of id:viewId.
  public double getX (double ux) {
    return (ux - currentUserMinX) / currentUserWidth;
  }
  public double getY (double uy) {
    return 1.0 - (uy - currentUserMinY) / currentUserHeight;
  }
  public double getSizeX (double ux) {
    return ux / (windowWidth * currentViewWidth);
  }
  public double getSizeY (double uy) {
    return uy / (windowHeight * currentViewHeight);
  }

  /*==================================================
     map Java AWT to viewport
   ==================================================*/
  
  public int GetViewPort (int ix, int iy) {

    double s = (double) (ix) / (double)windowWidth;
    double t = (double)( windowHeight - iy ) / (double)windowHeight;

    for(int i=1; i < viewportNum; i++){
      if( s >= viewMinX[i] && s <= viewMaxX[i] &&
          t >= viewMinY[i] && t <= viewMaxY[i]){
        return i;
      }
    }
    return 0;
  }
	
  // map viewport to useer
  public double GetUserX (int ix, int v) {

    double xv = (double)ix / (double)windowWidth;
    double x =
      userMinX[v] +
      (userMaxX[v]-userMinX[v])
      * ((xv - viewMinX[v]) / (viewMaxX[v] - viewMinX[v]));

    //TRACE("viewMinx[%d]=%f\n",v,viewMinx[v]);
    //TRACE("viewMaxx[%d]=%f\n",v,viewMaxx[v]);
    return x;
  }

  public double GetUserY (int iy, int v) {
    double yv = (double)(windowHeight-iy) / (double)windowHeight;
    double y =
      userMinY[v] +
      (userMaxY[v] - userMinY[v])
      * ( (yv-viewMinY[v] ) / (viewMaxY[v] - viewMinY[v]));

    //TRACE("viewMiny[%d]=%f\n",v,viewMiny[v]);
    //TRACE("viewMaxy[%d]=%f\n",v,viewMaxy[v]);

    return y;
  }
	
  public void clipRect(double x1, double y1,
                       double x2, double y2, int num){ }
  public void setClip(double x1, double y1,
                      double x2, double y2, int num){ }
  public void setClip(double x1, double y1,
                      double x2, double y2, boolean flag){ }

  // OpenGL android has not setcolor
  public void setColor (int color) {
    currentColorR = ((color>>16)&0xFF) / 255.0f;
    currentColorG = ((color>> 8)&0xFF) / 255.0f;
    currentColorB = ((color>> 0)&0xFF) / 255.0f;
  }
  /*
  public void setColor(int color) {
    int r = (color>>16)&0xFF;
    int g = (color>> 8)&0xFF;
    int b = (color>> 0)&0xFF;
    paint.setColor(Color.argb(255,r,g,b));
  }
  */
  public void setFontSize (int size) {
    currentTextSize = size;
  }

  /*==================================================
     Let's draw differrent shapes using user coords!!!
   ==================================================*/
  	
  public void AllClear() { allClear(); }
  public void allClear() {
    gl.glClearColor(CLEAR_COLOR_R, CLEAR_COLOR_G, CLEAR_COLOR_B, 1.0f);
    gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);

    gl.glLoadIdentity();
    GLU.gluLookAt(gl,
                  0.5f,  0.5f, -1.0f,
                  0.5f,  0.5f,  1.0f,
                  0.0f, -1.0f,  0.0f);

    gl.glMatrixMode(gl.GL_PROJECTION);
    gl.glLoadIdentity();
    gl.glOrthof(-0.5f, 0.5f, -0.5f, 0.5f, 1.0f, 10.0f);
    
    gl.glEnable (gl.GL_LINE_SMOOTH);
    
    //    gl.glEnable(GL10.GL_BLEND);
    //    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ZERO);
    //    gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

    /*
    int color = paint.getColor();
    paint.setColor(Color.argb(255, 255, 255, 255));
    paint.setStyle(Paint.Style.FILL);
    canvas.drawRect(0,0,windowWidth,windowHeight, paint);
    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(color);
    */
  }

  public void drawLine (double x1, double y1,
                        double x2, double y2, int viewId) {
    activateViewport(viewId);
    
    GraphicUtils.drawLine(gl,
                          (float)getX(x1), (float)getY(y1),
                          (float)getX(x2), (float)getY(y2),
                          currentColorR, currentColorG, currentColorB);
    /*
    int ix1 = getX(x1, num);
    int iy1 = getY(y1, num);
    int ix2 = getX(x2, num);
    int iy2 = getY(y2, num);

    canvas.drawLine(ix1, iy1, ix2, iy2, paint);
    //back.drawLine(ix1,iy1,ix2,iy2);
    //g.drawLine(0,0,500,500);
    */
  }
  
  // rectangle
  public void drawRect(double x1, double y1, double x2, double y2, int viewId){
    activateViewport(viewId);
    
    float vx1 = (float)getX(x1);
    float vy1 = (float)getY(y1);
    float vx2 = (float)getX(x2);
    float vy2 = (float)getY(y2);

    float w = Math.abs(vx1 - vx2);
    float h = Math.abs(vy1 - vy2);

    float x = vx1 <= vx2 ? vx1 : vx2;
    float y = vy1 <= vy2 ? vy1 : vy2;

    GraphicUtils.drawRect(gl, x, y, x+w, y+h,
                          currentColorR, currentColorG, currentColorB);
    /*
    int ix1 = getX(x1, num);
    int iy1 = getY(y1, num);
    int ix2 = getX(x2, num);
    int iy2 = getY(y2, num);
    int width = Math.abs(ix1-ix2)+1;
    int height = Math.abs(iy1-iy2)+1;
    int x0 = (ix1 <= ix2) ? ix1 : ix2;
    int y0 = (iy1 <= iy2) ? iy1 : iy2;
    //back.drawRect(x0,y0,width,height);
    // paint.setStyle(Paint.Style.STROKE);
    // canvas.drawRect(x0, y0, x0+width, y0+height, paint);
    // paint.setStyle(Paint.Style.STROKE);
    */
  }
  
  public void fillRect(double x1, double y1, double x2, double y2, int viewId){
    activateViewport(viewId);
    
    float vx1 = (float)getX(x1);
    float vy1 = (float)getY(y1);
    float vx2 = (float)getX(x2);
    float vy2 = (float)getY(y2);

    float w = Math.abs(vx1 - vx2);
    float h = Math.abs(vy1 - vy2);

    float x = vx1 <= vx2 ? vx1 : vx2;
    float y = vy1 <= vy2 ? vy1 : vy2;

    GraphicUtils.drawFilledRect(gl, x, y, x+w, y+h,
                                currentColorR, currentColorG, currentColorB);
    /*
    int ix1 = getX(x1, num);
    int iy1 = getY(y1, num);
    int ix2 = getX(x2, num);
    int iy2 = getY(y2, num);
    int width = Math.abs(ix1-ix2)+1;
    int height = Math.abs(iy1-iy2)+1;
    int x0 = (ix1 <= ix2) ? ix1 : ix2;
    int y0 = (iy1 <= iy2) ? iy1 : iy2;

    //back.fillRect(x0,y0,width,height);
    System.out.println("num="+num);
    System.out.println("x0="+x0);
    System.out.println("y0="+y0);
    System.out.println("width="+width);
    System.out.println("height="+height);
    paint.setStyle(Paint.Style.FILL);
    canvas.drawRect(x0, y0, x0+width, y0+height, paint);
    paint.setStyle(Paint.Style.STROKE);
    */
  }
	
  public void clearRect(double x1, double y1, double x2, double y2, int viewId) {
    activateViewport(viewId);
    
    float vx1 = (float)getX(x1);
    float vy1 = (float)getY(y1);
    float vx2 = (float)getX(x2);
    float vy2 = (float)getY(y2);

    float w = Math.abs(vx1 - vx2);
    float h = Math.abs(vy1 - vy2);

    float x = vx1 <= vx2 ? vx1 : vx2;
    float y = vy1 <= vy2 ? vy1 : vy2;

    GraphicUtils.drawFilledRect(gl, x, y, x+w, y+h,
                                CLEAR_COLOR_R, CLEAR_COLOR_G, CLEAR_COLOR_B);
    /*
    int ix1 = getX(x1, num);
    int iy1 = getY(y1, num);
    int ix2 = getX(x2, num);
    int iy2 = getY(y2, num);
    int width = Math.abs(ix1-ix2)+1;
    int height = Math.abs(iy1-iy2)+1;
    int x0 = (ix1 <= ix2) ? ix1 : ix2;
    int y0 = (iy1 <= iy2) ? iy1 : iy2;

    int color = paint.getColor();
    paint.setColor(Color.argb(255, 255, 255, 255));
    paint.setStyle(Paint.Style.FILL);
    canvas.drawRect(x0, y0, x0+width, y0+height, paint);
    
    paint.setStyle(Paint.Style.STROKE);
    paint.setColor(color);
    */
  }
	
  // corner is round
  public void drawRoundRect(double x1, double y1, double x2, double y2, 
                            double arcW, double arcH, int viewId){
    activateViewport(viewId);
    
    float vx1 = (float)getX(x1);
    float vy1 = (float)getY(y1);
    float vx2 = (float)getX(x2);
    float vy2 = (float)getY(y2);

    float w = Math.abs(vx1 - vx2);
    float h = Math.abs(vy1 - vy2);

    float x = vx1 <= vx2 ? vx1 : vx2;
    float y = vy1 <= vy2 ? vy1 : vy2;

    float arcw = (float)getX(arcW);
    float arch = (float)getY(arcH);
    
    GraphicUtils.drawRoundRect(gl, x, y, x+w, y+h,
                               arcw, arch,
                               currentColorR, currentColorG, currentColorB);
    /*
    int ix1 = getX(x1, num);
    int iy1 = getY(y1, num);
    int ix2 = getX(x2, num);
    int iy2 = getY(y2, num);
    int width = Math.abs(ix1-ix2)+1;
    int height = Math.abs(iy1-iy2)+1;
    int x0 = (ix1 <= ix2) ? ix1 : ix2;
    int y0 = (iy1 <= iy2) ? iy1 : iy2;
    int iarcWidth = getDimensionX(arcW, num);
    int iarcHeight = getDimensionY(arcH, num);

    //back.drawRoundRect(x0,y0,width,height,
    //	iarcWidth,iarcHeight);

    paint.setStyle(Paint.Style.STROKE);
    RectF rectf=new RectF(x0, y0, x0+width, y0+height);
    canvas.drawRoundRect(rectf, iarcWidth, iarcHeight, paint);
    paint.setStyle(Paint.Style.STROKE);
    */
  }
  
  public void fillRoundRect(double x1, double y1, double x2, double y2,
                            double arcW, double arcH, int viewId){
    activateViewport(viewId);
    
    float vx1 = (float)getX(x1);
    float vy1 = (float)getY(y1);
    float vx2 = (float)getX(x2);
    float vy2 = (float)getY(y2);

    float w = Math.abs(vx1 - vx2);
    float h = Math.abs(vy1 - vy2);

    float x = vx1 <= vx2 ? vx1 : vx2;
    float y = vy1 <= vy2 ? vy1 : vy2;

    float arcw = (float)getX(arcW);
    float arch = (float)getY(arcH);
    
    GraphicUtils.drawFilledRoundRect(
       gl, x, y, x+w, y+h,
       arcw, arch,
       currentColorR, currentColorG, currentColorB);
    /*
    int ix1 = getX(x1, num);
    int iy1 = getY(y1, num);
    int ix2 = getX(x2, num);
    int iy2 = getY(y2, num);
    int width = Math.abs(ix1-ix2)+1;
    int height = Math.abs(iy1-iy2)+1;
    int x0 = (ix1 <= ix2) ? ix1 : ix2;
    int y0 = (iy1 <= iy2) ? iy1 : iy2;
    int iarcWidth = getDimensionX(arcW,num);
    int iarcHeight = getDimensionY(arcH,num);

    paint.setStyle(Paint.Style.FILL);
    RectF rectf=new RectF(x0, y0, x0+width, y0+height);
    canvas.drawRoundRect(rectf, iarcWidth, iarcHeight, paint);
    paint.setStyle(Paint.Style.STROKE);
    */
  }

  // floating rectangle
  public void draw3DRect(double x1, double y1, double x2, double y2,
                         boolean raised, int num){
    /*
    int ix1 = getX(x1, num);
    int iy1 = getY(y1, num);
    int ix2 = getX(x2, num);
    int iy2 = getY(y2, num);
    int width = Math.abs(ix1-ix2)+1;
    int height = Math.abs(iy1-iy2)+1;
    int x0 = (ix1 <= ix2) ? ix1 : ix2;
    int y0 = (iy1 <= iy2) ? iy1 : iy2;
    //back.draw3DRect(x0,y0,width,height,raised);
    */
  }
  
  public void fill3DRect(double x1, double y1, double x2, double y2,
                         boolean raised,int num){
    /*
    int ix1 = getX(x1, num);
    int iy1 = getY(y1, num);
    int ix2 = getX(x2, num);
    int iy2 = getY(y2, num);
    int width = Math.abs(ix1-ix2)+1;
    int height = Math.abs(iy1-iy2)+1;
    int x0 = (ix1 <= ix2) ? ix1 : ix2;
    int y0 = (iy1 <= iy2) ? iy1 : iy2;
    //back.fill3DRect(x0,y0,width,height,raised);
    */
  }

  // ellipse
  public void drawOval(double x1, double y1, double x2, double y2, int viewId) {
    activateViewport(viewId);
    
    float vx1 = (float)getX(x1);
    float vy1 = (float)getY(y1);
    float vx2 = (float)getX(x2);
    float vy2 = (float)getY(y2);

    float w = Math.abs(vx1 - vx2);
    float h = Math.abs(vy1 - vy2);

    float x = vx1 <= vx2 ? vx1 : vx2;
    float y = vy1 <= vy2 ? vy1 : vy2;

    GraphicUtils.drawOval(gl, x, y, x+w, y+h,
                          currentColorR, currentColorG, currentColorB);

    /*
    int ix1 = getX(x1, num);
    int iy1 = getY(y1, num);
    int ix2 = getX(x2, num);
    int iy2 = getY(y2, num);
    int width = Math.abs(ix1-ix2)+1;
    int height = Math.abs(iy1-iy2)+1;
    int x0 = (ix1 <= ix2) ? ix1 : ix2;
    int y0 = (iy1 <= iy2) ? iy1 : iy2;

    paint.setStyle(Paint.Style.STROKE);

    RectF rectf = new RectF(x0, y0, x0+width, y0+height);
    canvas.drawOval(rectf, paint);
    paint.setStyle(Paint.Style.STROKE);
    */
  }
  public void fillOval(double x1, double y1, double x2, double y2, int viewId){
    activateViewport(viewId);
    
    float vx1 = (float)getX(x1);
    float vy1 = (float)getY(y1);
    float vx2 = (float)getX(x2);
    float vy2 = (float)getY(y2);

    float w = Math.abs(vx1 - vx2);
    float h = Math.abs(vy1 - vy2);

    float x = vx1 <= vx2 ? vx1 : vx2;
    float y = vy1 <= vy2 ? vy1 : vy2;

    GraphicUtils.drawFilledOval(gl, x, y, x+w, y+h,
                                currentColorR, currentColorG, currentColorB);
    /*
    int ix1 = getX(x1, num);
    int iy1 = getY(y1, num);
    int ix2 = getX(x2, num);
    int iy2 = getY(y2, num);
    int width = Math.abs(ix1-ix2)+1;
    int height = Math.abs(iy1-iy2)+1;
    int x0 = (ix1 <= ix2) ? ix1 : ix2;
    int y0 = (iy1 <= iy2) ? iy1 : iy2;

    paint.setStyle(Paint.Style.FILL);
    RectF rectf = new RectF(x0, y0, x0+width, y0+height);
    canvas.drawOval(rectf, paint);
    paint.setStyle(Paint.Style.STROKE);
    */
  }

  // arc: center(x,y), radius(xr, yr)
  public void drawArc(double x, double y, double xr, double yr,
                      double startAngle, double arcAngle, int num) {
    /*
    int ix = getX(x, num);
    int iy = getY(y, num);
    int ixr = getDimensionX(xr, num);
    int iyr = getDimensionY(yr, num);
    int x0 = ix - ixr;
    int y0 = iy - iyr;
    int is = (int)(90-(startAngle+arcAngle));
    int ia = (int)arcAngle; // degree 
    //back.drawArc(x0,y0,2*ixr,2*iyr,is,ia);
    */
  }

  // sector: center(x,y), radius(xr, yr)
  public void fillArc(double x, double y, double xr, double yr,
                      double startAngle, double arcAngle, int num) {
    /*
    int ix = getX(x, num);
    int iy = getY(y, num);
    int ixr = getDimensionX(xr, num);
    int iyr = getDimensionY(yr, num);
    int x0 = ix - ixr;
    int y0 = iy - iyr;
    int is = (int)(90-(startAngle+arcAngle));
    int ia = (int)arcAngle; // degree
    //back.fillArc(x0,y0,2*ixr,2*iyr,is,ia);
    */
  }	

  // polygonal line
  public void drawPolyline(double[] x, double[] y, int numPoints, int viewId) {
    activateViewport(viewId);

    float dx[] = new float[numPoints];
    float dy[] = new float[numPoints];
    for (int i=0; i<numPoints; i++) {
      dx[i] = (float)getX(x[i]);
      dy[i] = (float)getY(y[i]);
    }
    GraphicUtils.drawPath(gl, dx, dy, numPoints,
                          currentColorR, currentColorG, currentColorB);
  }
  /*
  public void drawPolyline(double[] x, double[] y, int numPoints,int num){
    int[] ix = new int[numPoints];
    int[] iy = new int[numPoints];
    int i=0;
    Path path = new Path();
    ix[i] = getX(x[i], num);
    iy[i] = getY(y[i], num);
    path.moveTo(ix[i], iy[1]);
		
    for (i=1; i < numPoints ; i++){
      ix[i] = getX(x[i], num);
      iy[i] = getY(y[i], num);
      path.lineTo(ix[i], iy[i]);
    }
    canvas.drawPath(path, paint);
    //back.drawPolyline(ix,iy,numPoints);
  }
  */

  // polygon
  public void drawPolygon(double[] x, double[] y, int numPoints, int viewId){
    activateViewport(viewId);

    float dx[] = new float[numPoints+1];
    float dy[] = new float[numPoints+1];
    for (int i=0; i<numPoints; i++) {
      dx[i] = (float)getX(x[i]);
      dy[i] = (float)getY(y[i]);
    }
    dx[numPoints] = (float)getX(x[numPoints]);
    dy[numPoints] = (float)getY(y[numPoints]);
    
    GraphicUtils.drawPath(gl, dx, dy, numPoints+1,
                          currentColorR, currentColorG, currentColorB);
    /*
    int[] ix = new int[numPoints];
    int[] iy = new int[numPoints];
    for (int i=0; i < numPoints ; i++){
      ix[i] = getX(x[i], num);
      iy[i] = getY(y[i], num);
    }
    //back.drawPolygon(ix,iy,numPoints);
    */
  }
  public void fillPolygon(double[] x, double[] y, int numPoints,int num){
    /*
    int[] ix = new int[numPoints];
    int[] iy = new int[numPoints];
    for (int i=0; i < numPoints ; i++){
      ix[i] = getX(x[i], num);
      iy[i] = getY(y[i], num);
    }
    //back.fillPolygon(ix,iy,numPoints);
    */
  }
	
  /*
  public void drawRawString(String str, double x, double y){
    canvas.drawText(str,
                    (float)(drawArea.left+x),
                    (float)(drawArea.top+y), paint);
  }
  */

  // texture
  public void drawTexture(int resId,
                          double x1, double y1, double x2, double y2,
                          float u1, float v1, float u2, float v2,
                          int viewId) {

    gl.glEnable(GL10.GL_BLEND);
    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
    
    activateViewport(viewId);
    GraphicUtils.drawTexture(gl, getTexture(resId),
                             (float)getX(x1), (float)getY(y1),
                             (float)getX(x2), (float)getY(y2),
                             u1, v1, u2, v2,
                             currentColorR, currentColorG, currentColorB);

    gl.glDisable(GL10.GL_BLEND);
  }

  public void drawChar(int resId, char c,
                       double x1, double y1, double x2, double y2,
                       int viewId) {
    activateViewport(viewId);

    gl.glEnable(GL10.GL_BLEND);
    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

    GraphicUtils.drawChar(gl, getTexture(resId),
                          c,
                          (float)getX(x1), (float)getY(y1),
                          (float)getX(x2), (float)getY(y2),
                          currentColorR, currentColorG, currentColorB);
    
    gl.glDisable(GL10.GL_BLEND);
  }
  
  public void drawString(int resId, String str, double x, double y, int viewId){
    activateViewport(viewId);

    gl.glEnable(GL10.GL_BLEND);
    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

    float vx = (float)getX(x);
    float vw = (float)getSizeX(currentTextSize/2.0);
    float vh = (float)getSizeY(currentTextSize);
    // For compatibility with canvas.
    float vy = (float)getY(y) - vh;

    //    Log.d("drawString", "(vx="+vx+", vw="+vw+", vy="+vy+", vh="+vh+")");

    GraphicUtils.drawText(gl, getTexture(resId), str,
                          vx, vy, vw, vh,
                          currentColorR, currentColorG, currentColorB);

    gl.glDisable(GL10.GL_BLEND);
    //back.drawString(str,ix,iy);
    //canvas.drawText(str, drawArea.left+ix, drawArea.top+iy, paint);
  }

  // ignore viewport
  //  public void drawRawString (String str, int x, int y) {
  public void drawRawString (int resId, String str, double x, double y) {
    int viewport = currentViewport;
    drawString(resId, str, x, y, 0);
    activateViewport(currentViewport);
    /*
    gl.glEnable(GL10.GL_BLEND);
    gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);

    GraphicUtils.drawText(gl, getTexture(resId), str,
                          (float)x/windowWidth, (float)y/windowHeight,
//                           (float)getX(currentTextSize/2.0),
//                           (float)getY(currentTextSize),
                          (float)currentTextSize / windowWidth / 2.0f,
                          (float)currentTextSize / windowHeight,
                          currentColorR, currentColorG, currentColorB);

    gl.glDisable(GL10.GL_BLEND);
    */
  }
                                                      
  /*
  // image
  public void drawImage(Bitmap img, double x, double y, int viewId){
    activateViewport(viewId);
    GraphicUtils.drawBitmap(gl, img, (float)getX(x), (float)getY(y));
    //return back.drawImage(img,ix,iy,observer);
    // canvas.drawBitmap(img, drawArea.left+ix, drawArea.top+iy, null);
  }
  */

  static final public int getColorOfRGB(int r, int g, int b) {
    return (r<<16)+(g)+b;
  } // but this is not used.

  // sign
  final static public int Sign (int x) { return sign(x); }
  final static public int Sign (double x) { return sign(x); }
  final static public int sign(int x) {
    if (x > 0) return 1;
    else if (x < 0) return -1;
    return 0;
  }
  final static public int sign(double x) {
    if (x > 0.0) return 1;
    else if (x < 0.0) return -1;
    return 0;
  }

  /*
  // straight line?
  public void moveTo(double x, double y,int num){
    lastx = x;
    lasty = y;
  }
  public void lineTo(double x, double y,int num){
    drawLine(lastx, lasty, x, y, num);
    lastx = x; lasty = y;
  }
  */

  // These are legacy of canvas.
  public void lock () {
    // canvas = holder.lockCanvas();
    // canvas.clipRect(drawArea);  
  }
  public void unlock (boolean flag) {
    // holder.unlockCanvasAndPost(canvas);
  }

}

