package com.min204.coseproject.user.mapper;

import com.min204.coseproject.user.entity.UserPhoto;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserPhotoMapper {
    public List<Map<String, Object>> toResponse(List<UserPhoto> userPhotos) throws IOException {
        List<Map<String, Object>> imageList = new ArrayList<>();
        for (UserPhoto userPhoto : userPhotos) {
            String absolutePath = new File("").getAbsolutePath() + File.separator;
            String path = userPhoto.getFilePath();
            byte[] imageByteArray = Files.readAllBytes(Path.of(absolutePath + path));

            String base64EncodedImage = Base64.encodeBase64String(imageByteArray);

            Map<String, Object> imageInfo = new HashMap<>();
            imageInfo.put("fileName", userPhoto.getOriginFileName());
            imageInfo.put("imageBytes", base64EncodedImage);
            imageList.add(imageInfo);
        }
        return imageList;
    }
}
