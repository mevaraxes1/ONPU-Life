
package com.visorien.coursach.data.model.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SettingsModel {

    @SerializedName("error")
    @Expose
    private boolean error;
    @SerializedName("login")
    @Expose
    private String login;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}
