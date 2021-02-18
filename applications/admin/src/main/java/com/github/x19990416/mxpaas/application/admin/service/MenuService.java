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
package com.github.x19990416.mxpaas.application.admin.service;


import com.github.x19990416.mxpaas.application.admin.domain.Menu;
import com.github.x19990416.mxpaas.application.admin.domain.vo.MenuVo;
import com.github.x19990416.mxpaas.application.admin.service.dto.MenuDto;
import com.github.x19990416.mxpaas.application.admin.service.dto.MenuQueryCriteria;
import com.github.x19990416.mxpaas.common.vo.PageVo;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface MenuService {
	public List<MenuDto> findByUser(Long userId);
	public List<MenuVo> buildMenu(List<MenuDto> menuDtoList);
	public List<MenuDto> buildTree(List<MenuDto> menuDtoList);
	public PageVo<MenuDto> findByRole(List<Long> roleIds,Integer type, Pageable pageable);
	public PageVo<MenuDto> queryAll(MenuQueryCriteria criteria, Pageable pageable);
	public List<MenuDto> queryAll(MenuQueryCriteria criteria);
}
