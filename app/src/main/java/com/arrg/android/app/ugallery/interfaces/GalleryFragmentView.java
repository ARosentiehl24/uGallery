package com.arrg.android.app.ugallery.interfaces;

import android.view.View;
import android.view.ViewGroup;

import com.arrg.android.app.ugallery.model.entity.PhoneAlbum;

import java.util.ArrayList;

public interface GalleryFragmentView {

    void initializeViews(ViewGroup container);

    void configViews();

    void showMessage(Integer message);

    void showMessage(String message);

    void showLoading();

    void showEmpty(View.OnClickListener onClickListener);

    void showContent();

    void showFavoriteMedia();

    ArrayList<PhoneAlbum> getAlbums();

    void toggleSelection();

    void selectAll();

    void unSelectAll();
}
