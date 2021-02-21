/**
 * create by Guo Limin on 2021/2/21.
 */
package com.github.x19990416.mxpaas.application.admin.repository;

import com.github.x19990416.mxpaas.application.admin.domain.SysModuleTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

public interface SysModuleTableRepository extends JpaRepository<SysModuleTable, Long>, JpaSpecificationExecutor<SysModuleTable> {
}
