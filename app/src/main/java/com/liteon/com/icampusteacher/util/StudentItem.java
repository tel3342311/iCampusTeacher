package com.liteon.com.icampusteacher.util;

import com.google.gson.annotations.SerializedName;

public class StudentItem {

    @SerializedName("student_id")
    private int student_id;
    @SerializedName("name")
    private String name;
    @SerializedName("nickname")
    private String nickname;
    @SerializedName("class")
    private String _class;
    @SerializedName("roll_no")
    private int roll_no;
    @SerializedName("height")
    private String height;
    @SerializedName("weight")
    private String weight;
    @SerializedName("dob")
    private String dob;
    @SerializedName("gender")
    private String gender;
    @SerializedName("uuid")
    private String uuid;

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public int getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(int roll_no) {
        this.roll_no = roll_no;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
