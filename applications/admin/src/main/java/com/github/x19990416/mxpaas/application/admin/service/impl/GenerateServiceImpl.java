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

import com.github.x19990416.mxpaas.application.admin.domain.Config;
import com.github.x19990416.mxpaas.application.admin.domain.Module;
import com.github.x19990416.mxpaas.application.admin.domain.vo.GenerateVo;
import com.github.x19990416.mxpaas.application.admin.repository.ConfigRepository;
import com.github.x19990416.mxpaas.application.admin.repository.ModuleRepository;
import com.github.x19990416.mxpaas.application.admin.service.GenerateService;
import com.github.x19990416.mxpaas.application.admin.service.dto.*;
import com.github.x19990416.mxpaas.application.admin.utils.ConvterUtil;
import com.github.x19990416.mxpaas.common.exception.EntityExistException;
import com.github.x19990416.mxpaas.common.exception.EntityNotFoundException;
import com.github.x19990416.mxpaas.common.vo.PageVo;
import com.github.x19990416.mxpaas.module.jpa.QueryHelper;
import com.github.x19990416.tools.constant.Template;
import com.github.x19990416.tools.constant.TemplateConfig;
import com.github.x19990416.tools.constant.TemplateEngine;
import com.github.x19990416.tools.constant.engine.TemplateFactory;
import com.github.x19990416.tools.constant.engine.freemarker.FreemarkerEngine;
import com.github.x19990416.tools.constant.engine.freemarker.FreemarkerTemplate;
import com.google.common.base.Charsets;
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
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateServiceImpl implements GenerateService {
  private final ConfigRepository genConfigRepository;
  private final ConfigMapper configMapper;
  private final ModuleRepository genModuleRepository;
  private final ModuleMapper genModuleMapper;
  @PersistenceContext private EntityManager em;

  public PageVo<ConfigDto> querySysConfig(ConfigQueryCriteria criteria, Pageable pageable) {
    Page<Config> page =
        genConfigRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelper.getPredicate(root, criteria, criteriaBuilder),
            pageable);
    return new PageVo<ConfigDto>()
        .setContents(page.map(configMapper::toDto).getContent())
        .setTotal(page.getTotalElements())
        .setPage(pageable.getPageNumber())
        .setSize(pageable.getPageSize());
  }

  @Transactional
  public void createSysConfig(ConfigDto resourceDto) {
    if (!genConfigRepository
        .findByNameOrAbbr(resourceDto.getName(), resourceDto.getAbbr())
        .isEmpty()) {
      throw new EntityExistException(Config.class, "name", resourceDto.getName());
    }
    genConfigRepository.save(configMapper.toEntity(resourceDto));
  }

  @Override
  @Transactional
  public void updateSysConfig(ConfigDto resourceDto) {
    Config sysGenConfig =
        genConfigRepository
            .findById(resourceDto.getId())
            .orElseThrow(
                () ->
                    new EntityNotFoundException(
                        resourceDto.getClass(), "id", String.valueOf(resourceDto.getId())));
    BeanUtils.copyProperties(configMapper.toEntity(resourceDto), sysGenConfig, "id", "abbr");
    genConfigRepository.save(sysGenConfig);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteSysConfig(Set<Long> ids) {
    genConfigRepository.deleteAllByIdIn(ids);
  }

  @Override
  public PageVo<ModuleDto> querySysModule(ModuleQueryCriteria criteria, Pageable pageable) {
    Page<Module> page =
        genModuleRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelper.getPredicate(root, criteria, criteriaBuilder),
            pageable);
    return ConvterUtil.toPageVo(page, genModuleMapper);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void createSysModule(ModuleDto resourceDto) {
    if (!genModuleRepository.findByName(resourceDto.getName()).isEmpty()) {
      throw new EntityExistException(Module.class, "name", resourceDto.getName());
    }
    genModuleRepository.save(genModuleMapper.toEntity(resourceDto));
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void updateSysModule(ModuleDto resourceDto) {
    Module module = genModuleRepository.findById(resourceDto.getId()).orElseThrow(()->new EntityNotFoundException(Module.class,"id",""));
    BeanUtils.copyProperties(resourceDto,module,"id","name","artifactId","groupId");
    genModuleRepository.save(module);
  }

  @Override
  public void deleteSysModule(Set<Long> ids) {
    genModuleRepository.deleteAllByIdIn(ids);
  }

  public List<TableDto> buildTableTrees() {
    Map<String, TableDto> tables = Maps.newConcurrentMap();
    List<?> results = em.createNativeQuery("show tables").getResultList();
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
                .setChildren(new ArrayList<>(List.of(new TableDto().setName(name)))));
      }
    }
    return Lists.newArrayList(tables.values());
  }

  public Boolean generate(GenerateVo vo) throws Exception {

    TemplateEngine engine =
        TemplateFactory.getTemplateEngine(
            FreemarkerEngine.class,
            new TemplateConfig("generate", Charsets.UTF_8, TemplateConfig.ResourceMode.CLASSPATH));
    Template template = engine.getInstance("gradle.ftl");
    template.render(Maps.newHashMap());

    return true;
  }

  public static void main(String...s) throws Exception {
    new GenerateServiceImpl(null,null,null,null).generate(null);
  }
}
