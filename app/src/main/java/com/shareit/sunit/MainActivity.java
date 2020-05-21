package com.shareit.sunit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sunit.rate.openapi.SUnitRate;
import com.ushareit.ads.ad.AdWrapper;
import com.ushareit.ads.ad.BannerAd;
import com.ushareit.ads.ad.IAdLoadListener;
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
        //preload banner ad
        BannerAd.preloadBannerAd(BANNER_UNIT_ID);

        //在启动app时,主动调用此方法(方法内含有Id初始化相关策略，即使接入方自己申请了权限也需要调用此方法)
        SHAREitAggregation.requestStoragePermissions(this);
    }

    private void initView() {
        statusTV = findViewById(R.id.tv_status);
        mAdContainer = findViewById(R.id.ad_container);
        mRewardBtn = findViewById(R.id.btn_reward_show);
    }

    public void showInterstitial(View view) {
        showMsg("showInterstitial");
        //如下方法适用于接入方未管理广告缓存的场景
        if (InterstitialAd.isAdReady(INTERSTITIAL_UNIT_ID, ShareItAd.HOME))
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
                RewardedAd.showAd(adWrapper, null);
            }

            @Override
            public void onAdError(String unitId, AdException adError) {

            }
        });
    }

    @Override
    protected void onResume() {//此方法页面从不可见到可见会调用
        super.onResume();
        if (mRewardBtn.isEnabled())//如果激励视频按钮可见可以点击
            RewardedAd.showRewardedBadgeView(ShareItAd.HOME, PORTAL_REWARD_BTN);//上报激励视频入口
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
}
