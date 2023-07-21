package com.bikkadit.electronic_store.service.impl;

import com.bikkadit.electronic_store.exception.BadApiRequestException;
import com.bikkadit.electronic_store.service.FileServiceI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileServiceI {

    private static Logger logger= LoggerFactory.getLogger(FileServiceImpl.class);
    @Override
    public String uploadFile(MultipartFile file, String path) throws IOException {

        String originalFilename = file.getOriginalFilename();
        logger.info("filename :{} ",originalFilename);
        String filename = UUID.randomUUID().toString();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension= filename + extension;
        String fullPathWithFileName= path +  fileNameWithExtension;
          logger.info("full image path: {} ",fullPathWithFileName);

        if(extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")){
            //save file

            logger.info("file extension :{}",extension);
            File folder=new File(path);

            if(!folder.exists()){

                //create folder
                folder.mkdirs();
            }

            //upload file

            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));

            return  fileNameWithExtension;
        }else {
            throw new BadApiRequestException("File with this "+ extension + "   not allowed !!" );
        }


    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
       logger.info("Initiating call to download image:{}",name);
        String fullPath = path + File.separator + name;

        InputStream inputStream=new FileInputStream(fullPath);
        logger.info("completed call to download image:{}",name);
        return inputStream;
    }
}
