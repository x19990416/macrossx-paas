/** create by Guo Limin on 2021/2/21. */
package com.github.x19990416.mxpaas.application.admin.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Accessors(chain = true)
@Table(name = "sys_gen_module_table")
public class SysModuleTable implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "module_id")
  private Long moduleId;

  @Column(name = "table_name")
  private String tableName;

  @JoinColumn(name = "id",insertable = false,updatable = false)
  @ManyToOne(fetch=FetchType.LAZY)
  private SysGenModule sysGenModule;
}
