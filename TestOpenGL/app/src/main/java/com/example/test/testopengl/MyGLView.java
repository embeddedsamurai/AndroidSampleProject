package com.example.test.testopengl;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * Created by embed_000 on 2016/05/10.
 */
public class MyGLView extends GLSurfaceView{

    MyRenderer myrenderer;

    public MyGLView(Context context){
        super(context);
        myrenderer = new MyRenderer();
        setRenderer(myrenderer);
    }



}
