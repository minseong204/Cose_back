package com.min204.coseproject.heart.service;

import com.min204.coseproject.constant.HeartType;
import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.content.repository.ContentRepository;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.heart.entity.Heart;
import com.min204.coseproject.heart.repository.HeartRepository;
import com.min204.coseproject.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final ContentRepository contentRepository;

    public Heart createHeart(User user, Content content) {
        Heart heart;
        int heartStatus = 0;

        if (isNotAlreadyHeart(user, content)) {
            heart = new Heart(user, content);
            heart.setHeartType(HeartType.ADD);
            heartStatus = 1;
        } else {
            heart = findVerifiedHeart(user, content);
            switch (heart.getHeartType()) {
                case ADD:
                    heart.setHeartType(HeartType.REMOVE);
                    heartStatus = -1;
                    break;
                case REMOVE:
                    heart.setHeartType(HeartType.ADD);
                    heartStatus = -1;
                    break;
                default:
            }
        }
        int heartCount = content.getHeartCount();
        heartCount += heartStatus;
        content.setHeartCount(heartCount);
        contentRepository.save(content);
        Heart SavedHeart = heartRepository.save(heart);
        return SavedHeart;
    }

    public Heart findVerifiedHeart(User user, Content content) {
        Optional<Heart> optionalHeart = heartRepository.findByUserAndContent(user, content);
        Heart heart = optionalHeart.orElseThrow(
                () -> new BusinessLogicException(ExceptionCode.HEART_NOT_FOUND)
        );
        return heart;
    }

    private boolean isNotAlreadyHeart(User user, Content content) {
        return heartRepository.findByUserAndContent(user, content).isEmpty();
    }
}
