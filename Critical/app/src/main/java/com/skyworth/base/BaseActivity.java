package com.skyworth.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;

import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviView;
import com.skyworth.utils.Constant;
import com.skyworth.utils.L;
import com.skyworth.utils.TTSController;
import com.skyworth.utils.ToastUtils;

import java.io.PrintWriter;
import java.util.List;


public class BaseActivity extends CheckPermissionActivity {

    private static final String tag = "BaseActivity";

    protected AMapNaviView mAMapNaviView;
    protected AMapNavi mAMapNavi;
    protected com.skyworth.utils.TTSController mTtsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        GlobalContext.addActivity(this);

        //实例化语音引擎
        mTtsManager = TTSController.getInstance(getApplicationContext());
        mTtsManager.init();

        //
        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(mTtsManager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();

        //        仅仅是停止你当前在说的这句话，一会到新的路口还是会再说的
        mTtsManager.stopSpeaking();
        //
        //        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
        //        mAMapNavi.stopNavi();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mAMapNaviView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        L.d(tag, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.d(tag, "onDestroy");
        GlobalContext.removeActivity(this);

        mAMapNaviView.onDestroy();
        //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
        mAMapNavi.stopNavi();
        mAMapNavi.destroy();

        mTtsManager.destroy();
        // TODO: 2017/9/13
        DebugApplication.getRefWatcher(this).watch(this);
    }

    @Override
    public void onBackPressed() {
//        L.i(tag,"onBackPressed ");
//        if (!handleBackPress()) {
//            L.i(tag,"onBackPressed exit");
//            super.onBackPressed();
//        }
        if (!handleBackPress()) {
            checkExit();
        }
    }
    private boolean mNeedExit = false;
    protected void checkExit() {
        if (mNeedExit) {
            super.onBackPressed();
        } else {
            ToastUtils.show("再按一次退出地图");
            mNeedExit = true;
            GlobalContext.postOnUIThread(new Runnable() {

                @Override
                public void run() {
                    mNeedExit = false;
                }
            }, Constant.SECOND_2);
        }
    }

    public boolean handleBackPress() {
        L.i(tag,"handleBackPress ");
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList == null) {
            L.i(tag,"handleBackPress frg null");
            return false;
        }
        L.i(tag,"handleBackPress frg list size: "+fragmentList.size());
        for (Fragment fragment : fragmentList) {
            if (isFragmentVisible(fragment)) {
                L.i(tag,"handleBackPress frg deal backpressed");
                return ((BaseFragment) fragment).onBackPressed();
            }
        }

        return false;
    }


    public boolean isFragmentVisible(Fragment fragment) {
        boolean isFragmentVisible =
                fragment != null && fragment.isVisible() && fragment.getUserVisibleHint();
        L.i(tag, "Fragment=" + fragment + ", isFragmentVisible=" + isFragmentVisible);
        return isFragmentVisible;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (handleKeyDown(keyCode, event)) {
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public boolean handleKeyDown(int keyCode, KeyEvent event) {
        List<Fragment> fragmentList = getSupportFragmentManager().getFragments();
        if (fragmentList == null) {
            return false;
        }

        for (Fragment fragment : fragmentList) {
            if (isFragmentVisible(fragment)) {
                return ((BaseFragment) fragment).onKeyDown(keyCode, event);
            }
        }

        return false;
    }

    public void dump() {
        Log.e(tag, "Activity state:");
        LogWriter logw = new LogWriter(tag);
        PrintWriter pw = new PrintWriter(logw);
        dump("  ", null, pw, new String[]{});
    }
}
