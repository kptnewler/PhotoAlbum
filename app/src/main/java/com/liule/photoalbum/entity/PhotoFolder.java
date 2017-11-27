package com.liule.photoalbum.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * 相册文件
 *
 * @author 刺雒
 * @time 2017/10/29
 */

public class PhotoFolder implements Parcelable {

    private int id;

    /**
     * 文件夹名称
     */
    private String name;

    /**
     * 是否选中
     */
    private boolean isChecked;

    /**
    * 图片列表
    */
    private ArrayList<PhotoFile> mPhotoFiles = new ArrayList<>();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    }

    public PhotoFolder() {
    }

    protected PhotoFolder(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.isChecked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<PhotoFolder> CREATOR = new Parcelable.Creator<PhotoFolder>() {
        @Override
        public PhotoFolder createFromParcel(Parcel source) {
            return new PhotoFolder(source);
        }

        @Override
        public PhotoFolder[] newArray(int size) {
            return new PhotoFolder[size];
        }
    };

    public void addPhotoFile(PhotoFile photoFile) {
        if (!mPhotoFiles.contains(photoFile)) {
            mPhotoFiles.add(photoFile);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public ArrayList<PhotoFile> getPhotoFiles() {
        return mPhotoFiles;
    }

    @Override
    public String toString() {
        return "PhotoFolder{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isChecked=" + isChecked +
                ", mPhotoFiles=" + mPhotoFiles +
                '}';
    }
}
