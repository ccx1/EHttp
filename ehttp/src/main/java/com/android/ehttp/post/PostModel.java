package com.android.ehttp.post;

import java.io.File;

/**
 * ================================================
 *
 * @author ：ccx
 * 版    本：1.0
 * 创建日期：2018/12/3
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class PostModel {

    private String name;
    private File   file;

    public PostModel(String name, File file) {
        this.name = name;
        this.file = file;
    }

    public boolean isEmpty() {
        return file == null;
    }

    public String getName() {
        return name;
    }

    public File getFile() {
        return file;
    }
}
