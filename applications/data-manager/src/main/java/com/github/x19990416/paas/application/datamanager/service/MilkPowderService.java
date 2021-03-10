/** create by Guo Limin on 2021/3/10. */
package com.github.x19990416.paas.application.datamanager.service;

import com.github.x19990416.mxpaas.common.vo.PageVo;
import com.github.x19990416.paas.application.datamanager.domain.nf.dto.NFIngredientDto;
import com.github.x19990416.paas.application.datamanager.domain.nf.dto.NFIngredientQueryCriteria;
import com.github.x19990416.paas.application.datamanager.domain.nf.dto.NFProductDto;
import com.github.x19990416.paas.application.datamanager.domain.nf.dto.NFProductQueryCriteria;
import org.springframework.data.domain.Pageable;

public interface MilkPowderService {
  public PageVo<NFProductDto> queryProducts(NFProductQueryCriteria criteria, Pageable pageable);

  public PageVo<NFIngredientDto> queryIngredients(
      NFIngredientQueryCriteria criteria, Pageable pageable);
}
