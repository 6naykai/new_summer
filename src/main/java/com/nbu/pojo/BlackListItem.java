package com.nbu.pojo;

public class BlackListItem {

    private Integer id;
    private Integer banUserId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBanUserId() {
        return banUserId;
    }

    public void setBanUserId(Integer banUserId) {
        this.banUserId = banUserId;
    }

    @Override
    public String toString() {
        return "BlackListItem{" +
                "id=" + id +
                ", banUserId=" + banUserId +
                '}';
    }
}
