/**
 * create by Guo Limin on 2021/3/10.
 */
package com.github.x19990416.paas.application.datamanager.domain.nf;


import com.github.x19990416.mxpaas.module.jpa.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "nf_product")
public class NFProduct extends BaseEntity {
	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "name")
	private String name;

	@Column(name = "brand")
	private String brand;

	@Column(name = "series")
	private String series;

	@Column(name = "type")
	private String type;

	@Column(name = "source")
	private String source;

	@Column(name = "approval")
	private String approval;

	@Column(name = "status")
	private Integer status;

	@Column(name = "score")
	private Float score;

	@Column(name = "img")
	private String img;

	@Column(name = "plb")
	private String plb;
}
