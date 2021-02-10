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
package com.github.x19990416.mxpaas.application.admin.controller;

import com.github.x19990416.mxpaas.application.admin.domain.vo.UserInfoVo;
import com.github.x19990416.mxpaas.application.admin.service.MenuService;
import com.github.x19990416.mxpaas.module.auth.domain.AuthRole;
import com.github.x19990416.mxpaas.module.auth.domain.AuthUser;
import com.github.x19990416.mxpaas.module.auth.service.AuthRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@Slf4j
@RequiredArgsConstructor
public class CurrentUserController {
  private final AuthRoleService authRoleService;
  private final MenuService menuService;

  @GetMapping("/info")
  public ResponseEntity<Object> info(String token) {
    AuthUser user = (AuthUser) SecurityUtils.getSubject().getPrincipal();
    log.info("{}",menuService.buildTree(menuService.findByUser(user.getId())));
    UserInfoVo userInfo =   new UserInfoVo()
            .setAvatar(user.getAvatarName())
            .setName(user.getNickName())
            .setRoles(user.getRoles().stream().map(AuthRole::getLevelName).collect(Collectors.toSet()))
            .setMenus(menuService.buildMenu(menuService.buildTree(menuService.findByUser(user.getId()))));
            log.info("{}",userInfo);
    return ResponseEntity.ok(userInfo
      );
  }
}
