package com.arrg.android.app.ugallery.model.entity;

import java.io.Serializable;
import java.util.ArrayList;

public class PhoneAlbum implements Serializable {

    private Integer id;
    private String albumName;
    private String coverMedia;
    private ArrayList<PhoneMedia> phoneMedias;
    private Boolean isSelected;

    public PhoneAlbum(Integer id, String albumName, String coverMedia) {
        this.id = id;
        this.albumName = albumName;
        this.coverMedia = coverMedia;
        this.phoneMedias = new ArrayList<>();
        this.isSelected = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getCoverMedia() {
        return coverMedia;
    }

    public void setCoverMedia(String coverMedia) {
        this.coverMedia = coverMedia;
    }

    public ArrayList<PhoneMedia> getPhoneMedias() {
        return phoneMedias;
    }

    public void setPhoneMedias(ArrayList<PhoneMedia> phoneMedias) {
        this.phoneMedias = phoneMedias;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
