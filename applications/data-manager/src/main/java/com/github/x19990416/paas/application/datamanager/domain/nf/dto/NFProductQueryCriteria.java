/** create by Guo Limin on 2021/3/10. */
package com.github.x19990416.paas.application.datamanager.domain.nf.dto;

import com.github.x19990416.mxpaas.module.jpa.annotation.Query;
import lombok.Data;

@Data
public class NFProductQueryCriteria {
  @Query(type = Query.Type.EQUAL)
  private String id;

  private String blurry;

  @Query(type = Query.Type.EQUAL)
  private String brand;

  @Query(type = Query.Type.EQUAL)
  private Integer status;
}
