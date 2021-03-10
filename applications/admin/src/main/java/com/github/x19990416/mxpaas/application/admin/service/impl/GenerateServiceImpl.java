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
package com.github.x19990416.mxpaas.application.admin.service.impl;

import com.github.x19990416.mxpaas.application.admin.domain.Config;
import com.github.x19990416.mxpaas.application.admin.domain.Module;
import com.github.x19990416.mxpaas.application.admin.repository.ConfigRepository;
import com.github.x19990416.mxpaas.application.admin.service.GenerateService;
import com.github.x19990416.mxpaas.common.exception.EntityNotFoundException;
import com.github.x19990416.tools.constant.Template;
import com.github.x19990416.tools.constant.TemplateConfig;
import com.github.x19990416.tools.constant.TemplateEngine;
import com.github.x19990416.tools.constant.engine.TemplateFactory;
import com.github.x19990416.tools.constant.engine.freemarker.FreemarkerEngine;
import com.google.common.base.Charsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenerateServiceImpl implements GenerateService {

  private static final String _GRADE_IMPLEMENTATION =
      "implementation group: ''{0}'', name: ''{1}'', version: ''{2}''";
  private final ConfigRepository configRepository;

  private TemplateEngine engine = null;

  @Override
  public boolean generateSystem(Long sysId) throws Exception {
    Config config =
        configRepository
            .findById(sysId)
            .orElseThrow(() -> new EntityNotFoundException(Config.class, "id", sysId));

    String gradleFile = getTemplate("gradle.ftl").render(generateModule(config.getModules()));
    System.out.println(gradleFile);
    return true;
  }

  public Template getTemplate(String templateName) throws Exception {
    if (Objects.isNull(engine)) {
      engine =
          TemplateFactory.getTemplateEngine(
              FreemarkerEngine.class,
              new TemplateConfig(
                  "generate", Charsets.UTF_8, TemplateConfig.ResourceMode.CLASSPATH));
    }
    return engine.getInstance(templateName);
  }

  private Map<String, List<String>> generateModule(List<Module> modules) {
    return Map.of(
        "dependencies",
        modules.stream()
            .map(
                module -> {
                  return MessageFormat.format(
                      _GRADE_IMPLEMENTATION,
                      module.getGroupId(),
                      module.getName(),
                      module.getVersion());
                })
            .collect(Collectors.toList()));
  }
}
