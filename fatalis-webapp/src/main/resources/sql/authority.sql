-- ----------------------------
-- 用户信息表
-- ----------------------------
DROP TABLE IF EXISTS `fata_user`;
CREATE TABLE `fata_user` (
  `user_id`       VARCHAR(32) NOT NULL COMMENT '用户id',
  `user_name`     VARCHAR(20) COMMENT '用户姓名',
  `mobile`        VARCHAR(20) COMMENT '手机号',
  `password`      VARCHAR(100) COMMENT '密码',
  `salt`          VARCHAR(32) COMMENT '自定义盐值',
  `create_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status`        VARCHAR(2) DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`user_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

insert into fata_user(user_id, user_name, mobile, password, salt)
    values('001', '陶仲文', '18888000088', '026e33e83d4773801176540e030596c9a190e0a85cc4f702', 'lawrence');
insert into fata_user(user_id, user_name, mobile, password, salt)
    values('002', '陶仲贤', '18888000087', '026e33e83d4773801176540e030596c9a190e0a85cc4f702', 'lawrence');



-- ----------------------------
-- 用户角色中间表
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id`       VARCHAR(32) NOT NULL COMMENT '用户id',
  `role_id`       VARCHAR(32) NOT NULL COMMENT '角色id',
  `create_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status`        VARCHAR(2) DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`user_id`, `role_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

insert into user_role(user_id, role_id) values('001', '001');
insert into user_role(user_id, role_id) values('002', '002');



-- ----------------------------
-- 角色信息表
-- ----------------------------
DROP TABLE IF EXISTS `fata_role`;
CREATE TABLE `fata_role` (
  `role_id`       VARCHAR(32) NOT NULL COMMENT '角色id',
  `role_name`     VARCHAR(20) COMMENT '角色名称',
  `role_code`     VARCHAR(32) NOT NULL COMMENT '角色代码',
  `create_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status`        VARCHAR(2) DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`role_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

insert into fata_role(role_id, role_name, role_code) values('001', '管理员', 'admin');
insert into fata_role(role_id, role_name, role_code) values('002', '操作者', 'operator');



-- ----------------------------
-- 角色权限中间表
-- ----------------------------
DROP TABLE IF EXISTS `role_authority`;
CREATE TABLE `role_authority` (
  `role_id`       VARCHAR(32) NOT NULL COMMENT '角色id',
  `authority_id`  VARCHAR(32) NOT NULL COMMENT '权限id',
  `create_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`   TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status`        VARCHAR(2) DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`role_id`, `authority_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

insert into role_authority(role_id, authority_id) values('001', '001');
insert into role_authority(role_id, authority_id) values('001', '002');
insert into role_authority(role_id, authority_id) values('002', '001');



-- ----------------------------
-- 权限信息表
-- ----------------------------
DROP TABLE IF EXISTS `fata_authority`;
CREATE TABLE `fata_authority` (
  `authority_id`        VARCHAR(32) NOT NULL COMMENT '权限id',
  `authority_name`      VARCHAR(20) COMMENT '权限名称',
  `main_url`            VARCHAR(50) COMMENT '权限主url',
  `branch_url`          MEDIUMTEXT COMMENT '权限分url',
  `permission`          VARCHAR(10) COMMENT 'shiro权限表达式',
  `sort`                VARCHAR(10) COMMENT '排序字段',
  `create_time`         TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time`         TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `status`              VARCHAR(2) DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`authority_id`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

insert into fata_authority(authority_id, authority_name, main_url, branch_url, permission, sort)
    values('001', '业务1', '/business', '{"/busi1":""}', 'authc', '1');
insert into fata_authority(authority_id, authority_name, main_url, branch_url, permission, sort)
    values('002', '业务2', '/business', '{"/busi2":""}', 'anon', '2');


