package com.example.beggar.ViewPagerIndicator;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.beggar.myapplication.R;

import java.util.List;

/**
 * Created by Beggar on 2016/7/25.
 */
public class ViewPagerIndicator extends LinearLayout {

    //画笔
    private Paint mPaint;
    //构建三角形
    private Path mPath;
    //三角形宽度
    private int mTriangleWidth;
    //三角形高度
    private int mTriangleHeight;
    // 三角形宽度 和 Tab 长度的比例（可以对外暴露，让用户自定义）
    private static final float RADIO_TRIANGLE_WIDTH = 1 / 6F;
    //三角形初始化时在X轴上的平移距离
    private int mInitTranslationX;
    //三角形平移的距离
    private int mTranlationX;

    private int mTabVisibleCount;

    private static final int COUNT_DEFAULT_TAB = 4;


    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        //获取可见tab的数量
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);

        mTabVisibleCount = typedArray.getInt(R.styleable.ViewPagerIndicator_visible_tab_count, COUNT_DEFAULT_TAB);
        if (mTabVisibleCount < 0) {
            mTabVisibleCount = COUNT_DEFAULT_TAB;
        }

        typedArray.recycle();

        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#000000"));
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));

    }

    //是ViewGroup里子空间的宽高变化时都会回调这个方法
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //初始化三角形的宽度
        mTriangleWidth = (int) (w / mTabVisibleCount * RADIO_TRIANGLE_WIDTH);
        mInitTranslationX = w / mTabVisibleCount / 2 - mTriangleWidth / 2;

        //初始化三角形
        initTriangle();

    }

    private void initTriangle() {
        mPath = new Path();
        mPath.moveTo(0, 0);
        mPath.lineTo(mTriangleWidth, 0);

        mTriangleHeight = mTriangleWidth / 2;
        mPath.lineTo(mTriangleWidth / 2, -mTriangleHeight);
        mPath.close();

    }

    //View绘制自动回调方法
    @Override
    protected void dispatchDraw(Canvas canvas) {

        canvas.save();

        canvas.translate(mInitTranslationX + mTranlationX, getHeight() + 2);
        canvas.drawPath(mPath, mPaint);

        canvas.restore();

        super.dispatchDraw(canvas);
    }

    //指示器跟随手指滚动
    public void scroll(int positon, float offset) {
        int tabWidth = getWidth() / mTabVisibleCount;
        mTranlationX = (int) (tabWidth * (positon + offset));

        if (positon >= (mTabVisibleCount - 2) && positon < getChildCount() - 2 && offset > 0 && getChildCount() > mTabVisibleCount) {

            if (mTabVisibleCount != 1) {
                this.scrollTo((positon - (mTabVisibleCount - 2)) * tabWidth + (int) (tabWidth * offset), 0);
            } else {
                this.scrollTo(positon * tabWidth + (int) (tabWidth * offset), 0);
            }
        }
        invalidate();

    }


    //在View中，当xml加载完成之后回调的方法
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        int cCount = getChildCount();
        if (cCount == 0) return;

        for (int i = 0; i < cCount; i++) {
            View view = getChildAt(i);
            LinearLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
            lp.weight = 0;
            lp.width = getSceenWidth() / mTabVisibleCount;
            view.setLayoutParams(lp);
        }

    }

    private List<String> titles;

    //动态的添加 Tab
    public void setTabTitle(List<String> titles) {
        if (titles != null && titles.size() > 0)
            this.titles = titles;
            this.removeAllViews();
        for (String title : titles) {
            this.addView(getTab(title));
        }
    }
    //动态创建 TabT
    private static final int COLOR_TEXT = 0x99999999;

    private View getTab(String title) {

        TextView mTextView = new TextView(getContext());
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.width = getSceenWidth() / mTabVisibleCount;
        mTextView.setText(title);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        mTextView.setTextColor(COLOR_TEXT);
        mTextView.setLayoutParams(lp);

        return mTextView;
    }

    //获得屏幕的宽度
    private int getSceenWidth() {

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);

        return outMetrics.widthPixels;
    }
}
