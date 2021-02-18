/**
 * create by Guo Limin on 2021/2/12.
 */
package com.github.x19990416.mxpaas.application.admin.domain.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Accessors(chain = true)
public class SysConfigVo {
	private Long id;
	@NotEmpty(message = "系统名不能为空")
	private String name;
	private String description;
	private String abbr;
	private Set<String> modules;
}
