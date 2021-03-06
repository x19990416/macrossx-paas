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
package com.github.x19990416.paas.module.user.controller;

import com.github.x19990416.paas.module.user.domain.dto.UserDto;
import com.github.x19990416.paas.module.user.domain.dto.UserQueryCriteria;
import com.github.x19990416.paas.module.user.domain.vo.UserInfoVo;
import com.github.x19990416.paas.module.user.service.UserService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping("/query")
  public ResponseEntity<Object> query(UserQueryCriteria criteria, Pageable pageable) {
    return ResponseEntity.ok(userService.queryAll(criteria, pageable));
  }

  @PostMapping("/create")
  public ResponseEntity<Object> create(@Validated @RequestBody UserInfoVo userInfo) {
    userService.createUser(
        new UserDto()
            .setUsername(userInfo.getUsername())
            .setNickName(
                StringUtils.isEmpty(userInfo.getNickname())
                    ? UUID.randomUUID().toString()
                    : userInfo.getNickname()));
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @PostMapping("/delete")
  public ResponseEntity<Object> delete(@RequestBody Set<Long> ids) {
    userService.deleteUser(ids);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

}
