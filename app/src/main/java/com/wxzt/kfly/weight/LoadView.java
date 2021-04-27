package com.wxzt.kfly.weight;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.wxzt.lib_common.R;
import com.wxzt.lib_common.enums.LoadStatusEnum;

public class LoadView extends View {

    private int progressColor;    //进度颜色
    private int loadSuccessColor;    //成功的颜色
    private int loadFailureColor;   //失败的颜色
    private float progressWidth;    //进度宽度
    private float progressRadius;   //圆环半径
    private float progressDiameter; //圆环直径

    private Paint mPaint;
    private LoadStatusEnum mStatus;     //状态

    private int startAngle = -90;
    private int minAngle = -90;
    private int sweepAngle = 120;
    private int curAngle = 0;

    //追踪Path的坐标
    private PathMeasure mPathMeasure;
    //画圆的Path
    private Path mPathCircle;
    //截取PathMeasure中的path
    private Path mPathCircleDst;
    private Path successPath;
    private Path failurePathLeft;
    private Path failurePathRight;

    private ValueAnimator circleAnimator;
    private float circleValue;
    private float successValue;
    private float failValueRight;
    private float failValueLeft;
    float centerX;
    float centerY;

    public LoadView(Context context) {
        this(context, null);
    }

    public LoadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.lib_common_LoadView, defStyleAttr, 0);
        progressColor = array.getColor(R.styleable.lib_common_LoadView_lib_common_progress_color, ContextCompat.getColor(context, R.color.lib_common_theme_green));
        loadSuccessColor = array.getColor(R.styleable.lib_common_LoadView_lib_common_progress_width, ContextCompat.getColor(context, R.color.lib_common_theme_green));
        loadFailureColor = array.getColor(R.styleable.lib_common_LoadView_lib_common_load_success_color, ContextCompat.getColor(context, R.color.lib_common_orange));
        array.recycle();
        mStatus = LoadStatusEnum.Loading;
        progressWidth = 4;
        initPaint();
        initPath();
        initAnim();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(progressColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(progressWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);    //设置画笔为圆角笔触
    }

    private void initPath() {
        mPathCircle = new Path();
        mPathMeasure = new PathMeasure();
        mPathCircleDst = new Path();
        successPath = new Path();
        failurePathLeft = new Path();
        failurePathRight = new Path();
    }

    private void initAnim() {
        circleAnimator = ValueAnimator.ofFloat(0, 1);
        circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                circleValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getPaddingLeft(), getPaddingTop());   //将当前画布的点移到getPaddingLeft,getPaddingTop,后面的操作都以该点作为参照点


        if (mStatus == LoadStatusEnum.Loading) {    //正在加载
            if (startAngle == minAngle) {
                sweepAngle += 6;
            }
            if (sweepAngle >= 300 || startAngle > minAngle) {
                startAngle += 6;
                if (sweepAngle > 20) {
                    sweepAngle -= 6;
                }
            }
            if (startAngle > minAngle + 300) {
                startAngle %= 360;
                minAngle = startAngle;
                sweepAngle = 20;
            }
            canvas.rotate(curAngle += 4, centerX, centerY);  //旋转的弧长为4
            canvas.drawArc(new RectF(centerX -progressRadius , centerY - progressRadius,
                            centerX +progressRadius,
                            centerY + progressRadius),
                    startAngle, sweepAngle, false, mPaint);
            invalidate();
        } else if (mStatus == LoadStatusEnum.LoadSuccess) {     //加载成功
            mPaint.setColor(loadSuccessColor);
            mPathCircle.addCircle(centerX, centerY, progressRadius, Path.Direction.CW);
            mPathMeasure.setPath(mPathCircle, false);

            mPathMeasure.getSegment(0, circleValue * mPathMeasure.getLength(), mPathCircleDst, true);   //截取path并保存到mPathCircleDst中
            canvas.drawPath(mPathCircleDst, mPaint);

            if (circleValue == 1) {      //表示圆画完了,可以钩了
                successPath.moveTo(centerX - progressRadius / 8 * 4, centerY);
                successPath.lineTo(centerX-progressRadius / 8 * 1, centerY + progressRadius / 5 * 3);
                successPath.lineTo(centerX + progressRadius / 3 * 2, centerY - progressRadius / 5 * 2);

                mPathMeasure.nextContour();
                mPathMeasure.setPath(successPath, false);
                mPathMeasure.getSegment(0, successValue * mPathMeasure.getLength(), mPathCircleDst, true);
                canvas.drawPath(mPathCircleDst, mPaint);
            }
        } else {      //加载失败
            mPaint.setColor(loadFailureColor);
            mPathCircle.addCircle(centerX, centerY, progressRadius, Path.Direction.CW);
            mPathMeasure.setPath(mPathCircle, false);
            mPathMeasure.getSegment(0, circleValue * mPathMeasure.getLength(), mPathCircleDst, true);
            canvas.drawPath(mPathCircleDst, mPaint);
            if (circleValue == 1) {  //表示圆画完了,可以画叉叉的右边部分
                failurePathRight.moveTo(centerX - progressRadius / 3, centerY - progressRadius / 3);
                failurePathRight.lineTo(centerX + progressRadius / 3, centerY + progressRadius / 3 );
                mPathMeasure.nextContour();
                mPathMeasure.setPath(failurePathRight, false);
                mPathMeasure.getSegment(0, failValueRight * mPathMeasure.getLength(), mPathCircleDst, true);
                canvas.drawPath(mPathCircleDst, mPaint);
            }
            if (failValueRight == 1) {    //表示叉叉的右边部分画完了,可以画叉叉的左边部分
                failurePathLeft.moveTo(centerX + progressRadius / 3, centerY - progressRadius / 3);
                failurePathLeft.lineTo(centerX - progressRadius / 3, centerY + progressRadius / 3 );
                mPathMeasure.nextContour();
                mPathMeasure.setPath(failurePathLeft, false);
                mPathMeasure.getSegment(0, failValueLeft * mPathMeasure.getLength(), mPathCircleDst, true);
                canvas.drawPath(mPathCircleDst, mPaint);
            }
        }
    }

    //重制路径
    private void resetPath() {
        successValue = 0;
        circleValue = 0;
        failValueLeft = 0;
        failValueRight = 0;
        mPathCircle.reset();
        mPathCircleDst.reset();
        failurePathLeft.reset();
        failurePathRight.reset();
        successPath.reset();
    }

    private void setStatus(LoadStatusEnum status) {
        mStatus = status;
    }

    public void loadLoading() {
        setStatus(LoadStatusEnum.Loading);
        invalidate();
    }

    public void loadSuccess() {
        resetPath();
        setStatus(LoadStatusEnum.LoadSuccess);
        startSuccessAnim();
    }

    public LoadStatusEnum getmStatus() {
        return this.mStatus;
    }

    public void loadFailure() {
        resetPath();
        setStatus(LoadStatusEnum.LoadFailure);
        startFailAnim();
    }

    private void startSuccessAnim() {
        ValueAnimator success = ValueAnimator.ofFloat(0f, 1.0f);
        success.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                successValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        //组合动画,一先一后执行
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(success).after(circleAnimator);
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    private void startFailAnim() {
        ValueAnimator failLeft = ValueAnimator.ofFloat(0f, 1.0f);
        failLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                failValueRight = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        ValueAnimator failRight = ValueAnimator.ofFloat(0f, 1.0f);
        failRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                failValueLeft = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        //组合动画,一先一后执行
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(failLeft).after(circleAnimator).before(failRight);
        animatorSet.setDuration(500);
        animatorSet.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        progressRadius = widthSize > heightSize ? heightSize / 2f : widthSize / 2f;
        progressDiameter = progressRadius * 2 - 4 - 4;
        progressRadius = progressDiameter / 2f;
        progressDiameter = progressRadius * 2;
        centerX = widthSize / 2f;
        centerY = heightSize / 2f;

        setMeasuredDimension(widthSize, heightSize);
    }

}
