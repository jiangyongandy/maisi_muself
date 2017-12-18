package org.cocos2dx.lua.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.zuiai.nn.R;

import org.cocos2dx.lua.CommonConstant;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 功能
 * Created by Jiang on 2017/10/16.
 */

public class GuideFragment extends BaseFragment {

    @BindView(R.id.imageView)
    ImageView mImageView;

    @Override
    protected void initData() {
        ButterKnife.bind(this, mRootView);
        if (!CommonConstant.isRelease) {
            mImageView.setImageResource(R.drawable.iv_app_utils_guide1);
        }
    }

    @Override
    protected int getRootViewLayoutId() {
        return R.layout.fragment_guide1;
    }

}
