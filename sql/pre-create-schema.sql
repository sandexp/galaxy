
create database galaxy;

use galaxy;

create index `table_name` on `data_warehouse_metadata`(`table_name`);

create table data_warehouse_metadata
(
  `database_name` varchar(128) not null default '' comment '数据库名称',
  `table_name` varchar(128) not null comment '数据表名称',
  `table_engine` varchar(16) not null default 'HIVE' comment '数据库存储引擎',
  `table_type` varchar(32) not null default 'UNDEFINED' comment '数据表类型- 数仓分层角色',
  `dimension_name` varchar(32) not null default 'UNDEFINED' comment '维度属性',
  `create_time` datetime not null default CURRENT_TIMESTAMP comment '表信息记录创建时间',
  `update_time` datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '表信息记录更新时间',
  `operate_type` varchar(32) not null default 'None' comment '操作类型',
  `is_online` tinyint(1) not null default 0 comment '表是否上线,默认下线',
  `available_user` varchar(32) not null default 'admin' comment '表可见用户,为数据集市层设计,默认管理员权限',
  primary key(`database_name`,`table_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


create table table_dependency
(
  `sub_table` varchar(128) not null comment '子表名称',
  `parent_table` varchar(128) not null comment '父表名称',
  `create_time` datetime not null default CURRENT_TIMESTAMP comment '表信息记录创建时间',
  `update_time` datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '表信息记录更新时间',
  `is_available` tinyint(1) not null  default 1 comment '血缘是否可用,默认1: 可用',
  primary key(`sub_table`,`parent_table`)
) engine=InnoDB DEFAULT CHARSET=utf8mb4;


create table field_dependency
(
  `parent_level_field` varchar(128) not null comment '父级字段名称',
  `sub_level_field` varchar(128) not null comment '子级字段名称',
  `projection_expression` varchar(255) not null comment '投影表达式',
  `create_time` datetime not null default CURRENT_TIMESTAMP comment '表信息记录创建时间',
  `update_time` datetime not null default CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP comment '表信息记录更新时间',
  `is_available` tinyint(1) not null  default 1 comment '血缘是否可用,默认1: 可用',
    primary key(`parent_level_field`,`sub_level_field`)
) engine=InnoDB DEFAULT CHARSET=utf8mb4;
