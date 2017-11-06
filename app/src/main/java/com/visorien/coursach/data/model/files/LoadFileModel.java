package com.visorien.coursach.data.model.files;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Visorien on 31.05.2017.
 */

public class LoadFileModel {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("path")
    @Expose
    private String path = null;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
