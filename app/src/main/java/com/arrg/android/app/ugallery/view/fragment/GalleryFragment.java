package com.arrg.android.app.ugallery.view.fragment;


import android.Manifest;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.afollestad.dragselectrecyclerview.DragSelectRecyclerView;
import com.arrg.android.app.ugallery.R;
import com.arrg.android.app.ugallery.UGallery;
import com.arrg.android.app.ugallery.interfaces.GalleryFragmentView;
import com.arrg.android.app.ugallery.model.entity.PhoneAlbum;
import com.arrg.android.app.ugallery.presenter.IGalleryFragmentPresenter;
import com.arrg.android.app.ugallery.view.adapter.GalleryAdapter;
import com.example.jackmiras.placeholderj.library.PlaceHolderJ;
import com.mukesh.permissions.AppPermissions;
import com.thefinestartist.utils.content.Res;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment implements GalleryAdapter.OnItemClickListener, GalleryFragmentView {

    public interface GalleryFragmentListener {
        void onClickPressed(int itemsSelected);
        void onLongClickPressed(boolean onLongClickPressed);
    }

    public static final int STORAGE_PERMISSION_RC = 100;
    public static final String[] STORAGE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @BindView(R.id.gallery)
    DragSelectRecyclerView gallery;

    private AppPermissions appPermissions;
    private ArrayList<PhoneAlbum> albumArrayList;
    private Boolean isLongClickPressed = false;
    private IGalleryFragmentPresenter iGalleryFragmentPresenter;
    private GalleryAdapter galleryAdapter;
    private GalleryFragmentListener galleryFragmentListener;
    private PlaceHolderJ placeHolderJ;

    public GalleryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        appPermissions = new AppPermissions(getActivity());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            galleryFragmentListener = (GalleryFragmentListener) context;
        } catch (ClassCastException e) {
            Log.e(getClass().getSimpleName(), e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        container = (ViewGroup) inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this, container);

        iGalleryFragmentPresenter = new IGalleryFragmentPresenter(this);
        iGalleryFragmentPresenter.onCreateView(container);

        return container;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        iGalleryFragmentPresenter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void initializeViews(ViewGroup container) {
        placeHolderJ = new PlaceHolderJ(container, R.id.gallery, UGallery.getPlaceHolderManager());
        placeHolderJ.init(R.id.view_loading, R.id.view_empty, R.id.view_error);

        placeHolderJ.viewEmptyTryAgainButton.setBackgroundTintList(ColorStateList.valueOf(Res.getColor(R.color.colorAccent)));
        placeHolderJ.viewEmptyTryAgainButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

        placeHolderJ.viewErrorTryAgainButton.setBackgroundTintList(ColorStateList.valueOf(Res.getColor(R.color.colorAccent)));
        placeHolderJ.viewErrorTryAgainButton.setTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    @Override
    public void configViews() {
        if (appPermissions.hasPermission(STORAGE_PERMISSIONS)) {
            new LoadAlbumTask().execute();
        } else {
            requestPermissions(STORAGE_PERMISSIONS, STORAGE_PERMISSION_RC);
        }
    }

    @Override
    public void showMessage(Integer message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading() {
        placeHolderJ.showLoading();
    }

    @Override
    public void showEmpty(View.OnClickListener onClickListener) {
        placeHolderJ.showEmpty(onClickListener);
    }

    @Override
    public void showContent() {
        placeHolderJ.hideLoading();
    }

    @Override
    public void showFavoriteMedia() {

    }

    @Override
    public ArrayList<PhoneAlbum> getAlbums() {
        return albumArrayList;
    }

    @Override
    public void toggleSelection() {
        if (isLongClickPressed) {
            unSelectAll();
        } else {
            selectAll();
        }
    }

    @Override
    public void selectAll() {
        isLongClickPressed = true;

        for (int i = 0; i < galleryAdapter.getItemCount(); i++) {
            galleryAdapter.setSelected(i, true);
        }

        galleryAdapter.notifyDataSetChanged();

        galleryFragmentListener.onClickPressed(galleryAdapter.getSelectedItems());
        galleryFragmentListener.onLongClickPressed(isLongClickPressed);
    }

    @Override
    public void unSelectAll() {
        isLongClickPressed = false;

        galleryAdapter.clearSelected();

        for (int i = 0; i < galleryAdapter.getItemCount(); i++) {
            galleryAdapter.setChecked(i, false);
        }

        galleryAdapter.notifyDataSetChanged();

        galleryFragmentListener.onClickPressed(galleryAdapter.getSelectedItems());
        galleryFragmentListener.onLongClickPressed(isLongClickPressed);
    }

    @Override
    public void onClick(GalleryAdapter.ViewHolder viewHolder, View itemView, int position) {
        if (isLongClickPressed) {
            CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.cbIsSelected);
            checkBox.setChecked(!checkBox.isChecked());

            galleryAdapter.setChecked(position, checkBox.isChecked());

            galleryFragmentListener.onClickPressed(galleryAdapter.getSelectedItems());
        } else {
            showMessage("Open Album");
        }
    }

    @Override
    public void onLongClick(GalleryAdapter.ViewHolder viewHolder, View itemView, int position) {
        if (isLongClickPressed) {
            unSelectAll();
        } else {
            CheckBox checkBox = (CheckBox) itemView.findViewById(R.id.cbIsSelected);
            checkBox.setChecked(!checkBox.isChecked());

            galleryAdapter.setChecked(position, checkBox.isChecked());

            selectAll();
        }
    }

    class LoadAlbumTask extends AsyncTask<Void, Void, ArrayList<PhoneAlbum>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            showLoading();
        }

        @Override
        protected ArrayList<PhoneAlbum> doInBackground(Void... params) {
            albumArrayList = iGalleryFragmentPresenter.getAlbums(getActivity());
            return albumArrayList;
        }

        @Override
        protected void onPostExecute(ArrayList<PhoneAlbum> phoneAlbumArrayList) {
            super.onPostExecute(phoneAlbumArrayList);

            if (phoneAlbumArrayList != null && phoneAlbumArrayList.size() > 0) {
                showContent();

                galleryAdapter = new GalleryAdapter(phoneAlbumArrayList, getContext());
                galleryAdapter.setOnItemClickListener(GalleryFragment.this);

                gallery.setAdapter(galleryAdapter);
                gallery.setHasFixedSize(true);
                gallery.setLayoutManager(new GridLayoutManager(getContext(), Res.getInteger(R.integer.grid_count_gallery)));
            } else {
                showEmpty(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new LoadAlbumTask().execute();
                    }
                });
            }
        }
    }
}
