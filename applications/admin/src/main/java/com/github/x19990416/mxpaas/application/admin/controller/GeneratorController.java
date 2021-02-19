/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.controller;

import com.github.x19990416.mxpaas.application.admin.domain.vo.SysConfigVo;
import com.github.x19990416.mxpaas.application.admin.service.GenerateService;
import com.github.x19990416.mxpaas.application.admin.service.dto.GenConfigDto;
import com.github.x19990416.mxpaas.application.admin.service.dto.GenConfigQueryCriteria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
  public ResponseEntity<Object> createSysConfig(@Validated SysConfigVo sysConfigVo) {
    generateService.createSysConfig(
        new GenConfigDto()
            .setName(sysConfigVo.getName())
            .setDescription(sysConfigVo.getDescription())
            .setAbbr(sysConfigVo.getAbbr())
            .setDataSource(sysConfigVo.getDataSource())
            .setType(sysConfigVo.getType()));

    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
