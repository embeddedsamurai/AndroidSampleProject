package com.example.test.testopengl;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by embed_000 on 2016/05/10.
 */
public class MyRenderer implements GLSurfaceView.Renderer{

    @Override
    public void onDrawFrame(GL10 gl){

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height){}

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config){}
}
