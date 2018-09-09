package com.timburkepe.lifedata;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.timburkepe.lifedata.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

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
    public ActivityMainBinding binding;

    private SleepModel sleepModel = new SleepModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.
                setContentView(this, R.layout.activity_main);

        String sleepDataurl = "http://burket.pythonanywhere.com/api/v1/sleeps/";

        try {
            postSleepData(client);
        } catch (IOException e) {
            Log.e(TAG, " IO Exception", e);
        }

        try {
            getSleepData(sleepDataurl, client);
        } catch (IOException e) {
            Log.e(TAG, " IO Exception", e);
        }

    }

    private void postSleepData(OkHttpClient client) throws IOException {
        RequestBody formBody = new FormBody.Builder()
                .add("bedtime", "2018-08-05T22:15:00-04:00")
                .add("wakeup_time", "2018-08-06T03:45:00-04:00")
                .add("quality", "GOOD")
                .add("description", "Tmmy, you  DID IT!!!")
                .build();
        Request requestPost = new Request.Builder()
                .url("http://burket.pythonanywhere.com/api/v1/sleeps/")
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

    private void getSleepData(String sleepDataurl, OkHttpClient client) throws IOException {
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
                String jsonData = response.body().string();
                if (response.isSuccessful()){
                    Log.v(TAG, jsonData);
                    getLatestSleepDetails(jsonData);

                    binding.setSleepData(sleepModel);
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
}
