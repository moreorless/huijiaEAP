# --------------------------------------------------------
# Host:                         127.0.0.1
# Database:                     psychometry
# Server version:               5.2.4-MariaDB
# Server OS:                    Win32
# HeidiSQL version:             5.0.0.3272
# Date/time:                    2011-01-12 17:24:40
# --------------------------------------------------------

# Dumping database structure
CREATE DATABASE IF NOT EXISTS psychometry;
USE psychometry;

DROP TABLE IF EXISTS auth_role;
CREATE TABLE IF NOT EXISTS auth_role (
  roleId bigint(10) NOT NULL,
  name varchar(64) NOT NULL,
  description varchar(256) DEFAULT NULL,
  created bigint(10) DEFAULT NULL,
  modified bigint(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS auth_user;
CREATE TABLE auth_user (
	userid BIGINT(10) NOT NULL,
	name VARCHAR(64) NOT NULL,
	realname VARCHAR(128) NOT NULL,
	password VARCHAR(160) NOT NULL,
	email VARCHAR(128) NULL DEFAULT NULL,
	phone VARCHAR(32) NULL DEFAULT NULL,
	mobile VARCHAR(32) NULL DEFAULT NULL,
	status INT(4) NOT NULL,
	description VARCHAR(256) NULL DEFAULT NULL,
	type INT(4) NOT NULL,
	lastLoginTime BIGINT(10) NULL DEFAULT NULL,
	lockedAt BIGINT(10) NULL DEFAULT NULL,
	created BIGINT(10) NULL DEFAULT NULL,
	modified BIGINT(10) NULL DEFAULT NULL,
	companyid INT(11) NULL DEFAULT NULL,
	dept VARCHAR(128) NULL DEFAULT NULL,
	iprestrict VARCHAR(128) NULL DEFAULT NULL,
	authedNavs VARCHAR(512) NULL DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


# Dumping structure for table auth_userrole_relation
DROP TABLE IF EXISTS auth_userrole_relation;
CREATE TABLE IF NOT EXISTS auth_userrole_relation (
  relationId bigint(10) NOT NULL,
  userId bigint(10) NOT NULL,
  roleId bigint(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping structure for table sys_logs
DROP TABLE IF EXISTS `sys_logs`;
CREATE TABLE IF NOT EXISTS `sys_logs` (
  `timestamp` bigint(20) NOT NULL,
  `userid` bigint(20) NOT NULL,
  `username` varchar(64) NOT NULL,
  `sourceip` varchar(32) DEFAULT NULL,
  `module` varchar(128) DEFAULT NULL,
  `action` varchar(128) DEFAULT NULL,
  `msg` varchar(512) DEFAULT NULL,
  `result` int(4) NOT NULL,
  `cause` varchar(128) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `quiz`;
CREATE TABLE `quiz` (
	`id` INT(11) UNSIGNED NOT NULL,
	`name` VARCHAR(256) NOT NULL,
	`description` TEXT NULL,
	`icon` VARCHAR(256),
	`categories` VARCHAR(512) NULL,
	`createBy` INT(11) UNSIGNED DEFAULT NULL,
	`createAt` INT(11) UNSIGNED DEFAULT NULL,
	`updateBy` INT(11) UNSIGNED DEFAULT NULL,
	`updateAt` INT(11) UNSIGNED DEFAULT NULL,
	`status` TINYINT(4) UNSIGNED NULL,
	PRIMARY KEY (`id`),
	INDEX `name` (`name`)
)
COMMENT='试卷'
ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `quiz_evaluation`;
CREATE TABLE `quiz_evaluation` (
	`id` INT(11) NOT NULL,
	`quizid` INT(11) NOT NULL,
	`categoryId` VARCHAR(50) NOT NULL,
	`categoryName` VARCHAR(255) NOT NULL,
	`minScore` INT(10) NOT NULL,
	`maxScore` INT(10) NOT NULL,
	`evaluation` TEXT NULL,
	`suggest` TEXT NULL
)
COMMENT='试题评价标准'
ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `quiz_item`;
CREATE TABLE `quiz_item` (
	`id` INT(10) NOT NULL,
	`question` TEXT NOT NULL,
	`category` VARCHAR(256) NOT NULL,
	`lieFlag` INT(1) NOT NULL,
	`optionsJson` VARCHAR(512) NOT NULL,
	`createBy` VARCHAR(512) NULL,
	`createAt` VARCHAR(512) NULL,
	`updateBy` VARCHAR(512) NULL,
	`updateAt` VARCHAR(512) NULL
)
COMMENT='题目'
ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `quiz_item_relation`;
CREATE TABLE `quiz_item_relation` (
	`id` INT(11) NOT NULL,
	`quizid` INT(11) NOT NULL,
	`quizitemid` INT(11) NOT NULL
)
COMMENT='试卷-题目关联'
ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
	`id` INT(11) NOT NULL,
	`name` VARCHAR(256) NOT NULL,
	`description` VARCHAR(2048),
)
COMMENT='企业'
ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `segment`;
CREATE TABLE `segment` (
	`id` INT(11) NOT NULL,
	`companyId` INT(11) NOT NULL,
	`startId` INT(11) NOT NULL COMMENT '起始值',
	`size` INT(11) NOT NULL COMMENT '号段大小',
	`status` INT(4) NOT NULL DEFAULT '1' COMMENT '0:已回收 1:正在使用'
)
COMMENT='号段'
ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `seg_quiz_relation`;
CREATE TABLE `seg_quiz_relation` (
	`id` INT(11) NOT NULL,
	`segmentid` INT(11) NOT NULL,
	`quizid` INT(11) NOT NULL,
	`startDate` VARCHAR(20) NOT NULL COMMENT '开始日期',
	`expireDate` VARCHAR(20) NOT NULL COMMENT '废止日期'
)
COMMENT='号段-试题关联'
ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `quiz_result`;
CREATE TABLE `quiz_result` (
	`id` INT(11) NOT NULL,
	`userid` INT(11) NOT NULL,
	`quizid` INT(11) NOT NULL,
	`testDate` VARCHAR(20) NOT NULL,
	`answer` VARCHAR(512) NOT NULL,
	`score` INT(10) NOT NULL,
	`scoreByCategory` VARCHAR(512) NOT NULL
)
COMMENT='测试结果'
ENGINE=InnoDB DEFAULT CHARSET=utf8;

# Dumping structure for table sys_tblids
DROP TABLE IF EXISTS `sys_tblids`;
CREATE TABLE IF NOT EXISTS `sys_tblids` (
  `name` varchar(32) NOT NULL,
  `maxid` bigint(20) NOT NULL,
  PRIMARY KEY (`name`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- admin / admin
INSERT INTO auth_user VALUES (1,'admin','超级管理员','d033e22ae348aeb5660fc2140aec35850c4da997',NULL,NULL,NULL,1,NULL,0,0,0,0,0,NULL,NULL,NULL);
INSERT INTO sys_tblids VALUES ('auth_user', 1);
