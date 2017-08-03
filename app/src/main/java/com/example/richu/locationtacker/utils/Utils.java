package com.example.richu.locationtacker.utils;

import android.app.Activity;
import android.app.ProgressDialog;

/**
 * Created by richu on 18/07/17.
 */
public class Utils {
    public void progressdialog(Activity a) {


    ProgressDialog progressdialog = new ProgressDialog(a);
    progressdialog.setMessage("Please Wait....");
progressdialog.show();
        progressdialog.setCancelable(false);
}
}
