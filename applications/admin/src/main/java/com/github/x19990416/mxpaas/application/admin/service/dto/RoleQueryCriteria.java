/**
 * create by Guo Limin on 2021/2/14.
 */
package com.github.x19990416.mxpaas.application.admin.service.dto;

import com.github.x19990416.mxpaas.module.jpa.annotation.Query;
import lombok.Data;

@Data
public class RoleQueryCriteria {
	@Query
	private Long id;

	@Query(blurry = "name")
	private String blurry;
}
