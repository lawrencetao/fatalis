-- ----------------------------
-- commission表
-- ----------------------------
DROP TABLE IF EXISTS `commission`;
CREATE TABLE `commission` (
  `sid`     VARCHAR(32) NOT NULL COMMENT '测试类id',
  `config`  VARCHAR(200) COMMENT '测试类配置',
  PRIMARY KEY (`sid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;