package com.min204.coseproject.place.service;
import com.min204.coseproject.constant.ErrorCode;
import com.min204.coseproject.course.mapper.CourseMapper;
import com.min204.coseproject.exception.BusinessLogicException;
import com.min204.coseproject.place.dto.PlaceDto;
import com.min204.coseproject.place.entity.Place;
import com.min204.coseproject.place.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final CourseMapper placeMapper;

    @Override
    @Transactional
    public PlaceDto updatePlaceContent(Long placeId, String content) {
        Place findPlace = findVerifiedPlace(placeId);
        findPlace.setContent(content);
        Place updatedPlace = placeRepository.save(findPlace);
        return placeMapper.placeToPlaceDto(updatedPlace);
    }

    private Place findVerifiedPlace(Long placeId) {
        return placeRepository.findById(placeId).orElseThrow(() ->
                new BusinessLogicException(ErrorCode.COURSE_NOT_FOUND));
    }
}
