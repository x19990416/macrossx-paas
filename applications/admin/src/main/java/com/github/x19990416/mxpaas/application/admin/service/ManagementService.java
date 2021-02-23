/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.service;

import com.github.x19990416.mxpaas.application.admin.domain.vo.GenerateVo;
import com.github.x19990416.mxpaas.application.admin.service.dto.*;
import com.github.x19990416.mxpaas.common.vo.PageVo;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

public interface ManagementService {

  public PageVo<ConfigDto> querySysConfig(ConfigQueryCriteria criteria, Pageable pageable);

  public void createSysConfig(ConfigDto resourceDto);

  public void updateSysConfig(ConfigDto resourceDto);

  public void deleteSysConfig(Set<Long> ids);

  public PageVo<ModuleDto> querySysModule(ModuleQueryCriteria criteria, Pageable pageable);

  public void createSysModule(ModuleDto resourceDto);

  public void updateSysModule(ModuleDto resourceDto);

  public void deleteSysModule(Set<Long> ids);

  public List<TableDto> buildTableTrees();
}
