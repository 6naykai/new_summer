package com.nbu.pojo;

public class Essay {
    private Integer id;
    private String myProject;
    private String myEssay;
    private String owner;

    private Integer score;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMyProject() {
        return myProject;
    }

    public void setMyProject(String myProject) {
        this.myProject = myProject;
    }

    public String getMyEssay() {
        return myEssay;
    }

    public void setMyEssay(String myEssay) {
        this.myEssay = myEssay;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "Essay{" +
                "id=" + id +
                ", myProject='" + myProject + '\'' +
                ", myEssay='" + myEssay + '\'' +
                ", owner='" + owner + '\'' +
                ", score=" + score +
                '}';
    }
}
