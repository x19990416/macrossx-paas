/** create by Guo Limin on 2021/2/14. */
package com.github.x19990416.mxpaas.application.admin.controller;

import com.github.x19990416.mxpaas.application.admin.service.MenuService;
import com.github.x19990416.mxpaas.application.admin.service.dto.MenuDto;
import com.github.x19990416.mxpaas.application.admin.service.dto.MenuQueryCriteria;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;

@Tag(name = "系统：菜单管理")
@RestController
@RequestMapping("/api/menu")
@Slf4j
@RequiredArgsConstructor
public class MenuContorller {

  private final MenuService menuService;

  @GetMapping("/query")
  public ResponseEntity<Object> query(MenuQueryCriteria criteria, Pageable pageable) {
    return ResponseEntity.ok(menuService.queryAll(criteria, pageable));
  }

  @GetMapping("/build")
  public ResponseEntity<Object> build(MenuQueryCriteria criteria) {
    List<MenuDto> menus = menuService.queryAll(criteria);
    menus.sort(Comparator.comparing(MenuDto::getMenuSort));
    return ResponseEntity.ok(menuService.buildMenu(menuService.buildTree(menus)));
  }

  @GetMapping("/query/role")
  public ResponseEntity<Object> queryByRole(Long roleId, Pageable pageable) {
    log.info("{}-{}-{}", roleId, pageable, "queryByRole");
    return ResponseEntity.ok(menuService.findByRole(List.of(roleId), null, pageable));
  }
}
