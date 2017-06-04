package com.example.nabella.moviestationscanner;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nabella.moviestationscanner.lib.FormData;
import com.example.nabella.moviestationscanner.lib.InternetTask;
import com.example.nabella.moviestationscanner.lib.OnInternetTaskFinishedListener;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    TextView showdialog;
    private ZXingScannerView zXingScannerView;
    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;
    String statusnya;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        zXingScannerView =new ZXingScannerView(getApplicationContext());
        setContentView(zXingScannerView);
        zXingScannerView.setResultHandler(this);
        zXingScannerView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    public void handleResult(final Result result) {
        //Toast.makeText(getApplicationContext(),result.getText(),Toast.LENGTH_SHORT).show();
        shDialog();
        showdialog.setText(result.getText());
        zXingScannerView.resumeCameraPreview(this);

    }

    public void check(){
        Log.d("testnya", showdialog.getText().toString());
        FormData data = new FormData();
        data.add("method", "statusTicket");
        data.add("id_pembelian", showdialog.getText().toString());
        Log.d("datakirim", data.toString());
        InternetTask uploadTask = new InternetTask("Ticket", data);
        uploadTask.setOnInternetTaskFinishedListener(new OnInternetTaskFinishedListener() {
            @Override
            public void OnInternetTaskFinished(InternetTask internetTask) {
                try {
                    JSONObject jsonObject = new JSONObject(internetTask.getResponseString());
                    if (jsonObject.get("code").equals(200)){
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        if (jsonArray.getJSONObject(0).get("status").equals("1")){
                            Toast.makeText(MainActivity.this, "Anda Sudah Masuk" ,Toast.LENGTH_SHORT).show();
                        }else {
                            doUpdateTicket();
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Anda belum membeli tiket" ,Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void OnInternetTaskFailed(InternetTask internetTask) {
            }
        });
        uploadTask.execute();
    }

    public void shDialog(){
        dialog = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.dialogbox, null);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        //dialog.setTitle("Edit Nomor Telepon");

        showdialog    = (TextView) dialogView.findViewById(R.id.textView);

        dialog.setPositiveButton("CHECK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //doUpdateTicket();
                check();
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
    public void doUpdateTicket(){
        FormData data = new FormData();
        data.add("method", "updateTicket");
        data.add("id_pembelian", showdialog.getText().toString());
        data.add("status", "1");
        Log.d("datakirim", data.toString());
        InternetTask uploadTask = new InternetTask("Ticket", data);
        uploadTask.setOnInternetTaskFinishedListener(new OnInternetTaskFinishedListener() {
            @Override
            public void OnInternetTaskFinished(InternetTask internetTask) {
                try {
                    JSONObject jsonObject = new JSONObject(internetTask.getResponseString());
                    if (jsonObject.get("code").equals(200)){
                        Toast.makeText(MainActivity.this, "Terimakasih" ,Toast.LENGTH_SHORT).show();
                    }else{
                    }
                } catch (JSONException e) {
                }
            }

            @Override
            public void OnInternetTaskFailed(InternetTask internetTask) {
            }
        });
        uploadTask.execute();
    }
}
