package org.janssen.scoreboard.task;

import android.os.AsyncTask;
import android.util.Log;

import org.janssen.scoreboard.Constants;
import org.janssen.scoreboard.comms.NetworkUtilities;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * An asynchronous start game task.
 */
public class StartGameTask extends AsyncTask<Void, Void, String> {

    private final OnTaskCompleted listener;
    private final String authToken;
    private final String game;
    private final boolean mirrored;

    public StartGameTask(final OnTaskCompleted listener,
                         final String authToken,
                         final String game,
                         final boolean mirrored) {
        this.listener = listener;
        this.authToken = authToken;
        this.mirrored = mirrored;
        this.game = game;
    }

    @Override
    protected String doInBackground(Void... params) {
        int gameId = 0;

        try {
            JSONObject gameArray = new JSONObject(game);
            JSONObject game = (JSONObject)gameArray.get(Constants.GAME);
            gameId = game.getInt(Constants.ID);
        } catch (JSONException e) {
            Log.e("StartGameTask", e.getMessage());
        }

        try {
            NetworkUtilities.startGame(authToken, gameId, mirrored);
        } catch (IOException e) {
            Log.e("StartGameTask", e.getMessage());
        }

        return "";
    }

    @Override
    protected void onPostExecute(final String games) {
        listener.onTaskCompleted(games);
    }
}
