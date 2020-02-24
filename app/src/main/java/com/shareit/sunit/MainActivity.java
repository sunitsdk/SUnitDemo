package com.shareit.sunit;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ushareit.ads.ad.AdWrapper;
import com.ushareit.ads.ad.BannerAd;
import com.ushareit.ads.ad.IAdLoadListener;
import com.ushareit.ads.ad.InterstitialAd;
import com.ushareit.ads.ad.RewardedAd;
import com.ushareit.ads.base.AdException;
import com.ushareit.ads.openapi.ShareItAd;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends Activity {
    private TextView statusTV;
    private Button rewardBadgeViewBtn;
    private FrameLayout mAdContainer;

    private static final String INTERSTITIAL_UNIT_ID = "1014yOdwNy";
    private static final String REWARD_UNIT_ID = "1014yOdeiW";
    private static final String BANNER_UNIT_ID = "1014yOdnGC";

    private static final String REWARD_SUB_PORTAL = "reward_badge_btn";
    private static final String REWARD_BADGE_SUB_PORTAL = "reward_badge_btn";

    private static final String TAG = "MainActivity";

    private Timer mTimer;
    private TimerTask mTimerTask;
    private static final int MSG_CHANGE_TO_MAIN_THREAD = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        statusTV = findViewById(R.id.tv_status);
        rewardBadgeViewBtn = findViewById(R.id.btn_reward_badge_view);

        mAdContainer = findViewById(R.id.ad_container);

        //load interstitial ad
        InterstitialAd.loadAd(INTERSTITIAL_UNIT_ID);
        //load rewarded ad
        RewardedAd.loadAd(REWARD_UNIT_ID);
        //preload banner ad
        BannerAd.preloadBannerAd(BANNER_UNIT_ID);

        //request sd card permission
        requestPermission();


    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
        }
    }

    public void showInterstitial(View view) {
        showMsg("showInterstitial");
        //如下方法适用于接入方未管理广告缓存的场景
        String scene = ShareItAd.GAME_REGAIN_FOCUS;
//      String scene = ShareItAd.GAME_LEVEL_START;
//      String scene = ShareItAd.GAME_LEVEL_END;
//      String scene = ShareItAd.LOOP;

        if (InterstitialAd.isAdReady(INTERSTITIAL_UNIT_ID, scene))
            InterstitialAd.showAd(INTERSTITIAL_UNIT_ID, null);
        else {
            showMsg("Interstitial AD not ready");
            InterstitialAd.loadAd(INTERSTITIAL_UNIT_ID);
        }
    }

    public void loadAndShowItl(View view) {
        showMsg("loadAndShowItl");

        //如下方法适用于接入方自己管理了广告缓存的场景
        InterstitialAd.loadAd(INTERSTITIAL_UNIT_ID, new IAdLoadListener() {
            @Override
            public void onAdLoaded(String unitId, AdWrapper adWrapper) {
                showMsg("load InterstitialAd Succeed, Will show it automatic");
                InterstitialAd.showAd(adWrapper, null);
            }

            @Override
            public void onAdError(String unitId, AdException adError) {

            }
        });

    }

    public void showReward(View view) {
        showMsg("showReward");
        //如下方法适用于接入方未管理广告缓存的场景
        if (RewardedAd.isAdReady(REWARD_UNIT_ID, ShareItAd.HOME, REWARD_SUB_PORTAL))
            RewardedAd.showAd(REWARD_UNIT_ID, null);
        else {
            showMsg("Rewarded AD not ready");
            RewardedAd.loadAd(REWARD_UNIT_ID);
        }
    }

    public void loadAndShowRwd(View view) {
        showMsg("loadAndShowRwd");

        RewardedAd.loadAd(REWARD_UNIT_ID, new IAdLoadListener() {
            @Override
            public void onAdLoaded(String unitId, AdWrapper adWrapper) {
                showMsg("load RewardedAd Succeed, Will show it automatic");
                RewardedAd.showAd(adWrapper, null);
            }

            @Override
            public void onAdError(String unitId, AdException adError) {

            }
        });
    }

    public void clickRewardBadgeView(View view) {
        if (RewardedAd.isAdReady(REWARD_UNIT_ID, ShareItAd.LOOP)) {
            showMsg("clickRewardedBadgeView");
            RewardedAd.showAd(REWARD_UNIT_ID, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        showRewardBadgeView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        rewardBadgeViewBtn.setEnabled(false);
    }

    private void showMsg(String msg) {
        if (statusTV == null) {
            return;
        }
        statusTV.setText(msg);
    }

    public void startShowBanner(View view) {
        showMsg("loadBanner");
        BannerAd.showBanner(BANNER_UNIT_ID, BannerAd.BOTTOM);
//        BannerAd.showBanner(BANNER_UNIT_ID,BannerAd.TOP);
//        BannerAd.showBanner(BANNER_UNIT_ID,mAdContainer);
    }

    public void hideBanner(View view) {
        BannerAd.hiddenBannerAd();
    }

    public void toLogin(View view) {
        Intent intent = new Intent(this, TestGameActivity.class);
        startActivity(intent);
    }

    public void toPay(View view) {
        Intent intent = new Intent(this, TestPayActivity.class);
        startActivity(intent);
    }

    private void showRewardBadgeView() {
        cancelTimerTask();
        //When current page is visible and check result return true, we can invoke "showRewardedBadgeView" function
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    handler.sendEmptyMessage(MSG_CHANGE_TO_MAIN_THREAD);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 1000, 5 * 1000);
    }

    Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_CHANGE_TO_MAIN_THREAD) {
                if (RewardedAd.isAdReady(REWARD_UNIT_ID, ShareItAd.LOOP)) {
                    cancelTimerTask();
                    rewardBadgeViewBtn.setEnabled(true);

                    showMsg("showRewardedBadgeView");
                    RewardedAd.showRewardedBadgeView(ShareItAd.LOOP, REWARD_BADGE_SUB_PORTAL);
                }
                return true;
            }
            return false;
        }
    });

    private void cancelTimerTask() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

}
