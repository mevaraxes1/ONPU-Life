
package com.visorien.coursach.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.visorien.coursach.data.model.schedule.Schedule;

import java.util.List;

public class SimpleAnswer {

    @SerializedName("error")
    @Expose
    private boolean error;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

}
