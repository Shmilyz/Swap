package com.shmily.tjz.swap.Gson;

/**
 * Created by Shmily_Z on 2017/6/2.
 */

public class Discuss {

    /**
     * date : 2017-06-01
     * love : 50
     * shoesid : 2
     * id : 1
     * content : 这双鞋真的很好看，我挺喜欢的,可不可以便宜点卖给我，我真的很喜欢，如果可以，你可以把我。
     * username : 张三
     */

    private String date;
    private int love;
    private int shoesid;
    private int id;
    private String content;
    private String username;

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

    public int getShoesid() {
        return shoesid;
    }

    public void setShoesid(int shoesid) {
        this.shoesid = shoesid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
