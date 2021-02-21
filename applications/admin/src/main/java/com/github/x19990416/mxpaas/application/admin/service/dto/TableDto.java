/**
 * create by Guo Limin on 2021/2/21.
 */
package com.github.x19990416.mxpaas.application.admin.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class TableDto {
	private String name;
	private List<TableDto> children;
}