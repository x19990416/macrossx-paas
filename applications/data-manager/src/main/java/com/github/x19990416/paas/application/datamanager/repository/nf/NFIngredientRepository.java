/** create by Guo Limin on 2021/3/10. */
package com.github.x19990416.paas.application.datamanager.repository.nf;

import com.github.x19990416.paas.application.datamanager.domain.nf.NFIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NFIngredientRepository
    extends JpaRepository<NFIngredient, String>, JpaSpecificationExecutor<NFIngredient> {}
