/** create by Guo Limin on 2021/2/14. */
package com.github.x19990416.mxpaas.application.admin.controller;

import com.github.x19990416.mxpaas.application.admin.service.RoleService;
import com.github.x19990416.mxpaas.application.admin.service.dto.RoleQueryCriteria;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "系统：权限管理")
@RestController
@RequestMapping("/api/role")
@Slf4j
@RequiredArgsConstructor
public class RoleController {

  private final RoleService roleService;

  @GetMapping("/query")
  public ResponseEntity<Object> query(RoleQueryCriteria criteria, Pageable pageable) {
    return ResponseEntity.ok(roleService.queryAll(criteria, pageable));
  }
}
