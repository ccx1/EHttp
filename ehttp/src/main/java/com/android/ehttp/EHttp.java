package com.android.ehttp;

/**
 * ================================================
 *
 * @author ：ccx
 * 版    本：1.0
 * 创建日期：2018/11/30
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class EHttp {

    public Call newCall(ERequest eRequest) {
        // 管理池
        return RealCall.newRealCall(eRequest);
    }
}
