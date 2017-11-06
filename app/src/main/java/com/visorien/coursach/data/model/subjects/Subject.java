
package com.visorien.coursach.data.model.subjects;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subject {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("teacher_id")
    @Expose
    private int teacherId;
    @SerializedName("exam_form")
    @Expose
    private String examForm;
    @SerializedName("teacher_name")
    @Expose
    private String teacherName;
    @SerializedName("messages")
    @Expose
    private List<Message> messages = null;
    @SerializedName("id")
    @Expose
    private int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getExamForm() {
        return examForm;
    }

    public void setExamForm(String examForm) {
        this.examForm = examForm;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
