package com.liule.photoalbum.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liule.photoalbum.R;
import com.liule.photoalbum.entity.ConstantIntentData;
import com.liule.photoalbum.entity.PhotoFolder;
import com.liule.photoalbum.impl.OnItemClickListener;
import com.liule.photoalbum.ui.activity.ThumbnailsListActivity;
import com.liule.photoalbum.ui.adapter.AlbumListAdapter;
import com.liule.photoalbum.ui.widget.divider.DividerItemDecoration;

import java.util.ArrayList;

/**
 * @author 刺雒
 * @time 2017/10/28
 */
public class AlbumListFragment extends Fragment {
    private RecyclerView rvAlbumList;

    private ArrayList<PhotoFolder> mPhotoFolders;
    public AlbumListFragment() {
    }

    public static AlbumListFragment newInstance(ArrayList<PhotoFolder> photoFolders) {
        AlbumListFragment fragment = new AlbumListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(ConstantIntentData.PHOTO_FOLDERS,photoFolders);
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
        Bundle bundle = getArguments();
        mPhotoFolders = bundle.getParcelableArrayList(ConstantIntentData.PHOTO_FOLDERS);
        mPhotoFolders.remove(0);
        return inflater.inflate(R.layout.fragment_album_list, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        rvAlbumList = (RecyclerView) view.findViewById(R.id.rv_album_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rvAlbumList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAlbumList.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));

        final AlbumListAdapter albumListAdapter = new AlbumListAdapter(getContext(),mPhotoFolders);

        albumListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int pos) {
                Intent intent = new Intent(getActivity(), ThumbnailsListActivity.class);
                intent.putParcelableArrayListExtra(ConstantIntentData.PHOTO_FILES,mPhotoFolders.get(pos).getPhotoFiles());
                startActivity(intent);
            }
        });

        rvAlbumList.setAdapter(albumListAdapter);
    }

}
