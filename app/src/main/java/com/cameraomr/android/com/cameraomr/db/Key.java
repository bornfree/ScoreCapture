package com.cameraomr.android.com.cameraomr.db;

/**
 * Created by harsha on 9/11/15.
 */
public class Key {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAnswers() {
        return answers;
    }

    public void setAnswers(String answers) {
        this.answers = answers;
    }

    private long id;
    private String title;
    private String date;
    private String answers;
}
