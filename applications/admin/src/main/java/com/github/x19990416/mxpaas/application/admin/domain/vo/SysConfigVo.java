/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class SysConfigVo {
  private Long id;

  @NotEmpty(message = "系统名不能为空")
  private String name;

  private String description;

  @NotEmpty(message = "简称不能为空")
  @Pattern(regexp = "[A-z]+-?[A-z]+", message = "必须为英文字母")
  private String abbr;

  private Set<String> modules;

  private Integer type;
  private String dataSource;
}
