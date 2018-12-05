package com.android.ehttp.body;

import java.io.File;

/**
 * ================================================
 *
 * @author ：ccx
 * 版    本：1.0
 * 创建日期：2018/12/5
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class Part {

    private String keyFile;
    private File   mFile;
    private String fileName;

    public Part(String keyFile, String fileName, File file) {
        this.keyFile = keyFile;
        this.fileName = fileName;
        this.mFile = file;
    }

    public Part(String keyFile, File file) {
        this(keyFile, file.getName(), file);
    }

    public String getKeyFile() {
        return keyFile;
    }

    public File getFile() {
        return mFile;
    }

    public String getFileName() {
        return fileName;
    }
}
