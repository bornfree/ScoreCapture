package com.cameraomr.android.com.cameraomr.db;

/**
 * Created by harsha on 10/11/15.
 */
public class Section {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getNum_answers() {
        return num_answers;
    }

    public void setNum_answers(int num_answers) {
        this.num_answers = num_answers;
    }



    public long getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(long template_id) {
        this.template_id = template_id;
    }

    private long id;
    private long template_id;
    private int width;
    private int height;
    private int top;
    private int left;
    private int num_answers;
}
