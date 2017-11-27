package com.liule.photoalbum.ui.activity;

import android.Manifest;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.liule.photoalbum.R;
import com.liule.photoalbum.asynctask.ImageAsyncTask;
import com.liule.photoalbum.entity.PhotoFolder;
import com.liule.photoalbum.ui.adapter.AlbumPagerAdapter;
import com.liule.photoalbum.ui.fragment.AlbumListFragment;
import com.liule.photoalbum.ui.fragment.AllPhotoListFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于照片和相册显示
 *
 * @date 2017/10/28
 * @author 刺雒
 */
public class PhotoAlbumActivity extends MPermissionsActivity {
    private ViewPager vpPhotoAndAlbum;
    private TabLayout tlPhotoAndAlbum;

    private AlbumPagerAdapter mPhotoAlbumAdapter;
    private List<Fragment> mFragments;

    private ArrayList<PhotoFolder> mPhotoFolders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);

        // 申请权限
        requestPermission(new String[] {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        },0x33);

        vpPhotoAndAlbum = (ViewPager) findViewById(R.id.vp_photo_album);
        tlPhotoAndAlbum = (TabLayout) findViewById(R.id.tl_photo_album);


    }

    @Override
    public void permissionSuccess(int requestCode) {
        Log.e("dsfs00","fsgs00");
        initData();
    }

    @Override
    public void permissionFail(int requestCode) {
        Toast.makeText(this,getString(R.string.prompt_message),Toast.LENGTH_SHORT).show();
    }

    /**
     * 初始控件
     *
     */
    private void initView() {
        mFragments = new ArrayList<>();
        mFragments.add(AllPhotoListFragment.newInstance(mPhotoFolders.get(0).getPhotoFiles()));
        mFragments.add(AlbumListFragment.newInstance(mPhotoFolders));

        mPhotoAlbumAdapter = new AlbumPagerAdapter(getSupportFragmentManager(),mFragments);
        vpPhotoAndAlbum.setAdapter(mPhotoAlbumAdapter);
        tlPhotoAndAlbum.setupWithViewPager(vpPhotoAndAlbum);


    }

    /**
     * 初始化数据
     *
     */
    private void initData() {
        ImageAsyncTask imageAsyncTask = new ImageAsyncTask(this,mCallback);
        imageAsyncTask.execute();

    }

    private ImageAsyncTask.AllAlbumCallback mCallback = new ImageAsyncTask.AllAlbumCallback() {
        @Override
        public void onScanAllAlbum(ArrayList<PhotoFolder> photoFolders) {
            mPhotoFolders = photoFolders;
            initView();
        }
    };


}
