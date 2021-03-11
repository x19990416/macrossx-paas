/** create by Guo Limin on 2021/3/10. */
package com.github.x19990416.paas.application.datamanager.domain.nf.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NFProductDto {
	private String id;
	private String name;
	private String brand;
	private String series;
	private String type;
	private String source;
	private String approval;
	private Integer status;
	private Float score;
	private String img;
	private String plb;
}
