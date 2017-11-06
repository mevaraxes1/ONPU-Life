package com.visorien.coursach.ui.other;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.visorien.coursach.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Visorien on 27.03.2017.
 */

public class MessageItem extends RelativeLayout {
    LayoutInflater mInflater;
    private int messageId;
    private String message;

    public MessageItem(Context context, int id, String message, long time) {
        super(context);
        this.messageId = id;
        this.message = message;
        mInflater = LayoutInflater.from(context);
        init(message, time);
    }

    public MessageItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void init(String message, long time) {
        time *= 1000;
        Log.d("time", String.valueOf(time));
        Calendar messageCal = new GregorianCalendar();
        messageCal.setTimeInMillis(time);
        Log.d("calendar time", messageCal.toString());
        Date messageDate = messageCal.getTime();
        String messageDateString = new SimpleDateFormat("HH:mm dd.MM.yyyy").format(messageDate);
        View v = mInflater.inflate(R.layout.message_item, this, true);

        TextView message_field = (TextView) v.findViewById(R.id.message_field);
        message_field.setText(message);

        TextView time_field = (TextView) v.findViewById(R.id.time_field);
        time_field.setText(messageDateString);
    }

    public int getMessageId() {
        return messageId;
    }

    public String getMessage() {
        return message;
    }
}
