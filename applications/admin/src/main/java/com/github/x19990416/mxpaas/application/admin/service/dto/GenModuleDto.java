/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.service.dto;

import com.github.x19990416.mxpaas.common.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GenModuleDto extends BaseDto {
  private Long Id;
  private String name;
  private String version;
  private String groupId;
  private String description;
  private String artifactId;
  private Set<String> tables;
}
