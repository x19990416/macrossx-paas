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
package com.github.x19990416.mxpaas.application.admin.controller;

import com.github.x19990416.mxpaas.application.admin.domain.vo.ConfigVo;
import com.github.x19990416.mxpaas.application.admin.domain.vo.GenerateVo;
import com.github.x19990416.mxpaas.application.admin.domain.vo.ModuleVo;
import com.github.x19990416.mxpaas.application.admin.service.ManagementService;
import com.github.x19990416.mxpaas.application.admin.service.dto.ConfigDto;
import com.github.x19990416.mxpaas.application.admin.service.dto.ConfigQueryCriteria;
import com.github.x19990416.mxpaas.application.admin.service.dto.ModuleDto;
import com.github.x19990416.mxpaas.application.admin.service.dto.ModuleQueryCriteria;
import com.github.x19990416.mxpaas.common.exception.EntityNotFoundException;
import com.github.x19990416.mxpaas.module.auth.AnonymousAccess;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.stream.Collectors;

@Tag(name = "管理：系统管理")
@RestController
@RequestMapping("/api/system")
@Slf4j
@RequiredArgsConstructor
public class ManagementController {
  private final ManagementService managementService;

  @Operation(method = "系统查询")
  @GetMapping("/config/query")
  public ResponseEntity<Object> sysConfigQuery(ConfigQueryCriteria criteria, Pageable pageable) {
    return ResponseEntity.ok(managementService.querySysConfig(criteria, pageable));
  }

  @AnonymousAccess
  @Operation(method = "系统生成")
  @GetMapping("/generate")
  public ResponseEntity<Object> generate(@Validated @RequestBody GenerateVo generateVo) {
    log.info("{}", generateVo);
   // generateService.generate(generateVo);
    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Operation(method = "新增系统")
  @PostMapping("/config/create")
  public ResponseEntity<Object> createSysConfig(@Validated @RequestBody ConfigVo sysConfigVo) {
    managementService.createSysConfig(toDto(sysConfigVo));

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Operation(method = "更新系统设置")
  @PostMapping("/config/update")
  public ResponseEntity<Object> updateSysConfig(@Validated @RequestBody ConfigVo sysConfigVo) {
    if (Objects.isNull(sysConfigVo.getId())) {
      throw new EntityNotFoundException(sysConfigVo.getClass(), "id", null);
    }
    managementService.updateSysConfig(toDto(sysConfigVo));

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Operation(method = "模块查询")
  @GetMapping("/module/query")
  public ResponseEntity<Object> moduleQuery(ModuleQueryCriteria criteria, Pageable pageable) {
    return ResponseEntity.ok(managementService.querySysModule(criteria, pageable));
  }

  @Operation(method = "新增模块")
  @PostMapping("/module/create")
  public ResponseEntity<Object> createSysModule(@Validated @RequestBody ModuleVo sysModuleVo) {
    log.info("{}", sysModuleVo);
    managementService.createSysModule(toDto(sysModuleVo));
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Operation(method = "更新模块")
  @PostMapping("/module/update")
  public ResponseEntity<Object> updateModule(@Validated @RequestBody ModuleVo sysModuleVo) {
    if (Objects.isNull(sysModuleVo.getId())) {
      throw new EntityNotFoundException(sysModuleVo.getClass(), "id", null);
    }
    managementService.updateSysModule(toDto(sysModuleVo));

    return new ResponseEntity<>(HttpStatus.OK);
  }

  @Operation(method = "获取所有数据库表")
  @GetMapping("/tables")
  public ResponseEntity<Object> tables() {
    return ResponseEntity.ok(managementService.buildTableTrees());
  }

  private static ConfigDto toDto(ConfigVo sysConfigVo) {
    ConfigDto dto = new ConfigDto();
    BeanUtils.copyProperties(sysConfigVo, dto);
    dto.setModules(
        sysConfigVo.getModules().stream()
            .map(
                e -> {
                  ModuleDto moduleDto = new ModuleDto();
                  moduleDto.setId(e);
                  return moduleDto;
                })
            .collect(Collectors.toList()));
    return dto;
  }

  private static ModuleDto toDto(ModuleVo sysModuleVo) {
    ModuleDto dto = new ModuleDto();
    BeanUtils.copyProperties(sysModuleVo, dto);
    if (!Objects.isNull(sysModuleVo.getTables())) {
      String tables = String.join(",", sysModuleVo.getTables());
      dto.setTables(tables);
    }
    return dto;
  }
}
