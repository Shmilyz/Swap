package com.shmily.tjz.swap.Db;

/**
 * Created by Shmily_Z on 2017/4/2.
 */

public class Fruit {
    private String name;
    private String imageId;
    public Fruit(String name, String imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
