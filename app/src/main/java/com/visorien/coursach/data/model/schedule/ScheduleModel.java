
package com.visorien.coursach.data.model.schedule;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScheduleModel {

    @SerializedName("error")
    @Expose
    private boolean error;

    public boolean isErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(boolean errorMsg) {
        this.errorMsg = errorMsg;
    }

    @SerializedName("error_msg")
    @Expose
    private boolean errorMsg;
    @SerializedName("schedule")
    @Expose
    private List<Schedule> schedule = null;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

}
