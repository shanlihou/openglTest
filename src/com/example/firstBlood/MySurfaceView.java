package com.example.firstBlood;

/**
 * Created by root on 15-9-9.
 */
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.GLSurfaceView.Renderer;
import android.view.MotionEvent;

public class MySurfaceView extends GLSurfaceView implements Renderer {
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;// 角度缩放比例
    Ball ball = new Ball(3);
    double lightAngleGreen;
    private float myPreviousX;
    private float myPreviousY;
    private int lightAngleRed;
    float cx=0;//摄像机x位置
    float cy=20;//摄像机y位置
    float cz=40;//摄像机z位置

    float tx=0;////目标点x位置
    float ty=0;//目标点y位置
    float tz=0;//目标点z位置
    public double getLightAngleGreen() {
        return lightAngleGreen;
    }

    public void setLightAngleGreen(double lightAngleGreen) {
        this.lightAngleGreen = lightAngleGreen;
    }

    public float getMyPreviousX() {
        return myPreviousX;
    }

    public void setMyPreviousX(float myPreviousX) {
        this.myPreviousX = myPreviousX;
    }

    public float getMyPreviousY() {
        return myPreviousY;
    }

    public void setMyPreviousY(float myPreviousY) {
        this.myPreviousY = myPreviousY;
    }

    public int getLightAngleRed() {
        return lightAngleRed;
    }

    public void setLightAngleRed(int lightAngleRed) {
        this.lightAngleRed = lightAngleRed;
    }

    public MySurfaceView(Context context) {
        super(context);
        setRenderer(this);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);// 这个为默认行为　
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                while (true) {
                    lightAngleGreen += 5;
                    lightAngleRed += 5;
                    requestRender();
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = x - myPreviousX;
                float dx = y - myPreviousY;
                ball.angleX += dy * TOUCH_SCALE_FACTOR;
                ball.angleZ += dx * TOUCH_SCALE_FACTOR;
                requestRender();
                break;
        }
        myPreviousX = x;
        myPreviousY = y;
        return true;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        gl.glShadeModel(GL10.GL_SMOOTH);
        gl.glEnable(GL10.GL_LIGHTING);

        float lxGreen = (float) (10 * Math.cos(Math.toRadians(lightAngleGreen)));
        float lzGreen = (float) (10 * Math.sin(Math.toRadians(lightAngleGreen)));
        float[] params = { lxGreen, 2f, lzGreen, 1f };// 定向光
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, params, 0);

        float lyRed = (float) (1 * Math.cos(Math.toRadians(lightAngleGreen)));
        float lzRed = (float) (1 * Math.sin(Math.toRadians(lightAngleGreen)));
        float[] paramsRed = { 0, lyRed, lzRed, 1f };// 定向光
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_POSITION, paramsRed, 0);

        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);// 清楚缓存
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
//
//        //设置camera位置
//        GLU.gluLookAt
//        (
//              gl,
//              cx,   //人眼位置的X
//              cy,     //人眼位置的Y
//              cz,   //人眼位置的Z
//              tx,     //人眼球看的点X
//              ty,   //人眼球看的点Y
//              tz,   //人眼球看的点Z
//              0,
//              1,
//              0
//        );
        gl.glTranslatef(0, 0, -10.8f);// 平移z
        ball.drawSelf(gl);// 画球
        gl.glLoadIdentity();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        float ratio = (float) width / height;
        gl.glFrustumf(-ratio, ratio, -1, 1, 8, 100);// 透视投影

    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        gl.glDisable(GL10.GL_DITHER);// 取消抗抖动
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);// 透视修正暗示为快速模式
        gl.glClearColor(0, 0, 0, 0);// 设置背景为黑色
        gl.glShadeModel(GL10.GL_SMOOTH);// 着色为平滑着色
        gl.glEnable(GL10.GL_DEPTH_TEST);// 启用深度检测
        initLightGreen(gl);
        initLightRed(gl);
        initMaterial(gl);
    }

    private void initLightGreen(GL10 gl) {
        gl.glEnable(GL10.GL_LIGHT0);// 启用灯0
        float[] ambientParams = { 0.1f, 0.1f, 0.1f, 1.0f };
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, ambientParams, 0);
        float[] diffuseParams = { 0f, 1f, 1f, 1.0f };
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, diffuseParams, 0);
        float[] specularParams = { 0f, 1.0f, 0f, 1.0f };
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, specularParams, 0);

    }

    private void initLightRed(GL10 gl) {
        gl.glEnable(GL10.GL_LIGHT1);// 启用灯0
        float[] ambientParams = { 0.1f, 0.1f, 0.1f, 1.0f };
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_AMBIENT, ambientParams, 0);
        float[] diffuseParams = { 1f, 0f, 1f, 1.0f };
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_DIFFUSE, diffuseParams, 0);
        float[] specularParams = { 1f, 0.0f, 0f, 1.0f };
        gl.glLightfv(GL10.GL_LIGHT1, GL10.GL_SPECULAR, specularParams, 0);

    }

    private void initMaterial(GL10 gl) {
        float[] ambientParams = { 0.4f, 0.4f, 0.4f, 1.0f };
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, ambientParams,
                0);
        float[] diffuseParams = { 0.8f, 0.8f, 0.8f, 1.0f };
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, diffuseParams,
                0);
        float[] specularParams = { 1f, 1.0f, 1f, 1.0f };
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR,
                specularParams, 0);
        float[] shininessParams = { 1.5f };
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS,
                shininessParams, 0);

        float[] emission = { 0.0f, 0.0f, 0.3f, 1.0f };
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_EMISSION, emission, 0);
    }
}
