package com.meepleconnect.boardgamesapi.mappers;

import com.meepleconnect.boardgamesapi.entities.User;
import com.meepleconnect.boardgamesapi.models.UserModel;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = { RoleMapper.class })
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserModel fromEntity(User entity);

    User toEntity(UserModel model);
}