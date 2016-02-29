package org.privy.liuysha.viewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/12/8.
 */
public class FlowLayout extends ViewGroup{


    private static final String TAG = "FlowLayout";

    public FlowLayout(Context context) {
        this(context,null);
        Log.i(TAG,"context");
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        Log.i(TAG,"context, attrs");
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.i(TAG,"context, attrs, defStyleAttr");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 获取他的父容器为它设置的测量模式和大小
         * */
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        /**
         * 如果是wrap_content情况下，记录宽和高
         * */
        int width = 0;
        int height = 0;

        /**
         * 记录每一行的宽度，width不断取最大宽度
         * */
        int lineWidth = 0;
        /**
         * 每一行的高度，累加至height
         * */
        int lineHeight = 0;

        /**
         * 子元素数量
         * */
        int cCount = getChildCount();

        //遍历每个子元素
        for (int i=0; i<cCount; i++){
            View childView = getChildAt(i);
            //测量每一个childView的宽和高
            measureChild(childView,widthMeasureSpec,heightMeasureSpec);
            //得到childView的LayoutParams
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            //当前子空间实际占据的宽度
            int cWidth = childView.getMeasuredWidth() + layoutParams.leftMargin
                    + layoutParams.rightMargin;
            //当前子空间实际占据的高度
            int cHeight = childView.getMeasuredHeight() + layoutParams.topMargin
                    + layoutParams.bottomMargin;

            /**
             * 如果加入当前child，超出最大宽度，则把目前最大宽度赋值给width，累加height，然后开启新行
             *
             *  childWidth + lineWidth - layoutParams.rightMarin
             *  每一行的最后一个控件可以忽略右边距
             * */
            if (lineWidth + cWidth - layoutParams.rightMargin > sizeWidth){
                width = Math.max(lineWidth, cWidth);
                //重新开始新行，开始记录
                lineWidth = cWidth;
                //叠加当前高度
                height += lineHeight;
                //开启记录下一行的高度
                lineHeight = cHeight;
            } else {
                //否则累加值lineWidth,lineHeight取最大高度
                lineWidth += cWidth;
                lineHeight = Math.max(lineHeight,cHeight);
            }

            //如果是最后一个，则将当前记录的最大宽度和当前lineWidth做比较
            if (i == cCount - 1){
                width = Math.max(width,lineWidth);
                height += lineHeight;
            }
        }

        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width
                , (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        //累加当前行的行宽
        int lineWidth = 0;
        //当前行的行高
        int lineHeight = 0;
        //当前坐标的top坐标和left坐标
        int top = 0;
        int left = 0;

        for (int i=0; i<count; i++){
            View child = getChildAt(i);
            MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + layoutParams.leftMargin
                    + layoutParams.rightMargin;
            int childHeight = child.getMeasuredHeight() + layoutParams.topMargin
                    + layoutParams.bottomMargin;

            /**
             *  childWidth + lineWidth - layoutParams.rightMarin
             *  每一行的最后一个控件可以忽略右边距
             * */
            if (childWidth + lineWidth - layoutParams.rightMargin > getMeasuredWidth()){
                //如果换行
                top += lineHeight;
                left = 0;
                lineHeight = childHeight;
                lineWidth = childWidth;
            } else {
                lineHeight = Math.max(lineHeight, childHeight);
                lineWidth += childWidth;
            }

            //计算childView的left，top，right，bottom
            int cl = left + layoutParams.leftMargin;
            int ct = top + layoutParams.topMargin;
            int cr = cl + child.getMeasuredWidth();
            int cb = ct + child.getMeasuredHeight();
            child.layout(cl,ct,cr,cb);
            //将left置为下一个控件的起始点
            left += childWidth;
        }
    }
}
