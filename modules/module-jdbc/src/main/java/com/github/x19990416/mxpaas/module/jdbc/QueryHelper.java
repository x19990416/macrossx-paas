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
import com.google.common.collect.Maps;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Slf4j
public class QueryHelper {

  /**
   * @param entity
   * @param includeNull
   * @return
   * @throws BadRequestException
   */
  public static Object[] toSelect(
      Object entity, Criteria criteria, Set<String> filter, boolean includeNull)
      throws BadRequestException {
    QueryParams queryParams = fetchSQLInfo(entity, criteria, filter, includeNull);
    Map<String, Object> columnInfo = queryParams.getColumns();
    if (StringUtils.isEmpty(queryParams.getTable()) || !includeNull && columnInfo.isEmpty()) {
      throw new BadRequestException("没有可执行的参数", 500);
    }
    StringBuilder sql = new StringBuilder("select ");
    if (CollectionUtils.isEmpty(columnInfo)) {
      sql.append("* ");
    } else {
      filter.forEach(e -> sql.append(e).append(","));
    }
    sql.deleteCharAt(sql.length() - 1)
        .append(" from ")
        .append(queryParams.getTable());
    if(queryParams.getCriteria()!=null){}
    columnInfo.keySet().forEach(e -> sql.append(e).append("=?"));
    for (String column : columnInfo.keySet()) {
      sql.append(column).append(",");
    }
    sql.deleteCharAt(sql.length() - 1);
  }

  /**
   * @param entity
   * @param includeNull 是否包含null
   * @return QueryParams
   */
  private static QueryParams fetchSQLInfo(
      Object entity, Criteria criteria, Set<String> filter, boolean includeNull) {
    try {
      Table table = entity.getClass().getDeclaredAnnotation(Table.class);
      if (Objects.isNull(table)) throw new BadRequestException("annotation not found", 500);
      String tableName = table.name();
      Map<String, Object> columns = Maps.newConcurrentMap();
      for (Field field : entity.getClass().getDeclaredFields()) {
        Column column = field.getAnnotation(Column.class);
        boolean isAccess = field.canAccess(entity);
        field.setAccessible(true);
        if (!Objects.isNull(column) && (includeNull & Objects.isNull(field.get(entity)))) {
          columns.put(column.name(), field.get(entity));
          if (CollectionUtils.isEmpty(filter) && filter.contains(field.getName())) {
            filter.remove(field.getName());
            filter.add(column.name());
          }
          if (!Objects.isNull(criteria) && criteria.getConditions().containsKey(field.getName())) {
            String condition = criteria.getConditions().remove(field.getName());
            criteria.getConditions().put(column.name(), condition);
          }
        }
        field.setAccessible(isAccess);
      }
      return new QueryParams()
          .setColumnFilter(filter)
          .setTable(tableName)
          .setColumns(columns)
          .setCriteria(criteria);
    } catch (IllegalAccessException e) {
      throw new BadRequestException(e.getLocalizedMessage(), e, 500);
    }
  }

  public static void main(String... s) {
    new Criteria();
  }

  @Data
  @Accessors(chain = true)
  private static class QueryParams {
    private String table;
    private Map<String, Object> columns;
    private Set<String> columnFilter;
    private Criteria criteria;
  }
}
