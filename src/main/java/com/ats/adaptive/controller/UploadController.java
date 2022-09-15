package com.ats.adaptive.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UploadController {

    //Save the uploaded file to this folder
    @Value("${kg.dir}")
    private String UPLOADED_FOLDER ;



    @PostMapping("/admin/importKG") // //new annotation since 4.3
    public String singleFileUpload(HttpSession session, @RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        String fileName = file.getOriginalFilename();
        if (file.isEmpty()||(!fileName.substring(fileName.lastIndexOf(".") + 1).equals("json"))) {
            redirectAttributes.addFlashAttribute("message", "需要选择一个json文件");
            return "redirect:uploadResult";
        }

        try {
            // Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path dir = Paths.get(UPLOADED_FOLDER);
            Path path = Paths.get(UPLOADED_FOLDER + "/"+session.getAttribute("generation")+"/"+file.getOriginalFilename());
            // Create parent dir if not exists
            if(!Files.exists(dir)) {
                Files.createDirectories(dir);
            }
            Files.write(path, bytes);
            redirectAttributes.addFlashAttribute("message",
                    "已成功上传实体集 '" + file.getOriginalFilename() + "'");

        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message", "Server throw IOException");
            e.printStackTrace();
        }
        return "redirect:/uploadResult";
    }

    @GetMapping("/uploadResult")
    public String uploadStatus() {
        return "uploadResult";
    }

}