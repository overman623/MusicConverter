package com.happyhouse.music.converter.help;

public class HowToItem {

    private int title;
    private int image;
    private int content;


    public HowToItem(int title, int image, int content) {
        this.title = title;
        this.image = image;
        this.content = content;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getContent() {
        return content;
    }

    public void setContent(int content) {
        this.content = content;
    }
}
