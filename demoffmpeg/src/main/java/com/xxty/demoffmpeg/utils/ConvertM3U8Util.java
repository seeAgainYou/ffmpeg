package com.xxty.demoffmpeg.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Author: llun
 * @DateTime: 2020/7/28 11:29
 * @Description: mp4转换m3u8工具类
 */

@Component
public class ConvertM3U8Util {
    // ffmpeg.exe的目录
    private String ffmpegpath = "C:\\Users\\Administrator\\Downloads\\ffmpeg\\bin/ffmpeg.exe";
    //每个ts的时间
    private String time = "10";
    //视频分辨率
    private String resolution = "1280x720";

    public boolean convertOss(String folderUrl,String fileName){
        if (!checkfile(folderUrl + fileName)){
            System.out.println("文件不存在!");
            return false;
        }
        //验证文件后缀
        String suffix = StringUtils.substringAfter(fileName, ".");
        String fileFullName = StringUtils.substringBefore(fileName, ".");
        if (!validFileType(suffix)){
            return false;
        }
        return processM3U8(folderUrl,fileName,fileFullName,resolution,time);
    }

    /**
     * 验证上传文件后缀
     * @param type
     * @return
     */
    private boolean validFileType ( String type ) {
        if ("mp4".equals(type)){
            return true;
        }
        return false;
    }

    /**
     * 验证是否是文件格式
     * @param path
     * @return
     */
    private boolean checkfile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            return false;
        } else {
            return true;
        }
    }

    // ffmpeg能解析的格式：（asx，asf，mpg，wmv，3gp，mp4，mov，avi，flv等）

    /**
     * ffmpeg程序转换m3u8
     * @param folderUrl
     * @param fileName
     * @param fileFullName
     * @return
     */
    private boolean processM3U8(String folderUrl,String fileName, String fileFullName) {
        //这里就写入执行语句就可以了
        List commend = new java.util.ArrayList();
        commend.add(ffmpegpath);
        commend.add("-i");
        commend.add(folderUrl+fileName);
        commend.add("-c:v");
        commend.add("libx264");
        commend.add("-hls_time");
        commend.add("20");
        commend.add("-hls_list_size");
        commend.add("0");
        commend.add("-c:a");
        commend.add("aac");
        commend.add("-strict");
        commend.add("-2");
        commend.add("-f");
        commend.add("hls");
        commend.add(folderUrl+ fileFullName +".m3u8");
        try {
            ProcessBuilder builder = new ProcessBuilder();//java
            builder.command(commend);
            Process p = builder.start();
            int i = doWaitFor(p);
            System.out.println("------>"+i);
            p.destroy();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     *
     * @param folderUrl
     * @param fileName
     * @param fileFullName
     * @param resolution 分辨率
     * @param time 每个ts时长
     * @return
     */
    private boolean processM3U8(String folderUrl,String fileName, String fileFullName,String resolution,String time){
        //这里就写入执行语句就可以了
        List commend = new java.util.ArrayList();
        commend.add(ffmpegpath);
        commend.add("-i");
        commend.add(folderUrl+fileName);
        commend.add("-profile:v");
        commend.add("baseline");
        commend.add("-level");
        commend.add("3.0");
        commend.add("-s");
        commend.add(resolution);
        commend.add("-start_number");
        commend.add("0");
        commend.add("-hls_time");
        commend.add(time);
        commend.add("-hls_list_size");
        commend.add("0");
        commend.add("-f");
        commend.add("hls");
        commend.add(folderUrl+ fileFullName +".m3u8");
        try {
            //java
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p = builder.start();
            int i = doWaitFor(p);
            System.out.println("------>"+i);
            p.destroy();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean processM3U8(String ffmpegpath,String folderUrl,String fileName, String fileFullName,String resolution,String time){
        //这里就写入执行语句就可以了
        List commend = new java.util.ArrayList();
        commend.add(ffmpegpath);
        commend.add("-i");
        commend.add(folderUrl+fileName);
        commend.add("-profile:v");
        commend.add("baseline");
        commend.add("-level");
        commend.add("3.0");
        commend.add("-s");
        commend.add(resolution);
        commend.add("-start_number");
        commend.add("0");
        commend.add("-hls_time");
        commend.add(time);
        commend.add("-hls_list_size");
        commend.add("0");
        commend.add("-f");
        commend.add("hls");
        commend.add(folderUrl+ fileFullName +".m3u8");
        try {
            //java
            ProcessBuilder builder = new ProcessBuilder();
            builder.command(commend);
            Process p = builder.start();
            int i = doWaitFor(p);
            System.out.println("------>"+i);
            p.destroy();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * 监听ffmpeg运行过程
     * @param p
     * @return
     */
    public int doWaitFor(Process p) {
        InputStream in = null;
        InputStream err = null;
        int exitValue = -1; // returned to caller when p is finished
        try {
            System.out.println("comeing");
            in = p.getInputStream();
            err = p.getErrorStream();
            boolean finished = false; // Set to true when p is finished

            while (!finished) {
                try {
                    while (in.available() > 0) {
                        Character c = new Character((char) in.read());
                        System.out.print(c);
                    }
                    while (err.available() > 0) {
                        Character c = new Character((char) err.read());
                        System.out.print(c);
                    }

                    exitValue = p.exitValue();
                    finished = true;

                } catch (IllegalThreadStateException e) {
                    Thread.currentThread().sleep(500);
                }
            }
        } catch (Exception e) {
            System.err.println("doWaitFor();: unexpected exception - "
                    + e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            if (err != null) {
                try {
                    err.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return exitValue;
    }

    public static void main(String[] args) {
//        ConvertM3U8Util util = new ConvertM3U8Util();
//        boolean b = util.processM3U8("C:\\Users\\Administrator\\Downloads\\ffmpeg\\bin/ffmpeg.exe",
//                "C:\\Users\\Administrator\\Desktop\\xxty\\OAuth", "zk.mp4",
//                "video-s", "1280x720", "10");
//        System.out.println(b);
    }

}

