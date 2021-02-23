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
package com.github.x19990416.mxpaas.application.admin.controller;

import com.github.x19990416.mxpaas.application.admin.service.GenerateService;
import com.github.x19990416.mxpaas.module.auth.AnonymousAccess;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "生成：系统构建")
@RestController
@RequestMapping("/api/generate")
@Slf4j
@RequiredArgsConstructor
public class GeneratorController {
	private final GenerateService generateService;
	@AnonymousAccess
	@GetMapping("/system")
	public ResponseEntity<?> doGenerate(@RequestParam Long sysId) throws Exception {
		generateService.generateSystem(sysId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
