package com.example.test.testopengl;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by embed_000 on 2016/05/10.
 */
public class MyRenderer implements GLSurfaceView.Renderer{

    MyCube cube = new MyCube();

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config){
        gl.glEnable(GL10.GL_DEPTH_TEST);
        gl.glDepthFunc(GL10.GL_LEQUAL);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height){
        gl.glViewport(0,0,width,height);

        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        GLU.gluPerspective(gl, 45f, (float)width/height, 1f, 50f);
    }

    @Override
    public void onDrawFrame(GL10 gl){
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BITS);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0,0,-3f);

        cube.draw(gl);
    }

}
