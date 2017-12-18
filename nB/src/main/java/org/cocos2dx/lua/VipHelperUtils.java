package org.cocos2dx.lua;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.maisi.video.obj.video.PlayLineEntity;
import com.maisi.video.obj.video.RecommendEntity;
import com.tencent.smtt.sdk.WebView;
import com.zuiai.nn.R;
import com.maisi.video.obj.WeiChatUserInfo;
import com.maisi.video.obj.video.UserInfoEntity;

import org.cocos2dx.lua.service.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by JIANG on 2017/9/16.
 */

public class VipHelperUtils {

    public static VipHelperUtils instance;

    public static String shareDescription = "我发现了一个可以破解全网VIP视频的APP，不用充钱享会员，满满黑科技⊙ω⊙。下载地址：http://39.108.151.95:8000/MyApp/apk/com.zuiai.nn.apk";
    public static String shareLink = "http://39.108.151.95:8000/MyApp/apk/nB-release.apk";

    //接口地址
    public static String api1 = "https://api.47ks.com/webcloud/?v=";
    public static String api2 = "http://hoooz.cn/v/?url=";
    public static String api3 = "http://p2.api.47ks.com/webcloud/?v=";
    public static String api4 = "http://000o.cc/jx/ty.php?url=";
    public static String api5 = "http://www.wmxz.wang/video.php?url=";
    public static String api6 = "http://www.ou522.cn/t2/1.php?url=";
    public static String api7 = "http://2gty.com/apiurl/yun.php?url=";
    public static String api8 = "http://api.pucms.com/index.php?url=";
    public static String api9 = "http://www.sfsft.com/video.php?url=";
    public static String api10 = "http://api.wlzhan.com/sudu/?url=";
    public static String apiYouKu = "http://api.baiyug.cn/vip/?url=";
    public static String noAdUrl = "http://at520.cn/atjx/jiexi.php?url=";
    private final Random random = new Random();

    private String[] apis = {api1, api2, api3, api4, api5, api6, api7, api8, api9, api10, apiYouKu, noAdUrl};
    /*
    *            腾讯                 土豆              芒果
    * pc         container_player     td-player         c-player-video
    * mobile       vip_player           player          video-area
    *
    * */
    private String[] ids = {"j-player", "vip_player", "m-video-player", "player", "td-player", "c-player-video"
            , "player-view", "", "", "j-player-layout", "ws_play relative", "player_section"
            , "playerbox", "player", "", "", "", ""};

    private String[] names = {"乐视视频", "腾讯视频", "爱奇艺", "优酷视频", "土豆视频", "芒果TV",
            "搜狐视频", "Ac弹幕网", "哔哩哔哩", "风行网", "华数视频", "1905电影",
            "PPTV", "优酷云C", "糖豆视频", "音悦台", "凤凰视频", "虎牙视频"
    };

    private String[] urls = {"http://vip.le.com/", "http://m.v.qq.com/", "http://vip.iqiyi.com/", "http://vip.youku.com/", "http://www.tudou.com/category", "http://www.mgtv.com/vip/",
            "https://film.sohu.com/", "http://www.acfun.tv/", "http://www.bilibili.com/", "http://www.fun.tv/", "http://www.wasu.cn/", "http://www.1905.com/",
            "http://www.pptv.com/", "http://vip.youku.com/", "http://tv.tangdou.com/", "http://www.yinyuetai.com/", "http://v.ifeng.com/", "http://v.huya.com/"
    };

    private int[] icons = {R.drawable.ic_letv, R.drawable.ic_tencent, R.drawable.ic_aqy, R.drawable.ic_youku, R.drawable.tudoulogo, R.drawable.hunantvlogo
            , R.drawable.sohulogo, R.drawable.acfun, R.drawable.bilibili, R.drawable.fengxing, R.drawable.wasulogo, R.drawable.oneninezerofivelogo
            , R.drawable.pptv, R.drawable.ykcloud, R.drawable.tangdoulogo, R.drawable.yinyuetailogo, R.drawable.ifenglogo, R.drawable.huya
    };

    private int[] homeIndexs = {1,2,3,0,5,12 };

    private int currentPosition = 0;
    private int currentApiIndex = apis.length - 1;
    private String currentApi = apis[currentApiIndex];
    private String currentPlayUrl = "";
    private boolean isWechatLogin = false;
    private boolean isValidVip = false;
    private WeiChatUserInfo userInfo;
    private UserInfoEntity vipUserInfo;
    private String loginResult;
    private List<PlayLineEntity> youkuUrls = new ArrayList<>();
    private List<PlayLineEntity> aqyUrls = new ArrayList<>();
    private List<PlayLineEntity> tencentUrls = new ArrayList<>();
    private List<PlayLineEntity> otherUrls = new ArrayList<>();
    private int youkuApiIndex;
    private int aqyApiIndex;
    private int tencentApiIndex;
    private int otherApiIndex;

