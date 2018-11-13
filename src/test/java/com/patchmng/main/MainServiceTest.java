package com.patchmng.main;

import com.patchmng.main.service.MainService;
import com.patchmng.utils.ConvertUtils;
import com.patchmng.utils.RestResult;
import com.patchmng.utils.StringBuilderEx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MainServiceTest {

    @Autowired
    private MainService mainService;

    @Test
    public void test() {
        RestResult restResult = RestResult.create();
        String srcPath = "C:\\git-source\\kiteserver\\02_web\\build\\libs\\exploded\\02_web-1.0.1-SNAPSHOT.war\\";
        String descPath = "C:\\发布\\kiteServer\\";
        String srcFileList = "inspec_org_report.html\ninspec_org_report.js";

        mainService.paserFile(restResult, srcPath, descPath, srcFileList);
        if (!restResult.isSuccess()){
            System.out.println(restResult.getMessage());
            return;
        }
        StringBuilderEx sbParserFiles = new StringBuilderEx();
        List<String> parserFileList = (List<String>) restResult.getData().get("parserFileList");
        for (String parserFile : parserFileList){
            sbParserFiles.appendRow(parserFile);
        }

        mainService.clearDescPath(restResult, descPath);
        System.out.println(restResult.getData().get("logs"));

        mainService.copyPatchFiles(restResult, srcPath, sbParserFiles.toString(), descPath);
        System.out.println(restResult.getData().get("logs"));

    }
}
