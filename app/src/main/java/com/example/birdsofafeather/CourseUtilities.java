package com.example.birdsofafeather;

import android.app.Activity;
import android.app.AlertDialog;

public class CourseUtilities {

    public static void showError(Activity activity, String message) {
        showError(activity, message, () -> {});
    }
    public static void showError(Activity activity, String message, Runnable callback) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder
                .setTitle("Error!")
                .setMessage(message)
                .setPositiveButton("OK", ((dialog, id) -> {
                    dialog.cancel();
                    callback.run();
                }))
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static void showAlert(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);
        alertBuilder
                .setTitle("Alert!")
                .setMessage(message)
                .setPositiveButton("OK", ((dialog, id) -> dialog.cancel()))
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }
}
