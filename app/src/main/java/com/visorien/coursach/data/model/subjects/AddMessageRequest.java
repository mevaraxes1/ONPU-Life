package com.visorien.coursach.data.model.subjects;

/**
 * Created by Visorien on 31.05.2017.
 */

public class AddMessageRequest {
    final String message;
    final int subject_id;

    public AddMessageRequest(String message, int subject_id) {
        this.message = message;
        this.subject_id = subject_id;
    }
}
