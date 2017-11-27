package com.liule.photoalbum.ui.activity;


import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.liule.photoalbum.R;
import com.liule.photoalbum.entity.ConstantIntentData;
import com.liule.photoalbum.entity.PhotoFile;
import com.liule.photoalbum.ui.fragment.ThumbnailsListFragment;

import java.util.ArrayList;

/**
 * 缩略图展示
 *
 * @author 刺雒
 * @time 2017/10/29 
 */
public class ThumbnailsListActivity extends AppCompatActivity {
    FragmentTransaction transaction;

    private ArrayList<PhotoFile> mPhotoFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumbnails_list);

        mPhotoFiles = getIntent().getParcelableArrayListExtra(ConstantIntentData.PHOTO_FILES);

        ThumbnailsListFragment thumbnailsListFragment = ThumbnailsListFragment.newInstance(mPhotoFiles);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_thumbnails,thumbnailsListFragment);
        transaction.commit();

    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}
