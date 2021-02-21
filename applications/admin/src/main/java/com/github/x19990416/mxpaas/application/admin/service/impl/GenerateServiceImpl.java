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
import com.github.x19990416.mxpaas.application.admin.domain.SysGenModule;
import com.github.x19990416.mxpaas.application.admin.domain.SysModuleTable;
import com.github.x19990416.mxpaas.application.admin.repository.SysGenConfigRepository;
import com.github.x19990416.mxpaas.application.admin.repository.SysGenModuleRepository;
import com.github.x19990416.mxpaas.application.admin.repository.SysModuleTableRepository;
import com.github.x19990416.mxpaas.application.admin.service.GenerateService;
import com.github.x19990416.mxpaas.application.admin.service.dto.*;
import com.github.x19990416.mxpaas.application.admin.utils.ConvterUtil;
import com.github.x19990416.mxpaas.common.exception.EntityExistException;
import com.github.x19990416.mxpaas.common.exception.EntityNotFoundException;
import com.github.x19990416.mxpaas.common.vo.PageVo;
import com.github.x19990416.mxpaas.module.jpa.QueryHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateServiceImpl implements GenerateService {
  private final SysGenConfigRepository genConfigRepository;
  private final GenConfigMapper genConfigMapper;
  private final SysGenModuleRepository genModuleRepository;
  private final GenModuleMapper genModuleMapper;
  private final SysModuleTableRepository sysModuleTableRepository;
  @PersistenceContext private EntityManager em;

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
    if (!genConfigRepository
        .findByNameOrAbbr(resourceDto.getName(), resourceDto.getAbbr())
        .isEmpty()) {
      throw new EntityExistException(SysGenConfig.class, "name", resourceDto.getName());
    }
    genConfigRepository.save(genConfigMapper.toEntity(resourceDto));
  }

  @Override
  @Transactional
  public void updateSysConfig(GenConfigDto resourceDto) {
    SysGenConfig sysGenConfig =
        genConfigRepository
            .findById(resourceDto.getId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        resourceDto.getClass(), "id", String.valueOf(resourceDto.getId())));
    BeanUtils.copyProperties(resourceDto, sysGenConfig, "id", "abbr");
    genConfigRepository.save(sysGenConfig);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteSysConfig(Set<Long> ids) {
    genConfigRepository.deleteAllByIdIn(ids);
  }

  @Override
  public PageVo<GenModuleDto> querySysModule(GenModuleQueryCriteria criteria, Pageable pageable) {
    Page<SysGenModule> page =
        genModuleRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelper.getPredicate(root, criteria, criteriaBuilder),
            pageable);
    return ConvterUtil.toPageVo(page, genModuleMapper);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void createSysModule(GenModuleDto resourceDto) {
    if (!genModuleRepository.findByName(resourceDto.getName()).isEmpty()) {
      throw new EntityExistException(SysGenModule.class, "name", resourceDto.getName());
    }
    genModuleRepository.save(genModuleMapper.toEntity(resourceDto));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void updateSysModule(GenModuleDto resourceDto) {
    log.info("{}", resourceDto);
    SysGenModule sysGenModule =
        genModuleRepository
            .findByIdAndNameAndGroupIdAndArtifactId(
                resourceDto.getId(),
                resourceDto.getName(),
                resourceDto.getGroupId(),
                resourceDto.getArtifactId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        resourceDto.getClass(), "id", String.valueOf(resourceDto.getId())));
    BeanUtils.copyProperties(resourceDto, sysGenModule, "id", "name");
    Set<SysModuleTable> toAddTables =
        resourceDto.getTables().stream()
            .filter(
                name -> {
                  for (SysModuleTable table : sysGenModule.getTables()) {
                    if (table.getTableName().equalsIgnoreCase(name)) {
                      return false;
                    }
                  }
                  return true;
                })
            .map(name -> new SysModuleTable().setTableName(name).setModuleId(sysGenModule.getId()))
            .collect(Collectors.toSet());
    Set<SysModuleTable> toDelete =
        sysGenModule.getTables().stream()
            .filter(table -> !resourceDto.getTables().contains(table.getTableName()))
            .collect(Collectors.toSet());
    if (!toAddTables.isEmpty()) {
      sysGenModule.getTables().addAll(toAddTables);
    }
    log.info("todelete ______>{}",toDelete);
    genModuleRepository.save(sysGenModule);
    if (!toDelete.isEmpty()) {
      log.info("execute delete{}",toDelete);
      System.out.println(sysModuleTableRepository.findById(7l));
      sysModuleTableRepository.deleteById(7l);
      //sysModuleTableRepository.deleteAllByIdIn(toDelete.stream().map(SysModuleTable::getId).collect(Collectors.toSet()));
    }
  }

  @Override
  public void deleteSysModule(Set<Long> ids) {
    genModuleRepository.deleteAllByIdIn(ids);
  }

  public List<TableDto> buildTableTrees() {
    Map<String, TableDto> tables = Maps.newConcurrentMap();
    List<Object> results = em.createNativeQuery("show tables").getResultList();
    for (Object o : results) {
      String name = String.valueOf(o);
      String prefix = name.split("_")[0];
      if (tables.containsKey(prefix)) {

        tables.get(prefix).getChildren().add(new TableDto().setName(name));
      } else {
        tables.put(
            prefix,
            new TableDto()
                .setName(prefix)
                .setChildren(new ArrayList<TableDto>(List.of(new TableDto().setName(name)))));
      }
    }
    return Lists.newArrayList(tables.values());
  }
}
