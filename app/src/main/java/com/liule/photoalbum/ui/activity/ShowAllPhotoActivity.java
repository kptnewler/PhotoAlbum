package com.liule.photoalbum.ui.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.liule.photoalbum.R;
import com.liule.photoalbum.entity.ConstantIntentData;
import com.liule.photoalbum.entity.PhotoFile;
import com.liule.photoalbum.ui.fragment.ShowPhotoFragment;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 显示图片详情
 *
 * @author 刺雒
 * @time 2017/10/30
 */
public class ShowAllPhotoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_photo);
        ArrayList<PhotoFile> photoFiles = getIntent().getParcelableArrayListExtra(ConstantIntentData.PHOTO_FILES);
        int pos = getIntent().getIntExtra(ConstantIntentData.POS,-1);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ShowPhotoFragment showPhotoFragment = ShowPhotoFragment.newInstance(photoFiles,pos);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_show_all,showPhotoFragment).commit();
    }
}
