package com.min204.coseproject.user.handler;

import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.entity.UserPhoto;
import com.min204.coseproject.oauth.entity.OAuthUser;
import com.min204.coseproject.oauth.entity.OAuthUserPhoto;
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

    public OAuthUserPhoto parseOAuthFileInfo(MultipartFile multipartFile, OAuthUser oAuthUser) throws Exception {
        return saveFile(multipartFile, "oauth_user_images");
    }

    private <T> T saveFile(MultipartFile multipartFile, String baseDir) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        String currentDate = now.format(dateTimeFormatter);

        String absolutePath = new File("").getAbsolutePath() + File.separator + File.separator;

        String path = baseDir + File.separator + currentDate;
        File file = new File(path);

        if (!file.exists()) {
            boolean wasSuccessful = file.mkdirs();

            if (!wasSuccessful) {
                throw new Exception("Failed to create directory");
            }
        }

        String contentType = multipartFile.getContentType();

        if (ObjectUtils.isEmpty(contentType) || (!contentType.contains("image/jpeg") && !contentType.contains("image/png"))) {
            throw new Exception("Invalid image format. Only JPEG and PNG are supported.");
        }

        String newFileName = multipartFile.getOriginalFilename();

        if (baseDir.equals("user_images")) {
            UserPhoto photo = UserPhoto.builder()
                    .originFileName(multipartFile.getOriginalFilename())
                    .filePath(path + File.separator + newFileName)
                    .fileSize(multipartFile.getSize())
                    .build();

            Path path1 = Paths.get(absolutePath + path + File.separator + newFileName).toAbsolutePath();
            multipartFile.transferTo(path1);

            return (T) photo;
        } else if (baseDir.equals("oauth_user_images")) {
            OAuthUserPhoto photo = OAuthUserPhoto.builder()
                    .originFileName(multipartFile.getOriginalFilename())
                    .filePath(path + File.separator + newFileName)
                    .fileSize(multipartFile.getSize())
                    .build();

            Path path1 = Paths.get(absolutePath + path + File.separator + newFileName).toAbsolutePath();
            multipartFile.transferTo(path1);

            return (T) photo;
        } else {
            throw new Exception("Invalid base directory");
        }
    }
}