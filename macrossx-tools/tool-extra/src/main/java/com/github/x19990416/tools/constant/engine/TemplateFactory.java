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
package com.github.x19990416.tools.constant.engine;

import com.github.x19990416.tools.constant.TemplateConfig;
import com.github.x19990416.tools.constant.TemplateEngine;

import java.lang.reflect.InvocationTargetException;

public class TemplateFactory {
  public static TemplateEngine getTemplateEngine(Class<? extends TemplateEngine> engineType)
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {
    return engineType.getDeclaredConstructor().newInstance();
  }

  public static TemplateEngine getTemplateEngine(Class<? extends TemplateEngine> engineType, TemplateConfig config)
          throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
          InstantiationException {
    return getTemplateEngine(engineType).init(config);
  }
}
