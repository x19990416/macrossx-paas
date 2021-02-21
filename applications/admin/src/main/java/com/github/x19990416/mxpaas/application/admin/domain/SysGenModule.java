/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "sys_gen_module")
@Accessors(chain = true)
public class SysGenModule implements Serializable {
  @Id
  @Column(name = "id")
  @Schema(name = "ID", hidden = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String groupId;
  private String version;
  private String description;
  private String artifactId;

  @ManyToOne(cascade = {CascadeType.ALL})
  @JoinTable(
      name = "sys_gen_config_module",
      joinColumns = {@JoinColumn(name = "sys_module_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "sys_id", referencedColumnName = "id")})
  private SysGenConfig sysGenConfig;

  @OneToMany(cascade = CascadeType.ALL,mappedBy="sysGenModule",fetch = FetchType.EAGER,orphanRemoval = true)
  private Set<SysModuleTable> tables = new HashSet<>();
}
