package com.liule.photoalbum.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * 文件实用类
 *
 * @author 刺雒
 * @time 2017/10/28
 */

public class FileUtils {

    /**
     * 判断SD卡是否可用
     */
    public static boolean sdCardIsAvailable() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().canWrite();
        } else {
            return false;
        }
    }

    /**
     * 获取根目录
     */
    public static File getRootPath(Context context) {
        if (sdCardIsAvailable()) {
            return Environment.getExternalStorageDirectory();
        } else {
            return context.getFilesDir();
        }
    }



}
