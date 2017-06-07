package com.shmily.tjz.swap.Gson;

/**
 * Created by Shmily_Z on 2017/6/7.
 */

public class Friends {


    /**
     * shoesid : 4
     * shoesurl : http://www.shmilyz.com/picture/4.jpg
     * id : 2
     * type : FRIENDS_DISCUSS
     * userdate : 2017-06-08 11:01:25
     * shoesname : Jordan男子训练运动鞋88
     * username : 我爱你哦
     */

    private int shoesid;
    private String shoesurl;
    private int id;
    private int type;
    private String userdate;
    private String shoesname;
    private String username;

    public int getShoesid() {
        return shoesid;
    }

    public void setShoesid(int shoesid) {
        this.shoesid = shoesid;
    }

    public String getShoesurl() {
        return shoesurl;
    }

    public void setShoesurl(String shoesurl) {
        this.shoesurl = shoesurl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUserdate() {
        return userdate;
    }

    public void setUserdate(String userdate) {
        this.userdate = userdate;
    }

    public String getShoesname() {
        return shoesname;
    }

    public void setShoesname(String shoesname) {
        this.shoesname = shoesname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
