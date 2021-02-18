/**
 * create by Guo Limin on 2021/2/14.
 */
package com.github.x19990416.mxpaas.application.admin.service;

import com.github.x19990416.mxpaas.application.admin.service.dto.RoleDto;
import com.github.x19990416.mxpaas.application.admin.service.dto.RoleQueryCriteria;
import com.github.x19990416.mxpaas.common.vo.PageVo;
import org.springframework.data.domain.Pageable;


public interface RoleService {

	public PageVo<RoleDto> queryAll(RoleQueryCriteria criteria, Pageable pageable);
}
