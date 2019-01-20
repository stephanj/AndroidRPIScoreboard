package org.janssen.scoreboard.task;

import android.os.AsyncTask;
import android.util.Log;

import org.janssen.scoreboard.comms.NetworkUtilities;

/**
 * Represents an asynchronous task used to set the quarters.
 */
public class QuarterTask extends AsyncTask<Void, Void, String> {

    private int gameId;
    private String authToken;
    private boolean isPositive;

    /**
     * QuarterTask constructor.
     *
     * @param gameId        the game identifier
     * @param authToken     authentication token
     * @param isPositive    is positive?
     */
    public QuarterTask(final int gameId,
                       final String authToken,
                       final boolean isPositive) {
        this.gameId = gameId;
        this.authToken = authToken;
        this.isPositive = isPositive;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return NetworkUtilities.updateQuarters(authToken, isPositive, gameId);
        } catch (Exception ex) {
            Log.e("QuarterTask", ex.getMessage());
            return ex.getMessage();
        }
    }
}