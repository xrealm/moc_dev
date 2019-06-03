package com.mao.dev.ui.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;

import com.mao.dev.R;
import com.orhanobut.logger.Logger;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Mao on 2017/4/6.
 */

public class FastImageRender implements GLSurfaceView.Renderer {

    private static final int BYTES_PER_FLOAT = 4;

    public static final String ATTRIBUTE_POSITION = "position";
    public static final String U_MATRIX = "u_Matrix";
    public static final String ATTRIBUTE_TEXCOORD = "inputTextureCoordinate";
    public static final String VARYING_TEXCOORD = "textureCoordinate";
    protected static final String UNIFORM_TEXTUREBASE = "inputImageTexture";
    public static final String UNIFORM_TEXTURE0 = UNIFORM_TEXTUREBASE + 0;

//    protected static final String UNIFORM_TEXELWIDTH = "texelWidthOffset";
//    protected static final String UNIFORM_TEXELHEIGHT = "texelHeightOffset";
    private final Context context;

    private int uMatrix;
    protected int programHandle;
    protected int textureHandle;
    protected int positionHandle;
    protected int texCoordHandle;
    protected int texture_in;

//    protected float texelWidth;
//    protected float texelHeight;
//    private int texelWidthHandle;
//    private int texelHeightHandle;

    private final FloatBuffer renderVertices;
    private final FloatBuffer textureVertices;

    private float[] viewMatrix = new float[16];
    private float[] projectMatrix = new float[16];
    private float[] viewProjectMatrix = new float[16];

    private String getVertexShader() {
        return
                "attribute vec4 " + ATTRIBUTE_POSITION + ";\n"
                        + "attribute vec2 " + ATTRIBUTE_TEXCOORD + ";\n"
                        + "varying vec2 " + VARYING_TEXCOORD + ";\n"
                        + "uniform mat4 " + U_MATRIX + ";\n"

                        + "void main() {\n"
                        + "  " + VARYING_TEXCOORD + " = " + ATTRIBUTE_TEXCOORD + ";\n"
                        + "   gl_Position = " + U_MATRIX + "*" + ATTRIBUTE_POSITION + ";\n"
                        + "}\n";
    }

    private String getFragmentShader() {
//        return
//                "precision mediump float;\n"
//                        + "uniform sampler2D " + UNIFORM_TEXTURE0 + ";\n"
//                        + "varying vec2 " + VARYING_TEXCOORD + ";\n"
//                        + "uniform float " + UNIFORM_TEXELWIDTH + ";\n"
//                        + "uniform float " + UNIFORM_TEXELHEIGHT + ";\n"
//
//
//                        + "void main(){\n"
//                        + "   vec2 firstOffset = vec2(1.3846153846 * " + UNIFORM_TEXELWIDTH + ", 1.3846153846 * " + UNIFORM_TEXELHEIGHT + ");\n"
//                        + "   vec2 secondOffset = vec2(3.2307692308 * " + UNIFORM_TEXELWIDTH + ", 3.2307692308 * " + UNIFORM_TEXELHEIGHT + ");\n"
//                        + "   vec3 sum = vec3(0,0,0);\n"
//                        + "   vec4 color = texture2D(" + UNIFORM_TEXTURE0 + ", " + VARYING_TEXCOORD + ");\n"
//                        + "   sum += color.rgb * 0.2270270270;\n"
//                        + "   sum += texture2D(" + UNIFORM_TEXTURE0 + ", " + VARYING_TEXCOORD + " - firstOffset).rgb * 0.3162162162;\n"
//                        + "   sum += texture2D(" + UNIFORM_TEXTURE0 + ", " + VARYING_TEXCOORD + " + firstOffset).rgb * 0.3162162162;\n"
//                        + "   sum += texture2D(" + UNIFORM_TEXTURE0 + ", " + VARYING_TEXCOORD + " - secondOffset).rgb * 0.0702702703;\n"
//                        + "   sum += texture2D(" + UNIFORM_TEXTURE0 + ", " + VARYING_TEXCOORD + " + secondOffset).rgb * 0.0702702703;\n"
//                        + "   gl_FragColor = vec4(sum, color.a);\n"
//                        + "}\n";

        return
                "precision mediump float;\n"
                        + "uniform sampler2D " + UNIFORM_TEXTURE0 + ";\n"
                        + "varying vec2 " + VARYING_TEXCOORD + ";\n"

                        + "void main(){\n"
                        + "   gl_FragColor = texture2D(" + UNIFORM_TEXTURE0 + "," + VARYING_TEXCOORD + ");\n"
                        + "}\n";
    }

