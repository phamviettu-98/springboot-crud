package com.pvt.crud.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import com.pvt.crud.dto.RoleDTO;
import com.pvt.crud.entity.Role;


@Mapper
public interface RoleMapper {
	public RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);

	public abstract RoleDTO toRoleDTO(Role role);
	
	public abstract List<RoleDTO> toRoleDTOs(List<Role> roles);
	
	public abstract Role toRole(RoleDTO roleDTO);
}
