package com.liule.photoalbum.asynctask;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.liule.photoalbum.R;
import com.liule.photoalbum.entity.PhotoFile;
import com.liule.photoalbum.entity.PhotoFolder;
import com.liule.photoalbum.ui.dialog.LoadingProgressDialog;

import java.util.ArrayList;

/**
 * 图片处理异步加载
 *
 * @author 刺雒
 * @time 2017/10/28
 */

public class ImageAsyncTask extends AsyncTask<Void, Void, ArrayList<PhotoFolder>> {
    private Context mContext;
    private AllAlbumCallback mAllAlbumCallback;
    private Dialog mLoadingDialog;

    public ImageAsyncTask(Context context, AllAlbumCallback callback) {
        mContext = context;
        mAllAlbumCallback = callback;
        mLoadingDialog = new LoadingProgressDialog(context,context.getString(R.string.loading_image_title),context.getString(R.string.loading_msg));
    }


    @Override
    protected void onPostExecute(ArrayList<PhotoFolder> photoFolders) {
        if (mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }

        mAllAlbumCallback.onScanAllAlbum(photoFolders);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    @Override
    protected ArrayList<PhotoFolder> doInBackground(Void... params) {
        ImageGenerator imageGenerator = new ImageGenerator(mContext);
        ArrayList<PhotoFolder> photoFolders = imageGenerator.getAllAlbum();

        return photoFolders;
    }

    public interface AllAlbumCallback {
        /**
         * 扫描所有相册
         *
         * @param photoFolders
         */
        void onScanAllAlbum(ArrayList<PhotoFolder> photoFolders);

    }

}
