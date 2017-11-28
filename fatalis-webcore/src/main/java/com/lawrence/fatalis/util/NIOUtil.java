package com.lawrence.fatalis.util;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * nio工具类
 */
public class NIOUtil {

    /**
     * nio读取文件到byte数组
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
     * nio读取文件到byte数据, 内存映射读取大文件效率高
     *
     * @param filepath
     * @return byte[]
     * @throws IOException
     */
    public static byte[] bigFile2Byte(String filepath) throws IOException {

        FileChannel channel = null;
        try {
            channel = new RandomAccessFile(filepath, "r").getChannel();
            MappedByteBuffer byteBuffer = channel.map(FileChannel.MapMode.READ_ONLY, 0,
                    channel.size()).load();
            byte[] result = new byte[(int) channel.size()];
            if (byteBuffer.remaining() > 0) {
                byteBuffer.get(result, 0, byteBuffer.remaining());
            }

            return result;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * nio将byte数组写到文件
     *
     * @param by
     * @throws IOException
     */
    public static void byte2File(byte[] by, String filepath) throws IOException {
        FileChannel channel = null;
        try {
            ByteBuffer byteBuffer = ByteBuffer.wrap(by);
            channel = new FileOutputStream(filepath).getChannel();
            channel.write(byteBuffer);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        } finally {
            try {
                channel.close();
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
