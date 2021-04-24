/*
 *  Copyright (c) 2018, Jaisel Rahman <jaiselrahman@gmail.com>.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.hybridoitc.ahmad;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.hybridoitc.filepicker.activity.DirSelectActivity;
import com.hybridoitc.filepicker.activity.FilePickerActivity;
import com.hybridoitc.filepicker.activity.PickFile;
import com.hybridoitc.filepicker.config.Configurations;
import com.hybridoitc.filepicker.model.MediaFile;
import com.hybridoitc.filepicker.utils.FilePickerProvider;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static int FILE_REQUEST_CODE = 1;
    private FileListAdapter fileListAdapter;
    private ArrayList<MediaFile> mediaFiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = findViewById(R.id.file_list);
        fileListAdapter = new FileListAdapter(mediaFiles);
        recyclerView.setAdapter(fileListAdapter);

        final ActivityResultLauncher<Configurations> pickImage = registerForActivityResult(new PickFile().throughDir(true), new ActivityResultCallback<List<MediaFile>>() {
            @Override
            public void onActivityResult(List<MediaFile> result) {
                if (result != null)
                    setMediaFiles(result);
                else
                    Toast.makeText(MainActivity.this, "Image not selected", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.launch_docPicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DirSelectActivity.class);
                intent.putExtra(DirSelectActivity.CONFIGS, new Configurations.Builder()
                        .setCheckPermission(true)
                        .setShowAudios(false)
                        .setShowImages(false)
                        .setShowVideos(false)
                        .setSuffixes("txt", "pdf",  "rtf", "csv", "xml",
                                "doc", "docx", "odt",
                                "ppt", "pptx", "pps",
                                "xls", "xlsx")
                        .setShowFiles(true)
                        .setSkipZeroSizeFiles(true)
                        .setMaxSelection(10)
                        .build());
                startActivityForResult(intent, FILE_REQUEST_CODE);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILE_REQUEST_CODE
                && resultCode == RESULT_OK
                && data != null) {
            List<MediaFile> mediaFiles = data.<MediaFile>getParcelableArrayListExtra(FilePickerActivity.MEDIA_FILES);
            if(mediaFiles != null) {
                setMediaFiles(mediaFiles);
            } else {
                Toast.makeText(MainActivity.this, "File not selected", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setMediaFiles(List<MediaFile> mediaFiles) {
        this.mediaFiles.clear();
        this.mediaFiles.addAll(mediaFiles);
        fileListAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.share_log) {
            shareLogFile();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void shareLogFile() {
        File logFile = new File(getExternalCacheDir(), "logcat.txt");
        try {
            if (logFile.exists())
                logFile.delete();
            logFile.createNewFile();
            Runtime.getRuntime().exec("logcat -f " + logFile.getAbsolutePath() + " -t 100 *:W Glide:S " + FilePickerActivity.TAG);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (logFile.exists()) {
            Intent intentShareFile = new Intent(Intent.ACTION_SEND);
            intentShareFile.setType("text/plain");
            intentShareFile.putExtra(Intent.EXTRA_STREAM,
                    FilePickerProvider.getUriForFile(this, logFile));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT, "FilePicker Log File");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "FilePicker Log File");
            startActivity(Intent.createChooser(intentShareFile, "Share"));
        }
    }
}
