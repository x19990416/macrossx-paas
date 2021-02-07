/** create by Guo Limin on 2021/2/7. */
package com.github.x19990416.mxpaas.module.auth.domain.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@Accessors(chain = true)
public class AuthUserDto {
  private Set<String> roles;
  private String token;
}
