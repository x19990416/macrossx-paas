/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.service.dto;

import com.github.x19990416.mxpaas.application.admin.domain.SysGenConfig;
import com.github.x19990416.mxpaas.application.admin.domain.SysGenModule;
import com.github.x19990416.mxpaas.common.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenConfigMapper extends BaseMapper<GenConfigDto, SysGenConfig> {
  default Set<String> toModuleName(Set<SysGenModule> roles) {
    return roles.stream().map(SysGenModule::getName).collect(Collectors.toSet());
  }

  default Set<SysGenModule> toModule(Set<String> modules) {
    return modules.stream()
        .map(name -> new SysGenModule().setName(name))
        .collect(Collectors.toSet());
  }
}
