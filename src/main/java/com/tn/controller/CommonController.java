package com.tn.controller;


import com.tn.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;
import java.util.logging.Filter;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basepath;

    /**
     * 文件上传
     * @param file 和传过来的name="file"引号里的值要保持一致
     * @return
     */
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        log.info(file.toString());
        //file是一个临时文件，需要转存到指定位置，否则本次请求完成后临时文件会删除

        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //得到后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //得到新文件名
        String filename = UUID.randomUUID().toString() + suffix;

        //创建一个目录对象
        File dir =new File(basepath);

        //如果没有这个目录就创建一个
        if(!dir.exists()){
            dir.mkdir();
        }

        //转存到文件夹
        file.transferTo(new File(basepath + filename));
        return R.success(filename);
    }

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response) throws IOException {
        //输入流，通过输入流读取文件内容
        FileInputStream fileInputStream=new FileInputStream(new File(basepath+name));

        //输出流，通过输出流将文件写回浏览器，在浏览器展示图片
        ServletOutputStream outputStream=response.getOutputStream();

        response.setContentType("image/jpeg");
        int len=0;
        byte[] bytes=new byte[1024];

        while((len = fileInputStream.read(bytes)) != -1){
            outputStream.write(bytes,0,len);
            outputStream.flush();
        }

        fileInputStream.close();
        outputStream.close();
    }
}
