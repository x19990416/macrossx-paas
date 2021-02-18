/** create by Guo Limin on 2021/2/12. */
package com.github.x19990416.mxpaas.application.admin.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Data
@Entity
@Table(name = "sys_gen_module")
@EqualsAndHashCode(callSuper = false)
@DynamicUpdate()
@Accessors(chain = true)
public class SysGenModule {
  @Id
  @Column(name = "id")
  @Schema(name = "ID", hidden = true)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;
  private String group;
  private String version;
  private String description;
}
