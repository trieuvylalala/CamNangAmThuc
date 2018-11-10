package com.duan2.camnangamthuc.camnangamthuc.Model;

import android.graphics.Bitmap;

/**
 * Created by PC on 10/12/2018.
 */

public class MenuHome {
    public String tenmenu;
    public Bitmap image;

    public MenuHome() {
    }

    public MenuHome(String tenmenu, Bitmap image) {
        this.tenmenu = tenmenu;
        this.image = image;
    }

    public String getTenmenu() {
        return tenmenu;
    }

    public void setTenmenu(String tenmenu) {
        this.tenmenu = tenmenu;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
