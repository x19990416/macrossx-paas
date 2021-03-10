/**
 * create by Guo Limin on 2021/3/10.
 */
package com.github.x19990416.paas.application.datamanager.domain.nf;


import com.github.x19990416.mxpaas.module.jpa.BaseEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class NFProduct extends BaseEntity {
	@Id
	private String id;
}
