package com.preciousumeha.globallotto.classes;

public class prf {
   private Integer img;
   private String title;
    public prf(Integer img, String title){
        this.img = img;
        this.title = title;
    }

    public Integer getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImg(Integer img) {
        this.img = img;
    }
}
