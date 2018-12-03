package com.android.ehttp.post;

import java.io.File;
import java.util.Map;

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
public interface PostRequest {

    PostRequest map2Json(Map<String, String> params);

    PostRequest map2form(Map<String, String> params);

    PostRequest map2formPostFile(Map<String, String> params, String key, File file);

    PostRequest map2formPostFile(Map<String, String> params, File file);


}
