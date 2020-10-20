### 2.3.0.0

2020-09-30

- 在顺序延迟和提前返显模型基础上，增加颗粒度并发模型
- 优化AdMob无底价并发逻辑
- 优化punish_coef,惩罚后时间如果大于超时时间则不进行惩罚机制
- 推广球红点优化
- 小电视模块展示时机优化，移除接入方主动调用，使用sdk内部逻辑自动展示。
- 新增联网提醒sdk
- adshonor移除 mopub-base 的依赖（SDK内部改动）

### 注意，已经接入小电视功能的接入方，升级到此版本需要移除小电视展示的方法，sdk内部会自动调用展示。
调用小电视展示方法如下，如果此前代码里主动调用过，直接移除即可。

```
//原生方法：
SHAREitAggregation.showVideoDialog(showX, showY, scene, isMute);   
```

### 2.1.2.1

2020-08-31

BugFix:
- 修复 2.1.1.0 版本的bug

### [Deprecated]~~2.1.1.0~~
> Use 2.1.2.1

2020-08-11

FEATURE:
- 增加[小视频推广位](https://github.com/sunitsdk/SUnitDemo/wiki/%E6%8E%A8%E5%B9%BF%E6%A8%A1%E5%9D%97), 由CP自主调用
- 新增对外暴露的云控接口, 调用方可以通过 SHAREitAggregation#getXXConfig 来获取对应的云控值, 云控值的key-value需要由我方运营配置
- 优化了数据埋点

### 2.0.1.0

2020-07-28

FEATURE:
- 升级支付SDK版本
- 优化广告加载模型, 提升广告加载效率
- 优化内置下载器逻辑, 有效提升分发效率

CHANGES:
1. 需关注 [NoClassDefFoundError com.ushareit.aggregationsdk.InitializeProvider](https://github.com/sunitsdk/SUnitDemo/wiki/SDK%E5%BC%95%E5%85%A5#11-%E5%A2%9E%E5%8A%A0-multidexkeep-%E9%85%8D%E7%BD%AE)

### 1.8.1.3

2020-06-19

FEATURE:
- 升级支付SDK版本.
- 升级 Facebook Audience Network SDK 版本到5.9.0

CHANGES:
1. 使用最新  [shareit_sdk.gradle](https://github.com/sunitsdk/SUnitDemo/blob/master_181/app/shareit_sdk.gradle)
2. gameLevelEnd 增加是否过关参数

### 1.6.2.1
2020-6-4

升级到1.6.2.1不需要特殊的步骤

新增[评分引导功能](https://github.com/sunitsdk/SUnitDemo/wiki/%E8%AF%84%E5%88%86%E5%BC%95%E5%AF%BC%EF%BC%88%E5%8F%AF%E9%80%89%E5%8A%9F%E8%83%BD%29)

### [重要]1.5.1.3

2020-05-12
1. 在第一个 Activity#onCreate 时主动申请sd卡权限(方法内含有Id初始化相关策略，即使接入方自己申请了权限也需要调用此方法)
    * 原生：SHAREitAggregation.requestStoragePermissions();
    * Unity：SHAREitSDK.SHAREitSDK.requestStoragePermissions();
2. 使用最新  [shareit_sdk.gradle](https://github.com/sunitsdk/SUnitDemo/blob/master_150/app/shareit_sdk.gradle) 文件
3. 在build.gradle中applicationId下方增加account_type、content_authority
```
    defaultConfig {
        applicationId  “com.xx.xx”
        //SHAREit begin
        resValue "string", "account_type", "${applicationId}.type"
        resValue "string", "content_authority", "${applicationId}.provider"
        multiDexEnabled true
        //SHAREit end
    }
```


### [里程碑版本]1.3.1.1

2020-04-20
1. 升级 androidx 版本
2. 升级广告源版本: admob facebook mopub applovin unityAds
3. 增加 admob 聚合 applovin, admob 聚合 unityAds

接入改动(替换 [shareit_sdk.gradle](https://github.com/sunitsdk/SUnitDemo/blob/8c0068eb2b/app/shareit_sdk.gradle) 文件):
1. 更改广告源版本为
```
    ext {
        VERSION_SUNIT = "1.3.1.1"

        VERSION_ADCOLONY_AD="4.1.0"
        VERSION_ADMOB_AD = "18.3.0"
        VERSION_ADMOB_M_FB = "5.6.1.0"
        VERSION_ADMOB_M_MOPUB = "5.10.0.0"
        VERSION_ADMOB_M_UNITYADS = "3.4.2.0"
        VERSION_ADMOB_M_APPLOVIN = "9.11.4.0"

        VERSION_APPLOVIN_AD = "9.11.4"
        VERSION_FACEBOOK_AD = "5.6.1"
        VERSION_FYBER_AD = "7.3.4"
        VERSION_IRONSOURCE_AD = "6.10.2"
        VERSION_UNITY_ADS_AD = "3.4.2"
        VERSION_VUNGLE_AD = "6.4.11"
        VERSION_MOPUB_AD = "5.10.0"
        VERSION_MOPUB_M_ADMOB = "18.3.0.3"
        VERSION_MOPUB_M_FB = "5.6.1.0"

        VERSION_PLAY_SERVICES_LOCATION = "16.0.0"
        VERSION_GSON = "2.8.1"
        VERSION_OKHTTP3 = "3.10.0"
        VERSION_FLURRY = "12.1.0"
    }
 ```

2. 新增admob 聚合 adapter依赖
```
    implementation "com.google.ads.mediation:facebook:$VERSION_ADMOB_M_FB"
    implementation("com.google.ads.mediation:mopub:$VERSION_ADMOB_M_MOPUB") {
        exclude group: 'com.mopub'
    }
    implementation "com.google.ads.mediation:unity:$VERSION_ADMOB_M_UNITYADS"
    implementation "com.google.ads.mediation:applovin:$VERSION_ADMOB_M_APPLOVIN"
    implementation "com.mopub.mediation:admob:$VERSION_MOPUB_M_ADMOB"
    implementation "com.mopub.mediation:facebookaudiencenetwork:$VERSION_MOPUB_M_FB"
```

    
### 1.2.6.0

2020-03-29

1. 修复一处埋点上报问题

### 1.2.5

2020-03-20

升级到1.2.5不需要特殊的步骤

### 1.2.4
2020-03-11
1. 更新 pay 模块, 修复bug
2. 修复 Applovin Banner 请求 API
3. 优化 Banner 广告逻辑, 内部优化
4. 增加悬浮球功能

### 1.2.3.6

2020-03-03

升级到1.2.3.6不需要特殊的步骤

### 1.2.2

2020-02-17

1. 优化 consumer proguard file

### 1.2.1.2
2020-02-14

1. 增加 'load_strategy' = 3, 顺序延迟提前返显

### 1.2.0.4

2020-02-12
1. 统一子sdk的版本号

### 1.2.0

2020-02-11

1. 更改为 maven 依赖方式
2. 针对 1.1.2.3 版本修复埋点bug

### 1.1.2.3

2020-01-19

1. 插屏广告isAdReady 增加方法说明
2. 激励视频广告 isAdReady 增加子入口参数和说明
3. 广告数据上报 showRewardedBadgeView 增加子入口参数和说明

### 1.1.1.3

2020-02-11

升级到1.1.1.3不需要特殊的步骤

### 1.1.1.1

2020-01-13

升级到1.1.1.1不需要特殊的步骤

### 1.1.0.1

2020-01-08

升级到1.1.0.1不需要特殊的步骤

### 1.0.9.2

2020-01-07

1. 预埋广告配置
2. 添加默认广告配置文件
3. 删除自动申请权限方式，提供申请权限方法

### 1.0.8.8

2019-12-31

1. 3.5修改初始化，读取配置文件自动初始化
2. 4.1此版本移除gameStart和gameEnd事件上报
3. 6.3 增加自动申请权限方式

### 1.0.8.4

2019-12-23

1. 3.3广告源的SDK Network依赖(Mopub)
2. 4.3.5 增加banner说明

### 1.0.4.0

2019-11-04

1. 接口参数说明
2. 注意事项里读写权限授权相关问题

### 1.0.1.0

2019-10-22

1. AppID必须要申请
2. 登录增加横屏适配
3. 对isAdReady进行说明
4. 对showAd进行说明
5. Fix bug

### 1.0.0.0

2019-09-10

1. 初始文档
