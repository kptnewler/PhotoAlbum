package com.liule.photoalbum.ui.widget.photoview;


import android.graphics.RectF;

/**
 * @author 刺雒
 * @time 2017/10/29
 */
public interface IPhotoView {

    float DEFAULT_MIN_SCALE = 1.0f;
    int DEFAULT_ZOOM_DURATION = 200;

    RectF getDisplayRect();


    float getScale();


    void setScale(float scale, float focalX, float focalY, boolean animate);

    void setZoomable(boolean zoomable);




}
