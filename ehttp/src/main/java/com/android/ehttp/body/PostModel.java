package com.android.ehttp.body;

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

    private byte[] buff;
    private int offset;
    private int length;

    public PostModel(byte[] buff, int offset, int length) {
        this.buff = buff;
        this.offset = offset;
        this.length = length;
    }

    public byte[] getBuff() {
        return buff;
    }

    public int getOffset() {
        return offset;
    }

    public int getLength() {
        return length;
    }

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
