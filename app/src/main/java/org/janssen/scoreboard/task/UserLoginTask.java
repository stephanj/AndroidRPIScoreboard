package org.janssen.scoreboard.task;

import android.os.AsyncTask;
import android.util.Log;

import org.janssen.scoreboard.comms.NetworkUtilities;

/**
 * Represents an asynchronous task used to authenticate a user against the
 * SampleSync Service.
 */
public class UserLoginTask extends AsyncTask<Void, Void, String> {

    private final OnTaskListener listener;
    private final String username;
    private final String password;

    public UserLoginTask(final OnTaskListener listener,
                         final String username,
                         final String password) {
        this.listener = listener;
        this.username = username;
        this.password = password;
    }

    @Override
    protected String doInBackground(Void... params) {
        // We do the actual work of authenticating the user
        // in the NetworkUtilities class.
        try {
            return NetworkUtilities.authenticate(username, password);
        } catch (Exception ex) {
            Log.e("UserLoginTask", ex.getMessage());
            return ex.toString();
        }
    }

    @Override
    protected void onPostExecute(final String authToken) {
        // On a successful authentication, call back into the Activity to
        // communicate the authToken (or null for an error).
        listener.onTaskCompleted(authToken);
    }

    @Override
    protected void onCancelled() {
        // If the action was canceled (by the user clicking the cancel
        // button in the progress dialog), then call back into the
        // activity to let it know.
        listener.onTaskCancelled();
    }
}
