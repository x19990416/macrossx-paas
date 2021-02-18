/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.service.impl;

import com.github.x19990416.mxpaas.application.admin.domain.SysGenConfig;
import com.github.x19990416.mxpaas.application.admin.repository.SysGenConfigRepository;
import com.github.x19990416.mxpaas.application.admin.service.GenerateService;
import com.github.x19990416.mxpaas.application.admin.service.dto.GenConfigDto;
import com.github.x19990416.mxpaas.application.admin.service.dto.GenConfigMapper;
import com.github.x19990416.mxpaas.application.admin.service.dto.GenConfigQueryCriteria;
import com.github.x19990416.mxpaas.common.exception.EntityExistException;
import com.github.x19990416.mxpaas.common.vo.PageVo;
import com.github.x19990416.mxpaas.module.jpa.QueryHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenerateServiceImpl implements GenerateService {
  private final SysGenConfigRepository genConfigRepository;
  private final GenConfigMapper genConfigMapper;

  public PageVo<GenConfigDto> querySysConfig(GenConfigQueryCriteria criteria, Pageable pageable) {
    Page<SysGenConfig> page =
        genConfigRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelper.getPredicate(root, criteria, criteriaBuilder),
            pageable);
    return new PageVo<GenConfigDto>()
        .setContents(page.map(genConfigMapper::toDto).getContent())
        .setTotal(page.getTotalElements())
        .setPage(pageable.getPageNumber())
        .setSize(pageable.getPageSize());
  }

	@Override
	public void createSysConfig(GenConfigDto resourceDto) {
		if (genConfigRepository.findByName(resourceDto.getName()) != null) {
			throw new EntityExistException(SysGenConfig.class, "name", resourceDto.getName());
		}
		genConfigRepository.save(genConfigMapper.toEntity(resourceDto));
	}
}
