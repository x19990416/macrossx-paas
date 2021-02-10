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
package com.github.x19990416.mxpaas.application.admin.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.x19990416.mxpaas.common.enums.DataScopeEnum;
import com.github.x19990416.mxpaas.module.jpa.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "sys_role")
@EqualsAndHashCode(callSuper = false)
@DynamicUpdate()
@Accessors(chain = true)
public class Role extends BaseEntity implements Serializable {

	@Id
	@Column(name = "role_id")
	@NotNull(groups = {Update.class})
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Schema(name = "ID", hidden = true)
	private Long id;
/*
	@JSONField(serialize = false)
	@ManyToMany(mappedBy = "roles")
	@Schema(name = "用户", hidden = true)
	private Set<User> users;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sys_roles_menus",
			joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "role_id")},
			inverseJoinColumns = {@JoinColumn(name = "menu_id",referencedColumnName = "menu_id")})
	@Schema(name = "菜单", hidden = true)
	private Set<Menu> menus;

	@ManyToMany
	@JoinTable(name = "sys_roles_depts",
			joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "role_id")},
			inverseJoinColumns = {@JoinColumn(name = "dept_id",referencedColumnName = "dept_id")})
	@Schema(name = "部门", hidden = true)
	private Set<Dept> depts;*/

	@NotBlank
	@Schema(name = "名称", hidden = true)
	private String name;

	@Schema(name = "数据权限，全部 、 本级 、 自定义")
	private String dataScope = DataScopeEnum.THIS_LEVEL.getValue();

	@Column(name = "level")
	@Schema(name = "级别，数值越小，级别越大")
	private Integer level = 3;

	@Schema(name = "描述")
	private String description;

	private String levelName;

}
