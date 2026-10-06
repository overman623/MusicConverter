package com.happyhouse.music.converter.soundtouch;

public class BgmListItem {

    private boolean isStorage;
    private String title;
    private int resource;

    public BgmListItem(boolean isStorage, String title, int resource) {
        this.isStorage = isStorage;
        this.title = title;
        this.resource = resource;
    }

    public boolean isStorage() {
        return isStorage;
    }

    public void setStorage(boolean storage) {
        isStorage = storage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

}
