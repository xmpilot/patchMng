package com.patchmng.main.service;

import com.patchmng.utils.FileHelper;
import com.patchmng.utils.RestResult;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Service
public class MainService {

    private boolean oneOfFile(String absFileName, String[] srcFileNames){
        for (String srcFileName : srcFileNames){
            if (absFileName.indexOf(srcFileName) >= 0){
                return true;
            }
        }
        return false;
    }

    public void paserFile(RestResult restResult, String srcPath, String descPath, String srcFileList) {
        File file = new File(srcPath);
        String[] srcFileNames = srcFileList.split("\n");
        List<String> fileList = FileHelper.listFileAbsPaths(file, "", "", true);
        List<String> parserFileList = new ArrayList<>();
        for (String fileItem : fileList){
            //去掉根目录
            fileItem = fileItem.replace(srcPath, "");
            if (oneOfFile(fileItem, srcFileNames)){
                parserFileList.add(fileItem);
            }
        }
        restResult.addData("parserFileList", parserFileList);

    }

    public void clearDescPath(RestResult restResult, String descPath) {
        List<String> logs = new ArrayList<>();
        FileHelper.deleteDir(descPath, logs);
        restResult.addData("logs", logs);
    }

    public void copyPatchFiles(RestResult restResult, String srcPath, String patchFileList, String descPath) {
        String[] patchFile = patchFileList.split("\n");
        List<String> logs = new ArrayList<>();
        for (String fileItem : patchFile){
            String absFileName = srcPath + fileItem;
            File file = new File(absFileName);
            if (!file.exists()){
                logs.add("不存在文件：" + fileItem);
                continue;
            }
            File destFile = new File(descPath + fileItem);
            if (destFile.exists()){
                //如果存在，那么删除文件
                destFile.delete();
            }
            if (!destFile.getParentFile().exists()){
                //如果不存在目录，那么创建目录
                destFile.getParentFile().mkdirs();
            }
            try {
                FileHelper.saveFile(file, descPath + fileItem);
            } catch (Exception e) {
                logs.add("复制文件[" + fileItem + "]出错：" + e.toString());
                e.printStackTrace();
            }
        }
        restResult.addData("logs", logs);
    }
}
