package com.liule.photoalbum.impl;

import android.view.View;

/**
 * 监听RecycleView每个Item点击事件
 *
 * @author 刺雒
 * @time 2017/10/28
 */

public interface OnItemClickListener {

    /**
     * 点item被点击
     *
     * @param view
     * @param pos
     */
    void onItemClick(View view, int pos);
}
