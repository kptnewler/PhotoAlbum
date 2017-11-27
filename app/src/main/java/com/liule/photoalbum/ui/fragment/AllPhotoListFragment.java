package com.liule.photoalbum.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liule.photoalbum.R;
import com.liule.photoalbum.entity.ConstantIntentData;
import com.liule.photoalbum.entity.PhotoFile;
import com.liule.photoalbum.impl.OnItemClickListener;
import com.liule.photoalbum.ui.activity.ShowAllPhotoActivity;
import com.liule.photoalbum.ui.adapter.ThumbnailsListAdapter;
import com.liule.photoalbum.ui.widget.divider.GridItemDivider;
import com.liule.photoalbum.utils.DisplayUtils;

import java.util.ArrayList;

public class AllPhotoListFragment extends Fragment {
    private ArrayList<PhotoFile> mPhotoFiles;
    private RecyclerView rvAllPhotoList;

    private final int columnCount = 5;
    private ThumbnailsListAdapter mAdapter;

    public AllPhotoListFragment() {
    }


    public static AllPhotoListFragment newInstance(ArrayList<PhotoFile> photoFiles) {
        AllPhotoListFragment fragment = new AllPhotoListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ConstantIntentData.PHOTO_FILES,photoFiles);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_photo_list, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        rvAllPhotoList = (RecyclerView) view.findViewById(R.id.rv_photo_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPhotoFiles = getArguments().getParcelableArrayList(ConstantIntentData.PHOTO_FILES);
        initView();
    }

    private void initView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),columnCount);
        GridItemDivider itemDivider = new GridItemDivider(Color.WHITE, DisplayUtils.dip2px(getContext(),5),DisplayUtils.dip2px(getContext(),5));

        int itemSize = (DisplayUtils.getScreenWidth(getActivity()) - itemDivider.getDividerWidth() * (columnCount + 1)) / columnCount;

        mAdapter = new ThumbnailsListAdapter(getContext(),mPhotoFiles,itemSize);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Intent intent = new Intent(getActivity(), ShowAllPhotoActivity.class);
                intent.putParcelableArrayListExtra(ConstantIntentData.PHOTO_FILES,mPhotoFiles);
                intent.putExtra(ConstantIntentData.POS,pos);
                startActivity(intent);
            }
        });

        rvAllPhotoList.setLayoutManager(gridLayoutManager);
        rvAllPhotoList.addItemDecoration(itemDivider);
        rvAllPhotoList.setAdapter(mAdapter);
    }
}
