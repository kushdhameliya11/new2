package natomic.com.techuplabs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.Settings;

public class NotificationUtils {

    private static final String PREF_NOTIFICATION_DIALOG_SHOWN = "notification_dialog_shown";

    public static void showNotificationPermissionDialogIfNeeded(final Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean dialogShown = preferences.getBoolean(PREF_NOTIFICATION_DIALOG_SHOWN, false);

        if (!dialogShown) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Notification Permission Required");
            builder.setMessage("This app requires notification permission to send notifications. Please grant the permission to continue.");
            builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    requestNotificationPermission(context);
                    // Mark the dialog as shown
                    markNotificationDialogShown(context);
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    // Handle cancellation if needed
                }
            });
            builder.setCancelable(false);
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    private static void markNotificationDialogShown(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_NOTIFICATION_DIALOG_SHOWN, true);
        editor.apply();
    }

    public static void requestNotificationPermission(Context context) {
        Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
        intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
        context.startActivity(intent);
    }
}
