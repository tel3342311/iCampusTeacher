package com.liteon.com.icampusteacher.util;

/**
 * Created by trdcmacpro on 2018/1/29.
 */

public class SearchItem {

    private boolean isStudent;
    private String content;
    private String id;

    public boolean isStudent() {
        return isStudent;
    }

    public void setStudent(boolean student) {
        isStudent = student;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
