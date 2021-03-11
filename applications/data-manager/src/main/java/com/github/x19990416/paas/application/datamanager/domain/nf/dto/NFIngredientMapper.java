/** create by Guo Limin on 2021/3/10. */
package com.github.x19990416.paas.application.datamanager.domain.nf.dto;

import com.github.x19990416.mxpaas.common.base.BaseMapper;
import com.github.x19990416.paas.application.datamanager.domain.nf.NFIngredient;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)

public interface NFIngredientMapper extends BaseMapper<NFIngredientDto, NFIngredient> {}
