package com.min204.coseproject.user.handler;

import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
public class UserFileHandler {

    public UserPhoto parseFileInfo(MultipartFile multipartFile, User user) throws Exception {
        return saveFile(multipartFile, "user_images");
    }

    private UserPhoto saveFile(MultipartFile multipartFile, String baseDir) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String currentDate = now.format(dateTimeFormatter);

        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

        String path = baseDir + File.separator + currentDate;
        File file = new File(path);

        if (!file.exists()) {
            boolean wasSuccessful = file.mkdirs();

            if (!wasSuccessful) {
                throw new Exception("디렉터리를 생성하지 못했습니다.");
            }
        }

        String contentType = multipartFile.getContentType();

        if (ObjectUtils.isEmpty(contentType) || (!contentType.contains("image/jpeg") && !contentType.contains("image/png"))) {
            throw new Exception("잘못된 이미지 형식입니다. JPEG 및 PNG 만 지원됩니다.");
        }

        String newFileName = multipartFile.getOriginalFilename();

        UserPhoto photo = UserPhoto.builder()
                .originFileName(multipartFile.getOriginalFilename())
                .filePath(path + File.separator + newFileName)
                .fileSize(multipartFile.getSize())
                .build();

        Path path1 = Paths.get(absolutePath + path + File.separator + newFileName).toAbsolutePath();
        multipartFile.transferTo(path1);

        return photo;
    }
}