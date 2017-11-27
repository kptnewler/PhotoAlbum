package com.liule.photoalbum.asynctask;

import android.content.Context;
import android.os.AsyncTask;

import com.liule.photoalbum.entity.PhotoFile;

import java.util.ArrayList;

/**
 * 生成缩略图
 *
 * @author 刺雒
 * @time 2017/10/28
 */

public class ThumbnailListAsyncTask extends AsyncTask<Void,Void,ArrayList<PhotoFile>> {

    private ArrayList<PhotoFile> mPhotoFiles;
    private CallBack mCallBack;

    private ThumbnailGenerator mThumbnailGenerator;

    public ThumbnailListAsyncTask(Context context, ArrayList<PhotoFile> photoFiles, CallBack callBack) {
        mPhotoFiles = photoFiles;
        mCallBack = callBack;

        this.mThumbnailGenerator = new ThumbnailGenerator(context);
    }

    @Override
    protected ArrayList<PhotoFile> doInBackground(Void... params) {
        String thumbnailPath;

        // 设置缩略图的路径
        for (PhotoFile photoFile : mPhotoFiles) {
            thumbnailPath = mThumbnailGenerator.createThumbnail(photoFile.getPath());
            photoFile.setThumpPath(thumbnailPath);
        }

        return mPhotoFiles;
    }

    @Override
    protected void onPostExecute(ArrayList<PhotoFile> photoFiles) {
        mCallBack.onGetThumbnailList(photoFiles);
    }


    public interface CallBack {
        /**
         * 返回缩略图列表
         *
         * @param thumbnails
         */
        void onGetThumbnailList(ArrayList<PhotoFile> thumbnails);


    }
}
