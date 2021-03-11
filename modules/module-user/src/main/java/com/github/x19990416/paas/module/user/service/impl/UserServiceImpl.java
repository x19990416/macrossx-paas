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
package com.github.x19990416.mxpaas.application.admin.service.impl;

import com.github.x19990416.mxpaas.application.admin.domain.Config;
import com.github.x19990416.mxpaas.application.admin.domain.User;
import com.github.x19990416.mxpaas.application.admin.repository.UserRepository;
import com.github.x19990416.mxpaas.application.admin.service.UserService;
import com.github.x19990416.mxpaas.application.admin.service.dto.UserDto;
import com.github.x19990416.mxpaas.application.admin.service.dto.UserMapper;
import com.github.x19990416.mxpaas.application.admin.service.dto.UserQueryCriteria;
import com.github.x19990416.mxpaas.common.exception.EntityExistException;
import com.github.x19990416.mxpaas.common.vo.PageVo;
import com.github.x19990416.mxpaas.module.jpa.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user")
@Slf4j
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public PageVo<UserDto> queryAll(UserQueryCriteria criteria, Pageable pageable) {
    Page<User> page =
        userRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelper.getPredicate(root, criteria, criteriaBuilder),
            pageable);
    return new PageVo<UserDto>()
        .setContents(page.map(userMapper::toDto).getContent())
        .setTotal(page.getTotalElements())
        .setPage(pageable.getPageNumber())
        .setSize(pageable.getPageSize());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void createUser(UserDto resourceDto) {
    log.info("{}", resourceDto);
    if (!Objects.isNull(userRepository.findByUsername(resourceDto.getUsername()))) {
      throw new EntityExistException(Config.class, "username", resourceDto.getUsername());
    }
    userRepository.save(userMapper.toEntity(resourceDto));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteUser(Set<Long> ids) {
    // TODO: 添加权限相关判定
    userRepository.deleteAllByIdIn(ids);
  }
}
