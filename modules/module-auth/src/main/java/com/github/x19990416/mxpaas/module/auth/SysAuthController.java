/**
 * create by Guo Limin on 2021/2/6.
 */
package com.github.x19990416.mxpaas.module.auth;

import com.github.x19990416.mxpaas.module.auth.domain.dto.UserPwdLogin;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class SysAuthController {
    @PostMapping("/login/pwd")
    public void userPwdLogin(@Validated UserPwdLogin login){

    }

    @RequiresGuest
    public void getCode(){

    }
}
