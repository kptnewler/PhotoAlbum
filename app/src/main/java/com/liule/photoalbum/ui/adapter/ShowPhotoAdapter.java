package com.liule.photoalbum.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liule.photoalbum.asynctask.AlbumLoaderTask;
import com.liule.photoalbum.entity.PhotoFile;
import com.liule.photoalbum.impl.OnItemClickListener;
import com.liule.photoalbum.ui.widget.photoview.AttacherImageView;
import com.liule.photoalbum.ui.widget.photoview.PhotoViewAttacher;
import com.liule.photoalbum.utils.DisplayUtils;

import java.util.ArrayList;

/**
 * 显示大图适配器
 *
 * @author 刺雒
 * @time 2017/10/29
 */

public class ShowPhotoAdapter extends PagerAdapter {
    private Activity mContext;
    private ArrayList<PhotoFile> mPhotoFiles;
    private AttacherImageView imageView;
    public ShowPhotoAdapter(Activity context, ArrayList<PhotoFile> photoFiles) {
        mContext = context;
        this.mPhotoFiles = photoFiles;

    }

    private void loadShowPhoto(ImageView imageView, PhotoFile photoFile, int pos) {
        AlbumLoaderTask.getInstance().loadAlbumFile(imageView,photoFile, DisplayUtils.getScreenWidth(mContext),DisplayUtils.getScreenWidth(mContext));
    }

    @Override
    public int getCount() {
        return mPhotoFiles == null ? 0 : mPhotoFiles.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(((View) object));
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
         imageView = new AttacherImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
        imageView.setAttacher(attacher);

        loadShowPhoto(imageView, mPhotoFiles.get(position), position);

        container.addView(imageView);
        return imageView;
    }



}
