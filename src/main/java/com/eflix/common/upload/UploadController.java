package com.eflix.common.upload;

import java.net.MalformedURLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class UploadController {

    @Value("${upload.path}")
    private String path;

    // 0708
    @GetMapping("/images/hr/emp/{filename}")
    public ResponseEntity<Resource> getMethodName(@PathVariable String filename) throws MalformedURLException {
        String fullPath = path + "/hr/emp/" + filename;
        Resource resource = new UrlResource("file:" + fullPath);
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resource);
    }

}
