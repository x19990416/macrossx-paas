/** create by Guo Limin on 2021/2/14. */
package com.github.x19990416.mxpaas.application.admin.service.impl;

import com.github.x19990416.mxpaas.application.admin.domain.Role;
import com.github.x19990416.mxpaas.application.admin.repository.RoleRepository;
import com.github.x19990416.mxpaas.application.admin.service.RoleService;
import com.github.x19990416.mxpaas.application.admin.service.dto.RoleDto;
import com.github.x19990416.mxpaas.application.admin.service.dto.RoleMapper;
import com.github.x19990416.mxpaas.application.admin.service.dto.RoleQueryCriteria;
import com.github.x19990416.mxpaas.common.vo.PageVo;
import com.github.x19990416.mxpaas.module.jpa.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "user")
@Slf4j
public class RoleServiceImpl implements RoleService {
  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  @Override
  public PageVo<RoleDto> queryAll(RoleQueryCriteria criteria, Pageable pageable) {
    Page<Role> page =
        roleRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelper.getPredicate(root, criteria, criteriaBuilder),
            pageable);
	  return new PageVo<RoleDto>()
			  .setContents(page.map(roleMapper::toDto).getContent())
			  .setTotal(page.getTotalElements())
			  .setPage(pageable.getPageNumber())
			  .setSize(pageable.getPageSize());
  }
}
