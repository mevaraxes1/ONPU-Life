package com.visorien.coursach.ui.subject;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.visorien.coursach.R;
import com.visorien.coursach.data.DataManager;
import com.visorien.coursach.data.local.PreferencesHelper;
import com.visorien.coursach.data.model.subjects.Message;
import com.visorien.coursach.data.model.subjects.Subject;
import com.visorien.coursach.ui.files.FilesFragment;
import com.visorien.coursach.ui.other.MessageItem;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SubjectFragment extends Fragment implements SubjectMvpView {

    private int id;

    Toolbar toolbar;
    Button calendarButton;
    Button filesButton;

    Button addMessageButton;
    EditText messageField;
    LinearLayout messagesContainer;
    MessageItem lastSelectedMessage;
    RelativeLayout mainLayout;
    View rootLayout;
    SubjectPresenter presenter;

    @SuppressLint("ValidFragment")
    public SubjectFragment(int id) {
        super();
        this.id = id;
    }

    public SubjectFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        calendarButton = (Button) toolbar.findViewById(R.id.calendar_button);
        calendarButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new SubjectPresenter(new DataManager(new PreferencesHelper(getActivity().getApplicationContext())));
        presenter.attachView(this);
        mainLayout = (RelativeLayout) inflater.inflate(R.layout.loading_layout, container, false);
        rootLayout = inflater.inflate(R.layout.fragment_subject, container, false);
        showLoading();
        presenter.getSubject(getSubjectId());
        return mainLayout;
    }

    public int getSubjectId(){
        return this.id;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if(v instanceof MessageItem) {
            lastSelectedMessage = (MessageItem) v;
        }
        MenuInflater inflater = getActivity().getMenuInflater();
        if(presenter.getRole().equals("teacher"))
            inflater.inflate(R.menu.message_context_menu, menu);
        else
            inflater.inflate(R.menu.message_context_menu_student, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()) {
            case R.id.copy_message:
                try {
                    copyMessage(lastSelectedMessage);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return true;
            case R.id.delete_message:
                try {
                    deleteMessage(lastSelectedMessage);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void deleteMessage(MessageItem message) {
        presenter.deleteMessage(message);
    }

    private void copyMessage(MessageItem message) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Текст скопирован", message.getMessage());
        clipboard.setPrimaryClip(clip);
    }

    private void addMessage(String message) {
        presenter.addMessage(getSubjectId(), message);
    }

    @Override
    public void updateView() {
        ((ViewGroup) rootLayout.getParent()).removeView(rootLayout);
        showLoading();
        presenter.getSubject(getSubjectId());
    }

    @Override
    public void showLoading() {
        View loadingBar = mainLayout.findViewById(R.id.progressBar);
        if(loadingBar!=null) loadingBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        View loadingBar = mainLayout.findViewById(R.id.progressBar);
        if(loadingBar!=null) loadingBar.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        hideLoading();
        try {
            Toast.makeText(getContext(), "Ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showSubject(Subject subject) {
        messagesContainer = (LinearLayout) rootLayout.findViewById(R.id.messages_container);
        messagesContainer.removeAllViews();
        addMessageButton = (Button) rootLayout.findViewById(R.id.add_message_button);
        messageField = (EditText) rootLayout.findViewById(R.id.new_message_field);
        filesButton = (Button) rootLayout.findViewById(R.id.files_button);
        addMessageButton.setVisibility(View.INVISIBLE);
        addMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageField.getText().toString();
                if(message != null) {
                    if(!message.equals("")) {
                        messageField.setText("");
                        InputMethodManager imm = (InputMethodManager)
                                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(messageField.getWindowToken(), 0);
                        addMessage(message);
                    }
                }
            }
        });
        messageField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    addMessageButton.setVisibility(View.VISIBLE);
                } else {
                    addMessageButton.setVisibility(View.INVISIBLE);
                }
            }
        });
        filesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilesFragment filesFragment = new FilesFragment(getSubjectId());
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.content_main, filesFragment).addToBackStack(null);
                ft.commit();
            }
        });

        if(presenter.getRole().equals("student")) {
            messageField.setVisibility(View.GONE);
        }

        TextView subjectNameField, teacherNameField, examTypeField;

        subjectNameField = (TextView) rootLayout.findViewById(R.id.subject_name);
        teacherNameField = (TextView) rootLayout.findViewById(R.id.teacher_name);
        examTypeField = (TextView) rootLayout.findViewById(R.id.exam_type);

        subjectNameField.setText(subject.getTitle());
        teacherNameField.setText(subject.getTeacherName());
        examTypeField.setText(subject.getExamForm());
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(subject.getTitle());

        List<Message> messages = subject.getMessages();
        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return Integer.compare(o2.getTime(), o1.getTime());
            }
        });

        for (int i = 0; i < messages.size(); i++) {
            MessageItem messageItem = new MessageItem(getContext(), messages.get(i).getId(),
                    messages.get(i).getMessage(), messages.get(i).getTime());
            registerForContextMenu(messageItem);
            messagesContainer.addView(messageItem);
        }
        hideLoading();
        mainLayout.addView(rootLayout);
    }
}
