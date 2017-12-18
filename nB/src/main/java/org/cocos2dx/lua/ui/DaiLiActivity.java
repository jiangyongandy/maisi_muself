package org.cocos2dx.lua.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.maisi.video.obj.video.RecommendEntity;
import com.maisi.video.obj.video.UpdateEntity;
import com.maisi.video.obj.video.WechatTipsEntity;
import com.zuiai.nn.R;

import org.cocos2dx.lua.APPAplication;
import org.cocos2dx.lua.DownLoadUtil;
import org.cocos2dx.lua.VipHelperUtils;
import org.cocos2dx.lua.service.Service;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 功能
 * Created by Jiang on 2017/10/17.
 */

public class DaiLiActivity extends BaseActivity {

    @BindView(R.id.tv_recommend_no)
    TextView mTvRecommendNo;
    @BindView(R.id.tv_recommend_left)
    TextView mTvRecommendLeft;
    @BindView(R.id.tv_wechat_uid)
    TextView mTvWechatUid;
    @BindView(R.id.tv_wechat_tips)
    TextView mTvWechatTips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setContentView(R.layout.activity_daili);
        ButterKnife.bind(this);


        Service.getComnonService().getWechatTips()
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
                .subscribe(new Subscriber<WechatTipsEntity >() {
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
                    public void onNext(final WechatTipsEntity  result) {

                        mTvWechatTips.setText(result.getRemarkValue());
                    }
                });

        if (!VipHelperUtils.getInstance().isWechatLogin())
            return;
        mTvWechatUid.setText(VipHelperUtils.getInstance().getVipUserInfo().getUid());
        Service.getComnonService().getRecommendNo(VipHelperUtils.getInstance().getUserInfo().getUnionid())
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
                .subscribe(new Subscriber<RecommendEntity>() {
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
                    public void onNext(RecommendEntity result) {
                        if (result.getCommendNo() != null)
                            mTvRecommendNo.setText(result.getCommendNo());
//                        if (result.getCommendLeft() != null)
                        mTvRecommendLeft.setText(result.getCommendLeft() + "元");
                    }
                });

    }

}
