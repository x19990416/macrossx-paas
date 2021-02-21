/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.repository;

import com.github.x19990416.mxpaas.application.admin.domain.SysGenConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Set;

public interface SysGenConfigRepository
    extends JpaRepository<SysGenConfig, Long>, JpaSpecificationExecutor<SysGenConfig> {
  public List<SysGenConfig> findByNameOrAbbr(String name, String abbr);

  public void deleteAllByIdIn(Set<Long> ids);
}
