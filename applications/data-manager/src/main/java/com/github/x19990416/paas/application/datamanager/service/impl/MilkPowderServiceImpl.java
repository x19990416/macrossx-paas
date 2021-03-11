/** create by Guo Limin on 2021/3/10. */
package com.github.x19990416.paas.application.datamanager.service.impl;

import com.github.x19990416.mxpaas.common.utils.ConvertUtil;
import com.github.x19990416.mxpaas.common.vo.PageVo;
import com.github.x19990416.mxpaas.module.jpa.QueryHelper;
import com.github.x19990416.paas.application.datamanager.domain.nf.NFIngredient;
import com.github.x19990416.paas.application.datamanager.domain.nf.NFProduct;
import com.github.x19990416.paas.application.datamanager.domain.nf.dto.*;
import com.github.x19990416.paas.application.datamanager.repository.nf.NFIngredientRepository;
import com.github.x19990416.paas.application.datamanager.repository.nf.NFProductRepository;
import com.github.x19990416.paas.application.datamanager.service.MilkPowderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MilkPowderServiceImpl implements MilkPowderService {
  private final NFProductRepository nfProductRepository;
  private final NFProductMapper nfProductMapper;
  private final NFIngredientRepository nfIngredientRepository;
  private final NFIngredientMapper nfIngredientMapper;

  public PageVo<NFProductDto> queryProducts(NFProductQueryCriteria criteria, Pageable pageable) {
    Page<NFProduct> page =
        nfProductRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelper.getPredicate(root, criteria, criteriaBuilder),
            pageable);
    return ConvertUtil.toPageVo(page, nfProductMapper);
  }

  public PageVo<NFIngredientDto> queryIngredients(
      NFIngredientQueryCriteria criteria, Pageable pageable) {
    Page<NFIngredient> page =
        nfIngredientRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelper.getPredicate(root, criteria, criteriaBuilder),
            pageable);
    return ConvertUtil.toPageVo(page, nfIngredientMapper);
  }
}
