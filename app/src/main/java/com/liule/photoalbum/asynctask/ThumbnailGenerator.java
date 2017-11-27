package com.liule.photoalbum.asynctask;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.liule.photoalbum.utils.FileUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 缩略图生成器
 *
 * @author 刺雒
 * @time 2017/10/28 
 */

public class ThumbnailGenerator {
    private Context mContext;
    private File imageCacheDir;

    public ThumbnailGenerator(Context context) {
        mContext = context;
        imageCacheDir = createCacheDir(context);
    }

    public ThumbnailGenerator() {

    }

    /**
     * 创建一个目录缓存缩略图
     */
    private File createCacheDir(Context context) {
        File rootDir = FileUtils.getRootPath(context);
        File cacheDir = new File(rootDir,"PhotoAlbumCache");

        // 如果文件存在
        if (cacheDir.exists() && cacheDir.isFile()) {
            cacheDir.delete();
        }

        // 如果文件不存在
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        return cacheDir;

    }

   /**
    * 生成图片对应的文件
    *
    * @param path
    */
    public String createThumbnail(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }

        File imageFile = new File(path);
        if (!imageFile.exists()) {
            return path;
        }

        File thumbnailFile = createThumbnailPath(imageCacheDir,path);
        if (thumbnailFile.exists()) {
            return thumbnailFile.getAbsolutePath();
        }

        Bitmap bitmap = convertBitmap(path,720,1280);
        if (bitmap == null) {
            return path;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG,options,outputStream);

        // 压缩成缩略图，直到小于200kb
        while (outputStream.toByteArray().length > 200 * 1024) {
            // 有的图片过大
            if (options <= 0) {
                break;
            }
            // 清空stream
            outputStream.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
        }

        try {
            thumbnailFile.createNewFile();
        } catch (IOException e) {
            return path;
        }

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(thumbnailFile);
            fileOutputStream.write(outputStream.toByteArray());
        } catch (Exception e) {
            return path;
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return thumbnailFile.getAbsolutePath();
    }

    /**
     * 生成缩略图图片
     */
    private File createThumbnailPath(File cacheDir, String filePath ) {
        return new File(cacheDir,new StringBuilder(filePath).append("Thumbnail").toString());
    }

    /**
     * 将图片路径转换为压缩后的Bitmap
     *
     * @param imagePath
     * @param width
     * @param height
     */
    public Bitmap convertBitmap(String imagePath, int width,int height) {
        File imageFile = new File(imagePath);

        if (imageFile.exists()) {
            BufferedInputStream inputStream = null;
            ByteArrayOutputStream outputStream = null;
            try {
                inputStream = new BufferedInputStream(new FileInputStream(imageFile));
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds =true;
                BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();

                options.inSampleSize = calculateScaleSize(options, width, height);
                options.inJustDecodeBounds = false;

                Bitmap decodeBitmap = null;

                // 把输入流转换成bitmap
                boolean decodeAttemptSuccess = false;
                while (!decodeAttemptSuccess) {
                    inputStream = new BufferedInputStream(new FileInputStream(imageFile));
                    try {
                        byte[] data=readStream(inputStream);
                        decodeBitmap = BitmapFactory.decodeByteArray(data, 0, data.length,options);
                        decodeAttemptSuccess = true;
                    } catch (Exception e) {
                        options.inSampleSize *= 2;
                    }
                    inputStream.close();
                }


                if (imagePath.endsWith(".jpg") || imagePath.endsWith(".JPG") ||
                        imagePath.endsWith(".jpeg") || imagePath.endsWith(".JPEG")) {
                    int degrees = readDegree(imagePath);
                    if (degrees > 0) {
                        Matrix matrix = new Matrix();
                        matrix.setRotate(degrees, decodeBitmap.getWidth() / 2, decodeBitmap.getHeight() / 2);
                        decodeBitmap = Bitmap.createBitmap(
                                decodeBitmap, 0, 0,
                                decodeBitmap.getWidth(),
                                decodeBitmap.getHeight(),
                                matrix, true);
                    }
                }

                 outputStream = new ByteArrayOutputStream();
                int o = 100;
                decodeBitmap.compress(Bitmap.CompressFormat.JPEG,o,outputStream);

                // 压缩成缩略图，直到小于200kb
                while (outputStream.toByteArray().length > 200 * 1024) {
                    // 有的图片过大
                    if (o <= 0) {
                        break;
                    }
                    // 清空stream
                    outputStream.reset();
                    o -= 10;
                    decodeBitmap.compress(Bitmap.CompressFormat.JPEG, o, outputStream);
                }

                return decodeBitmap;
            } catch (Exception e){
                e.printStackTrace();
            }finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 计算缩放比
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     */
    private  int calculateScaleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 获取到图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        // 默认缩放比是1
        int scale = 1;

        if (height > reqHeight || width > reqWidth) {
            while ((height / scale) > reqHeight || (width / scale) > reqWidth) {
                scale *= 2;
            }
        }
        return scale;
    }

    /**
     * 读取返回照片拍摄角度转变
     *
     * @param path image path.
     */
    private   int readDegree(String path) {
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return 90;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return 180;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return 270;
                default:
                    return 0;
            }
        } catch (Exception e) {
            return 0;
        }
    }

   /**
    * 得到图片字节流 数组大小
    */
    private byte[] readStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1){
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }
}
