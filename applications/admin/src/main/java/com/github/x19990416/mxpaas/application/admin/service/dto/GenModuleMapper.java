/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.service.dto;

import com.github.x19990416.mxpaas.application.admin.domain.SysGenModule;
import com.github.x19990416.mxpaas.application.admin.domain.SysModuleTable;
import com.github.x19990416.mxpaas.common.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GenModuleMapper extends BaseMapper<GenModuleDto, SysGenModule> {
	default Set<String> toTableName(Set<SysModuleTable> tables) {
		return tables.stream().map(SysModuleTable::getTableName).collect(Collectors.toSet());
	}

	default Set<SysModuleTable> toModuleTable(Set<String> tableNames) {
		return tableNames.stream()
				.map(name -> new SysModuleTable().setTableName(name))
				.collect(Collectors.toSet());
	}
}
