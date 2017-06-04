package com.example.nabella.moviestationscanner.lib;


import android.os.AsyncTask;

import java.util.ArrayList;

public class InternetTask  extends AsyncTask<String, Void, Void> {

    private OnInternetTaskFinishedListener listener;
    private String method;
    private String urlString;
    private ArrayList<KeyValue> requestData;
    private String responseString;
    private String tag;
    private Exception exception;
    private FormData formData;
    private boolean usesForm;
    private String base_url = "http://192.168.0.5/MovieStationService/";
    public InternetTask(String urlString, FormData data)
    {
        this.method = InternetHelper.REQUEST_METHOD_POST;
        this.urlString = base_url + urlString;
        this.formData = data;
        this.listener = null;
        this.responseString = "";
        this.tag = "";
        this.usesForm = true;
    }

    public InternetTask(String method, String urlString, ArrayList<KeyValue> requestData)
    {
        this.method = method;
        this.urlString = base_url + urlString;
        this.requestData = requestData;
        this.listener = null;
        this.responseString = "";
        this.tag = "";
    }

    public String getTag() {
        return tag;
    }

    public Exception getException() {
        return exception;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setOnInternetTaskFinishedListener(OnInternetTaskFinishedListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(String... params) {
        try
        {
            if(this.usesForm)
            {
                this.responseString = InternetHelper.uploadFiles(this.urlString,
                        this.formData);
            }
            else
            {
                this.responseString = InternetHelper.sendHttpRequest(
                        this.method, this.urlString,
                        this.requestData);
            }
        }catch (Exception e){
            this.exception = e;
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(this.listener != null) {
            if (this.exception == null){
                this.listener.OnInternetTaskFinished(this);
            }else {
                this.listener.OnInternetTaskFailed(this);
            }
        }
    }
}
