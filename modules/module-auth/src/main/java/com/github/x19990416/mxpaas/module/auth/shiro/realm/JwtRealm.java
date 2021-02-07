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
package com.github.x19990416.mxpaas.module.auth.shiro.realm;

import com.github.x19990416.mxpaas.module.auth.domain.AuthUser;
import com.github.x19990416.mxpaas.module.auth.service.AuthRoleService;
import com.github.x19990416.mxpaas.module.auth.service.AuthUserService;
import com.github.x19990416.mxpaas.module.auth.shiro.token.JwtToken;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Objects;
import java.util.Set;

@RequiredArgsConstructor
public class JwtRealm extends AuthorizingRealm {
  private final AuthUserService authUserService;
  private final AuthRoleService authRoleService;

  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof JwtToken;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
      throws AuthenticationException {
    JwtToken jwtToken = (JwtToken) authcToken;
    if (jwtToken.getPrincipal() == null) {
      throw new AccountException("JWT token参数异常！");
    }
    // 从 JwtToken 中获取当前用户
    String username = jwtToken.getPrincipal().toString();
    // 查询数据库获取用户信息，此处使用 Map 来模拟数据库
    AuthUser user = authUserService.getUserByUsername(username);

    if (Objects.isNull(user)) {
      throw new UnknownAccountException("用户不存在！");
    }
    SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, username, getName());
    return info;
  }

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    // 获取当前用户
    AuthUser currentUser = (AuthUser) SecurityUtils.getSubject().getPrincipal();
    Set<String> roles = authRoleService.getUserRoleLevels(currentUser);
    // 查询数据库，获取用户的权限信息
    // Set<String> perms
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    info.setRoles(roles);
    return info;
  }
}
