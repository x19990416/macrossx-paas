CREATE TABLE `sys_menu`
(
    `menu_id`     bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `pid`         bigint(20)   DEFAULT NULL COMMENT '上级菜单ID',
    `sub_count`   int(5)       DEFAULT '0' COMMENT '子菜单数目',
    `type`        int(11)      DEFAULT NULL COMMENT '菜单类型',
    `title`       varchar(255) DEFAULT NULL COMMENT '菜单标题',
    `name`        varchar(255) DEFAULT NULL COMMENT '组件名称',
    `component`   varchar(255) DEFAULT NULL COMMENT '组件',
    `menu_sort`   int(5)       DEFAULT NULL COMMENT '排序',
    `icon`        varchar(255) DEFAULT NULL COMMENT '图标',
    `path`        varchar(255) DEFAULT NULL COMMENT '链接地址',
    `i_frame`     bit(1)       DEFAULT NULL COMMENT '是否外链',
    `cache`       bit(1)       DEFAULT b'0' COMMENT '缓存',
    `hidden`      bit(1)       DEFAULT b'0' COMMENT '隐藏',
    `permission`  varchar(255) DEFAULT NULL COMMENT '权限',
    `create_by`   varchar(255) DEFAULT NULL COMMENT '创建者',
    `update_by`   varchar(255) DEFAULT NULL COMMENT '更新者',
    `create_time` datetime     DEFAULT NULL COMMENT '创建日期',
    `update_time` datetime     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`menu_id`) USING BTREE,
    UNIQUE KEY `uniq_title` (`title`),
    UNIQUE KEY `uniq_name` (`name`),
    KEY `inx_pid` (`pid`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 119
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPACT COMMENT ='系统菜单';
CREATE TABLE `sys_roles_menus`
(
    `menu_id` bigint(20) NOT NULL COMMENT '菜单ID',
    `role_id` bigint(20) NOT NULL COMMENT '角色ID',
    PRIMARY KEY (`menu_id`, `role_id`) USING BTREE,
    KEY `fk_role_id` (`role_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  ROW_FORMAT = COMPACT COMMENT ='角色菜单关联';
