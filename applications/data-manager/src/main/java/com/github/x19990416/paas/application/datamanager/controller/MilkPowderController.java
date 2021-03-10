/** create by Guo Limin on 2021/3/10. */
package com.github.x19990416.paas.application.datamanager.controller;

import com.github.x19990416.paas.application.datamanager.domain.nf.dto.NFIngredientQueryCriteria;
import com.github.x19990416.paas.application.datamanager.domain.nf.dto.NFProductQueryCriteria;
import com.github.x19990416.paas.application.datamanager.service.MilkPowderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/nf")
@Slf4j
@RequiredArgsConstructor
public class MilkPowderController {

  private final MilkPowderService milkPowderService;

  @GetMapping("/product/query")
  public ResponseEntity<Object> queryProduct(NFProductQueryCriteria criteria, Pageable pageable) {
    return ResponseEntity.ok(milkPowderService.queryProducts(criteria, pageable));
  }

  @GetMapping("/ingredient/query")
  public ResponseEntity<Object> queryIngredient(
      NFIngredientQueryCriteria criteria, Pageable pageable) {
    return ResponseEntity.ok(milkPowderService.queryIngredients(criteria, pageable));
  }
}
