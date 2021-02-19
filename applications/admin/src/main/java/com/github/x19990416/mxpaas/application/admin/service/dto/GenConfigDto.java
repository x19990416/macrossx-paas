/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.service.dto;

import com.github.x19990416.mxpaas.application.admin.domain.SysGenModule;
import com.github.x19990416.mxpaas.common.base.BaseDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.persistence.OneToMany;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class GenConfigDto extends BaseDto {
  private Long Id;
  private String name;
  private String description;
  private String abbr;
  private String dataSource;
  private Integer type;

}
