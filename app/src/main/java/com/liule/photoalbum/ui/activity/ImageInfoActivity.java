package com.liule.photoalbum.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.liule.photoalbum.R;
import com.liule.photoalbum.entity.ConstantIntentData;
import com.liule.photoalbum.entity.PhotoFile;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 刺雒
 * @time 2017/10/29
 */
public class ImageInfoActivity extends AppCompatActivity {
    private TextView tvImageName, tvImageSize, tvImagePath, tvImageTime, tvImageResolution;
    private Toolbar mToolbar;

    private PhotoFile mPhotoFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_info);
        mPhotoFile = getIntent().getParcelableExtra(ConstantIntentData.PHOTO_FILE);

        initView();
    }

    private void  initView() {
        tvImageName = (TextView) findViewById(R.id.tv_image_name);
        tvImageSize = (TextView) findViewById(R.id.tv_image_size);
        tvImagePath = (TextView) findViewById(R.id.tv_image_path);
        tvImageTime = (TextView) findViewById(R.id.tv_image_time);
        tvImageResolution = (TextView) findViewById(R.id.tv_image_resolution);

        tvImageName.setText(mPhotoFile.getFolderName());
        float size = mPhotoFile.getSize() / 1024 / 1024;
        tvImageSize.setText(String.format(getString(R.string.image_size),size));
        tvImagePath.setText(mPhotoFile.getPath());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        String date = simpleDateFormat.format(new Date(mPhotoFile.getAddDate()));
        tvImageTime.setText(date);

        tvImageResolution.setText(String.format(getString(R.string.image_resolution),mPhotoFile.getWidth(),mPhotoFile.getHeight()));
    }
}
