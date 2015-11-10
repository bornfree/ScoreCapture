package com.cameraomr.android.classes;

/**
 * Created by harsha on 10/11/15.
 */
public class SectionNDK {
    private int width;
    private int height;
    private int top;
    private int left;
    private int num_answers;
    private String answers;

    public SectionNDK(Section section, String sectionAnswers)
    {
        width    = section.getWidth();
        height   = section.getHeight();
        top      = section.getTop();
        left     = section.getLeft();
        num_answers = section.getNum_answers();
        answers = sectionAnswers;
    }
}
