package com.visorien.coursach.ui.files;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.visorien.coursach.R;
import com.visorien.coursach.data.DataManager;
import com.visorien.coursach.data.local.PreferencesHelper;
import com.visorien.coursach.ui.utils.FileUtils;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import okhttp3.MultipartBody;

import static android.app.Activity.RESULT_OK;

public class FilesFragment extends Fragment implements FilesMvpView, ProgressRequestBody.UploadCallbacks {

    private int subjectId;
    ListView filesList;
    FloatingActionButton fabAddFile;
    RelativeLayout mainLayout;
    private View rootLayout;
    private static final int FILE_SELECT_CODE = 1254;
    Toolbar toolbar;
    Button calendarButton;
    FilesPresenter presenter;
    TextView uploadingField;
    ProgressDialog progressDialog;
    private long downloadedBytes;
    private long fileSize;

    @SuppressLint("ValidFragment")
    public FilesFragment(int id) {
        super();
        this.subjectId = id;
    }

    public FilesFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter = new FilesPresenter(new DataManager(new PreferencesHelper(getActivity().getApplicationContext())));
        presenter.attachView(this);

        uploadingField = new TextView(getContext());
        uploadingField.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        uploadingField.setGravity(Gravity.CENTER);
        uploadingField.setVisibility(View.INVISIBLE);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Загрузка файла");

        mainLayout = (RelativeLayout) inflater.inflate(R.layout.loading_layout, container, false);
        mainLayout.addView(uploadingField);
        rootLayout = inflater.inflate(R.layout.fragment_files, container, false);
        filesList = (ListView) rootLayout.findViewById(R.id.files_list);
        if(presenter.getRole().equals("teacher")) {
            registerForContextMenu(filesList);
        }
        fabAddFile = (FloatingActionButton) rootLayout.findViewById(R.id.fab_add_file);
        fabAddFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent()
                        .setType("*/*")
                        .setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select a file"), FILE_SELECT_CODE);
            }
        });
        if(presenter.getRole().equals("student")) {
            fabAddFile.setVisibility(View.GONE);
        }

        filesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.openFile(getSubjectId(), position);
            }
        });
        filesList.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });
        //load list of files
        presenter.getFiles(getSubjectId());
        return mainLayout;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==FILE_SELECT_CODE && resultCode == RESULT_OK) {
            Uri selectedfile = data.getData(); //The uri with the location of the file
            Log.d("file select", "success");
            //show loading animation
            ((ViewGroup) rootLayout.getParent()).removeView(rootLayout);
            mainLayout.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            uploadFile(selectedfile);
        } else {
            Log.d("file select", "error");
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.files_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.rename_file:
                //show loading animation
                ((ViewGroup) rootLayout.getParent()).removeView(rootLayout);
                mainLayout.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                renameFile(info.position);
                return true;
            case R.id.delete_file:
                //show loading animation
                ((ViewGroup) rootLayout.getParent()).removeView(rootLayout);
                mainLayout.findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                presenter.deleteFile(getSubjectId(), info.position);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void renameFile(final int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Переименовать");
        final EditText input = new EditText(getContext());
        alert.setView(input);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String newName = input.getEditableText().toString();
                presenter.renameFile(getSubjectId(), position, newName);
            }
        });
        alert.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        updateView();
                    }
                });
        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    private void uploadFile(Uri fileUri) {
        //make file from uri
        File file = null;
        try {
            file = new File(FileUtils.getFilePath(getContext(), fileUri));
            Log.d("file path", file.getAbsolutePath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if(!FileUtils.checkFileExtension(file.getAbsolutePath())) {
            try {
                Toast.makeText(getContext(), "Недопустимый тип файла. Разрешается загружать .pdf, .doc, .docx, .xls, .txt, .rar, .zip", Toast.LENGTH_LONG).show();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            updateView();
            return;
        }

        ProgressRequestBody requestFile = new ProgressRequestBody(file, this);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file_upload", file.getName(), requestFile);

        presenter.uploadFile(getSubjectId(), body);
        hideLoading();
        beginUpload(file.getName(), file.length());
        Log.d("file size", ""+file.length());
    }

    private void beginUpload(String filename, long filesize) {
        this.fileSize = filesize;
        progressDialog.setMessage(filename);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setProgressNumberFormat((FileUtils.bytesToString(downloadedBytes)) + "/" + (FileUtils.bytesToString(filesize)));
        progressDialog.show();
    }

    public int getSubjectId() {
        return subjectId;
    }

    @Override
    public void showEmpty() {
        filesList.setAdapter(null);
        mainLayout.addView(rootLayout);
        hideLoading();
        try {
            Toast.makeText(getContext(), "Файлы не найдены", Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showFiles(List<String> files) {
        hideLoading();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1, files);
        filesList.setAdapter(adapter);
        mainLayout.addView(rootLayout);
        progressDialog.hide();
    }

    @Override
    public void openFile(String url) {
        //open link in default app
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    @Override
    public void updateView() {
        showLoading();
        presenter.getFiles(getSubjectId());
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
        progressDialog.hide();
    }

    @Override
    public void showError() {
        hideLoading();
        uploadingField.setVisibility(View.INVISIBLE);
        try {
            Toast.makeText(getContext(), "Ошибка, попробуйте еще раз", Toast.LENGTH_LONG).show();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgressUpdate(int percentage) {
        this.downloadedBytes = this.fileSize * percentage / 100;
        progressDialog.setProgressNumberFormat((FileUtils.bytesToString(downloadedBytes)) + "/" + (FileUtils.bytesToString(this.fileSize)));
        progressDialog.setProgress(percentage);
        Log.d("percentage", ""+percentage);
    }

    @Override
    public void onError() {
        progressDialog.hide();
        Log.d("error", "h");
    }

    @Override
    public void onFinish() {
        progressDialog.hide();
        Log.d("finish", "h");
    }
}
