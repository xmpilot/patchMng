package com.patchmng.main.controller;

import com.patchmng.main.service.MainService;
import com.patchmng.utils.RestResult;
import com.patchmng.utils.StringUtilsEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/main")
public class MainController {

    @Autowired
    private MainService mainService;

    @RequestMapping("/parserFile")
    public RestResult paserFile(String srcPath, String descPath, String srcFileList){
        RestResult restResult = RestResult.create();
        try{
            mainService.paserFile(restResult, srcPath, descPath, srcFileList);
        }catch (Exception e){
            restResult.addError(e.toString());
            e.printStackTrace();
        }
        return restResult;
    }

    @RequestMapping("/clearDescPath")
    public RestResult clearDescPath(String descPath){
        RestResult restResult = RestResult.create();
        try{
            if (descPath.indexOf("发布") < 0){
                restResult.addError("清空脚本失败，原因是：为了安全期间，目的目录必须包含[发布]两字！");
                return restResult;
            }
            mainService.clearDescPath(restResult, descPath);
        }catch (Exception e){
            restResult.addError(e.toString());
            e.printStackTrace();
        }
        return restResult;
    }

    @RequestMapping("/copyPatchFiles")
    public RestResult copyPatchFiles(String srcPath, String patchFileList, String descPath){
        RestResult restResult = RestResult.create();
        try{
            mainService.copyPatchFiles(restResult, srcPath, patchFileList, descPath);
        }catch (Exception e){
            restResult.addError(e.toString());
            e.printStackTrace();
        }
        return restResult;
    }
}
