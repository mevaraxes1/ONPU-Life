
package com.visorien.coursach.data.model.schedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Schedule {

    @SerializedName("room")
    @Expose
    private String room;
    @SerializedName("subject_id")
    @Expose
    private int subjectId;
    @SerializedName("subject_time")
    @Expose
    private int subjectTime;
    @SerializedName("subject_type")
    @Expose
    private String subjectType;
    @SerializedName("subject_name")
    @Expose
    private String subjectName;
    @SerializedName("subject_teacher")
    @Expose
    private String subjectTeacher;
    @SerializedName("std_group")
    @Expose
    private String stdGroup;

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getSubjectTime() {
        return subjectTime;
    }

    public void setSubjectTime(int subjectTime) {
        this.subjectTime = subjectTime;
    }

    public String getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(String subjectType) {
        this.subjectType = subjectType;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectTeacher() {
        return subjectTeacher;
    }

    public void setSubjectTeacher(String subjectTeacher) {
        this.subjectTeacher = subjectTeacher;
    }

    public String getStdGroup() {
        return stdGroup;
    }

    public void setStdGroup(String stdGroup) {
        this.stdGroup = stdGroup;
    }

}
