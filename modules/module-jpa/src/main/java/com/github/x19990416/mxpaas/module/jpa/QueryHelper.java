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
package com.github.x19990416.mxpaas.module.jpa;

import com.github.x19990416.mxpaas.module.jpa.annotation.Query;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
public class QueryHelper {
  public static <R, Q> Predicate getPredicate(Root<R> root, Q query, CriteriaBuilder cb) {
    List<Predicate> list = new ArrayList<>();
    if (query == null) {
      return cb.and(list.toArray(new Predicate[0]));
    }
    try {
      List<Field> fields = getAllFields(query.getClass(), new ArrayList<>());
      for (Field field : fields) {
        boolean accessible = field.isAccessible();
        if (accessible == false)
          // 设置对象的访问权限，保证对private的属性的访
          field.setAccessible(true);
        Query q = field.getAnnotation(Query.class);
        if (q != null) {
          String propName = q.propName();
          String joinName = q.joinName();
          String blurry = q.blurry();
          String attributeName = StringUtils.isBlank(propName) ? field.getName() : propName;
          Class<?> fieldType = field.getType();
          Object val = field.get(query);
          if (val == null || "".equals(val)) {
            continue;
          }
          Join join = null;
          // 模糊多字段
          if (StringUtils.isNotEmpty(blurry)) {
            List<Predicate> orPredicate = new ArrayList<>();
            for (String s : blurry.split(",")) {
              orPredicate.add(cb.like(root.get(s).as(String.class), "%" + val.toString() + "%"));
            }
            Predicate[] p = new Predicate[orPredicate.size()];
            list.add(cb.or(orPredicate.toArray(p)));
            continue;
          }
          if (StringUtils.isNotEmpty(joinName)) {
            String[] joinNames = joinName.split(">");
            for (String name : joinNames) {
              switch (q.join()) {
                case LEFT:
                  if (null != join && null != val) {
                    join = join.join(name, JoinType.LEFT);
                  } else {
                    join = root.join(name, JoinType.LEFT);
                  }
                  break;
                case RIGHT:
                  if (null != join && null != val) {
                    join = join.join(name, JoinType.RIGHT);
                  } else {
                    join = root.join(name, JoinType.RIGHT);
                  }
                  break;
                case INNER:
                  if (null != join && null != val) {
                    join = join.join(name, JoinType.INNER);
                  } else {
                    join = root.join(name, JoinType.INNER);
                  }
                  break;
                default:
                  break;
              }
            }
          }
          switch (q.type()) {
            case EQUAL:
              list.add(
                  cb.equal(
                      getExpression(attributeName, join, root)
                          .as((Class<? extends Comparable>) fieldType),
                      val));
              break;
            case GREATER_THAN:
              list.add(
                  cb.greaterThanOrEqualTo(
                      getExpression(attributeName, join, root)
                          .as((Class<? extends Comparable>) fieldType),
                      (Comparable) val));
              break;
            case LESS_THAN:
              list.add(
                  cb.lessThanOrEqualTo(
                      getExpression(attributeName, join, root)
                          .as((Class<? extends Comparable>) fieldType),
                      (Comparable) val));
              break;
            case LESS_THAN_NQ:
              list.add(
                  cb.lessThan(
                      getExpression(attributeName, join, root)
                          .as((Class<? extends Comparable>) fieldType),
                      (Comparable) val));
              break;
            case INNER_LIKE:
              list.add(
                  cb.like(
                      getExpression(attributeName, join, root).as(String.class),
                      "%" + val.toString() + "%"));
              break;
            case LEFT_LIKE:
              list.add(
                  cb.like(
                      getExpression(attributeName, join, root).as(String.class),
                      "%" + val.toString()));
              break;
            case RIGHT_LIKE:
              list.add(
                  cb.like(
                      getExpression(attributeName, join, root).as(String.class),
                      val.toString() + "%"));
              break;
            case IN:
              if (!CollectionUtils.isEmpty((Collection<Long>) val)) {
                list.add(getExpression(attributeName, join, root).in((Collection<Long>) val));
              }
              break;
            case NOT_EQUAL:
              list.add(cb.notEqual(getExpression(attributeName, join, root), val));
              break;
            case NOT_NULL:
              list.add(cb.isNotNull(getExpression(attributeName, join, root)));
              break;
            case IS_NULL:
              list.add(cb.isNull(getExpression(attributeName, join, root)));
              break;
            case BETWEEN:
              List<Object> between = new ArrayList<>((List<Object>) val);
              list.add(
                  cb.between(
                      getExpression(attributeName, join, root)
                          .as((Class<? extends Comparable>) between.get(0).getClass()),
                      (Comparable) between.get(0),
                      (Comparable) between.get(1)));
              break;
            default:
              break;
          }
        }
        field.setAccessible(accessible);
      }
    } catch (Exception e) {

      log.error(e.getMessage(), e);
    }
    int size = list.size();
    return cb.and(list.toArray(new Predicate[size]));
  }

  private static <T, R> Expression<T> getExpression(String attributeName, Join join, Root<R> root) {
    if (null != join) {
      return join.get(attributeName);
    } else {
      return root.get(attributeName);
    }
  }

  public static List<Field> getAllFields(Class clazz, List<Field> fields) {
    if (clazz != null) {
      fields.addAll(Lists.newArrayList(clazz.getDeclaredFields()));
      getAllFields(clazz.getSuperclass(), fields);
    }
    return fields;
  }
}
