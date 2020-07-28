package com.xxty.demoffmpeg.controller;

import com.xxty.demoffmpeg.utils.ConvertM3U8Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * @Author: llun
 * @DateTime: 2020/7/28 12:10
 * @Description: TODO
 */
@Controller
@RequestMapping("/upload")
public class UploadController {
    //视频转换后的地址
    private static final String TMP_PATH = "C:\\Users\\Administrator\\Desktop\\xxty\\OAuth\\tmp";
    @Autowired
    private ConvertM3U8Util convertM3U8Util;

    @ResponseBody
    @PostMapping("/upload")
    public Result fileUpload(@RequestParam("uploadFile") MultipartFile file) throws  Exception {
        if (file.isEmpty()) {
            return Result.fail("文件不能为空");
        }
        try {
            File tmp = new File(TMP_PATH, file.getOriginalFilename());
            if (!tmp.getParentFile().exists()) {
                tmp.getParentFile().mkdirs();
            }
            String[] fileInfo = getFileInfo(tmp);
            File orRenameFile = createOrRenameFile(tmp, fileInfo[0], fileInfo[1]);
            if (tmp.renameTo(orRenameFile)) {
                file.transferTo(orRenameFile);
            }else {
                file.transferTo(tmp);
            }
            if(!convertM3U8Util.convertOss(TMP_PATH+'/', file.getOriginalFilename())){
                return Result.fail("上传失败");
            }
            return Result.suc("success");
        } catch (IOException e) {
            return Result.fail("文件不能为空");
        }

    }

    /**
     * 创建或重命名文件
     * ps：sss.jpg    sss(1).jpg
     * @param from
     * @param toPrefix
     * @param toSuffix
     * @return
     */
    public static File createOrRenameFile(File from, String toPrefix, String toSuffix) {
        File directory = from.getParentFile();
        if (!directory.exists()) {
            if (directory.mkdir()) {
                System.out.println("Created directory " + directory.getAbsolutePath());
            }
        }
        File newFile = new File(directory, toPrefix + toSuffix);
        for (int i = 1; newFile.exists() && i < Integer.MAX_VALUE; i++) {
            newFile = new File(directory, toPrefix + "(" + i + ")" + toSuffix);
        }
        if (!from.renameTo(newFile)) {
            System.out.println("Couldn't rename file to " + newFile.getAbsolutePath());
            return from;
        }
        return newFile;
    }

    /**
     * 获取File的   . 前后字串
     * @param from
     * @return
     */
    public static String[] getFileInfo(File from) {
        String fileName = from.getName();
        int index = fileName.lastIndexOf(".");
        String toPrefix = "";
        String toSuffix = "";
        if (index == -1) {
            toPrefix = fileName;
        } else {
            toPrefix = fileName.substring(0, index);
            toSuffix = fileName.substring(index, fileName.length());
        }
        return new String[]{toPrefix, toSuffix};
    }
}
