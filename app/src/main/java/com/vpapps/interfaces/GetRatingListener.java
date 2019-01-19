package com.vpapps.interfaces;

public interface GetRatingListener {
    void onStart();
    void onEnd(String success, String message, float rating);
}
