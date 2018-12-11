package com.example.richard.angrykeys;

import android.content.Intent;
import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputConnection;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.lang.StringBuilder;

public class MyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {
    private static final int ANGER_INCREASE = 15;
    private static final int NEUTRAL_COOLDOWN = -3;
    private static final int POSITIVE_COOLDOWN = -9;
    private KeyboardView keyboardView;
    private Keyboard keyboard;
    private int wordsInBuffer;
    private StringBuilder buffer = new StringBuilder();
    private SharedMemory mSharedMemory;
    private static int anger = 0;

    private boolean caps = false;

    @Override
    public View onCreateInputView() {
        requestQueue = Volley.newRequestQueue(this);
        keyboardView = (KeyboardView) getLayoutInflater().inflate(R.layout.keyboard_view, null);
        keyboard = new Keyboard(this, R.xml.keys_layout);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }

    @Override
    public void onPress(int i) {

    }

    @Override
    public void onRelease(int i) {

    }



    @Override
    public void onKey(int primaryCode, int[] keyCodes) {
        InputConnection inputConnection = getCurrentInputConnection();
        if (inputConnection != null) {
            switch(primaryCode) {
                case Keyboard.KEYCODE_DELETE :
                    if (buffer.length() > 0) {
                        buffer.deleteCharAt(buffer.length() - 1);
                    }
                    CharSequence selectedText = inputConnection.getSelectedText(0);

                    if (TextUtils.isEmpty(selectedText)) {
                        inputConnection.deleteSurroundingText(1, 0);
                    } else {
                        inputConnection.commitText("", 1);
                    }
                case Keyboard.KEYCODE_SHIFT:
                    caps = !caps;
                    keyboard.setShifted(caps);
                    keyboardView.invalidateAllKeys();
                    break;
                case Keyboard.KEYCODE_DONE:
                    inputConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                    sendThought();
                    break;
                default :
                    char code = (char) primaryCode;
                    if(Character.isLetter(code) && caps){
                        code = Character.toUpperCase(code);
                    }
                inputConnection.commitText(String.valueOf(code), 1);
                buffer.append(code);
                if (Character.toString(code).equals(" ")) {
                    wordsInBuffer++;
                    if (wordsInBuffer >= 15) {
                        sendThought();
                    }
                }
                if (code == '.'
                        || code == '?'
                        || code == '!') {
                    sendThought();
                }

            }
        }

    }

    public void analyzeSentiment(String input) {
        String request = "http://api.datumbox.com/1.0/SentimentAnalysis.json?api_key=ce30c2175b219dc10f252483d6b0b632&text=";
        request = request + input;
        Log.d("URL Request", request);
        startAPICall(request);
        //int sentimentScore = 10;
        //return sentimentScore;
    }
    public void JsonToAnger(JSONObject sentiment) {
        String thing = sentiment.toString();
        String[] same = thing.split(":");
        char identifier = same[3].toCharArray()[3];
        Log.d("Thingy", Character.toString(identifier));
        switch (identifier) {
            case 's':
                addAnger(POSITIVE_COOLDOWN);
                Log.d("Switching", "positive");
                break;
            case 'u':
                addAnger(NEUTRAL_COOLDOWN);
                Log.d("Switching", "neutral");
                break;
            case 'g':
                addAnger(ANGER_INCREASE);
                Log.d("Switching", "negative");
                break;
            default:
                Log.d("Sentiment Switching", "Invalid");
                break;
        }
        Log.d("Switching", Integer.toString(anger));
        Intent i = new Intent(MyInputMethodService.this, ScreenFilterService.class);
        mSharedMemory = new SharedMemory(this);
        mSharedMemory.setRed(255);
        mSharedMemory.setGreen(0);
        mSharedMemory.setBlue(0);
        Log.d("Anger preprocess", Integer.toString(anger));
        if (anger < 0) {
            anger = 0;
        }
        if (anger > MainActivity.getMaxRed()) {
            anger = MainActivity.getMaxRed();
        }
        Log.d("Anger post", Integer.toString(anger));
        mSharedMemory.setAlpha(anger);
        startService(i);
    }
    public static void addAnger(int adding) {
        anger += adding;
    }
    public void sendThought() {
        String thought = buffer.toString();
        Log.d("Thought", thought);
        wordsInBuffer = 0;
        buffer = new StringBuilder();
        if (!MainActivity.filterOn()) {
            Log.d("thought", "out");
            return;
        }
        analyzeSentiment(thought);
        /**
        Intent i = new Intent(MyInputMethodService.this, ScreenFilterService.class);
        mSharedMemory = new SharedMemory(this);
        mSharedMemory.setRed(255);
        mSharedMemory.setGreen(0);
        mSharedMemory.setBlue(0);
        Log.d("Anger preprocess", Integer.toString(anger));
        if (anger < 0) {
            anger = 0;
        }
        if (anger > MainActivity.getMaxRed()) {
            anger = MainActivity.getMaxRed();
        }
        Log.d("Anger post", Integer.toString(anger));
        mSharedMemory.setAlpha(anger);
        startService(i);
         */
    }

    @Override
    public void onText(CharSequence charSequence) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    private static final String TAG = "APIRequest";


    /** Request queue for our network requests. */
    private RequestQueue requestQueue;

    /**
     * Run when our activity comes into view.
     *
     * @param savedInstanceState state that was saved by the activity last time it was paused
     */

    /**
     * Make an API call.
     */
    public void startAPICall(String url) {
        Log.d("API","API Method called");

        //String output = "";
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            Log.d(TAG, response.toString());
                            JsonToAnger(response);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            Log.d("Object",jsonObjectRequest.toString());
            requestQueue.add(jsonObjectRequest);
            //JsonParser parser =  new JsonParser();
            //JSONObject result = parser.parse(jsonObjectRequest).getAsJsonObject();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Error", "Failed");
        }
    }
}