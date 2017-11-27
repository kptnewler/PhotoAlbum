package com.liule.photoalbum.asynctask;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;

import com.liule.photoalbum.R;
import com.liule.photoalbum.entity.PhotoFile;
import com.liule.photoalbum.entity.PhotoFolder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图片生成器
 *
 * @author 刺雒
 * @time 2017/10/28 
 */

public class ImageGenerator {
    private Context mContext;
    private ArrayList<PhotoFile> allPhotoFiles;

    private static final String[] IMAGES =  {
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.TITLE,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATE_MODIFIED,
            MediaStore.Images.Media.SIZE
    };

    public ImageGenerator(Context context) {
        mContext = context;
    }

    /**
     * 扫描手机中的所有图片
     *
     * @param photoFolderMap
     * @param photoFolderTotal
     */
    private void scanImage(Map<String, PhotoFolder> photoFolderMap, PhotoFolder photoFolderTotal) {
        ContentResolver contentResolver = mContext.getContentResolver();

        // 根据图片添加时间顺序读取所有图片
        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGES, null, null, MediaStore.Images.Media.DATE_ADDED);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String path = cursor.getString(cursor.getColumnIndex(IMAGES[0]));
                String name = cursor.getString(cursor.getColumnIndex(IMAGES[1]));
                String title = cursor.getString(cursor.getColumnIndex(IMAGES[2]));
                int folderId = cursor.getInt(cursor.getColumnIndex(IMAGES[3]));
                String folderName = cursor.getString(cursor.getColumnIndex(IMAGES[4]));
                String mimeType = cursor.getString(cursor.getColumnIndex(IMAGES[5]));
                long addDate = cursor.getLong(cursor.getColumnIndex(IMAGES[6]));
                long modifyDate = cursor.getLong(cursor.getColumnIndex(IMAGES[7]));
                long size = cursor.getLong(cursor.getColumnIndex(IMAGES[8]));

                PhotoFile photoFile = new PhotoFile();
                photoFile.setPath(path);
                photoFile.setName(name);
                photoFile.setTitle(title);
                photoFile.setFolderID(folderId);
                photoFile.setFolderName(folderName);
                photoFile.setMimeType(mimeType);
                photoFile.setAddDate(addDate);
                photoFile.setModifyDate(modifyDate);
                photoFile.setSize(size);

                photoFolderTotal.addPhotoFile(photoFile);
                PhotoFolder photoFolder = photoFolderMap.get(folderName);

                if (photoFolder != null) {
                    photoFolder.addPhotoFile(photoFile);
                } else {
                    photoFolder = new PhotoFolder();
                    photoFolder.setId(folderId);
                    photoFolder.setName(folderName);
                    photoFolder.addPhotoFile(photoFile);

                    photoFolderMap.put(folderName,photoFolder);
                }
            }
            cursor.close();
        }
    }

    /**
     * 将获取的所有图片分成相册
     *
     */
    public ArrayList<PhotoFolder> getAllAlbum() {
        ArrayList<PhotoFolder> photoFolders = new ArrayList<>();

        Map<String,PhotoFolder> photoFolderMap = new HashMap<>();
        PhotoFolder photoFolderTotal = new PhotoFolder();
        photoFolderTotal.setName(mContext.getString(R.string.photo_total));
        scanImage(photoFolderMap,photoFolderTotal);

        // 所有照片集合
        allPhotoFiles = photoFolderTotal.getPhotoFiles();
        Collections.sort(allPhotoFiles);
        photoFolders.add(photoFolderTotal);

        for (Map.Entry<String,PhotoFolder> entry : photoFolderMap.entrySet()) {

            PhotoFolder photoFolder =  entry.getValue();
            Collections.sort(photoFolder.getPhotoFiles());
            photoFolders.add(photoFolder);
        }

        return photoFolders;
    }

    /**
     * 所有照片按照时间顺序
     *
     */
    public ArrayList<PhotoFolder> getAllImageByData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        ArrayList<PhotoFolder> photoFolders = new ArrayList<>();
        String preDate = "";
        PhotoFolder photoFolder = null;
        if (allPhotoFiles != null && allPhotoFiles.size() > 0) {

            for (int i = 0; i < allPhotoFiles.size(); i++) {
                if (i > 0) {
                    preDate = simpleDateFormat.format(new Date(allPhotoFiles.get(0).getAddDate()));
                }
                String nextDate = simpleDateFormat.format(new Date(allPhotoFiles.get(0).getAddDate()));

                if (preDate.equals(nextDate)) {
                    photoFolder.addPhotoFile(allPhotoFiles.get(i));
                } else {
                    photoFolders.add(photoFolder);
                    photoFolder = new PhotoFolder();
                }
            }
        }

        return photoFolders;
    }
}
