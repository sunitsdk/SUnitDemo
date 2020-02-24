package com.shareit.sunit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ushareit.aggregationsdk.BuildConfig;
import com.ushareit.aggregationsdk.SHAREitAggregation;
import com.ushareit.aggregationsdk.SHAREitEnv;
import com.ushareit.paysdk.pay.entry.SPBuildType;
import com.ushareit.paysdk.pay.entry.SPMerchantParam;
import com.ushareit.paysdk.pay.entry.SPPayCallback;
import com.ushareit.paysdk.pay.entry.SPPayService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TestPayActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "payDemo.MainActivity";

    private static final String SHARE_PREF_NAME = "paySdkSharePref";

    private static final String KEY_BUILD_TYPE = "buildType";
    private static final String KEY_MERCHANT_ID = "merchantId";
    private static final String KEY_ORDER_ID = "orderId";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_CURRENTCY = "currency";
    private static final String KEY_CALLBACK_URL = "callbackUrl";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_MERCHANT_KEY = "merchantKey";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_SECRET_KEY = "secretKey";
    private static final String KEY_COUNTRY_CODE = "countryCode"; //国家码：2位字母缩写，必须大写
    private static final String KEY_TEST_URL = "testUrl";

    private static final String KEY_DESC = "desc";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_DISCOUNT_AMOUNT = "discountAmount";
    private static final String KEY_MAIL = "mail";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_EXTRA = "extra";
    private static final String KEY_PAY_VALID_DURATION = "payValidDuration"; // 单位：秒
    private static final String KEY_PAY_RESULT_TYPE = "payResultType"; //是否使用商户支付结果页

    private static final String MERCHANT_ID = "M36977092608";
    private static final String SECRET_KEY = "payment-bootstra";

    //    private static final String DEFAULT_URL = "http://192.168.23.200:8076/#/";
    private static final String DEFAULT_URL = "";

    private HashMap<String, SPBuildType> buildTypeMap = new HashMap<>();
    private static final String DEFAULT_BUILD_TYPE = SPBuildType.Test.name();

    private EditText merchantIdEt;
    private EditText orderIdEt;
    private EditText amountEt;
    private EditText currentcyEt;
    private EditText callbackUrlEt;
    private EditText subjectEt;
    private EditText userIdEt;
    private EditText tokenEt;
    private EditText secretKeyEt;
    private EditText countryCodeEt;
    private EditText testUrlEt;

    //optional
    private EditText descEt;
    private EditText discountAmountEt;
    private EditText mailEt;
    private EditText phoneEt;
    private EditText extraEt;
    private Spinner mLangSpinner;
    private EditText mTimeoutInSeconds; //支付有效时长，单位毫秒
    private CheckBox mPayResultTypeCheckBox;

    private View dividerLayout;
    private ImageView indicatorIv;
    private View optionalLayout;

    private static final int MSG_GET_TOKEN_SUCCESS = 1;
    private static final int MSG_CLICK_COUNT = 2;
    private static final int MSG_SHOW_RESULT = 3;
    private AlertDialog mDialog;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            if(msg.what == MSG_GET_TOKEN_SUCCESS) {
                String message = (String)msg.obj;
                tokenEt.setText(message);
                save(KEY_TOKEN, message);
            }
            else if(msg.what == MSG_SHOW_RESULT) {
                String message = (String)msg.obj;
                showResultDialog(message);
            }

            return false;
        }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pay);
        initControl();
        initData();

    }


    private void initControl() {
        merchantIdEt = findViewById(R.id.merchantIdEt);
        orderIdEt = findViewById(R.id.orderIdEt);
        amountEt = findViewById(R.id.amountEt);
        currentcyEt = findViewById(R.id.currentcyEt);
        callbackUrlEt = findViewById(R.id.callbackUrlEt);
        subjectEt = findViewById(R.id.subjectEt);
        userIdEt = findViewById(R.id.userIdEt);
        tokenEt = findViewById(R.id.tokenEt);
        secretKeyEt = findViewById(R.id.secretKeyEt);
        countryCodeEt = findViewById(R.id.countryEt);
        testUrlEt = findViewById(R.id.testUrlEt);

        descEt = findViewById(R.id.desEt);
        mLangSpinner = findViewById(R.id.langSpinner);
        discountAmountEt = findViewById(R.id.discountAmountEt);
        mailEt = findViewById(R.id.mailEt);
        phoneEt = findViewById(R.id.phoneEt);
        extraEt = findViewById(R.id.extraEt);

        dividerLayout = findViewById(R.id.dividerLayout);
        indicatorIv = findViewById(R.id.indicatorIv);
        optionalLayout = findViewById(R.id.optionalLinearLayout);
        mTimeoutInSeconds = findViewById(R.id.timeoutInSeconds);
        mPayResultTypeCheckBox = findViewById(R.id.payResultTypeCb);

        dividerLayout.setOnClickListener(this);

        mLangSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initData() {
        setTitle(getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME);

        initDefaultData();

        initEditText(merchantIdEt, KEY_MERCHANT_ID);
        initEditText(orderIdEt, KEY_ORDER_ID);
        initEditText(amountEt, KEY_AMOUNT);
        initEditText(currentcyEt, KEY_CURRENTCY);
        initEditText(callbackUrlEt, KEY_CALLBACK_URL);
        initEditText(subjectEt, KEY_SUBJECT);
        initEditText(userIdEt, KEY_USER_ID);
        initEditText(tokenEt, KEY_TOKEN);
        initEditText(secretKeyEt, KEY_SECRET_KEY);
        initEditText(countryCodeEt, KEY_COUNTRY_CODE);
        initEditText(testUrlEt, KEY_TEST_URL);

        initEditText(descEt, KEY_DESC);
        String language = getVal(KEY_LANGUAGE, "0");
        if(!TextUtils.isEmpty(language)) {
            mLangSpinner.setSelection(Integer.parseInt(language));
        }
        initEditText(discountAmountEt, KEY_DISCOUNT_AMOUNT);
        initEditText(mailEt, KEY_MAIL);
        initEditText(phoneEt, KEY_PHONE);
        initEditText(extraEt, KEY_EXTRA);
        initEditText(mTimeoutInSeconds, KEY_PAY_VALID_DURATION);
        String payResultType = getVal(KEY_PAY_RESULT_TYPE, SPMerchantParam.PAY_RESULT_TYPE_SDK);
        if(!TextUtils.isEmpty(payResultType) && payResultType.equals(SPMerchantParam.PAY_RESULT_TYPE_MERCHANT)) {
            mPayResultTypeCheckBox.setChecked(true);
        }

        getToken();
    }


    private void initDefaultData() {
        if(buildTypeMap.size() == 0) {
            for(SPBuildType type : SPBuildType.values())
                buildTypeMap.put(type.name(), type);
        }
        String merchantId = getVal(KEY_MERCHANT_ID, "-1");
        if(TextUtils.isEmpty(merchantId) || merchantId.equals("-1")) {
            save(KEY_BUILD_TYPE, DEFAULT_BUILD_TYPE);
            save(KEY_MERCHANT_ID, MERCHANT_ID);
            save(KEY_ORDER_ID, "2015032001010100091");
            save(KEY_AMOUNT, "100");
            save(KEY_CURRENTCY, "INR");
            save(KEY_CALLBACK_URL, "http://pay.ushareit.com/callback");
            save(KEY_SUBJECT, "test subject");
            save(KEY_USER_ID, "TESTID54722");
            save(KEY_MAIL, "endlesshb@gmail.com");
            save(KEY_PHONE, "13564320000");
            save(KEY_TOKEN, "test");
            save(KEY_SECRET_KEY, SECRET_KEY);
            save(KEY_COUNTRY_CODE, "IN");
            save(KEY_TEST_URL, DEFAULT_URL);
        }
    }

    public void onStartH5PayClick(View view) {
//        TestWebUtils.testHttps();
        Log.d(TAG, "onStartH5PayClick");
//        openH5("file:///android_asset/paysdk_hybrid.html");
        openH5(testUrlEt.getText().toString());
    }

    private void openH5(String url) {
        saveAll();

        SPMerchantParam merchantParam = null;
        try {
            SPMerchantParam.Builder builder = new SPMerchantParam.Builder()
                    .setMerchantId(merchantIdEt.getText().toString())
                    .setOrderId(orderIdEt.getText().toString())
                    .setAmount(amountEt.getText().toString())
                    .setCurrency(currentcyEt.getText().toString())
                    .setCallbackUrl(callbackUrlEt.getText().toString())
                    .setSubject(subjectEt.getText().toString())
                    .setCustId(userIdEt.getText().toString())
                    .setToken(tokenEt.getText().toString())
                    .setCountryCode(countryCodeEt.getText().toString())
                    .setDescription(descEt.getText().toString())
                    .setMail(mailEt.getText().toString())
                    .setMobileNo(phoneEt.getText().toString())
                    .setExtra(extraEt.getText().toString())
                    .setResultPageShowType(mPayResultTypeCheckBox.isChecked() ? SPMerchantParam.PAY_RESULT_TYPE_MERCHANT : SPMerchantParam.PAY_RESULT_TYPE_SDK);
            //
            if(!TextUtils.isEmpty(mTimeoutInSeconds.getText())) {
                builder.setTimeoutInSeconds(safeParseLong(mTimeoutInSeconds.getText().toString()));
            }
            if(mLangSpinner.getSelectedItemPosition() != 0) {
                builder.setLanguage((String)mLangSpinner.getSelectedItem());
            }

            merchantParam = builder.build();
        }
        catch (Exception e) {
            e.printStackTrace();
            showToast(this, e.getMessage(), Toast.LENGTH_SHORT);
            return;
        }

        SPPayCallback mPayCallback = new SPPayCallback() {
            @Override
            public void onResult(int code, String orderId, String message, String extra) {
                Log.d(TAG, "pay callback: [code=" + code + " orderId=" + orderId
                        + " message=" + message + " " + extra);
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("code", code);
                    jsonObject.put("message", message);
                    jsonObject.put("orderId", orderId);
                    jsonObject.put("extra", extra);
                    if(code == 10000)
                        Log.d(TAG, "Payment success");
                    else if(code == 10001)
                        Log.d(TAG, "Payment pending");
                    else
                        Log.d(TAG, "Payment failed");

                    Message msg = new Message();
                    msg.what = MSG_SHOW_RESULT;
                    msg.obj = jsonObject.toString();
                    mHandler.sendMessageDelayed(msg, 200);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        SPPayService payService = new SPPayService();
        if(!TextUtils.isEmpty(url))
            payService.startPayActivityByUrl(this, url, merchantParam, mPayCallback);
        else
            payService.startPayActivity(this, merchantParam, mPayCallback);
    }

    private void initEditText(EditText editText, String key) {
        String val = getVal(key, "");
        if(!TextUtils.isEmpty(val)) {
            editText.setText(val);
        }
    }

    private void saveAll() {
        saveEditText(merchantIdEt, KEY_MERCHANT_ID);
        saveEditText(orderIdEt, KEY_ORDER_ID);
        saveEditText(amountEt, KEY_AMOUNT);
        saveEditText(currentcyEt, KEY_CURRENTCY);
        saveEditText(callbackUrlEt, KEY_CALLBACK_URL);
        saveEditText(subjectEt, KEY_SUBJECT);
        saveEditText(userIdEt, KEY_USER_ID);
        saveEditText(tokenEt, KEY_TOKEN);
        saveEditText(secretKeyEt, KEY_SECRET_KEY);
        saveEditText(countryCodeEt, KEY_COUNTRY_CODE);
        saveEditText(testUrlEt, KEY_TEST_URL);

        saveEditText(descEt, KEY_DESC);
        saveEditText(discountAmountEt, KEY_DISCOUNT_AMOUNT);
        saveEditText(mailEt, KEY_MAIL);
        saveEditText(phoneEt, KEY_PHONE);
        saveEditText(extraEt, KEY_EXTRA);
        saveEditText(mTimeoutInSeconds, KEY_PAY_VALID_DURATION);

        save(KEY_LANGUAGE, mLangSpinner.getSelectedItemPosition()+"");
        save(KEY_PAY_RESULT_TYPE, mPayResultTypeCheckBox.isChecked() ? SPMerchantParam.PAY_RESULT_TYPE_MERCHANT : SPMerchantParam.PAY_RESULT_TYPE_SDK);
    }

    private void saveEditText(EditText editText, String key) {
        String val = editText.getText().toString();
        String storeVal = getVal(key, "!---!");
        if(storeVal.equals("!---!"))
            save(key, val);
        else if(!val.equals(storeVal))
            save(key, val);
    }

    private void save(String key, String val) {
        SharedPreferences sharedPref = getSharedPreferences(SHARE_PREF_NAME, 0);
        if(sharedPref != null) {
            sharedPref.edit().putString(key, val).commit();
        }

    }

    private String getVal(String key, String defaultVal) {
        SharedPreferences sharedPref = getSharedPreferences(SHARE_PREF_NAME, 0);
        if(sharedPref != null)
            return sharedPref.getString(key, defaultVal);
        return "";
    }

    private void remove(String key) {
        SharedPreferences sharedPref = getSharedPreferences(SHARE_PREF_NAME, 0);
        if(sharedPref != null)
            sharedPref.edit().remove(key).commit();
    }

    public void onGenerateOrderClick(View view) {
        orderIdEt.setText(new Date().getTime() + "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dividerLayout: {
                if(optionalLayout.getVisibility() == View.GONE) {
                    optionalLayout.setVisibility(View.VISIBLE);
                    indicatorIv.setImageResource(R.drawable.icon_up_arrow);
                }
                else {
                    optionalLayout.setVisibility(View.GONE);
                    indicatorIv.setImageResource(R.drawable.ic_down_arrow);
                }
                break;
            }
        }
    }

    public void onGenerateTokenClick(View view) {
        sendToken();
    }

    private void showResultDialog(String message) {
        String title = "Payment result";
        if(!isFinishing())
            mDialog = new AlertDialog.Builder(TestPayActivity.this)
                    .setTitle(title)
                    .setMessage(message)
                    .setNegativeButton("Exit", null)
                    .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    private void getToken() {
        if(TextUtils.isEmpty(tokenEt.getText()))
            sendToken();
    }

    private void sendToken() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                sendTokenInner();
            }
        }).start();

    }

    private void sendTokenInner() {
        String url = getTokenUrl();

        String timestamp = new Date().getTime()/1000 + "";
        Map<String, String> params = new HashMap<>();
        params.put("bizType", "token");
        params.put("merchantId", merchantIdEt.getText().toString());
        params.put("timestamp", timestamp);
        params.put("version", "1.0");
        params.put("secretKey", secretKeyEt.getText().toString());
        try {
            HttpResponse response = HttpHelper.post(url, null, params, 15000, 15000);
            if(response != null && response.getStatusCode() == 200) {
                TokenBean tokenBean = new TokenBean();
                tokenBean.parseData(response.getContent());
                if(tokenBean != null) {
                    Message msg = new Message();
                    msg.what = MSG_GET_TOKEN_SUCCESS;
                    msg.obj = tokenBean.getData();
                    mHandler.sendMessage(msg);
                }
            }
            else {
                final String msg = response != null ? response.getStatusCode() + " " + response.getContent()
                        : "Unknow error";
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        showToast(TestPayActivity.this, msg, Toast.LENGTH_SHORT);
                    }
                });

            }
        } catch (final Exception e) {
            e.printStackTrace();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    showToast(TestPayActivity.this, "getToken failed. " + e.getMessage(), Toast.LENGTH_SHORT);
                }
            });
        }
    }

    private long safeParseLong(String val) {
        long result = 0;
        try {
            result = Long.parseLong(val);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private String getTokenUrl() {
        String testUrl = "https://pay-gate-uat.shareitpay.in/aggregate-pay-gate/api/gateway";
        String prodUrl = "https://pay-gate.shareitpay.in/aggregate-pay-gate/api/gateway";

        SHAREitEnv env = SHAREitAggregation.getEnv();
        String url = "";
        switch (env) {
            case Test:
                url = testUrl;
                break;
            case Prod:
                url = prodUrl;
                break;
        }
        return url;
    }

    private static void showToast(Context context, String msg, int duration) {
        Toast toast = Toast.makeText(context, null, duration);
        toast.setText(msg);
        toast.show();
    }
}
