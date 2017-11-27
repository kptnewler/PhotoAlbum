package com.liule.photoalbum.ui.adapter;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.liule.photoalbum.R;
import com.liule.photoalbum.asynctask.AlbumLoaderTask;
import com.liule.photoalbum.entity.PhotoFile;
import com.liule.photoalbum.entity.PhotoFolder;
import com.liule.photoalbum.impl.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 缩略图列表适配器
 *
 * @author 刺雒
 * @time 2017/10/29 
 */

public class ThumbnailsListAdapter extends RecyclerView.Adapter<ThumbnailsListAdapter.ThumbnailsListViewHolder>{
    private Context mContext;
    private List<PhotoFile> mPhotoFiles;
    private OnItemClickListener mOnItemClickListener;
    private int itemSize;


    public ThumbnailsListAdapter(Context context, List<PhotoFile> photoFiles, int itemSize) {
        this.mContext = context;
        this.mPhotoFiles = photoFiles;
        this.itemSize = itemSize;

    }

    @Override
    public ThumbnailsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThumbnailsListViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_thumbnails,parent,false),
                mOnItemClickListener,
                itemSize);
    }

    @Override
    public void onBindViewHolder(ThumbnailsListViewHolder holder, int position) {
        PhotoFile photoFile = mPhotoFiles.get(holder.getAdapterPosition());
        holder.setData(photoFile);

    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mPhotoFiles.size();
    }

    class ThumbnailsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView ivThumbnails;
        private CardView cdThumbnails;
        private OnItemClickListener mOnItemClickListener;
        private int itemSize;

        public ThumbnailsListViewHolder(View itemView, OnItemClickListener onItemClickListener, int itemSize) {
            super(itemView);
            ivThumbnails = (ImageView) itemView.findViewById(R.id.iv_item_photo_content);
            cdThumbnails = (CardView) itemView.findViewById(R.id.cd_item_thumbnails);

            mOnItemClickListener = onItemClickListener;
            this.itemSize = itemSize;

            ViewGroup.LayoutParams layoutParams = cdThumbnails.getLayoutParams();
            layoutParams.height = itemSize;
            cdThumbnails.setLayoutParams(layoutParams);

            itemView.setOnClickListener(this);
        }

        public void setData(PhotoFile photoFile) {
            AlbumLoaderTask.getInstance().loadAlbumFile(ivThumbnails, photoFile, itemSize, itemSize);

        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null ) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
}
