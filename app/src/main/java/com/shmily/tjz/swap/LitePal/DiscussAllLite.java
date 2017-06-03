package com.shmily.tjz.swap.LitePal;

import org.litepal.crud.DataSupport;

/**
 * Created by Shmily_Z on 2017/6/4.
 */

public class DiscussAllLite extends DataSupport{
    private int id;

    public int getDiscussid() {
        return discussid;
    }

    public void setDiscussid(int discussid) {
        this.discussid = discussid;
    }

    private int discussid;
    private int shoesid;
    private String username;
    private String date;
    private int love;
    private  String content;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShoesid() {
        return shoesid;
    }

    public void setShoesid(int shoesid) {
        this.shoesid = shoesid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getLove() {
        return love;
    }

    public void setLove(int love) {
        this.love = love;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
