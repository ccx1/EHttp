package com.android.ehttp.get;


import java.util.HashMap;

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
public interface GetRequest {

    GetRequest map2params(HashMap<String, String> objectObjectHashMap);
}
