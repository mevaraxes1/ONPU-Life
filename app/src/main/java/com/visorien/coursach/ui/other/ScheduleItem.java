package com.visorien.coursach.ui.other;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.visorien.coursach.R;

/**
 * Created by Visorien on 27.03.2017.
 */

public class ScheduleItem extends RelativeLayout {
    LayoutInflater mInflater;
    private int id;

    public ScheduleItem(Context context, int time) {
        super(context);
        mInflater = LayoutInflater.from(context);
        initEmpty(time);
    }

    public ScheduleItem(Context context, int id, String subject, String teacher, String room, String type, int time) {
        super(context);
        mInflater = LayoutInflater.from(context);
        init(subject, teacher, room, type, time);
        this.id = id;
    }

    public ScheduleItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScheduleItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void init(String subject, String teacher, String room, String type, int time) {
        View v = mInflater.inflate(R.layout.schedule_item, this, true);
        TextView subject_field = (TextView) v.findViewById(R.id.subj_name);
        subject_field.setText(subject);

        TextView teacher_field = (TextView) v.findViewById(R.id.teacher);
        teacher_field.setText(teacher);

        TextView room_field = (TextView) v.findViewById(R.id.room);
        room_field.setText(room);

        TextView time_field = (TextView) v.findViewById(R.id.time);
        if(time == 1) {
            time_field.setText("08:00 - 09:35");
        } else if(time == 2) {
            time_field.setText("09:50 - 11:25");
        } else if(time == 3) {
            time_field.setText("11:40 - 13:15");
        }

        TextView type_field = (TextView) v.findViewById(R.id.subj_type);
        type_field.setText(type);
        if(type.equals("Лекция")) {
            type_field.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.blue, null));
        } else if(type.equals("Практика")) {
            type_field.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green, null));
        } else if(type.equals("Лабораторная")) {
            type_field.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.orange, null));
        }
    }
    private void initEmpty(int time) {
        View v = mInflater.inflate(R.layout.schedule_item_empty, this, true);
        TextView time_field = (TextView) v.findViewById(R.id.time);
        if(time == 1) {
            time_field.setText("08:00 - 09:35");
        } else if(time == 2) {
            time_field.setText("09:50 - 11:25");
        } else if(time == 3) {
            time_field.setText("11:40 - 13:15");
        }
    }

    @Override
    public int getId() {
        return id;
    }
}
