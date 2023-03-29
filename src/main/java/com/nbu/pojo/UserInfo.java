package com.nbu.pojo;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * +-------------+---------------+------+-----+---------+----------------+
 * | Field       | Type          | Null | Key | Default | Extra          |
 * +-------------+---------------+------+-----+---------+----------------+
 * | id          | int           | NO   | PRI | NULL    | auto_increment |
 * | username    | varchar(200)  | NO   | UNI | NULL    |                |
 * | password    | varchar(200)  | NO   |     | NULL    |                |
 * | create_date | datetime      | NO   |     | NULL    |                |
 * | update_date | datetime      | NO   |     | NULL    |                |
 * | level       | enum('0','1') | NO   |     | NULL    |                |
 * +-------------+---------------+------+-----+---------+----------------+
 */
@JsonPropertyOrder({"id","username","password","create_date","update_date","level"})
public class UserInfo {
    private Integer id;
    private String username;
    private String password;

    @JsonProperty("create_date")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @JsonProperty("update_date")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    private Integer level;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", createDate=" + createDate +
                ", updateDate=" + updateDate +
                ", level=" + level +
                '}';
    }
}
