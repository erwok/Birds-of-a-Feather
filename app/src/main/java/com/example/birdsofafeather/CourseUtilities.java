package com.example.birdsofafeather;

import android.app.Activity;
import android.app.AlertDialog;

/**
 * Utility class for courses.
 */
public class CourseUtilities {

    /**
     * Alerts of what error occurs that does not perform any additional actions.
     *
     * @param activity
     * @param message
     */
    public static void showError(Activity activity, String message) {
        showError(activity, message, () -> {});
    }

    /**
     * Alerts of what error occurs running a callback function for additional actions
     * to be taken.
     *
     * @param activity
     * @param message
     * @param callback
     */
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

    /**
     * Sends an alert to users with a given message.
     *
     * @param activity
     * @param message
     */
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
