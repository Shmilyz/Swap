package com.shmily.tjz.swap.LitePal;

import org.litepal.crud.DataSupport;

/**
 * Created by Shmily_Z on 2017/5/7.
 */

public class Hot extends DataSupport {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
