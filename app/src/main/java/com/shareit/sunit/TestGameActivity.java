package com.shareit.sunit;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ushareit.ads.ad.RewardedAd;
import com.ushareit.ads.openapi.ShareItAd;
import com.ushareit.aggregationsdk.SHAREitAggregation;
import com.ushareit.logindialog.utils.GameLoginHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * create by huanting on 2019-09-03 11:54
 */
public class TestGameActivity extends Activity {
    private static final String TAG = "TestGameActivity";
    private static final String GAME_TEST_CLIENT_SECRET = "cb41338ff0e6779eaeb44740b28e0cdc";
    private static final String REWARD_UNIT_ID = "1014yOdeiW";

    private TextView loginInfoTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_game);
        loginInfoTv = findViewById(R.id.loginInfoTv);
    }

    public void onDataUpload(View view) {
        try {
            HashMap<String, String> params = new LinkedHashMap<String, String>();
            params.put("para1", "val1");
            params.put("para2", "val2");
            SHAREitAggregation.onEvent(this,"TEST_EVENT_NAME", params);
        } catch (Exception e) {

        }
    }

    public void onGameLevelStartDataUpload(View view) {
        SHAREitAggregation.gameLevelStart("1");
    }

    public void onGameLevelEndDataUpload(View view) {
        SHAREitAggregation.gameLevelEnd("1");
    }

    public void onLoginInClick(View view) {

        GameLoginHelper mHelper = GameLoginHelper.getInstance();
        //If you have logged in, it doesn't need login again
        if (mHelper.isLogin()) {

            String userId = mHelper.getUserId();
            Log.d(TAG, "onLoginInClick userId=" + userId);
            Toast.makeText(this, "Has logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        //call login UI
        SHAREitAggregation.userLogin(this, GAME_TEST_CLIENT_SECRET, new GameLoginHelper.OnLoginCompleteListener() {
            @Override
            public void onLoginSuccess(String userId, String username, String avatarUrl) {
                //LoginSuccess
                Log.d(TAG, "onLoginSuccess userId=" + userId + " username=" + username + " avatarUrl=" + avatarUrl);
                loginInfoTv.setText("userId=" + userId + " userName=" + username);
                Toast.makeText(TestGameActivity.this, "Log successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void onLoginOutClick(View view) {
        GameLoginHelper mHelper = GameLoginHelper.getInstance();
        if (mHelper.isLogin()) {
            if (mHelper.logout())
                Toast.makeText(this, "Has logged out successfully", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "You haven't logged in.");
            Toast.makeText(this, "You haven't logged in.", Toast.LENGTH_SHORT).show();
        }
    }
}
