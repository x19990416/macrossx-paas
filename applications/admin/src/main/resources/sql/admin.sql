
drop table if exists  `gen_config_module`
CREATE TABLE `gen_config_module` (
  `module_id` int(11) NOT NULL,
  `config_id` int(11) NOT NULL,
  UNIQUE KEY `idx_ids` (`module_id`,`config_id`),
  KEY `fk_config` (`config_id`),
  CONSTRAINT `fk_config` FOREIGN KEY (`config_id`) REFERENCES `gen_config` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_moudle` FOREIGN KEY (`module_id`) REFERENCES `gen_module` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4

drop table if exists  `gen_config`
CREATE TABLE `gen_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `description` text,
  `abbr` varchar(10) NOT NULL,
  `data_source` varchar(255) DEFAULT NULL,
  `type` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4


drop table if exists  `gen_module`
CREATE TABLE `gen_module` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `group_id` varchar(255) NOT NULL,
  `version` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `tables` text COMMENT '所需数据库表，以 , 分割',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4
