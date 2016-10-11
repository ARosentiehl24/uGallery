package com.arrg.android.app.ugallery.presenter;

import com.arrg.android.app.ugallery.interfaces.SearchPresenter;
import com.arrg.android.app.ugallery.interfaces.SearchView;
import com.arrg.android.app.ugallery.model.entity.PhoneAlbum;
import com.arrg.android.app.ugallery.model.entity.PhoneMedia;
import com.arrg.android.app.ugallery.view.adapter.MediaSearchAdapter;
import com.thefinestartist.Base;

import java.util.ArrayList;

public class ISearchPresenter implements SearchPresenter {

    private SearchView searchView;

    public ISearchPresenter(SearchView searchView) {
        this.searchView = searchView;
    }

    @Override
    public void onCreate() {
        searchView.configViews();
    }

    @Override
    public MediaSearchAdapter makeQuery(CharSequence s, ArrayList<PhoneAlbum> phoneAlbumArrayList) {
        ArrayList<PhoneMedia> mediaArrayList = new ArrayList<>();

        for (PhoneAlbum album : phoneAlbumArrayList) {
            for (PhoneMedia media : album.getPhoneMedias()) {
                if (media.getTitle().toLowerCase().contains(s.toString().toLowerCase())) {
                    mediaArrayList.add(media);
                }
            }
        }

        return new MediaSearchAdapter(Base.getContext(), mediaArrayList);
    }
}
