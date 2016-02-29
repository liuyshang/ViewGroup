package org.privy.liuysha.viewgroup;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by admin on 2015/12/8.
 */
public class ViewGroupTest extends ViewGroup{

    private static final String TAG = "ViewGroupTest";

    public ViewGroupTest(Context context) {
        this(context, null);
        Log.i(TAG, "context");
    }

    public ViewGroupTest(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        Log.i(TAG,"context, attrs");
    }

    public ViewGroupTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.i(TAG, "context, attrs, defStyleAttr");
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ViewGroupTest(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 决定该ViewGroup的LayoutParams
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(),attrs);
    }

    /**
     * 计算childView的测量值以及模式，以及设置自己的宽和高
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 获取此ViewGroup上级容器为其推荐的宽和高，以及计算模式
         * */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.i(TAG,"sizeWidth:" + sizeWidth + "; sizeHeight:" + sizeHeight);

        /**
         * 计算出所有的childView的宽和高
         * */
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        /**
         * 记录如果是wrap_content是设置的宽和高
         * */
        int width = 0;
        int height = 0;

        /**
         * childView数量
         * */
        int cCount = getChildCount();

        int cWidth = 0;
        int cHeight = 0;
        MarginLayoutParams cParams = null;

        /**
         * 用于计算左边两个childView的高度
         * */
        int lHeight = 0;

        /**
         * 用于计算右边两个childView的高度，最终高度取二者之间较大值
         * */
        int rHeight = 0;

        /**
         * 用于计算上边两个childView的宽度
         * */
        int tWidth = 0;

        /**
         * 用于计算下面两个childView的宽度，最终宽度取二者之间较大值
         * */
        int bWidth = 0;

        /**
         * 根据childView计算出的宽和高，以及设置的margin计算容器的宽和高，主要用于容器是wrap_content时
         * */
        for (int i=0; i<cCount; i++){
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            /**
             * 上面两个childView
             * */
            if (i == 0 || i == 1) {
                tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }
            if (i == 2 || i == 3){
                bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            }

            if (i == 0 || i == 2){
                lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }
            if (i == 1 || i == 3){
                rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
            }
        }

        width = Math.max(tWidth, bWidth);
        height = Math.max(lHeight,rHeight);
        Log.i(TAG,"width:" + width + "; height:" + height);

        /**
         * 如果是wrap_content设置为我们计算的值
         * 否则：直接设置为父容器计算的值
         * */
        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth : width
                ,(heightMode == MeasureSpec.EXACTLY) ? sizeHeight : height);

    }

    /**
     * onLayout对其所有childView进行定位（设置childView的绘制区域）
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;

        /**
         * 容器
         * */
        MarginLayoutParams cParams = null;

        /**
         * 遍历所有childView根据其宽和高，以及margin进行布局
         * */
        for (int i=0; i<cCount; i++){
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            Log.i(TAG,i + " cWidth:" + cWidth + "; cHeight:" + cHeight);
            cParams = (MarginLayoutParams) childView.getLayoutParams();

            int cl =0 , ct = 0, cr = 0, cb = 0;

            switch (i){
                case 0:
                    cl = cParams.leftMargin;
                    ct = cParams.topMargin;
                    break;
                case 1:
                    cl = getWidth() - cWidth - cParams.leftMargin - cParams.rightMargin;
                    ct = cParams.topMargin;
                    break;
                case 2:
                    cl = cParams.leftMargin;
                    ct = getHeight() - cHeight - cParams.bottomMargin;
                    break;
                case 3:
                    cl = getWidth() - cWidth - cParams.leftMargin - cParams.rightMargin;
                    ct = getHeight() - cHeight - cParams.bottomMargin;
                    break;
            }
            cr = cl + cWidth;
            cb = cHeight + ct;
            childView.layout(cl,ct,cr,cb);
        }
    }
}
