/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.github.x19990416.mxpaas.application.admin.domain.Module;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SysGenModuleRepository
    extends JpaRepository<Module, Long>, JpaSpecificationExecutor<Module> {
  public List<Module> findByName(String name);

  public void deleteAllByIdIn(Set<Long> ids);

  public Optional<Module> findByIdAndNameAndGroupIdAndArtifactId(Long id,String name,String groupId,String artifactId);
}
