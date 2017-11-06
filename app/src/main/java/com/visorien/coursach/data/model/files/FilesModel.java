
package com.visorien.coursach.data.model.files;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FilesModel {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("files")
    @Expose
    private List<String> files = null;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

}
