package com.visorien.coursach.data.model.settings;

/**
 * Created by Visorien on 31.05.2017.
 */

public class UpdateUserDataRequest {
    final String target;
    final String new_value;

    public UpdateUserDataRequest(String target, String new_value) {
        this.target = target;
        this.new_value = new_value;
    }
}
