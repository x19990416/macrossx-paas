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
package com.github.x19990416.mxpaas;

import com.github.x19990416.mxpaas.common.utils.SpringContextHolder;
import com.github.x19990416.mxpaas.module.auth.AnonymousAccess;
import com.github.x19990416.mxpaas.module.auth.domain.dto.UserPwdLogin;
import com.github.x19990416.mxpaas.module.auth.shiro.token.SysUserToken;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/*
 *  Copyright 2019-2020 Zheng Jie
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

/**
 * 开启审计功能 -> @EnableJpaAuditing
 *
 * @author Zheng Jie
 * @date 2018/11/15 9:20:19
 */
@EnableAsync
@RestController
@SpringBootApplication
@EnableTransactionManagement
@Slf4j
@ControllerAdvice
public class App {

  public static void main(String[] args) {
    org.springframework.web.servlet.HandlerMapping s;
    SpringApplication.run(App.class, args);
  }

  @Bean
  public SpringContextHolder springContextHolder() {
    return new SpringContextHolder();
  }

  public static class Tx {
    List<String> li = Lists.newArrayList();
  }

  /**
   * 访问首页提示
   *
   * @return /
   */
  @RequestMapping("/login")
  public String index(@Validated UserPwdLogin login) {
    Subject subject = SecurityUtils.getSubject();
    SysUserToken usernamePasswordToken = new SysUserToken(login.getUsername(), login.getPassword());
    subject.login(usernamePasswordToken);
    return "Backend service started successfully";
  }

  @ExceptionHandler
  @ResponseBody
  public Object handleException(HttpServletResponse response, Exception ex) {
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    System.out.println("程序异常：" + ex.toString());
    return ex.getMessage();
  }

}
