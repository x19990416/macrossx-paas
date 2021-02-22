/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.service.dto;

import com.github.x19990416.mxpaas.application.admin.domain.Config;
import com.github.x19990416.mxpaas.application.admin.domain.Module;
import com.github.x19990416.mxpaas.common.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ConfigMapper extends BaseMapper<ConfigDto, Config> {
  default Set<String> toModuleName(Set<Module> modules) {
    return modules.stream().map(Module::getName).collect(Collectors.toSet());
  }

  default Set<Module> toModule(Set<String> modules) {
    return modules.stream().map(name -> new Module().setName(name)).collect(Collectors.toSet());
  }
}
