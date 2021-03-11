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
package com.github.x19990416.paas.module.user.service;


import com.github.x19990416.mxpaas.common.vo.PageVo;
import com.github.x19990416.paas.module.user.domain.dto.UserDto;
import com.github.x19990416.paas.module.user.domain.dto.UserQueryCriteria;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface UserService {
	public PageVo<UserDto> queryAll(UserQueryCriteria criteria, Pageable pageable);

	public void createUser(UserDto resourceDto);

	public void deleteUser(Set<Long> ids);
}
