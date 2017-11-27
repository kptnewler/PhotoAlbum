package com.liule.photoalbum.impl;

/**
 * Created by 刺雒 on 2017/10/27.
 */

public interface PhotoAlbumCallback {

    /**
     * 获取相册图片成功回调
     */
    void onGetPhotoSucceeded();

    /**
     * 获取相册图片失败
     */
    void onGetPhotoFailed();
}
