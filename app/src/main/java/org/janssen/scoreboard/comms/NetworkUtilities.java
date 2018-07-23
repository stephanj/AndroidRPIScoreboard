package org.janssen.scoreboard.comms;

import android.support.annotation.NonNull;

import org.janssen.scoreboard.model.Server;
import org.janssen.scoreboard.model.types.ClockAction;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.Buffer;
import java.nio.charset.Charset;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;

/**
 * The network utilities class.
 */
final public class NetworkUtilities {

    private static final String OK = "OK";

    private static RequestBody emptyBody = RequestBody.create(null, new byte[0]);

    private NetworkUtilities() {
    }

    /**
     * Configures the httpClient to connect to the URL provided.
     */
    private static OkHttpClient getHttpClient() {
        return new OkHttpClient();
    }

    /**
     * Connects to the scoreboard server, authenticates the provided username and password.
     *
     * @param username The server account username
     * @param password The server account password
     *
     * @return String The authentication token returned by the server (or null)
     */
    public static String authenticate(final String username,
                                      final String password) throws IOException {

        String AUTH_URL = String.format(RestURI.LOGIN.getValue(), Server.getIp(), username, password);

        Request request = new Request.Builder()
                .url(AUTH_URL)
                .post(emptyBody)
                .build();

        try (Response response = getHttpClient().newCall(request).execute()) {
            return getResponseMessage(response);
        }
    }

    /**
     * Connects to the scoreboard server and creates a new game.
     *
     * @return ok
     */
    public static String newGame(final String token,
                                 final String teamA,
                                 final String teamB,
                                 final int gameType,
                                 final int ageCategory,
                                 final int court,
                                 final boolean mirrored) throws IOException {

        String NEW_GAME_URL = String.format(RestURI.CREATE_GAME.getValue(), Server.getIp(),
                URLEncoder.encode(teamA, "UTF8"),
                URLEncoder.encode(teamB, "UTF8"),
                gameType, ageCategory, court, mirrored, token);

        Request request = new Request.Builder()
                .url(NEW_GAME_URL)
                .post(emptyBody)
                .build();

        try (Response response = getHttpClient().newCall(request).execute()) {
            return getResponseMessage(response);
        }
    }

    /**
     * Connects to the scoreboard server and starts the new game.
     */
    public static void startGame(final String token,
                                 final int gameId,
                                 final boolean mirrored) throws IOException {

        String START_GAME_URI = String.format(RestURI.START_GAME.getValue(), Server.getIp(),
                token, gameId, mirrored);

        Request request = new Request.Builder()
                .url(START_GAME_URI)
                .post(emptyBody)
                .build();

        try (Response resp = getHttpClient().newCall(request).execute()) {
            if (!resp.isSuccessful()) {
                throw new IOException(resp.message());
            }
        }
    }

    @NonNull
    private static String getResponseMessage(Response response) throws IOException {
        if (response.isSuccessful()) {
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                BufferedSource source = responseBody.source();
                source.request(Long.MAX_VALUE); // request the entire body.
                okio.Buffer buffer = source.buffer();
                return buffer.clone().readString(Charset.forName("UTF-8"));
            }
        }

        throw new IOException(response.message());
    }

    /**
     * Connects to the scoreboard server and starts a clock countdown.
     *
     */
    public static void countDown(final int seconds,
                                 final boolean mirrored,
                                 final String token) throws IOException {

        String COUNTDOWN_URI = String.format(RestURI.COUNTDOWN.getValue(), Server.getIp(), seconds, mirrored, token);

        Request request = new Request.Builder()
                .url(COUNTDOWN_URI)
                .put(emptyBody)
                .build();

        try (Response resp = getHttpClient().newCall(request).execute()) {
            if (!resp.isSuccessful()) {
                throw new IOException(resp.message());
            }
        }
    }

    /**
     * Connects to the scoreboard server and increments the score
     *
     * @return ok
     */
    public static String updateScore(final String token,
                                     final boolean isPositive,
                                     final int teamId,
                                     final int points) throws IOException {

        String SCORE_URI= String.format(isPositive?RestURI.INC_SCORE.getValue():RestURI.DEC_SCORE.getValue(), Server.getIp(), teamId, points, token);

        Request request = new Request.Builder()
                .url(SCORE_URI)
                .put(emptyBody)
                .build();

        try (Response response = getHttpClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                return getResponseMessage(response);
            } else {
                throw new IOException("Network problem");
            }
        }
    }

    /**
     * Connects to the scoreboard server and increments the score
     *
     * @return ok
     */
    public static String updateFouls(String token, boolean isPositive, int teamId, int totalFouls) throws IOException {
        String SCORE_URI= String.format(isPositive?RestURI.INC_FOULS.getValue():RestURI.DEC_FOULS.getValue(), Server.getIp(), teamId, totalFouls, token);
        return executePut(SCORE_URI);
    }

    /**
     * Connects to the scoreboard server and increments the score
     *
     * @return ok
     */
    public static String updateQuarters(String token, boolean isPositive, int gameId) throws IOException {
        String SCORE_URI = String.format(isPositive?RestURI.INC_QUARTERS.getValue():RestURI.DEC_QUARTERS.getValue(), Server.getIp(), gameId, token);
        return executePut(SCORE_URI);
    }

    /**
     *
     * @param token
     * @param teamId
     * @return
     * @throws IOException
     */
    public static String incrementTimeout(final String token,
                                          final int teamId) throws IOException {
        return executePut(String.format(RestURI.INC_TIMEOUT.getValue(), Server.getIp(), teamId, token));
    }

    /**
     *
     * @param token
     * @return
     * @throws IOException
     */
    public static String attention(final String token) throws IOException {
        return executeGet(String.format(RestURI.ATTENTION.getValue(), Server.getIp(), token));
    }

    /**
     *
     * @return
     * @throws IOException
     */
    public static String pingServer() throws IOException {
        return executeGet(String.format(RestURI.PING.getValue(), Server.getIp()));
    }

    /**
     * Connects to the scoreboard server and increments the score
     *
     * @return ok
     */
    public static String triggerClock(final String token,
                                      final int gameId,
                                      final ClockAction action,
                                      final int seconds) throws IOException {
        String CLOCK_URI;

        switch (action) {
            case START: CLOCK_URI= String.format(RestURI.START.getValue(), Server.getIp(), gameId, token);
                break;

            case STOP:  CLOCK_URI= String.format(RestURI.STOP.getValue(), Server.getIp(), gameId, token);
                break;

            case INC:   CLOCK_URI= String.format(RestURI.INC_CLOCK.getValue(), Server.getIp(), gameId, token, seconds);
                break;

            default:    CLOCK_URI= String.format(RestURI.DEC_CLOCK.getValue(), Server.getIp(), gameId, token, seconds);
                break;
        }

        return executePut(CLOCK_URI);
    }

    private static String executePut(final String URL) throws IOException {
        Request request = new Request.Builder()
                .url(URL)
                .put(emptyBody)
                .build();

        try (Response resp = getHttpClient().newCall(request).execute()) {
            if (resp.isSuccessful()) {
                return OK;
            } else {
                throw new IOException(resp.message());
            }
        }
    }

    private static String executeGet(final String URL) throws IOException {
        Request request = new Request.Builder()
                .url(URL)
                .get()
                .build();

        try (Response response = getHttpClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                return OK;
            } else {
                throw new IOException(response.message());
            }
        }
    }
}
