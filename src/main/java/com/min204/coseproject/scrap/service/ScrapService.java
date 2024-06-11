package com.min204.coseproject.scrap.service;

import com.min204.coseproject.content.entity.Content;
import com.min204.coseproject.content.repository.ContentRepository;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.exception.ExceptionCode;
import com.min204.coseproject.scrap.dto.ScrapDto;
import com.min204.coseproject.scrap.entity.Scrap;
import com.min204.coseproject.scrap.mapper.ScrapMapper;
import com.min204.coseproject.scrap.repository.ScrapRepository;
import com.min204.coseproject.user.entity.User;
import com.min204.coseproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScrapService {
    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final ScrapMapper scrapMapper;

    @Transactional
    public void scrapContent(Long contentId) {
        User user = getCurrentUser();
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CONTENT_NOT_FOUND));

        if (scrapRepository.findByUserAndContent(user, content).isPresent()) {
            throw new BusinessLogicException(ExceptionCode.SCRAP_ALREADY_EXISTS);
        }

        Scrap scrap = new Scrap(user, content);
        scrapRepository.save(scrap);
    }

    @Transactional
    public void unscriptContent(Long contentId) {
        User user = getCurrentUser();
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.CONTENT_NOT_FOUND));

        Scrap scrap = scrapRepository.findByUserAndContent(user, content)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.SCRAP_NOT_FOUND));

        scrapRepository.delete(scrap);
    }

    public List<ScrapDto> getUserScraps() {
        User user = getCurrentUser();
        List<Scrap> scraps = scrapRepository.findByUser(user);
        return scraps.stream()
                .map(scrapMapper::scrapToScrapDto)
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        }
        throw new BusinessLogicException(ExceptionCode.UNAUTHORIZED);
    }
}