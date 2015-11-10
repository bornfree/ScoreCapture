package com.cameraomr.android.com.cameraomr.db;

import java.util.List;

/**
 * Created by harsha on 10/11/15.
 */
public class Template {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getNum_answers() {
        return num_answers;
    }

    public void setNum_answers(int num_answers) {
        this.num_answers = num_answers;
    }

    public int getNum_options() {
        return num_options;
    }

    public void setNum_options(int num_options) {
        this.num_options = num_options;
    }

    private long id;
    private int height;
    private int width;
    private int num_answers;
    private int num_options;
    private List<Section> sections;

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }
}
