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

import com.github.x19990416.mxpaas.application.admin.domain.Menu;
import com.github.x19990416.mxpaas.application.admin.domain.Role;
import com.github.x19990416.mxpaas.application.admin.domain.vo.MenuMetaVo;
import com.github.x19990416.mxpaas.application.admin.domain.vo.MenuVo;
import com.github.x19990416.mxpaas.application.admin.repository.MenuRepository;
import com.github.x19990416.mxpaas.application.admin.repository.RoleRepository;
import com.github.x19990416.mxpaas.application.admin.service.MenuService;
import com.github.x19990416.mxpaas.application.admin.service.dto.MenuDto;
import com.github.x19990416.mxpaas.application.admin.service.dto.MenuMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "menu")
@Slf4j
public class MenuServiceImpl implements MenuService {
  private final RoleRepository roleRepository;
  private final MenuRepository menuRepository;
  private final MenuMapper menuMapper;

  @Override
 // @Cacheable(key = "'user:' + #p0")
  public List<MenuDto> findByUser(Long userId) {
    Set<Long> roles =
        roleRepository.findByUserId(userId).stream().map(Role::getId).collect(Collectors.toSet());
    Set<Menu> menus = menuRepository.findByRoleIdsAndTypeNot(roles, 2);
    return menus.stream().map(menuMapper::toDto).collect(Collectors.toList());
  }

   public List<MenuDto> buildTree(List<MenuDto> menuDtos) {
    List<MenuDto> trees = new ArrayList<>();
    Set<Long> ids = new HashSet<>();
    for (MenuDto menuDTO : menuDtos) {
      if (menuDTO.getPid() == null) {
        trees.add(menuDTO);
      }
      for (MenuDto it : menuDtos) {
        if (menuDTO.getId().equals(it.getPid())) {
          if (menuDTO.getChildren() == null) {
            menuDTO.setChildren(new ArrayList<>());
          }
          menuDTO.getChildren().add(it);
          ids.add(it.getId());
        }
      }
    }
    if (trees.size() == 0) {
      trees = menuDtos.stream().filter(s -> !ids.contains(s.getId())).collect(Collectors.toList());
    }
    return trees;
  }

  public List<MenuVo> buildMenu(List<MenuDto> menuDtos) {
    List<MenuVo> menus = Lists.newArrayList();
    menuDtos.forEach(
        menuDto -> {
          if (menuDto != null) {
            List<MenuDto> children = menuDto.getChildren();
            MenuVo menuVo = new MenuVo();
            menuVo.setName(
                StringUtils.isNotEmpty(menuDto.getComponentName())
                    ? menuDto.getComponentName()
                    : menuDto.getTitle());
            // 一级目录需要加斜杠，不然会报警告
            menuVo.setPath(menuDto.getPid() == null ? "/" + menuDto.getPath() : menuDto.getPath());
            menuVo.setHidden(menuDto.getHidden());

            if (!menuDto.getIFrame()) {
              if (menuDto.getPid() == null) {
                menuVo.setComponent(
                    StringUtils.isEmpty(menuDto.getComponent())
                        ? "Layout"
                        : menuDto.getComponent());
                // 如果不是一级菜单，并且菜单类型为目录，则代表是多级菜单
              } else if (menuDto.getType() == 0) {
                menuVo.setComponent(
                    StringUtils.isEmpty(menuDto.getComponent())
                        ? "ParentView"
                        : menuDto.getComponent());
              } else if (StringUtils.isNotEmpty(menuDto.getComponent())) {
                menuVo.setComponent(menuDto.getComponent());
              }
            }
            menuVo.setMeta(
                new MenuMetaVo()
                    .setTitle(menuDto.getTitle())
                    .setIcon(menuDto.getIcon())
                    .setNoCache(!menuDto.getCache()));
            if (CollectionUtils.isNotEmpty(children)) {
              menuVo.setAlwaysShow(true);
              menuVo.setRedirect("noredirect");
              menuVo.setChildren(buildMenu(children));
              // 处理是一级菜单并且没有子菜单的情况
            } else if (menuDto.getPid() == null) {
              MenuVo menuVo1 = new MenuVo();
              menuVo1.setMeta(menuVo.getMeta());
              // 非外链
              if (!menuDto.getIFrame()) {
                menuVo1.setPath("index");
                menuVo1.setName(menuVo.getName());
                menuVo1.setComponent(menuVo.getComponent());
              } else {
                menuVo1.setPath(menuDto.getPath());
              }
              menuVo.setName(null);
              menuVo.setMeta(null);
              menuVo.setComponent("Layout");
              List<MenuVo> list1 = new ArrayList<>();
              list1.add(menuVo1);
              menuVo.setChildren(list1);
            }
            menus.add(menuVo);
          }
        });

    return menus;
  }
}
