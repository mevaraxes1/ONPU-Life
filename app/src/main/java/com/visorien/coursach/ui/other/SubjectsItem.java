package com.visorien.coursach.ui.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.visorien.coursach.R;

/**
 * Created by Visorien on 27.03.2017.
 */

public class SubjectsItem extends RelativeLayout {
    LayoutInflater mInflater;
    private int id;

    public SubjectsItem(Context context, int id, String title, String teacher_name, String exam_form) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init(title, teacher_name, exam_form);
        this.id = id;
    }

    public SubjectsItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SubjectsItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void init(String title, String teacher_name, String exam_form) {
        View v = mInflater.inflate(R.layout.subjects_item, this, true);
        TextView title_field = (TextView) v.findViewById(R.id.title_field);
        title_field.setText(title);

        TextView teacher_field = (TextView) v.findViewById(R.id.teacher_field);
        teacher_field.setText(teacher_name);

        TextView exam_form_field = (TextView) v.findViewById(R.id.exam_form_field);
        exam_form_field.setText(exam_form);
    }

    @Override
    public int getId() {
        return id;
    }
}