    public FastImageRender(Context context) {
        this.context = context;
        float[] vertexData = new float[]{
//                -1f, -1f,
//                1f, -1f,
//                -1f, 1f,
//                1f, 1f
                -1.0f,1.0f,
                -1.0f,-1.0f,
                1.0f,1.0f,
                1.0f,-1.0f
        };

        renderVertices = ByteBuffer.allocateDirect(vertexData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(vertexData);
        renderVertices.position(0);

        float[] texData = new float[]{
//                0.0f, 0.0f,
//                1.0f, 0.0f,
//                0.0f, 1.0f,
//                1.0f, 1.0f,

                0.0f,0.0f,
                0.0f,1.0f,
                1.0f,0.0f,
                1.0f,1.0f,
        };
        textureVertices = ByteBuffer.allocateDirect(texData.length * BYTES_PER_FLOAT)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer()
                .put(texData);
        textureVertices.position(0);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.6f, 0.6f, 0.6f, 0.0f);
        initWithGLContext();
//        texture_in = loadTexture(BitmapFactory.decodeResource(context.getResources(), R.mipmap.bbbbbb));
        texture_in = loadTexutre2(R.mipmap.bbbbbb);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
//        texelWidth = 1.0f / (float) width;
//        texelHeight = 1.0f / (float) height;


    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        GLES20.glUseProgram(programHandle);
        passShaderValues();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }


    private int loadTexture( int resId) {
        int[] textureObjectId = new int[1];
        GLES20.glGenTextures(1, textureObjectId, 0);//创建纹理对象
        if (textureObjectId[0] == 0) {
            Logger.w("Could not generate a new OpenGL texture object.");
            return 0;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        if (bitmap == null) {
            GLES20.glDeleteTextures(1, textureObjectId, 0);
            return 0;
        }
        //绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureObjectId[0]);
        //缩小情况三线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);
        //放大情况双线性过滤
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        //加载bitmap
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        //生成级别
        GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        //解除绑定
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return textureObjectId[0];
    }

    private int loadTexutre2(int resId) {
        int[] textureObjectId = new int[1];
        GLES20.glGenTextures(1, textureObjectId, 0);//创建纹理对象
        if (textureObjectId[0] == 0) {
            Logger.w("Could not generate a new OpenGL texture object.");
            return 0;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId, options);
        if (bitmap == null) {
            GLES20.glDeleteTextures(1, textureObjectId, 0);
            return 0;
        }
        //生成纹理
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,textureObjectId[0]);
        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER,GLES20.GL_NEAREST);
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,GLES20.GL_TEXTURE_MAG_FILTER,GLES20.GL_LINEAR);
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        //根据以上指定的参数，生成一个2D纹理
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return textureObjectId[0];
    }

    private void initWithGLContext() {
        String vertexShaderSource = getVertexShader();
        String fragmentShaderSource = getFragmentShader();
        programHandle = ShaderHelper.buildProgram(vertexShaderSource, fragmentShaderSource);
        initShaderHandles();
    }

    private void initShaderHandles() {
        positionHandle = GLES20.glGetUniformLocation(programHandle, ATTRIBUTE_POSITION);
        texCoordHandle = GLES20.glGetAttribLocation(programHandle, ATTRIBUTE_TEXCOORD);
        textureHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_TEXTURE0);

//        texelWidthHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_TEXELWIDTH);
//        texelHeightHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_TEXELHEIGHT);
    }

    private void passShaderValues() {
        renderVertices.position(0);
        GLES20.glVertexAttribPointer(positionHandle, 2, GLES20.GL_FLOAT, false, 0, renderVertices);
        GLES20.glEnableVertexAttribArray(positionHandle);
        textureVertices.position(0);
        GLES20.glVertexAttribPointer(texCoordHandle, 2, GLES20.GL_FLOAT, false, 0, textureVertices);
        GLES20.glEnableVertexAttribArray(texCoordHandle);

        GLES20.glUniform1i(textureHandle, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texture_in);

//        GLES20.glUniform1f(texelWidthHandle, texelWidth);
//        GLES20.glUniform1f(texelHeightHandle, texelHeight);
    }
}
