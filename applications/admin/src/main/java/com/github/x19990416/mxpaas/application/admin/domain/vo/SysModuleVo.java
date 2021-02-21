/** create by Guo Limin on 2021/2/20. */
package com.github.x19990416.mxpaas.application.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class SysModuleVo {
  private Long id;
  @NotEmpty private String name;
  @NotEmpty private String groupId;
  @NotEmpty private String version;
  private String description;
  @NotEmpty private String artifactId;
  private Set<String> tables;
}
