
package com.visorien.coursach.data.model.subjects;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SubjectsModel {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("subjects")
    @Expose
    private List<Subject> subjects = null;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subjects) {
        this.subjects = subjects;
    }

}
