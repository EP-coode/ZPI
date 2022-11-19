package com.core.backend.controller;

import com.core.backend.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("blob")
public class BlobController {

    @Autowired
    private FileService fileService;

    @GetMapping("/download")
    public ResponseEntity<Object> readBlobFile(@RequestParam("fileName") String fileName){
        try {
            byte[] file = fileService.downloadFile(fileName);
            return new ResponseEntity<>(file, HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Object> deleteFile(@RequestParam("fileName") String fileName){
        if(fileService.deleteFile(fileName))
            return new ResponseEntity<>("plik usunięty", HttpStatus.OK);
        return new ResponseEntity<>("plik nie istnieje", HttpStatus.BAD_REQUEST);
    }

   @PostMapping(path = "/upload")
   public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file){
        try {
            if (fileService.uploadFile(file, null))
                return new ResponseEntity<>("plik zapisany", HttpStatus.OK);
            return new ResponseEntity<>("nie udało się zapisać pliku", HttpStatus.BAD_REQUEST);
        }catch(Exception e){
            return new ResponseEntity<>("coś poszło nie tak: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
   }
}
