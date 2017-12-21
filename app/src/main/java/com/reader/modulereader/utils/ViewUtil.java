package com.reader.modulereader.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.lang.reflect.Field;

/**
 * 创建者     金国栋              <br/><br/>
 * 创建时间   2016/9/22 15:04       <br/><br/>
 * 描述	      view工具类  修改宽高以及dp和px互转
 */
public class ViewUtil {

    public static ViewUtil newInstance() {
        return new ViewUtil();
    }

    /**
     * 修改View的宽和高,有设置过LayoutParams的时候调用此方法,没设置的话就调用setViewWH方法
     *
     * @param view
     * @param width  宽
     * @param height 高
     */
    public void modViewWH(View view, int width, int height) {
        if (view == null || view.getLayoutParams() == null)
            return;
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        layoutParams.height = height;
        layoutParams.width = width;
    }

    /**
     * 初次修改View的宽高，没有设置LayoutParams的时候调用此方法，如果有设置过就调用 modViewWH这个方法
     *
     * @param view
     * @param width  宽
     * @param height 高
     */
    public void setViewWH(View view, int width, int height) {
        if (view == null || view.getLayoutParams() != null)
            return;
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        view.setLayoutParams(params);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue 需要转换的值
     */
    public static int dp2px(float dpValue) {
        final float scale = SpUtil.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param pxValue 需要转换的值
     */
    public static int px2dp(float pxValue) {
        final float scale = SpUtil.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取状态栏高度
     */
    public static int getStatusBarHeight(Context mContext) {
        Class<?> c;
        Object obj;
        Field field;
        int x, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = mContext.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return statusBarHeight;
    }


    /**
     * 获取ListView|GridView单个条目View
     */
    public View getViewByPosition(int position, AbsListView listView) {

        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition) {
            return listView.getAdapter().getView(position, null, listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
