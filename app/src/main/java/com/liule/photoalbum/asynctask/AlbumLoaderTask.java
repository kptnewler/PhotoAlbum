package com.liule.photoalbum.asynctask;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.widget.ImageView;

import com.liule.photoalbum.R;
import com.liule.photoalbum.entity.PhotoFile;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 加载Image，内存缓存
 *
 * @author 刺雒
 * @time 2017/10/28 
 */

public class AlbumLoaderTask {
    private static final int TAG = 0x1111;
    private static Handler sInstanceHandler;
    private static final AlbumLoaderTask sInstanceTask = new AlbumLoaderTask();

    /**
     * image缓存池
     */
    private LruCache<String, Bitmap> mLruCache;
    /**
     * 线程池
     */
    private  ExecutorService mExecutorService;

    private static Drawable sPlaceHolderDrawable = new ColorDrawable(Color.parseColor("#FF2B2B2B"));


    private AlbumLoaderTask() {
        // 创建一个可重用固定线程数的线程池
        this.mExecutorService = Executors.newFixedThreadPool(6);

        // 使用虚拟机1/4的内存作为最大内存
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 4);
        mLruCache = new LruCache<String, Bitmap>(maxMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 根据宽高
                return value.getRowBytes() * value.getHeight();
            }
        };

    }


    public static AlbumLoaderTask getInstance() {
        return sInstanceTask;
    }

    /**
     * 缓存图片到内存
     *
     * @param path
     * @param width
     * @param height
     * @param bitmap
     */
    private void setCacheImage(String path, int width, int height, Bitmap bitmap) {
        if (getCacheImage(path,width,height) == null && bitmap != null) {
            synchronized (mLruCache) {
                mLruCache.put(path + width + height,bitmap);
            }
        }
    }

    /**
     * 从缓存中获取图片
     *
     * @param path
     * @param width
     * @param height
     */
    private Bitmap getCacheImage(String path, int width, int height) {
        synchronized (mLruCache) {
            return mLruCache.get(path + width + height);
        }
    }

    /**
     * 加载相册中图片
     *
     */
    public void loadAlbumFile(ImageView imageView, PhotoFile photoFile, int viewWidth, int viewHeight ) {
        loadImage(imageView,photoFile.getPath(),viewWidth,viewHeight);
    }

    /**
     * 加载图片
     *
     * @param imageView
     * @param imgPath
     * @param viewWidth
     * @param viewHeight
     */
   private void loadImage(ImageView imageView, String imgPath, int viewWidth, int viewHeight) {
       imageView.setTag(R.id.iv_album_list,imgPath);
       Bitmap bitmap = getCacheImage(imgPath, viewWidth, viewHeight);

       if (bitmap == null) {
           imageView.setImageDrawable(sPlaceHolderDrawable);
           mExecutorService.execute(new LoadImageTask(this,imageView,imgPath,viewWidth,viewHeight));
       } else {
           BitmapHolder holder = new BitmapHolder();
           holder.mImageView = imageView;
           holder.mPath = imgPath;
           holder.mBitmap = bitmap;
           getHandler().post(holder);
       }
   }

   /**
    * handler单例
    */
   private static Handler getHandler() {
       if (sInstanceHandler == null) {
           synchronized (AlbumLoaderTask.class) {
               if (sInstanceHandler == null) {
                   synchronized (AlbumLoaderTask.class) {
                       sInstanceHandler = new Handler(Looper.getMainLooper());
                   }
               }
           }
       }
       return sInstanceHandler;
   }

   /**
    * ImageView设置图片类
    *
    */
   private static class BitmapHolder implements Runnable {
        Bitmap mBitmap;
        ImageView mImageView;
        String mPath;

        @Override
        public void run() {
            if (mPath.equals(mImageView.getTag(R.id.iv_album_list))) {
                if (mBitmap != null) {
                    mImageView.setImageBitmap(mBitmap);
                }
            }
        }
   }

   /**
    * 加载图片
    */
   private static class LoadImageTask implements Runnable {

        private AlbumLoaderTask mLoader;
        private ImageView mImageView;
        private String mImagePath;
        private int mViewWidth;
        private int mViewHeight;

        LoadImageTask(AlbumLoaderTask loader, ImageView imageView, String imagePath, int viewWidth, int viewHeight) {
            this.mLoader = loader;
            this.mImagePath = imagePath;
            this.mImageView = imageView;
            this.mViewWidth = viewWidth;
            this.mViewHeight = viewHeight;
        }

        @Override
        public void run() {
            ThumbnailGenerator thumbnailGenerator = new ThumbnailGenerator();
            Bitmap bitmap = thumbnailGenerator.convertBitmap(mImagePath, mViewWidth, mViewHeight);
            postResult(bitmap);
        }

        private void postResult(Bitmap result) {
            mLoader.setCacheImage(mImagePath, mViewWidth, mViewHeight, result);
            BitmapHolder holder = new BitmapHolder();
            holder.mBitmap = result;
            holder.mImageView = mImageView;
            holder.mPath = mImagePath;
            getHandler().post(holder);
        }
    }


}
