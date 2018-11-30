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
public class CallPool {

    private static CallPool sCallPool;

    public static CallPool getInstance() {
        if (sCallPool == null) {
            synchronized (CallPool.class) {
                if (sCallPool == null) {
                    sCallPool = new CallPool();
                }
            }
        }
        return sCallPool;
    }

    public EHttpAsync getOneAsync() {
        // 统一管理池
        return new EHttpAsync();
    }
}