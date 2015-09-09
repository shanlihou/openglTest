package com.example.firstBlood;

import android.opengl.GLU;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.opengles.GL10;

public class Triangle {
    ///////////////////start
    static private FloatBuffer vertex;//顶点对应的字节缓冲
    static private FloatBuffer normal;//法向量对应的字节缓冲

    float[] lightPos = new float[] {10.0f, 10.0f, 10.0f, 1.0f };//光源的坐标
    private static final int STEP = 24;//
    private static final float RADIUS = 1.0f;//半径

    protected void init(GL10 gl) {
        gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);//设置背景颜色
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, lightPos, 0);
        gl.glEnable(GL10.GL_LIGHTING);//启用光照
        gl.glEnable(GL10.GL_LIGHT0); //打开光源
        gl.glClearDepthf(1.0f);//设置深度缓存
        gl.glDepthFunc(GL10.GL_LEQUAL);//设置深度缓存比较函数，GL_LEQUAL表示新的像素的深度缓存值小于等于当前像素的深度缓存值时通过深度测试
        gl.glEnable(GL10.GL_DEPTH_TEST);//启用深度缓存
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glShadeModel(GL10.GL_SMOOTH);//设置阴影模式GL_SMOOTH
    }

    protected void drawFrame(GL10 gl) {
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT |
                GL10.GL_DEPTH_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, 0, 0, 7f, 0f, 0f, 0f, 0f, 1.0f, 0.0f);//
        drawSphere(gl, RADIUS, STEP, STEP); //绘制球形
    }
private static void drawSphere(GL10 gl, float radius,
                                   int stacks, int slices) {
        vertex=allocateFloatBuffer( 4* 6 * stacks * (slices+1) );
        normal=allocateFloatBuffer( 4* 6 * stacks * (slices+1) );
        int i, j, triangles;
        float slicestep, stackstep;

        stackstep = ((float)Math.PI) / stacks;
        slicestep = 2.0f * ((float)Math.PI) / slices;
        for (i = 0; i < stacks; ++i)
        {
            float a = i * stackstep;
            float b = a + stackstep;
            float s0 = (float)Math.sin(a);
            float s1 = (float)Math.sin(b);
            float c0 = (float)Math.cos(a);
            float c1 = (float)Math.cos(b);

            float nv;
            for (j = 0; j <= slices; ++j)
            {
                float c = j * slicestep;
                float x = (float)Math.cos(c);
                float y = (float)Math.sin(c);
                nv=x * s0;
                normal.put(nv);
                vertex.put( nv * radius);
                nv=y * s0;
                normal.put(nv);
                vertex.put( nv * radius);
                nv=c0;
                normal.put(nv);
                vertex.put( nv * radius);
                nv=x * s1;
                normal.put(nv);
                vertex.put( nv * radius);
                nv=y * s1;
                normal.put(nv);
                vertex.put( nv * radius);
                nv=c1;
                normal.put(nv);
                vertex.put( nv * radius);
            }
        }
        normal.position(0);
        vertex.position(0);
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertex);
        gl.glNormalPointer(GL10.GL_FLOAT, 0, normal);
        gl.glEnableClientState (GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState (GL10.GL_NORMAL_ARRAY);
        triangles = (slices + 1) * 2;
        for(i = 0; i < stacks; i++)
            gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP,
                    i * triangles, triangles);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL10.GL_NORMAL_ARRAY);
    }

    private static FloatBuffer allocateFloatBuffer(int capacity){
        ByteBuffer vbb = ByteBuffer.allocateDirect(capacity);
        vbb.order(ByteOrder.nativeOrder());
        return vbb.asFloatBuffer();
    }
    ////////////////////end

    private FloatBuffer vertexBuffer;   // buffer holding the vertices

    private float vertices[] = {
            -0.5f, -0.5f,  0.0f,        // V1 - first vertex (x,y,z)
            0.5f, -0.5f,  0.0f,        // V2 - second vertex
            0.0f,  0.5f,  0.0f         // V3 - third vertex
    };

    public Triangle() {
        // a float has 4 bytes so we allocate for each coordinate 4 bytes
        ByteBuffer vertexByteBuffer = ByteBuffer.allocateDirect(vertices.length * 4);
        vertexByteBuffer.order(ByteOrder.nativeOrder());

        // allocates the memory from the byte buffer
        vertexBuffer = vertexByteBuffer.asFloatBuffer();

        // fill the vertexBuffer with the vertices
        vertexBuffer.put(vertices);

        // set the cursor position to the beginning of the buffer
        vertexBuffer.position(0);

    }


    /** The draw method for the triangle with the GL context */
    public void draw(GL10 gl, SurfaceHolder holder) {
/*
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        // set the colour for the background
//      gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);

        // to show the color (paint the screen) we need to clear the color buffer
//      gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

        // set the colour for the triangle
        gl.glColor4f(0.0f, 1.0f, 0.0f, 0.5f);

        // Point to our vertex buffer
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vertexBuffer);

        // Draw the vertices as triangle strip
        gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, vertices.length / 3);

        //Disable the client state before leaving
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
  */



        EGL10 egl = (EGL10)EGLContext.getEGL();
        EGLDisplay dpy = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);        int[] version = new int[2];
        egl.eglInitialize(dpy, version);
        int[] configSpec = {
                EGL10.EGL_RED_SIZE,      5,
                EGL10.EGL_GREEN_SIZE,    6,
                EGL10.EGL_BLUE_SIZE,     5,
                EGL10.EGL_DEPTH_SIZE,   16,
                EGL10.EGL_NONE
        };
        EGLConfig[] configs = new EGLConfig[1];
        int[] num_config = new int[1];
        egl.eglChooseConfig(dpy, configSpec, configs, 1, num_config);
        EGLConfig config = configs[0];
        EGLContext context = egl.eglCreateContext(dpy, config,
                EGL10.EGL_NO_CONTEXT, null);

        EGLSurface surface = egl.eglCreateWindowSurface(dpy, config,
                holder, null);
        egl.eglMakeCurrent(dpy, surface, surface, context);
        init(gl);
        drawFrame(gl);
    }
}