    public static VipHelperUtils getInstance() {
        if (instance == null) {
            instance = new VipHelperUtils();
        }
        return instance;

    }

    public VipHelperUtils() {
        currentApi = apis[DataHelper.getIntergerSF(APPAplication.instance, "currentapi", apis.length - 1)];
    }

    public String getURLbyPositon() {
        return urls[currentPosition];
    }

    private String tips = "卡顿，播放不了，不清晰，试试右上角切换线路（自备八条线路）！！！";
    private String imgUrl = "file:///android_asset/player.png";
    private String imgAdUrl = "file:///android_asset/iv_ad.png";
    private String imgAdUrl2 = "file:///android_asset/iv_ad.png";

    public String changePlayURLbyPositon(Context context) {
        String temp = "console.log(\"-------------------------\");";
       /* if(currentPosition == 6 || currentPosition == 5 || currentPosition == 10 || currentPosition == 4 || currentPosition == 2) {
            String tempHeight = "400px";
            if(currentPosition == 6 || currentPosition == 2) {
                tempHeight = "203px";
            }
            temp = "javascript:" +
                    "\t\t\tvar  elementName=\"=====\";\n" +
                    "            function countTotalElement(node){\n" +
                    "                var total=0;\n" +
                    "                if(node.nodeType==1){\n" +
                    "                   total++;\n" +
                    "                   elementName=elementName+node.tagName+\"\\\\r\\\\n\";\n" +
                    "\t\t\t\t   if(node.className == '" + ids[currentPosition] + "') {\n" +
                    "console.log(\"找到对应的node------------------\");"+
                    "\t\t\t\t\t\tvar parent = node.parentNode;\n" +
                    "\t\t\t\t\t\tvar div = document.createElement('div');\n" +
                    "\t\t\t\t\t\tdiv.innerHtml = node.innerHtml;\n" +
                    "\t\t\t\t\t\tdiv.id = node.id;\n" +
                    "div.style.cssText =\"\t\tbackground-repeat: no-repeat; background-position: center; background-size: cover; background-image: url(http://oufaforwi.bkt.clouddn.com/vs.png);\"\n"+
                    "\t\t\t\t\t\tdiv.style.width = '100%';\n" +
                    "\t\t\t\t\t\tdiv.style.height = '"+ tempHeight +"';\n" +
                    "\t\t\t\t\t\tdiv.onclick = function (){\n" +
                    "\t\t\t\t\t\t\tconsole.log(\"jjskdfsdfdsjfkdsfj\");\n" +
                    "\t\t\t\t\t\t\twindow.jsHook.goToPlayPage(window.location.href)\n" +
                    "\t\t\t\t\t\t}\n" +
                    "\t\t\t\t\t\tparent.replaceChild(div, node);\n" +
                    "\t\t\t\t   }\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\tvar childrens=node.childNodes;\n" +
                    "\t\t\t\tfor(var i=0;i<childrens.length;i++){\n" +
                    "\t\t\t\t\ttotal+=countTotalElement(childrens[i]);\n" +
                    "\t\t\t\t} \n" +
                    "\t\t\t\treturn total;\n" +
                    "\t\t\t}\n" +
                    "\t\t\tcountTotalElement(document);"
            ;
        }else {
            String tempheight = "203px";
            if(currentPosition == 3) {
                tempheight = "400px";
            }
            temp = "javascript:" +
                    "var player = document.getElementById('"+ ids[currentPosition] + "');\n" +
                    "\t\tvar parent = player.parentNode;\n" +
                    "\t\t\t\t\t\tvar div = document.createElement('div');\n" +
                    "\t\t\t\t\t\tdiv.innerHtml = player.innerHtml;\n" +
                    "\t\t\t\t\t\tdiv.id = player.id;\n" +
                    "div.style.cssText =\"\t\tbackground-repeat: no-repeat; background-position: center; background-size: cover; background-image: url(http://oufaforwi.bkt.clouddn.com/vs.png);\"\n"+
                    "\t\tdiv.style.width = '100%';\n" +
                    "\t\tdiv.style.height = '" + tempheight + "';\n" +
                    "\t\tdiv.onclick = function (){\n" +
                    "\t\t\tconsole.log(\"jjskdfsdfdsjfkdsfj\");\n" +
                    "       window.jsHook.goToPlayPage(window.location.href)"+
                    "\t\t} \n" +
                    "\t\tparent.replaceChild(div, player); "
            ;
        }*/
        return temp;
    }

