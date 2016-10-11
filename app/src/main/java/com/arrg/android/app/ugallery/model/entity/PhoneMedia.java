package com.arrg.android.app.ugallery.model.entity;

import java.io.Serializable;

public class PhoneMedia implements Serializable {

    private Integer id;
    private String albumName;
    private String mediaPath;
    private String dateAdded;
    private Integer mediaType;
    private String mimeType;
    private String title;
    private Boolean isChecked;

    public PhoneMedia(Integer id, String albumName, String mediaPath, String dateAdded, Integer mediaType, String mimeType, String title) {
        this.id = id;
        this.albumName = albumName;
        this.mediaPath = mediaPath;
        this.dateAdded = dateAdded;
        this.mediaType = mediaType;
        this.mimeType = mimeType;
        this.title = title;
        this.isChecked = false;
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

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public Integer getMediaType() {
        return mediaType;
    }

    public void setMediaType(Integer mediaType) {
        this.mediaType = mediaType;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
