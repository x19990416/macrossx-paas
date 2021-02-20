/*
 *  Copyright (c) 2020-2021 Guo Limin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.x19990416.mxpaas.application.admin.service.impl;

import com.github.x19990416.mxpaas.application.admin.domain.SysGenConfig;
import com.github.x19990416.mxpaas.application.admin.repository.SysGenConfigRepository;
import com.github.x19990416.mxpaas.application.admin.service.GenerateService;
import com.github.x19990416.mxpaas.application.admin.service.dto.GenConfigDto;
import com.github.x19990416.mxpaas.application.admin.service.dto.GenConfigMapper;
import com.github.x19990416.mxpaas.application.admin.service.dto.GenConfigQueryCriteria;
import com.github.x19990416.mxpaas.common.exception.EntityExistException;
import com.github.x19990416.mxpaas.common.exception.EntityNotFoundException;
import com.github.x19990416.mxpaas.common.vo.PageVo;
import com.github.x19990416.mxpaas.module.jpa.QueryHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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

  @Transactional
  public void createSysConfig(GenConfigDto resourceDto) {
    if (genConfigRepository.findByNameOrAbbr(resourceDto.getName(), resourceDto.getAbbr())
        != null) {
      throw new EntityExistException(SysGenConfig.class, "name", resourceDto.getName());
    }
    genConfigRepository.save(genConfigMapper.toEntity(resourceDto));
  }

  @Override
  public void updateSysConfig(GenConfigDto resourceDto) {
    SysGenConfig sysGenConfig =
        genConfigRepository
            .findById(resourceDto.getId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        resourceDto.getClass(), "id", String.valueOf(resourceDto.getId())));
    BeanUtils.copyProperties(resourceDto,sysGenConfig,"id","abbr");
    genConfigRepository.save(sysGenConfig);
  }
}
