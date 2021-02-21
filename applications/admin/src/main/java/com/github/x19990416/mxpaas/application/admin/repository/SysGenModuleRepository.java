/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.repository;

import com.github.x19990416.mxpaas.application.admin.domain.SysGenModule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SysGenModuleRepository
    extends JpaRepository<SysGenModule, Long>, JpaSpecificationExecutor<SysGenModule> {
  public List<SysGenModule> findByName(String name);

  public void deleteAllByIdIn(Set<Long> ids);

  public Optional<SysGenModule> findByIdAndNameAndGroupIdAndArtifactId(Long id,String name,String groupId,String artifactId);
}
