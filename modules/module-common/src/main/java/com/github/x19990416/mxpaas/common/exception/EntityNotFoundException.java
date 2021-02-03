/** create by Guo Limin on 2021/1/30. */
package com.github.x19990416.mxpaas.common.exception;

import org.apache.commons.lang3.StringUtils;

public class EntityNotFoundException extends RuntimeException {

  public EntityNotFoundException(Class clazz, String field, String val) {
    super(EntityNotFoundException.generateMessage(clazz.getSimpleName(), field, val));
  }

  private static String generateMessage(String entity, String field, String val) {
    return StringUtils.capitalize(entity) + " with " + field + " " + val + " does not exist";
  }
}
