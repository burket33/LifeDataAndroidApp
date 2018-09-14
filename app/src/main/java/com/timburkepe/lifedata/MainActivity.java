package com.timburkepe.lifedata;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.timburkepe.lifedata.adapters.SleepAdapter;
import com.timburkepe.lifedata.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final OkHttpClient client = new OkHttpClient();

    private String sleepDataurl = "http://burket.pythonanywhere.com/api/v1/sleeps/";

    private SleepModel sleepModel = new SleepModel();

    public ActivityMainBinding binding;
    private SleepAdapter adapter;
    private LinearLayoutManager linearLayout;
    public Button createSleepDataButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //createSleepDataButton = findViewById(R.id.createSleepDataButton);


        Activity activity = this;
        Context context = this;
        linearLayout = new LinearLayoutManager(this);

        try {
            getSleepData(sleepDataurl, client, linearLayout, context, activity);
        } catch (IOException e) {
            e.printStackTrace();
        }





    }

    public void onClickCreateSleepData(View view) {
        startCreateSleepData();
    }

    private void startCreateSleepData() {
        Intent intent = new Intent(this, CreateSleepDataActivity.class);
        startActivity(intent);

    }

    private void postSleepData(OkHttpClient client) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("bedtime", "2018-08-05T22:15:00-04:00")
                .add("wakeup_time", "2018-08-06T03:45:00-04:00")
                .add("quality", "GOOD")
                .add("description", "Tmmy, you  DID IT!!!")
                .build();
        Request requestPost = new Request.Builder()
                .url(sleepDataurl)
                .header("Authorization", "Token e8eee846c0ec515008c4bd21056f555825ce8aa5")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build();
        Log.i(TAG, "Code is here");

        client.newCall(requestPost).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "failed: ", e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "Response body: " + response.body().string());
            }
        });
    }

    private void getSleepData(String sleepDataurl, OkHttpClient client, final LinearLayoutManager linearLayout, final Context context, final Activity activity) throws IOException {
        Request request = new Request.Builder()
                .url(sleepDataurl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "IO Exception Caught: ", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String jsonData = response.body().string();
                if (response.isSuccessful()){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding = DataBindingUtil.setContentView(activity, R.layout.activity_main);
                            adapter = new SleepAdapter(getSleepDataList(jsonData), context);
                            binding.sleepListItems.setAdapter(adapter);
                            binding.sleepListItems.setLayoutManager(linearLayout);

                        }
                    });

                }
            }
        });
    }

    private void getLatestSleepDetails(String jsonData) {
        try {
            JSONArray sleepData = new JSONArray(jsonData);
            JSONObject sleepDataObj1 = sleepData.getJSONObject(sleepData.length() - 1);
            sleepModel.setQuality(sleepDataObj1.getString("quality"));
            sleepModel.setDescription(sleepDataObj1.getString("description"));
            Log.i(TAG, "FROM JSON: " + sleepModel.getDescription());
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception Caught: ", e);
        }

    }


    private List<SleepModel> getSleepDataList(String jsonData) {
        List<SleepModel> sleeps = new ArrayList<>();
        try {
            JSONArray sleepData = new JSONArray(jsonData);
            for (int i = 0; i < sleepData.length(); i++){
                JSONObject sleepDataObj = sleepData.getJSONObject(i);
                SleepModel localSleepModel = new SleepModel();
                localSleepModel.setBedtime(0); //TODO determine how to convert time string into long
                localSleepModel.setWakeupTime(0); //TODO determine how to convert time string into long
                localSleepModel.setQuality(sleepDataObj.getString("quality"));
                localSleepModel.setDescription(sleepDataObj.getString("description"));
                localSleepModel.setSleepDuration(Double.parseDouble(sleepDataObj.getString("sleep_duration")));
                sleeps.add(localSleepModel);
                Log.i(TAG, "FROM JSON: " + localSleepModel.getBedtime() +
                        localSleepModel.getWakeupTime() +
                        localSleepModel.getDescription() +
                        localSleepModel.getQuality());
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception Caught: ", e);
            sleeps = null;
        }
        return sleeps;
    }
}
