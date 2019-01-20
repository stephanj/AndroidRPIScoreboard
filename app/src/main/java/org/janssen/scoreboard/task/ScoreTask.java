package org.janssen.scoreboard.task;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import org.janssen.scoreboard.comms.NetworkUtilities;

import java.io.IOException;
import java.lang.ref.WeakReference;

import static org.janssen.scoreboard.ScoreActivity.NETWORK_PROBLEEM_PROBEER_OPNIEUW;
import static org.janssen.scoreboard.ScoreActivity.OPNIEUW;
import static org.janssen.scoreboard.task.ClockTask.ERROR_PROBEER_OPNIEUW;

/**
 * Represents an asynchronous task used to set the score
 */
public class ScoreTask extends AsyncTask<Void, Void, String> {

    private String token;
    private int teamId;
    private int points;
    private String teamName;
    private boolean isPositive;

    private WeakReference<TextView> textView;

    public ScoreTask(final String token,
              final TextView textView,
              final String teamName,
              final int teamId,
              final int points,
              final boolean isPositive) {
        this.token = token;
        this.textView = new WeakReference<>(textView);
        this.teamName = teamName;
        this.teamId = teamId;
        this.points = points;
        this.isPositive = isPositive;
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            return NetworkUtilities.updateScore(token, isPositive, teamId, points);
        } catch (IOException ex) {
            Log.e("ScoreTask", ex.getMessage());
            return NETWORK_PROBLEEM_PROBEER_OPNIEUW;
        } catch (Exception ex) {
            Log.e("ScoreTask", ex.getMessage());
            return ERROR_PROBEER_OPNIEUW + ex.toString();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (!result.contains(OPNIEUW)) {
            TextView textView = this.textView.get();
            textView.setText(String.format("%s %s", teamName, result));
        }
    }
}