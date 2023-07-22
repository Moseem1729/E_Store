package com.lcwd.electronic.store.services.impl;

import com.lcwd.electronic.store.config.AppConstants;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {


    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    @Override
    public String uploadImage(MultipartFile file, String path) throws IOException {

        logger.info("Entering uploadImage method");
        //file name
        String originalFilename = file.getOriginalFilename();
        logger.info("originalFilename: {}", originalFilename);

        //file path
        String fileName = UUID.randomUUID().toString();
        logger.info("Randomly generated fileName: {}", fileName);

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileNameWithExtension = fileName + extension;
        logger.info("fileNameWithExtension: {}", fileNameWithExtension);

        String fullPathWithFileName = path+fileNameWithExtension;
        logger.info("fullPathWithFileName: {}", fullPathWithFileName);


        if (extension.equalsIgnoreCase(AppConstants.FILE_EXTENSION_PNG) || extension.equalsIgnoreCase(AppConstants.FILE_EXTENSION_JPG) || extension.equalsIgnoreCase(AppConstants.FILE_EXTENSION_JPEG)){

            //file save
            File f = new File(path);

            //create folder if not created
            if (!f.exists()){
                f.mkdirs();
            }

            //upload
            Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
            logger.info("Exiting uploadImage method");

            return  fileNameWithExtension;

        }else {
            logger.info("BadApiRequestException fileNameWithExtension: {}", fileNameWithExtension);
            logger.info("Exiting uploadImage method");


            throw new BadApiRequestException(AppConstants.EXTENSION_NOT_ALLOWED + extension);
        }

    }

    @Override
    public InputStream getResource(String path, String name) throws FileNotFoundException {
        logger.info("Entering getResource method");

        String fullPath = path+File.separator+name;
        logger.info("fullPath: {}", fullPath);

        InputStream inputStream = new FileInputStream(fullPath);
        logger.info("Exiting getResource method");

        return inputStream;
    }
}
