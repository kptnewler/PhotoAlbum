package com.liule.photoalbum.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * 图像文件
 *
 * @author 刺雒
 * @time 2017/10/29
 */

public class PhotoFile implements Comparable<PhotoFile>,Parcelable {

    /*
    * 文件路径
    * */
    private String path;

    /*
    * 文件名
    * */
    private String name;

    /*
    * 标题
    * */
    private String title;


    /*
    * 文件所在文件夹的ID
    * */
    private int folderID;

    /*
    * 文件所在文件夹名称
    * */
    private String folderName;

    /*
    * 文件类型
    * */
    private String mimeType;

    /*
    * 文件修改日期
    * */
    private long modifyDate;

    /*
    * 文件添加时间
    * */
    private long addDate;

    /*
    * 文件大小
    * */
    private long size;

    /*
    * 缩略图路径
    * */
    private String thumpPath;

    /*
    * 长度
    * */
    private int width;

    /*
    * 高度
    *
    * */
    private int height;




    /**
     * 根据添加照片时间排序
     */
    @Override
    public int compareTo(@NonNull PhotoFile o) {
        long time = o.getAddDate() - getAddDate();
        if (time > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        } else if (time < Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        return (int)time;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeInt(this.folderID);
        dest.writeString(this.folderName);
        dest.writeString(this.mimeType);
        dest.writeLong(this.modifyDate);
        dest.writeLong(this.addDate);
        dest.writeLong(this.size);
        dest.writeString(this.thumpPath);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
    }

    public PhotoFile() {
    }

    protected PhotoFile(Parcel in) {
        this.path = in.readString();
        this.name = in.readString();
        this.title = in.readString();
        this.folderID = in.readInt();
        this.folderName = in.readString();
        this.mimeType = in.readString();
        this.modifyDate = in.readLong();
        this.addDate = in.readLong();
        this.size = in.readLong();
        this.thumpPath = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
    }

    public static final Parcelable.Creator<PhotoFile> CREATOR = new Parcelable.Creator<PhotoFile>() {
        @Override
        public PhotoFile createFromParcel(Parcel source) {
            return new PhotoFile(source);
        }

        @Override
        public PhotoFile[] newArray(int size) {
            return new PhotoFile[size];
        }
    };

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getFolderID() {
        return folderID;
    }

    public void setFolderID(int folderID) {
        this.folderID = folderID;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(long modifyDate) {
        this.modifyDate = modifyDate;
    }

    public long getAddDate() {
        return addDate;
    }

    public void setAddDate(long addDate) {
        this.addDate = addDate;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getThumpPath() {
        return thumpPath;
    }

    public void setThumpPath(String thumpPath) {
        this.thumpPath = thumpPath;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }


    @Override
    public String toString() {
        return "PhotoFile{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", title='" + title + '\'' +
                ", folderID=" + folderID +
                ", folderName='" + folderName + '\'' +
                ", mimeType='" + mimeType + '\'' +
                ", modifyDate=" + modifyDate +
                ", addDate=" + addDate +
                ", size=" + size +
                ", thumpPath='" + thumpPath + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
