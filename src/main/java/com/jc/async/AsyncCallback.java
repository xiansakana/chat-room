package com.jc.async;

public interface AsyncCallback {
    void onResponse(String message);

    void onFailure(Exception e);
}