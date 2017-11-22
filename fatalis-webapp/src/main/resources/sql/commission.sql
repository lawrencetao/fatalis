-- ----------------------------
-- commission表
-- ----------------------------
DROP TABLE IF EXISTS `commission`;
CREATE TABLE `commission` (
  `sid`    VARCHAR(32) NOT NULL,
  `config` VARCHAR(200),
  PRIMARY KEY (`sid`)
)
  ENGINE = InnoDB
  DEFAULT CHARSET = utf8;