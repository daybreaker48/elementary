package com.mhd.stard.business;

import android.app.Activity;
import android.os.AsyncTask;

import com.mhd.stard.util.MHDLog;
import com.mhd.stard.util.Util;

// 예제 구현 설명을 위한 import 예제 ::: 다이나트레이스
//import com.compuware.apm.uem.mobile.android.CompuwareUEM;
//import com.compuware.apm.uem.mobile.android.UemAction;

/**
 * Created by MH.D on 2017-03-22.
 */

public class ExternalLibraryManager {

    private static final String TAG = ExternalLibraryManager.class.getName();
    /**
     * 사용하는 라이브러리들의 instance, init, drive, destory 등
     * ex) 다이나트레이스
     */
//    private UemAction mLoadingAction = null;
    /**
     * 라이브러리 일괄 초기화를 위한 asynctask
     * 앱 초기 구동시 비동기 처리해도 되는 것들.
     */
    private InitAsyncTask mInitTask = null;
    /**
     * calling activity
     */
    private Activity mActivity;
    /**
     * 필수적인 체크요소가 있다면 그 종류별로 생성 가능
     */
    private boolean mIsEssentialCheckNeeded;
    /**
     * 최초구동( or 초기화) 여부
     */
    private boolean mIsInitializeExternalLibraryCalled = false;
    /**
     * ExternalLibraryManager
     * needCheckEssential 값을 넘기며 생성시에 해당 요소를 필수적으로 체크할지에 대한 여부를 판단.
     * 이 클래스가 호출될때마다(모든 액티비티??)
     * 체크해야 할 요소가 여러가지라면 파라미터를 늘리거나 변경할 수 있다. hashmap 등과 같이.
     */
    public ExternalLibraryManager(Activity activity, boolean needCheckEssential) {
        MHDLog.d(TAG, "called ExternalLibraryManager()");

        mActivity = activity;
        mIsEssentialCheckNeeded = needCheckEssential;
    }
    /**
     * 외부 라이브러리모듈 초기화
     * 인터넷 연결이 가능해야 pass
     */
    public boolean initializeExternalLibrary() {
        MHDLog.d(TAG, "called initializeExternalLibrary()");

        if (Util.getInstance().isNetWorkEnabled(mActivity) == false) { return false; }
        if(mInitTask == null) {
            mInitTask = new InitAsyncTask();
            mInitTask.execute();
        }

        mIsInitializeExternalLibraryCalled = true;
        return true;
    }
    /**
     * 외부라이브러리 모듈 destroy action
     */
    public void destroyExternalLibrary() {
        MHDLog.d(TAG, "called destroyExternalLibrary()");

        // 별도의 destroy 처리를 해줘야 하는 모듈의 종료처리 호출
        // To Do
    }
    /**
     * 외부라이브러리 모듈 onCreate action
     */
    public void onActivityCreateProcess() {
        MHDLog.d(TAG, "called onActivityCreateProcess() >> "  + mActivity.getClass().getSimpleName());

        // Main thread 에서 해야하는 외부 라이브러리 초기화 / 호출 / start
        // To Do
    }
    /**
     * 외부라이브러리 모듈 destroy action
     */
    public void onActivityDestroyProcess() {
        MHDLog.d(TAG, "called onActivityDestroyProcess() >> " + mActivity.getClass().getSimpleName());

        if(mIsInitializeExternalLibraryCalled) {
            // 외부라이브러리 destroy
            // ex)다이나트레이스 destory
//            if (mLoadingAction != null) {
//                int ret = mLoadingAction.leaveAction();
//            }
        }

        if(mInitTask != null) {
            mInitTask.cancel(true);
        }
    }
    /**
     * 외부라이브러리 모듈 resume action
     */
    public void onActivityResumeProcess() {
        MHDLog.d(TAG, "called onActivityResumeProcess() >> " + mActivity.getClass().getSimpleName());

        if(mIsEssentialCheckNeeded) {
            // activity 내 꼭 수행해야 할 외부라이브러리 프로세스 호출
            // To Do
        }
    }
    /**
     * 외부라이브러리 모듈 pause action
     */
    public void onActivityPauseProcess() {
        MHDLog.d(TAG, "called onActivityPauseProcess() >> " + mActivity.getClass().getSimpleName());

        // To Do
    }
    /**
     * ex) Adobe Tagging initialize
     */
    private void initializeAdobeTagging() {
        MHDLog.d(TAG, "called initializeAdobeTagging()");

        //MSATaggingHelper.init(mActivity);
    }
    /**
     * ex) 다이나트레이스 initialize
     */
    private void initializeCompuwareUEM() {
        MHDLog.d(TAG, "called initializeCompuwareUEM()");

//        int ret = CompuwareUEM.startup(mActivity, MHDConstants.COMPUWARE_UEM_NM, "url", true, null);
//        mLoadingAction = CompuwareUEM.enterAction("MSA Loading");
    }
    /**
     * initialize Task
     * Main Thread 에서 해결해야하는걸 제외하곤 여기서 call 한다.
     */
    private class InitAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            initializeAdobeTagging();
            initializeCompuwareUEM();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            mInitTask = null;
        }

        @Override
        protected void onCancelled() {
            mInitTask = null;
        }
    }
}
