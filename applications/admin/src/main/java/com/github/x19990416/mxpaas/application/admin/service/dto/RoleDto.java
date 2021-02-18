/** create by Guo Limin on 2021/2/14. */
package com.github.x19990416.mxpaas.application.admin.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RoleDto {
  private Long id;
  private String name;
}
