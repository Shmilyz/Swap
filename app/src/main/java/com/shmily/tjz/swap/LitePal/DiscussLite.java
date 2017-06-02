package com.shmily.tjz.swap.LitePal;

import org.litepal.crud.DataSupport;

/**
 * Created by Shmily_Z on 2017/6/2.
 */

public class DiscussLite extends DataSupport {
    private int id;
    private int discussid;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiscussid() {
        return discussid;
    }

    public void setDiscussid(int discussid) {
        this.discussid = discussid;
    }

}
