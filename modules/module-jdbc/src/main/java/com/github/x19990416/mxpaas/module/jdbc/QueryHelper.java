/*
 *  Copyright (c) 2020-2021 Guo Limin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.github.x19990416.mxpaas.module.jdbc;

import com.github.x19990416.mxpaas.common.exception.BadRequestException;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class QueryHelper {
  private static final String _SQL_INISERT = "insert into {0} ({1}) values ({2})";
  // select {*} from {table} {where} {limit}
  private static final String _SQL_SELECT = "select {0} from {1} {2}";
  // update {table} set {key=?} {where}
  private static final String _SQL_UPDATE = "update {0} set {1} {2}";
  private static final String COMMA = ",";

  public static Map<String, List<Object>> toInsert(Object entity, boolean includeNull) {
    try {
      String tableName = entity.getClass().getDeclaredAnnotation(Table.class).name();
      StringBuilder builder = new StringBuilder();
      StringBuilder values = new StringBuilder();
      List<Object> paramsValue = Lists.newArrayList();
      for (Field field : entity.getClass().getDeclaredFields()) {
        Column column = field.getDeclaredAnnotation(Column.class);
        if (column != null) {
          boolean canAccess = field.canAccess(entity);
          field.setAccessible(true);
          if (includeNull || Objects.nonNull(field.get(entity))) {
            paramsValue.add(field.get(entity));
            builder.append(column.name()).append(COMMA);
            values.append("?").append(COMMA);
          }
          field.setAccessible(canAccess);
        }
      }
      return Map.of(
          MessageFormat.format(
              _SQL_INISERT,
              tableName,
              builder.deleteCharAt(builder.length() - 1),
              values.deleteCharAt(values.length() - 1)),
          paramsValue);
    } catch (IllegalAccessException e) {
      throw new BadRequestException(e, 500);
    }
  }

  public static Map<String, List<Object>> toSelect(
      Object entity, CriteriaBuilder criteria, String[] selectFields) {
    try {
      String tableName = entity.getClass().getDeclaredAnnotation(Table.class).name();
      StringBuilder columnBuilder = new StringBuilder();
      if (Objects.isNull(selectFields) || selectFields.length == 0) {
        columnBuilder.append("* ");
      } else {
        for (String field : selectFields) {
          columnBuilder
              .append(
                  entity
                      .getClass()
                      .getDeclaredField(field)
                      .getDeclaredAnnotation(Column.class)
                      .name())
              .append(COMMA);
        }
      }
      List<Object> paramValues = criteria.getFields(entity);
      return Map.of(
          MessageFormat.format(
              _SQL_SELECT,
              columnBuilder.deleteCharAt(columnBuilder.length() - 1).toString(),
              tableName,
              Objects.isNull(criteria) ? "" : criteria.build()),
          Objects.isNull(criteria) ? Lists.newArrayList() : criteria.getFields(entity));
    } catch (NoSuchFieldException e) {
      throw new BadRequestException(e, 500);
    }
  }

  public Map<String, List<Object>> toUpdate(
      @NotNull Object newEntity,
      CriteriaBuilder criteria,
      @NotNull Object oldEntity,
      boolean includeNull) {
    try {
      StringBuilder setBuilder = new StringBuilder();
      String tableName = newEntity.getClass().getDeclaredAnnotation(Table.class).name();
      List<Object> paramsValues = Lists.newArrayList();
      for (Field field : newEntity.getClass().getDeclaredFields()) {
        if (includeNull == false && Objects.isNull(field.get(newEntity))) continue;
        boolean canAccess = field.canAccess(newEntity);
        setBuilder.append(field.getDeclaredAnnotation(Column.class).name()).append("=?,");
        field.setAccessible(true);
        paramsValues.add(field.get(newEntity));
        field.setAccessible(canAccess);
      }
      paramsValues.addAll(
          Objects.isNull(criteria) ? Lists.newArrayList() : criteria.getFields(oldEntity));
      return Map.of(
          MessageFormat.format(
              _SQL_UPDATE,
              tableName,
              setBuilder.deleteCharAt(setBuilder.length() - 1).toString(),
              Objects.isNull(criteria) ? "" : criteria.build()),
          paramsValues);
    } catch (java.lang.IllegalAccessException e) {
      log.error("", e);
      throw new BadRequestException(e, 500);
    }
  }
}
