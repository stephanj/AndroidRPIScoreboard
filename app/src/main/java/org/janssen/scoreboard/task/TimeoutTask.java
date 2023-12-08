package org.janssen.scoreboard.task;

import android.os.AsyncTask;
import android.util.Log;

import org.janssen.scoreboard.comms.NetworkUtilities;

/**
 * Represents an asynchronous task used to set the quarters
 */
public class TimeoutTask extends AsyncTask<Void, Void, String> {

    /**
     * The tag used to log to adb console.
     */
    private static final String TAG = "TimeoutTask";

    private final int teamId;
    private final String authToken;

    public TimeoutTask(final int teamId,
                       final String authToken) {
        this.teamId = teamId;
        this.authToken = authToken;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return NetworkUtilities.incrementTimeout(authToken, teamId);
        } catch (Exception ex) {
            Log.e("TimeoutTask", ex.getMessage());
            return null;
        }
    }
}
