package com.lawrence.fatalis.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * nio工具类
 */
public class NIOUtil {

    /**
     * nio读取文件到byte数据
     *
     * @param filepath
     * @return byte[]
     * @throws IOException
     */
    public static byte[] file2Byte(String filepath) throws IOException {

        // 检查文件路径
        File file = new File(filepath);
        if (!file.exists()) {
            throw new FileNotFoundException(filepath);
        }

        FileChannel channel = null;
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            channel = inputStream.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) channel.size());
            while ((channel.read(byteBuffer)) > 0) {}

            return byteBuffer.array();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * byte数组转String
     *
     * @param by
     * @return String
     * @throws UnsupportedEncodingException
     */
    public static String byte2String(byte[] by, String... charset) throws UnsupportedEncodingException {
        if (charset.length > 0) {

            return new String(by, charset[0]);
        } else {

            return new String(by, "UTF-8");
        }
    }

}
