package org.janssen.scoreboard;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;

import org.janssen.scoreboard.comms.NetworkUtilities;
import org.janssen.scoreboard.model.Server;

import java.lang.ref.WeakReference;

/**
 * The Wifi controller.
 *
 * Created by stephan on 25/01/14.
 */
public class ServerControlActivity extends FragmentActivity {

    public void pingServer() {
        if (Server.getIp() != null) {
            PingServerTask pingServerTask = new PingServerTask(this);
            pingServerTask.execute();
        }
    }

    public static void onPingResult(final String result, final Context context) {

        if (result == null || !result.equalsIgnoreCase("OK")) {
            AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setTitle(context.getString(R.string.error));
            dialog.setMessage(context.getString(R.string.geenComms) + " " + Server.getCourt() + "\n" + context.getString(R.string.herstart));
            dialog.setCancelable(false);
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, context.getString(R.string.ok), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int buttonId) {
                    //
                }
            });
            dialog.setIcon(android.R.drawable.ic_dialog_alert);
            dialog.show();
        }
    }

    /**
     * Ping the RPI server to see if it's available.
     */
    private static class PingServerTask extends AsyncTask<Void, Void, String> {

        WeakReference<Context> context;

        PingServerTask(Context context) {
            this.context = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                return NetworkUtilities.pingServer();
            } catch (Exception ex) {
                return ex.toString();
            }
        }

        @Override
        protected void onPostExecute(final String result) {
            onPingResult(result, context.get());
        }
    }
}
