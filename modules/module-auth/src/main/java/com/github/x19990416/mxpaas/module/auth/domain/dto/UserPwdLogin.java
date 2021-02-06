/**
 * create by Guo Limin on 2021/2/6.
 */
package com.github.x19990416.mxpaas.module.auth.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserPwdLogin {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
    private String code;
}
