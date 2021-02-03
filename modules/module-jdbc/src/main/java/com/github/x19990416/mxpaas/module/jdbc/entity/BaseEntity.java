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
package com.github.x19990416.mxpaas.module.jdbc.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class BaseEntity implements Serializable {
	@Column(name = "create_by", updatable = false)
	private String createBy;

	@Column(name = "update_by")
	private String updatedBy;

	@Column(name = "create_time", updatable = false)
	private Timestamp createTime;

	@Column(name = "update_time")
	private Timestamp updateTime;

	/* 分组校验 */
	public @interface Create {}

	/* 分组校验 */
	public @interface Update {}
}
