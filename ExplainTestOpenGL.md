# Summary
This file is to explain how to use Android OpenGL

### References
http://android.keicode.com/basics/opengl-overview.php

# Base
-MainActivity

onCreate 呼び出し
myGLViewを作成
myGLView = new MyGLView
setContentView(myGLView)

---------------
MyGLViewは、GLSurfaceViewから作る ( extends GLSurfaceView)

MyGLViewは、rendereを持つ

MyRenderer MyRenderer

コンストラクタ内でmyrendererを生成
setRenderer(myrenderer)

-------------------
MyRendererは、GLSurfaceView.Rendererをimplementsして作る

public class MyRenderer implements GLSurfaceView.Renderer

#
