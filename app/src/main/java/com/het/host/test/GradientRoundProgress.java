package com.het.host.test;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.het.attendance.test.R;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class GradientRoundProgress extends View {
    private Paint mPaint;
    private Paint mShadowPaint;

    private int startColor;
    private int endColor;
    private int shadowColor;
    private Path mPath;
    private Path mShadowPath;
    private Timer mTimer;
    private int maxProgress = 100;
    private float progress;
    private float mStrokeWidth;
    private int mWidth;
    private int mHeight;
    private float mCornerRadius;
    private float mDuration = 3000f;
    private int mFreq = 30;

    public GradientRoundProgress(Context context) {
        super(context);
        init(null);
    }

    public GradientRoundProgress(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public GradientRoundProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public GradientRoundProgress(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    public void setProgress(float mp) {
        this.progress = mp;
        if (mTimer!=null) {
            mTimer.cancel();mTimer = null;
        }
        invalidate();
    }

    public void startToProgress(float mp) {
        if (mp < 0 || mp > maxProgress) return;
        progress = 0;
        if (mTimer!=null) {
            mTimer.cancel();mTimer = null;
        }
        float cost = mDuration / maxProgress * mp;
        float freq = mFreq;
        int period = (int) (1000 / freq);
        float step = period / cost * mp;


        Log.e("TTTT", "mStep:"+step);
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                progress += step;
                invalidate();
                if (progress >= maxProgress || progress >= mp) {
                    mTimer.cancel();
                }
            }
        },new Date(), period);
    }

    public void setMaxProgress(int maxProgress) {
        this.maxProgress = maxProgress;
    }

    public void setCornerRadius(float mCornerRadius) {
        this.mCornerRadius = mCornerRadius;
        invalidate();
    }

    public void setDuration(float duration) {
        this.mDuration = duration;
    }

    public void setFreqment(int freq) {
        this.mFreq = freq;
    }

    private void init(AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.GradientRoundProgress);
        mCornerRadius = ta.getDimension(R.styleable.GradientRoundProgress_grp_roundRadius,20);
        mStrokeWidth = ta.getDimension(R.styleable.GradientRoundProgress_grp_roundWidth,20);
        mDuration = ta.getFloat(R.styleable.GradientRoundProgress_grp_duration,1500);
        mFreq = ta.getInt(R.styleable.GradientRoundProgress_grp_freqment,30);
        maxProgress = ta.getInt(R.styleable.GradientRoundProgress_grp_max,100);
        startColor = ta.getColor(R.styleable.GradientRoundProgress_grp_startColor, Color.parseColor("#63dc80"));
        endColor = ta.getColor(R.styleable.GradientRoundProgress_grp_endColor, Color.parseColor("#fe7b39"));
        shadowColor = ta.getColor(R.styleable.GradientRoundProgress_grp_shadowColor, Color.parseColor("#33333333"));
        ta.recycle();

        mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setDither(true);
        mShadowPaint.setStrokeWidth(mStrokeWidth);
        mShadowPaint.setStyle(Paint.Style.STROKE);
        mShadowPaint.setColor(shadowColor);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);

        mShadowPath = new Path();
        mPath = new Path();
    }

    private void drawShadowPath() {
        float cornerRadius = mCornerRadius;
        int offset = (int) mStrokeWidth/2;
        int width = mWidth-offset;
        int height = mHeight-offset;

        Path path = mShadowPath;
        // 重置路径
        path.reset();

        // 移动到左上角
        path.moveTo(offset, offset+cornerRadius);

        // 绘制左上圆角
        path.arcTo(new RectF(offset, offset, 2*offset+cornerRadius * 2, 2*offset+cornerRadius * 2), 180, 90);

        // 绘制顶部直线
        path.lineTo(width - cornerRadius-offset, offset);

        // 绘制右上圆角
        path.arcTo(new RectF(width - cornerRadius * 2-offset*2, offset, width, cornerRadius * 2+offset*2), 270, 90);

        // 绘制右侧直线
        path.lineTo(width, offset+cornerRadius+(height-offset*2 - cornerRadius*2));

        // 绘制右下圆角
        path.arcTo(new RectF(width - cornerRadius * 2-offset*2, -offset*2+height - cornerRadius * 2, width, height), 0, 90);

        // 绘制底部直线
        path.lineTo(offset+cornerRadius + (width-2*cornerRadius-offset*2), height);

        // 绘制左下圆角
        path.arcTo(new RectF(offset, -offset*2+height - cornerRadius * 2, offset*2+cornerRadius * 2, height), 90, 90);

        // 闭合路径
        path.close();

    }

    private void drawPath() {
        float cornerRadius = mCornerRadius;
        int offset = (int) mStrokeWidth/2;
        int width = mWidth-offset;
        int height = mHeight-offset;

        Path path = mPath;
        mPaint.setShader(new LinearGradient(mWidth/2,0,mWidth/2, mHeight, new int[]{startColor, endColor}, null, Shader.TileMode.CLAMP));
        // 重置路径
        path.reset();

        // 移动到左上角
        path.moveTo(offset, offset*2+cornerRadius);

        // 绘制左上圆角
        if (progress > 0) {
            path.arcTo(new RectF(offset, offset, 2*offset+cornerRadius * 2, 2*offset+cornerRadius * 2), 180, 90*(Math.min(progress, 10)/10));
        }

        if (progress>=10) {
            // 绘制顶部直线
            path.lineTo(offset+cornerRadius+(width-offset*2 - cornerRadius*2)*(Math.min((progress-10), 15)/15), offset);
        }

        if (progress>=25) {
            // 绘制右上圆角
            path.arcTo(new RectF(width - cornerRadius * 2-offset*2, offset, width, cornerRadius * 2+offset*2), 270, 90*(Math.min((progress-25), 10)/10));
        }

        if (progress>=35) {
            // 绘制右侧直线
            path.lineTo(width, offset+cornerRadius+(height-offset*2 - cornerRadius*2)*(Math.min((progress-35), 15)/15));
        }

        if (progress>=50) {
            // 绘制右下圆角
            path.arcTo(new RectF(width - cornerRadius * 2-offset*2, -offset*2+height - cornerRadius * 2, width, height), 0, 90*(Math.min((progress-50), 10)/10));
        }

        if (progress>=60) {
            // 绘制底部直线
//            path.lineTo(offset+width - (cornerRadius + (width - cornerRadius*2)*(Math.min((progress-60), 15)/15)), height);
            float percent = 1-(Math.min((progress-60), 15)/15f);
            path.lineTo(2*offset+cornerRadius + (width-2*cornerRadius-offset*2)*percent, height);
        }

        if (progress>=75) {
            // 绘制左下圆角
            path.arcTo(new RectF(offset, -offset*2+height - cornerRadius * 2, offset*2+cornerRadius * 2, height), 90, 90*(Math.min((progress-75), 10)/10));
        }
//
        if (progress>=85) {
            // 绘制左侧直线
            float percent = 1-(Math.min((progress-85), 15)/15f);
            path.lineTo(offset, offset + cornerRadius + (height - cornerRadius*2-offset*2)*percent);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthM = MeasureSpec.getMode(widthMeasureSpec);
        int widthS = MeasureSpec.getSize(widthMeasureSpec);

        int heightM = MeasureSpec.getMode(heightMeasureSpec);
        int heightS = MeasureSpec.getSize(heightMeasureSpec);

        if (widthM == MeasureSpec.EXACTLY) {
            mWidth = widthS;
        } else if (widthM == MeasureSpec.AT_MOST) {
            mWidth = Math.max(mWidth, widthS);
        }

        if (heightM == MeasureSpec.EXACTLY) {
            mHeight = heightS;
        } else if (heightM == MeasureSpec.AT_MOST) {
            mHeight = Math.max(mHeight, heightS);
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawShadowPath();
        drawPath();
        canvas.drawPath(mShadowPath,mShadowPaint);
        canvas.drawPath(mPath,mPaint);
    }
}
