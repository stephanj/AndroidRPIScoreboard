package org.janssen.scoreboard.task;

import android.os.AsyncTask;
import android.util.Log;

import org.janssen.scoreboard.comms.NetworkUtilities;

/**
 * Represents an asynchronous task used to set the foul
 * Created by stephan on 10/12/2017.
 */
public class FoulTask extends AsyncTask<Void, Void, String> {

    /**
     * The tag used to log to adb console.
     */
    private static final String TAG = "FoulTask";

    private final String token;
    private final int teamId;
    private final int totalFouls;
    private final boolean isPositive;

    public FoulTask(final String token,
                    final int teamId,
                    final int totalFouls,
                    final boolean isPositive) {
        this.token = token;
        this.teamId = teamId;
        this.totalFouls = totalFouls;
        this.isPositive = isPositive;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return NetworkUtilities.updateFouls(token, isPositive, teamId, totalFouls);
        } catch (Exception ex) {
            Log.e("FoulTask", ex.getMessage());
            return null;
        }
    }
}
