package org.janssen.scoreboard.task;

import android.os.AsyncTask;
import android.util.Log;

import org.janssen.scoreboard.comms.NetworkUtilities;

import java.io.IOException;

/**
 * Represents an asynchronous task used to create a new game.
 */
public class NewGameTask extends AsyncTask<Void, Void, String> {

    private final OnTaskListener listener;
    private final String teamA;
    private final String teamB;
    private final int gameType;
    private final int ageCategory;
    private final int selectedCourt;
    private final boolean mirroring;
    private final String authToken;

    public NewGameTask(
                final OnTaskListener listener,
                final String teamA,
                final String teamB,
                final int gameType,
                final int ageCategory,
                final int selectedCourt,
                final boolean mirroring,
                final String authToken) {
        this.listener = listener;
        this.teamA = teamA;
        this.teamB = teamB;
        this.gameType = gameType;
        this.ageCategory = ageCategory;
        this.selectedCourt = selectedCourt;
        this.mirroring = mirroring;
        this.authToken = authToken;
    }

    @Override
    protected String doInBackground(Void... params) {

        try {
            return NetworkUtilities.newGame(authToken, teamA, teamB, gameType, ageCategory, selectedCourt, mirroring);
        } catch (IOException ex) {
            Log.e("NewGameTask", ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(final String games) {
        // On a successful authentication, call back into the Activity to
        // communicate the games (or null for an error).
        listener.onTaskCompleted(games);
    }

    @Override
    protected void onCancelled() {
        // If the action was canceled (by the user clicking the cancel
        // button in the progress dialog), then call back into the
        // activity to let it know.
        listener.onTaskCancelled();
    }
}

