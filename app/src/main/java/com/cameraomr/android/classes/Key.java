package com.cameraomr.android.classes;

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

    public long getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(long template_id) {
        this.template_id = template_id;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public String getTemplate_title() {
        return template_title;
    }

    public void setTemplate_title(String template_title) {
        this.template_title = template_title;
    }

    private long id;
    private long template_id;
    private String title;
    private String template_title;
    private String date;
    private String answers;
    private Template template;
}
