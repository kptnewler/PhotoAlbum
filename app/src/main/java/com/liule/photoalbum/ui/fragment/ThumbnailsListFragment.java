package com.liule.photoalbum.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.liule.photoalbum.R;
import com.liule.photoalbum.entity.ConstantIntentData;
import com.liule.photoalbum.entity.PhotoFile;
import com.liule.photoalbum.impl.OnItemClickListener;
import com.liule.photoalbum.ui.activity.ShowAllPhotoActivity;
import com.liule.photoalbum.ui.adapter.ThumbnailsListAdapter;
import com.liule.photoalbum.ui.widget.divider.GridItemDivider;
import com.liule.photoalbum.utils.DisplayUtils;

import java.util.ArrayList;

/**
 *
 * @author 刺雒
 * @time 2017/10/29
 */
public class ThumbnailsListFragment extends Fragment {
    private RecyclerView rvThumbnails;
    private TextView tvToolbarTitle;

    private final int columnCount = 4;
    private ThumbnailsListAdapter mAdapter;
    private ArrayList<PhotoFile> mPhotoFiles;


    public ThumbnailsListFragment() {
        
    }
    public static ThumbnailsListFragment newInstance(ArrayList<PhotoFile> photoFiles) {
        ThumbnailsListFragment fragment = new ThumbnailsListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ConstantIntentData.PHOTO_FILES,photoFiles);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_thumbnails_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        rvThumbnails = (RecyclerView) view.findViewById(R.id.rv_thumbnails_list);
        tvToolbarTitle = (TextView) view.findViewById(R.id.tv_toolbar_title);

        tvToolbarTitle.setText(getString(R.string.album));
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
                Intent intent = new Intent(getContext(),ShowAllPhotoActivity.class);
                intent.putParcelableArrayListExtra(ConstantIntentData.PHOTO_FILES,mPhotoFiles);
                intent.putExtra(ConstantIntentData.POS,pos);
                startActivity(intent);

            }
        });

        rvThumbnails.setLayoutManager(gridLayoutManager);
        rvThumbnails.addItemDecoration(itemDivider);
        rvThumbnails.setAdapter(mAdapter);
    }


}
