package com.liule.photoalbum.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liule.photoalbum.R;
import com.liule.photoalbum.asynctask.AlbumLoaderTask;
import com.liule.photoalbum.entity.PhotoFile;
import com.liule.photoalbum.entity.PhotoFolder;
import com.liule.photoalbum.impl.OnItemClickListener;
import com.liule.photoalbum.utils.DisplayUtils;

import java.util.List;

/**
 * 相册适配器
 *
 * @author 刺雒
 * @time 2017/10/28
 */

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumViewHolder>{
    private Context mContext;
    private List<PhotoFolder> mPhotoFolders;
    private OnItemClickListener mOnItemClickListener;


    public AlbumListAdapter(Context context, List<PhotoFolder> photoFolders) {
        mContext = context;
        mPhotoFolders = photoFolders;

    }

    @Override
    public AlbumListAdapter.AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_album,parent,false);
        return new AlbumViewHolder(itemView,mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder holder, int position) {
         int newPosition = holder.getAdapterPosition();
        holder.setData(mPhotoFolders.get(newPosition));
    }

    @Override
    public int getItemCount() {
        return mPhotoFolders.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private OnItemClickListener mOnItemClickListener;

        private ImageView ivAlbumPhoto;
        private TextView tvAlbumContent;

        public AlbumViewHolder(View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            this.mOnItemClickListener = onItemClickListener;

            ivAlbumPhoto = (ImageView) itemView.findViewById(R.id.iv_album_list);
            tvAlbumContent = (TextView) itemView.findViewById(R.id.tv_album_content);

            itemView.setOnClickListener(this);
        }

        public void setData(PhotoFolder photoFolder) {
            String content = String.format(mContext.getResources().getString(R.string.album_content_text)
                    ,photoFolder.getPhotoFiles().size(),photoFolder.getName());
            tvAlbumContent.setText(content);

            PhotoFile photoFile = photoFolder.getPhotoFiles().get(0);

            AlbumLoaderTask.getInstance().loadAlbumFile(ivAlbumPhoto,
                    photoFile,DisplayUtils.dip2px(mContext,50),
                    DisplayUtils.dip2px(mContext,50));
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v,getAdapterPosition());
            }
        }
    }
}
