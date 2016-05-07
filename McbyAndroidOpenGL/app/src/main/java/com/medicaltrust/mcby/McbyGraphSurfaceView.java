package com.medicaltrust.mcby;

import android.content.Context;
import android.opengl.GLSurfaceView;

import android.view.MotionEvent;

public class McbyGraphSurfaceView extends GLSurfaceView {

  public McbyGraphSurfaceView(Context context) {
    super(context);
  }
  /*
  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    super.surfaceChanged(holder, format, w, h);
  }
  */
  public void setRenderer(McbyGraphRenderer renderer) {
    super.setRenderer(renderer);
  }

  @Override
  public boolean onTouchEvent (MotionEvent event) {
    return super.onTouchEvent(event);
  }
}