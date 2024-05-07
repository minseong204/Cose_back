package com.min204.coseproject.user.handler;

import com.min204.coseproject.user.entity.UserPhoto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class UserFileHandler {
    public List<UserPhoto> parseFileInfo(
            List<MultipartFile> multipartFiles
    ) throws Exception {
        List<UserPhoto> fileList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(multipartFiles)) {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

            String current_date = now.format(dateTimeFormatter);

            String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

            String path = "user_images" + File.separator + current_date;
            File file = new File(path);

            if (!file.exists()) {
                boolean wasSuccessful = file.mkdirs();

                if (!wasSuccessful)
                    System.out.println("file : was not successful");
            }

            for (MultipartFile multipartFile : multipartFiles) {
                String contentType = multipartFile.getContentType();

                if (ObjectUtils.isEmpty(contentType)) {
                    break;
                } else {
                    if (contentType.contains("image/jpeg"))
                        log.info("jpeg");
                    else if (contentType.contains("image/png"))
                        log.info("png");
                    else
                        break;
                }

                String new_file_name = multipartFile.getOriginalFilename();

                UserPhoto photo = UserPhoto.builder()
                        .originFileName(multipartFile.getOriginalFilename())
                        .filePath(path + File.separator + new_file_name)
                        .fileSize(multipartFile.getSize())
                        .build();

                UserPhoto userPhoto = new UserPhoto(
                        photo.getOriginFileName(),
                        photo.getFilePath(),
                        photo.getFileSize()
                );

                fileList.add(userPhoto);

                Path path1 = Paths.get(absolutePath + path + File.separator + new_file_name).toAbsolutePath();
                multipartFile.transferTo(path1);

            }
        }
        return fileList;
    }
}
