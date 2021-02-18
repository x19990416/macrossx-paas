/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.service;

import com.github.x19990416.mxpaas.application.admin.service.dto.GenConfigDto;
import com.github.x19990416.mxpaas.application.admin.service.dto.GenConfigQueryCriteria;
import com.github.x19990416.mxpaas.common.vo.PageVo;
import org.springframework.data.domain.Pageable;

public interface GenerateService {

  public PageVo<GenConfigDto> querySysConfig(GenConfigQueryCriteria criteria, Pageable pageable);

  public void createSysConfig(GenConfigDto resourceDto);

}
