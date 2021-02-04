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
package com.github.x19990416.mxpaas.module.auth;

import com.github.x19990416.mxpaas.common.utils.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ShiroConfig {
  private final SpringContextHolder springContextHolder;

  @Bean
  public SecurityManager securityManager() {
    DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
    securityManager.setAuthenticator(sysUserModularRealmAuthenticator());
    //RequestMappingHandlerMapping
    return securityManager;
  }

  @Bean("SysUserModularRealmAuthenticator")
  public SysUserModularRealmAuthenticator sysUserModularRealmAuthenticator() {
    SysUserModularRealmAuthenticator sysUserModularRealmAuthenticator =
        new SysUserModularRealmAuthenticator();
    // 设置realm判断条件
    sysUserModularRealmAuthenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());

    return sysUserModularRealmAuthenticator;
  }
}