    /*public String changePlayURLbyPositon(Context context) {
        String temp;
        if(currentPosition == 6 || currentPosition == 5 || currentPosition == 10 || currentPosition == 2 || currentPosition == 4) {
            temp = "javascript:" +
                    "\t\t\tvar  elementName=\"=====\";\n" +
                    "            function countTotalElement(node){\n" +
                    "                var total=0;\n" +
                    "                if(node.nodeType==1){\n" +
                    "                   total++;\n" +
                    "                   elementName=elementName+node.tagName+\"\\\\r\\\\n\";\n" +
                    "\t\t\t\t   if(node.className == '" + ids[currentPosition] + "') {\n" +
                    "console.log(\"找到对应的node------------------\");"+
                    "\t\t\t\t\t\tvar parent = node.parentNode;\n" +
                    "\t\t\t\t\t\tvar div = document.createElement('div');\n" +
                    "\t\t\t\t\t\tdiv.innerHtml = node.innerHtml;\n" +
                    "\t\t\t\t\t\tdiv.id = node.id;\n" +
                    "div.style.cssText =\"\t\tbackground-repeat: no-repeat; background-position: center; background-size: cover; background-image: url(http://oufaforwi.bkt.clouddn.com/vs.png);\"\n"+
                    "\t\t\t\t\t\tdiv.style.width = '100%';\n" +
                    "\t\t\t\t\t\tdiv.style.height = '400px';\n" +
                    "\t\t\t\t\t\tdiv.onclick = function (){\n" +
                    "\t\t\t\t\t\t\tconsole.log(\"jjskdfsdfdsjfkdsfj\");\n" +
                    "\t\t\t\t\t\t\twindow.jsHook.goToPlayPage(window.location.href)\n" +
                    "\t\t\t\t\t\t}\n" +
                    "\t\t\t\t\t\tparent.replaceChild(div, node);\n" +
                    "\t\t\t\t   }\n" +
                    "\t\t\t\t}\n" +
                    "\t\t\t\tvar childrens=node.childNodes;\n" +
                    "\t\t\t\tfor(var i=0;i<childrens.length;i++){\n" +
                    "\t\t\t\t\ttotal+=countTotalElement(childrens[i]);\n" +
                    "\t\t\t\t} \n" +
                    "\t\t\t\treturn total;\n" +
                    "\t\t\t}\n" +
                    "\t\t\tcountTotalElement(document);"
            ;
        }else {
            temp = "javascript:" +
                    "var player = document.getElementById('"+ ids[currentPosition] + "');\n" +
                    "\t\tvar parent = player.parentNode;\n" +
                    "\t\t\t\t\t\tvar div = document.createElement('div');\n" +
                    "\t\t\t\t\t\tdiv.innerHtml = player.innerHtml;\n" +
                    "\t\t\t\t\t\tdiv.id = player.id;\n" +
                    "div.style.cssText =\"\t\tbackground-repeat: no-repeat; background-position: center; background-size: cover; background-image: url(http://oufaforwi.bkt.clouddn.com/vs.png);\"\n"+
                    "\t\tdiv.style.width = '100%';\n" +
                    "\t\tdiv.style.height = '203px';\n" +
                    "\t\tdiv.onclick = function (){\n" +
                    "\t\t\tconsole.log(\"jjskdfsdfdsjfkdsfj\");\n" +
                    "       window.jsHook.goToPlayPage(window.location.href)"+
                    "\t\t} \n" +
                    "\t\tparent.replaceChild(div, player); "
            ;
        }
        return temp;
    }*/

    public String getRealPlaySite() {
        String temp = "javascript:" +
                "\t\tvar player = document.getElementById('post');\n" +
                "\t\tvar input = document.getElementById('v');\n" +
                "\t\tinput.value = '" + currentPlayUrl + "';\n" +
                "\t\t\talert('" + tips + "');\n" +
                "\t\tplayer.onclick();";
        return temp;
    }

    //play_script
//屏蔽播放页面底部广告，用张图片
//    public String pingbiAD() {
//        String temp = "javascript:" +
//                "var div = document.createElement('img');\n" +
//                "\t\t\t\tdiv.src = \""+imgAdUrl+"\";\n" +
//                "\t\t\t\tdiv.setAttribute('style', 'width: 100%!important; height: 30%!important; z-index: 2147483647!important; position: fixed!important; bottom: 0px; left: 0px; ');\n" +
//                "\t\t\t\tdocument.body.appendChild(div);";
//        return temp;
//    }

