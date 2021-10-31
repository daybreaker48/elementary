package com.mhd.stard.network.manager;

public interface ResponseListener {

    void onError(String message);
    void onResponse(String response);
}