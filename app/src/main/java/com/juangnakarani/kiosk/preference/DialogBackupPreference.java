package com.juangnakarani.kiosk.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.util.Log;

import com.juangnakarani.kiosk.R;

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
        } else {
            // User selected Cancel
            Log.i("chk","bakup cancel");
        }
    }

}
