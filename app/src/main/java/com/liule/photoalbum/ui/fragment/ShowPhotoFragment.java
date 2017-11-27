package com.liule.photoalbum.ui.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.liule.photoalbum.R;
import com.liule.photoalbum.entity.ConstantIntentData;
import com.liule.photoalbum.entity.PhotoFile;
import com.liule.photoalbum.ui.adapter.ShowPhotoAdapter;

import java.util.ArrayList;

/**
 * @author 刺雒
 * @time 2017/10/29
 */
public class ShowPhotoFragment extends Fragment {
    private ViewPager vpShowPhoto;


    private ShowPhotoAdapter mShowPhotoAdapter;
    private int mCurrentPosition ;
    private int pos;
    private ArrayList<PhotoFile> mPhotoFiles;
    public ShowPhotoFragment() {
    }

    public static ShowPhotoFragment newInstance(ArrayList<PhotoFile> photoFiles,int pos) {
        ShowPhotoFragment fragment = new ShowPhotoFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ConstantIntentData.PHOTO_FILES,photoFiles);
        args.putInt(ConstantIntentData.POS,pos);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_show_photo, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        this.mPhotoFiles = getArguments().getParcelableArrayList(ConstantIntentData.PHOTO_FILES) ;
        this.pos = getArguments().getInt(ConstantIntentData.POS);
        vpShowPhoto = (ViewPager) view.findViewById(R.id.vp_show_photo);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
    }


    private void initView() {
        mCurrentPosition = pos;
        mShowPhotoAdapter = new ShowPhotoAdapter(getActivity(), mPhotoFiles);

        ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mCurrentPosition = position;
            }
        };

        vpShowPhoto.setAdapter(mShowPhotoAdapter);
        vpShowPhoto.addOnPageChangeListener(pageChangeListener);
        vpShowPhoto.setCurrentItem(mCurrentPosition);
        pageChangeListener.onPageSelected(mCurrentPosition);

    }


}
