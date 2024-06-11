package com.min204.coseproject.follow.mapper;

import com.min204.coseproject.follow.dto.FollowDto;
import com.min204.coseproject.follow.entity.Follow;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FollowMapper {

    @Mapping(source = "follower.userId", target = "followerId")
    @Mapping(source = "follower.email", target = "followerEmail")
    @Mapping(source = "follower.nickname", target = "followerNickname")
    @Mapping(source = "followee.userId", target = "followeeId")
    @Mapping(source = "followee.email", target = "followeeEmail")
    @Mapping(source = "followee.nickname", target = "followeeNickname")
    FollowDto followToFollowDto(Follow follow);
}