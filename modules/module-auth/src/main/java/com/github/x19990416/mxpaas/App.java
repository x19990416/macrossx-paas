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
import com.github.x19990416.mxpaas.module.auth.annotation.AnonymousGetMapping;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
@EnableAsync
@SpringBootApplication
@Getter
@RestController
@RequestMapping("/xbbb/a")
public class App {

  public static void main(String... s) {
    SpringApplication.run(App.class);
  }


  @Bean
  public SpringContextHolder springContextHolder() {
    return new SpringContextHolder();
  }
  @AnonymousGetMapping(value = "/downloadaaa")
  public void download(HttpServletResponse response) throws Exception {
    System.out.println("123");
  }
}
