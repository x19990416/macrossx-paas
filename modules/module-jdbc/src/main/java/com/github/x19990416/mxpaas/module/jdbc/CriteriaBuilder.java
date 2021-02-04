/** create by Guo Limin on 2021/2/3. */
package com.github.x19990416.mxpaas.module.jdbc;

import com.github.x19990416.mxpaas.common.exception.BadRequestException;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

@Slf4j
public class CriteriaBuilder {
  private Class<?> clazz;
  private StringBuilder where;
  private boolean limit = false;
  @Getter private List<String> fields = Lists.newArrayList();

  public CriteriaBuilder(Class<?> clazz) {
    this.clazz = clazz;
  }

  public CriteriaBuilder where(String fieldName) {
    where = new StringBuilder();
    where.append("where ").append(getColumnByField(fieldName)).append("=? ");
    return this;
  }

  private String getColumnByField(String fieldName) {
    try {
      fields.add(fieldName);
      return (clazz.getDeclaredField(fieldName).getDeclaredAnnotation(Column.class)).name();
    } catch (NoSuchFieldException e) {
      log.error(e.getMessage());
      throw new BadRequestException(e, 500);
    }
  }

  public CriteriaBuilder or(String fieldName) {
    where.append("or ").append(getColumnByField(fieldName)).append("=? ");
    return this;
  }

  public CriteriaBuilder and(String fieldName) {
    where.append("and ").append(getColumnByField(fieldName)).append("=? ");
    return this;
  }

  public CriteriaBuilder hasLimit() {
    this.limit = true;
    return this;
  }

  public List<Object> getFields(@NotNull Object entity) {
    try {
      List<Object> paramValues = Lists.newArrayList();
      for (String fieldName : this.getFields()) {
        Field field = entity.getClass().getDeclaredField(fieldName);
        boolean canAccess = field.canAccess(entity);
        field.setAccessible(true);
        paramValues.add(field.get(entity));
        field.setAccessible(canAccess);
      }
      return paramValues;
    } catch (java.lang.NoSuchFieldException | IllegalAccessException e) {
      log.error("", e);
      throw new BadRequestException(e, 500);
    }
  }

  public String build() {
    String whereQuery = Objects.isNull(where) ? "" : where.toString();
    if (limit) whereQuery = whereQuery + "limit ?,?";
    return whereQuery;
  }
}
