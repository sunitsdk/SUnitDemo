package com.shareit.sunit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.ushareit.ads.ad.AdWrapper;
import com.ushareit.ads.ad.BannerAd;
import com.ushareit.ads.ad.IAdLoadListener;
import com.ushareit.ads.ad.IAdShowListener;
import com.ushareit.ads.ad.InterstitialAd;
import com.ushareit.ads.ad.RewardedAd;
import com.ushareit.ads.base.AdException;
import com.ushareit.ads.openapi.ShareItAd;
import com.ushareit.aggregationsdk.SHAREitAggregation;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    private TextView statusTV;
    private FrameLayout mAdContainer;

    private Button mRewardBtn;

    private static final String INTERSTITIAL_UNIT_ID = "1014yOdwNy";
    private static final String REWARD_UNIT_ID = "1014yOdeiW";
    private static final String BANNER_UNIT_ID = "1014yOdnGC";

    private static final String PORTAL_REWARD_BTN = "reward_btn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        //load interstitial ad
        InterstitialAd.loadAd(INTERSTITIAL_UNIT_ID);
        //load rewarded ad
        RewardedAd.loadAd(REWARD_UNIT_ID);

        //The method contains id initialization function, you must use it
        SHAREitAggregation.requestStoragePermissions(this);

        initCloudConfig();
    }

    private void initView() {
        statusTV = findViewById(R.id.tv_status);
        mAdContainer = findViewById(R.id.ad_container);
        mRewardBtn = findViewById(R.id.btn_reward_show);
    }

    public void showInterstitial(View view) {
        showMsg("showInterstitial");
        //SDK manages the Ad cache
        if (InterstitialAd.isAdReady(INTERSTITIAL_UNIT_ID, ShareItAd.HOME))
            InterstitialAd.showAd(INTERSTITIAL_UNIT_ID, null);
        else {
            showMsg("Interstitial AD not ready");
            InterstitialAd.loadAd(INTERSTITIAL_UNIT_ID);
        }
    }

    public void loadAndShowItl(View view) {
        showMsg("loadAndShowItl");

        //You manage the cache yourself
        InterstitialAd.loadAd(INTERSTITIAL_UNIT_ID, new IAdLoadListener() {
            @Override
            public void onAdLoaded(String unitId, AdWrapper adWrapper) {
                showMsg("load InterstitialAd Succeed, Will show it automatic");
                InterstitialAd.showAd(adWrapper, ShareItAd.HOME, null);
            }

            @Override
            public void onAdError(String unitId, AdException adError) {

            }
        });

    }

    public void showReward(View view) {
        showMsg("showReward");
        //SDK manages the Ad cache
        if (RewardedAd.isAdReady(REWARD_UNIT_ID, ShareItAd.HOME, PORTAL_REWARD_BTN))
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
                RewardedAd.showAd(adWrapper, ShareItAd.HOME, PORTAL_REWARD_BTN, new IAdShowListener() {
                    @Override
                    public void onAdShowFailed(String s, AdException e) {

                    }

                    @Override
                    public void onAdImpression(String s, String s1) {

                    }

                    @Override
                    public void onAdClicked(String s, String s1) {

                    }

                    @Override
                    public void onAdRewarded(String s, String s1) {

                    }

                    @Override
                    public void onAdClosed(String s, String s1, boolean b) {

                    }
                });
            }

            @Override
            public void onAdError(String unitId, AdException adError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mRewardBtn.isEnabled() && mRewardBtn.getVisibility() == View.VISIBLE)//Button can click and visible
            RewardedAd.showRewardedBadgeView(ShareItAd.HOME, PORTAL_REWARD_BTN);//report rewarded button
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

    public void toShowPromoteVideo(View view) {
        //From v2.3.0.0，need not invoke these methods yourself.
//        String scene = "main_page";//The real show scene of promote video.
//        int showX = 0;//show x in screen
//        int showY = 100;//show y in screen
//        if (SHAREitAggregation.canShowVideo(scene))
//            SHAREitAggregation.showVideoDialog(showX, showY, scene, false);
    }

    public void toHidePromoteVideo(View view) {
        //From v2.3.0.0，need not invoke this method yourself.
//        SHAREitAggregation.hideVideoDialog();
    }

    private void initCloudConfig() {
        String testKey = "key_interstitial_show_enable";//your key
        boolean booleanConfig = SHAREitAggregation.getBooleanConfig(testKey, false, true);//sdk cloud service return value.
        //other type sample
        String stringConfig = SHAREitAggregation.getStringConfig(testKey, "", true);
        int intConfig = SHAREitAggregation.getIntConfig(testKey, 0, true);
        long longConfig = SHAREitAggregation.getLongConfig(testKey, 0L, true);
    }
}
