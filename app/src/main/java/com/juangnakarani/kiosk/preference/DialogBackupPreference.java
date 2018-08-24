package com.juangnakarani.kiosk.preference;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.preference.DialogPreference;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Toast;

import com.juangnakarani.kiosk.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class DialogBackupPreference extends DialogPreference {
    public DialogBackupPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.dialog_backup);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(null);
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            // User selected OK
            Log.i("chk","bakup ok");

            doBackup();

        } else {
            // User selected Cancel
            Log.i("chk","bakup cancel");
        }
    }

    private void doBackup(){
        Log.e("chk","doBackup()");
        Log.e("chk",getContext().getPackageName());
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = "/data/"+ getContext().getPackageName() + "/databases/Kiosk.db";
        String backupDBPath = "/Kiosbak.db";
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Toast.makeText(getContext(), "Backup success!", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            e.printStackTrace();
            Log.e("chk","doBackup() IOException " + e);
            Toast.makeText(getContext(), "Backup failed!", Toast.LENGTH_SHORT).show();

        }
    }


}
