package com.shmily.tjz.swap.LitePal;

import org.litepal.crud.DataSupport;

/**
 * Created by Shmily_Z on 2017/6/9.
 */

public class FriendsLite extends DataSupport {
    private int id;
    private String name;
    private String phone;
    private String username;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
