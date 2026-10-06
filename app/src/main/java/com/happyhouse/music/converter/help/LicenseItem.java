package com.happyhouse.music.converter.help;

public class LicenseItem {

    private int title;
    private int copyRight;
    private int link;
    private int content;

    public LicenseItem(int title, int copyRight, int link, int content) {
        this.title = title;
        this.copyRight = copyRight;
        this.link = link;
        this.content = content;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getCopyRight() {
        return copyRight;
    }

    public void setCopyRight(int copyRight) {
        this.copyRight = copyRight;
    }

    public int getLink() {
        return link;
    }

    public void setLink(int link) {
        this.link = link;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }
}
