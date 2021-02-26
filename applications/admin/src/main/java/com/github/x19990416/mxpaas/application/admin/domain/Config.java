/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "gen_config")
@EqualsAndHashCode(callSuper = false)
@DynamicUpdate()
public class Config {
  @Id
  @Column(name = "id")
  @Schema(name = "ID", hidden = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String description;
  private String abbr;
  private String dataSource;
  private Integer type;
  private String basePackage;

  @OneToMany
  @JoinTable(
      name = "gen_config_module",
      joinColumns = {@JoinColumn(name = "config_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "module_id", referencedColumnName = "id")})
  private List<Module> modules;
}
