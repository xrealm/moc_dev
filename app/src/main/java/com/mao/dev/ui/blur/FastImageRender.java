package com.mao.dev.ui.blur;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Mao on 2017/4/6.
 */

public class FastImageRender implements GLSurfaceView.Renderer {

    public static final String ATTRIBUTE_POSITION = "position";
    public static final String ATTRIBUTE_TEXCOORD = "inputTextureCoordinate";
    public static final String VARYING_TEXCOORD = "textureCoordinate";
    protected static final String UNIFORM_TEXTUREBASE = "inputImageTexture";
    public static final String UNIFORM_TEXTURE0 = UNIFORM_TEXTUREBASE + 0;

    protected static final String UNIFORM_TEXELWIDTH = "texelWidthOffset";
    protected static final String UNIFORM_TEXELHEIGHT = "texelHeightOffset";

    protected int programHandle;
    protected int textureHandle;
    protected int positionHandle;
    protected int texCoordHandle;
    protected int texture_in;

    protected float texelWidth;
    protected float texelHeight;
    private int texelWidthHandle;
    private int texelHeightHandle;

    private String getVertexShader() {
        return
                "attribute vec4 " + ATTRIBUTE_POSITION + ";\n"
                        + "attribute vec2 " + ATTRIBUTE_TEXCOORD + ";\n"
                        + "varying vec2 " + VARYING_TEXCOORD + ";\n"

                        + "void main() {\n"
                        + "  " + VARYING_TEXCOORD + " = " + ATTRIBUTE_TEXCOORD + ";\n"
                        + "   gl_Position = " + ATTRIBUTE_POSITION + ";\n"
                        + "}\n";
    }

    private String getFragmentShader() {
        return
                "precision mediump float;\n"
                        + "uniform sampler2D " + UNIFORM_TEXTURE0 + ";\n"
                        + "varying vec2 " + VARYING_TEXCOORD + ";\n"
                        + "uniform float " + UNIFORM_TEXELWIDTH + ";\n"
                        + "uniform float " + UNIFORM_TEXELHEIGHT + ";\n"


                        + "void main(){\n"
                        + "   vec2 firstOffset = vec2(1.3846153846 * " + UNIFORM_TEXELWIDTH + ", 1.3846153846 * " + UNIFORM_TEXELHEIGHT + ");\n"
                        + "   vec2 secondOffset = vec2(3.2307692308 * " + UNIFORM_TEXELWIDTH + ", 3.2307692308 * " + UNIFORM_TEXELHEIGHT + ");\n"
                        + "   vec3 sum = vec3(0,0,0);\n"
                        + "   vec4 color = texture2D(" + UNIFORM_TEXTURE0 + ", " + VARYING_TEXCOORD + ");\n"
                        + "   sum += color.rgb * 0.2270270270;\n"
                        + "   sum += texture2D(" + UNIFORM_TEXTURE0 + ", " + VARYING_TEXCOORD + " - firstOffset).rgb * 0.3162162162;\n"
                        + "   sum += texture2D(" + UNIFORM_TEXTURE0 + ", " + VARYING_TEXCOORD + " + firstOffset).rgb * 0.3162162162;\n"
                        + "   sum += texture2D(" + UNIFORM_TEXTURE0 + ", " + VARYING_TEXCOORD + " - secondOffset).rgb * 0.0702702703;\n"
                        + "   sum += texture2D(" + UNIFORM_TEXTURE0 + ", " + VARYING_TEXCOORD + " + secondOffset).rgb * 0.0702702703;\n"
                        + "   gl_FragColor = vec4(sum, color.a);\n"
                        + "}\n";
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        initWithGLContext();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        texelWidth = 1.0f / (float) width;
        texelHeight = 1.0f / (float) height;
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(0, 0, 0, 0);
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

        texelWidthHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_TEXELWIDTH);
        texelHeightHandle = GLES20.glGetUniformLocation(programHandle, UNIFORM_TEXELHEIGHT);
    }

    private void passShaderValues() {
    }
}
