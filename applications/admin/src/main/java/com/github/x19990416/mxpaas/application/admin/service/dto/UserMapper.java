/*
 *  Copyright (c) 2020-2021 Guo Limin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.x19990416.mxpaas.application.admin.service.dto;

import com.github.x19990416.mxpaas.application.admin.domain.Role;
import com.github.x19990416.mxpaas.application.admin.domain.User;
import com.github.x19990416.mxpaas.common.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper extends BaseMapper<UserDto, User> {
  @Mappings({@Mapping(source = "role.name", target = "roles")})
  default Set<String> toRoleName(Set<Role> roles) {
    if (roles == null) {
      return null;
    }
    return roles.stream().map(Role::getLevelName).collect(Collectors.toSet());
  }

  default Set<Role> toRole(Set<String> roleName) {
    if (roleName == null) {
      return null;
    }
    return roleName.stream().map(name -> new Role().setLevelName(name)).collect(Collectors.toSet());
  }
}
