package com.craftelix.filestorage.mapper;

import com.craftelix.filestorage.dto.user.UserSignupDto;
import com.craftelix.filestorage.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "id", ignore = true)
    User toEntity(UserSignupDto userSignupDto);
}
