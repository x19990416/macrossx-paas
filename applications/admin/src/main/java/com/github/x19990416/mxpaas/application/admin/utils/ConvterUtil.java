/** create by Guo Limin on 2021/2/14. */
package com.github.x19990416.mxpaas.application.admin.utils;

import com.github.x19990416.mxpaas.common.base.BaseMapper;
import com.github.x19990416.mxpaas.common.vo.PageVo;
import org.springframework.data.domain.Page;

public class ConvterUtil {
  public static <D, E> PageVo<D> toPageVo(Page<E> page, BaseMapper<D, E> mapper) {
    return new PageVo<D>()
        .setContents(mapper.toDto(page.getContent()))
        .setTotal(page.getTotalElements())
        .setPage(page.getNumber())
        .setSize(page.getSize());
  }
}
