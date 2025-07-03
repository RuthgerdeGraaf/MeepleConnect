package com.meepleconnect.boardgamesapi.mappers;

import com.meepleconnect.boardgamesapi.entities.Role;
import com.meepleconnect.boardgamesapi.models.RoleModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "roleName", source = "roleName")
    @Mapping(target = "active", source = "active")
    RoleModel fromEntity(Role role);

    @Mapping(target = "description", source = "description")
    @Mapping(target = "roleName", source = "roleName")
    @Mapping(target = "active", source = "active")
    Role toEntity(RoleModel model);

    List<RoleModel> fromEntities(List<Role> entities);

    List<Role> toEntities(List<RoleModel> models);
}