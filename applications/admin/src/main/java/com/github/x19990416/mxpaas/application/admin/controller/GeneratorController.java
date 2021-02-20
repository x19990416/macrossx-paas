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

import com.github.x19990416.mxpaas.application.admin.domain.vo.SysConfigVo;
import com.github.x19990416.mxpaas.application.admin.service.GenerateService;
import com.github.x19990416.mxpaas.application.admin.service.dto.GenConfigDto;
import com.github.x19990416.mxpaas.application.admin.service.dto.GenConfigQueryCriteria;
import com.github.x19990416.mxpaas.common.exception.EntityNotFoundException;
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

@Tag(name = "生成：系统管理")
@RestController
@RequestMapping("/api/system")
@Slf4j
@RequiredArgsConstructor
public class GeneratorController {
  private final GenerateService generateService;

  @GetMapping("/config/query")
  public ResponseEntity<Object> sysConfigQuery(GenConfigQueryCriteria criteria, Pageable pageable) {
    return ResponseEntity.ok(generateService.querySysConfig(criteria, pageable));
  }

  @Operation(method = "新增系统")
  @PostMapping("/config/create")
  public ResponseEntity<Object> createSysConfig(@Validated @RequestBody SysConfigVo sysConfigVo) {
    generateService.createSysConfig(toDto(sysConfigVo));

    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Operation(method = "更新系统设置")
  @PostMapping("/config/update")
  public ResponseEntity<Object> updateSysConfig(@Validated @RequestBody SysConfigVo sysConfigVo) {
    if (Objects.isNull(sysConfigVo.getId())) {
      throw new EntityNotFoundException(sysConfigVo.getClass(), "id", null);
    }
    generateService.updateSysConfig(toDto(sysConfigVo));

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private static GenConfigDto toDto(SysConfigVo sysConfigVo) {
    GenConfigDto dto = new GenConfigDto();
    BeanUtils.copyProperties(sysConfigVo, dto);
    return dto;
  }
}
