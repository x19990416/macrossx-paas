package com.github.x19990416.mxpaas.tool.generator.rest;

import com.github.x19990416.mxpaas.tool.generator.domain.GenConfig;
import com.github.x19990416.mxpaas.tool.generator.service.GenConfigService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genConfig")
@Tag(name = "系统：代码生成器配置管理")
public class GenConfigController {

    private final GenConfigService genConfigService;

    @Operation(method = "查询")
    @GetMapping(value = "/{tableName}")
    public ResponseEntity<Object> query(@PathVariable String tableName){
        return new ResponseEntity<>(genConfigService.find(tableName), HttpStatus.OK);
    }

    @Operation(method = "修改")
    @PutMapping
    public ResponseEntity<Object> update(@Validated @RequestBody GenConfig genConfig){
        return new ResponseEntity<>(genConfigService.update(genConfig.getTableName(), genConfig), HttpStatus.OK);
    }
}
