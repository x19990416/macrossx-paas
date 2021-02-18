/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.domain;

import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
@Table(name = "sys_gen_config")
@EqualsAndHashCode(callSuper = false)
@DynamicUpdate()
public class SysGenConfig {
  @Id
  @Column(name = "id")
  @Schema(name = "ID", hidden = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private String abbr;

  @OneToMany
  @JoinTable(
      name = "sys_gen_config_module",
      joinColumns = {@JoinColumn(name = "sys_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "sys_module_id", referencedColumnName = "id")})
  private List<SysGenModule> modules = Lists.newArrayList();
}
