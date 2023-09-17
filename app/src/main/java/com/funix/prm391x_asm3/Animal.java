package com.funix.prm391x_asm3;

import android.graphics.Bitmap;

public class Animal {
    private Bitmap photo;
    private Bitmap photoBg;
    private String path;
    private String name;
    private String content;
    private boolean isFav;


    public Animal(String path, Bitmap photo, Bitmap photoBg, String name, String content, boolean isFav) {
        this.path = path;
        this.photo = photo;
        this.photoBg = photoBg;
        this.name = name;
        this.isFav = isFav;
        this.content = content;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }

    public String getPath() {
        return path;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public String getContent() {
        return content;
    }

    public String getName() {
        return name;
    }

    public Bitmap getPhotoBg() {
        return photoBg;
    }
}
