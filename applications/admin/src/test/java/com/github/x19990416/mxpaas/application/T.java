/** create by Guo Limin on 2021/2/21. */
package com.github.x19990416.mxpaas.application;

import com.github.x19990416.mxpaas.application.admin.service.impl.ManagementServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class T {

  @Resource()
  private ManagementServiceImpl userDetailsService;

  @Test
  public void contextLoads() {
	  userDetailsService.buildTableTrees();
  }
}
