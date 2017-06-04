package com.example.nabella.moviestationscanner.lib;

/**
 * Created by Syauqi on 14-Nov-16.
 */
public interface OnInternetTaskFinishedListener {
    void OnInternetTaskFinished(InternetTask internetTask);

    void OnInternetTaskFailed(InternetTask internetTask);
}