    //屏蔽底部广告用黑色背景
    public String pingbiAD() {
        String temp = "javascript:" +
                "var div = document.createElement('div');\n" +
                "\t\t\t\tdiv.setAttribute('style', 'width: 100%!important; height: 30%!important; z-index: 2147483647!important; position: fixed!important; bottom: 0px; left: 0px; background: #000000;');\n" +
                "\t\t\t\tdocument.body.appendChild(div);" +
                "var div = document.createElement('div');\n" +
                "\t\t\t\tdiv.setAttribute('style', 'width: 100%!important; height: 30%!important; z-index: 2147483647!important; position: fixed!important; top: 0px; left: 0px; background: #000000;');\n" +
                "\t\t\t\tdocument.body.appendChild(div);";
        return temp;
    }

    private ArrayList<String> hasSelectedlist = new ArrayList();


    public void changePlayLine(WebView view) {

        //default
        currentApiIndex++;
        if (currentApiIndex >= apis.length)
            currentApiIndex = 0;
        currentApi = apis[currentApiIndex];

        //other
        if (otherUrls.size() > 0) {
            otherApiIndex++;
            if (otherApiIndex >= otherUrls.size())
                otherApiIndex = 0;
            currentApi = otherUrls.get(otherApiIndex).getValue1();
        }

        switch (currentPosition) {
            //tencet
            case 1:
                if (tencentUrls.size() > 0) {
                    tencentApiIndex++;
                    if (tencentApiIndex >= tencentUrls.size())
                        tencentApiIndex = 0;
                    currentApi = tencentUrls.get(tencentApiIndex).getValue1();
                }
                break;
            //aqy
            case 2:
                if (aqyUrls.size() > 0) {
                    aqyApiIndex++;
                    if (aqyApiIndex >= aqyUrls.size())
                        aqyApiIndex = 0;
                    currentApi = aqyUrls.get(aqyApiIndex).getValue1();
                }
                break;
            //youku
            case 3:
                if (youkuUrls.size() > 0) {
                    youkuApiIndex++;
                    if (youkuApiIndex >= youkuUrls.size())
                        youkuApiIndex = 0;
                    currentApi = youkuUrls.get(youkuApiIndex).getValue1();
                }
                break;
        }

        view.loadUrl("javascript:" +
                "$(\"#proj_Iframe\").attr(\"src\",\"" + currentApi + currentPlayUrl + "\");\t"
        );
/*        view.loadUrl("javascript:" +
                "var itta = document.getElementById(\"iframe\");\n" +
                "itta.src = '" +currentApi + currentPlayUrl +"'"
        );*/
    }

/*    public void changePlayLine(WebView view) {

        //default
        currentApiIndex++;
        if(currentApiIndex >= apis.length)
            currentApiIndex = 0;
        currentApi = apis[currentApiIndex];

        //other
        if(otherUrls.size() > 0 ) {
            otherApiIndex++;
            if(otherApiIndex >= otherUrls.size())
                otherApiIndex = 0;
            currentApi = otherUrls.get(otherApiIndex).getValue1();
        }

        switch (currentPosition) {
            //tencet
            case 1:
                if(tencentUrls.size() > 0) {
                    tencentApiIndex++;
                    if(tencentApiIndex >= tencentUrls.size())
                        tencentApiIndex = 0;
                    currentApi = tencentUrls.get(tencentApiIndex).getValue1();
                }
                break;
            //aqy
            case 2:
                if(aqyUrls.size() > 0){
                    aqyApiIndex++;
                    if(aqyApiIndex >= aqyUrls.size())
                        aqyApiIndex = 0;
                    currentApi = aqyUrls.get(aqyApiIndex).getValue1();
                }
                break;
            //youku
            case 3:
                if(youkuUrls.size() > 0){
                    youkuApiIndex++;
                    if(youkuApiIndex >= youkuUrls.size())
                        youkuApiIndex = 0;
                    currentApi = youkuUrls.get(youkuApiIndex).getValue1();
                }
                break;
        }
//        DataHelper.setIntergerSF(APPAplication.instance, "currentapi", tempPosition);
        view.loadUrl(currentApi + currentPlayUrl);
        Log.i("changePlayLine", "changePlayLine-------------"+ currentApi + currentPlayUrl);
    }*/

    public void getNewPlayLine() {

        int tempPosition = random.nextInt(10);
        currentApi = apis[tempPosition];
    }

    public void changeTitle(WebView view) {

/*        String temp = "javascript:" +
                "document.title = 'VIP';";
        view.loadUrl(temp);*/
    }

    public void changeCurrentSite(int position) {
        currentPosition = position;
    }

    public String getDefaultApi() {
        if (otherUrls.size() > 0)
            currentApi = otherUrls.get(otherApiIndex).getValue1();
        switch (currentPosition) {
            case 1:
                if (tencentUrls.size() > 0)
                    currentApi = tencentUrls.get(tencentApiIndex).getValue1();
                break;
            case 2:
                if (aqyUrls.size() > 0)
                    currentApi = aqyUrls.get(aqyApiIndex).getValue1();
                break;
            case 3:
                if (youkuUrls.size() > 0)
                    currentApi = youkuUrls.get(youkuApiIndex).getValue1();
                break;
        }
        return currentApi;
    }

    public String getCurrentApi() {
        return currentApi;
    }


    public void updatePlayLineByServer() {
        Service.getComnonService().getPlayLine(15)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(
                                APPAplication.instance,
                                "错误:" + throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(new Subscriber<ArrayList<PlayLineEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                APPAplication.instance,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArrayList<PlayLineEntity> result) {
                        youkuUrls.clear();
                        youkuUrls.addAll(result);
                        Toast.makeText(
                                APPAplication.instance,
                                "优酷线路已更新",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        Service.getComnonService().getPlayLine(16)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(
                                APPAplication.instance,
                                "错误:" + throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(new Subscriber<ArrayList<PlayLineEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                APPAplication.instance,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArrayList<PlayLineEntity> result) {
                        aqyUrls.clear();
                        aqyUrls.addAll(result);
                        Toast.makeText(
                                APPAplication.instance,
                                "aqy线路已更新",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        Service.getComnonService().getPlayLine(17)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(
                                APPAplication.instance,
                                "错误:" + throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(new Subscriber<ArrayList<PlayLineEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                APPAplication.instance,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArrayList<PlayLineEntity> result) {
                        tencentUrls.clear();
                        tencentUrls.addAll(result);
                        Toast.makeText(
                                APPAplication.instance,
                                "tencent线路已更新",
                                Toast.LENGTH_SHORT).show();
                    }
                });

        Service.getComnonService().getPlayLine(22)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Toast.makeText(
                                APPAplication.instance,
                                "错误:" + throwable.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .subscribe(new Subscriber<ArrayList<PlayLineEntity>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(
                                APPAplication.instance,
                                e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ArrayList<PlayLineEntity> result) {
                        otherUrls.clear();
                        otherUrls.addAll(result);
                        Toast.makeText(
                                APPAplication.instance,
                                "other线路已更新",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String[] getApis() {
        return apis;
    }

    public String[] getIds() {
        return ids;
    }

    public String[] getNames() {
        return names;
    }

    public String[] getUrls() {
        return urls;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public int[] getIcons() {
        return icons;
    }

    public String getCurrentPlayUrl() {
        return currentPlayUrl;
    }

    public void setCurrentPlayUrl(String currentPlayUrl) {
        this.currentPlayUrl = currentPlayUrl;
    }

    public void setCurrentApi(String currentApi) {
        this.currentApi = currentApi;
    }

    public boolean isWechatLogin() {
        return isWechatLogin;
    }

    public void setWechatLogin(boolean wechatLogin) {
        isWechatLogin = wechatLogin;
    }

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public WeiChatUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(WeiChatUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getLoginResult() {
        return loginResult;
    }

    public void setLoginResult(String loginResult) {
        this.loginResult = loginResult;
    }

    public boolean isValidVip() {
        return isValidVip;
    }

    public void setValidVip(boolean validVip) {
        isValidVip = validVip;
    }

    public UserInfoEntity getVipUserInfo() {
        return vipUserInfo;
    }

    public void setVipUserInfo(UserInfoEntity vipUserInfo) {
        this.vipUserInfo = vipUserInfo;
    }

    public List<PlayLineEntity> getYoukuUrls() {
        return youkuUrls;
    }

    public List<PlayLineEntity> getAqyUrls() {
        return aqyUrls;
    }

    public List<PlayLineEntity> getTencentUrls() {
        return tencentUrls;
    }

    public List<PlayLineEntity> getOtherUrls() {
        return otherUrls;
    }

    public int[] getHomeIndexs() {
        return homeIndexs;
    }
}
