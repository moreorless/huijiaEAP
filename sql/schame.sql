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
  `id` int(11) unsigned NOT NULL,
  `name` varchar(256) NOT NULL,
  `type` tinyint(4) NOT NULL default '0' COMMENT '0,独立试卷;1,父试卷;2,子试卷',
  `description` text,
  `icon` varchar(256) default NULL,
  `parentId` int(11) default NULL COMMENT '如果问卷类型为子试卷，填上父试卷的id',
  `children` varchar(128) default '' COMMENT '如果问卷类型为父试卷，填写子试卷的id.格式为"1,3,5"',
  `categoryJson` varchar(512) default NULL COMMENT '{维度ID,维度名称}的Json对象',
  `createBy` int(11) unsigned default NULL,
  `createAt` int(11) unsigned default NULL,
  `updateBy` int(11) unsigned default NULL,
  `updateAt` int(11) unsigned default NULL,
  `status` tinyint(4) unsigned default NULL,
  `categoryNum` int(8) default '0' COMMENT '维度个数',
  `itemNum` int(8) default '0' COMMENT '题目个数',
  `lieBorder` int(8) default '0' COMMENT '测谎题分数分割线',
  `guideline` text COMMENT '指导语',
  `reporttpl` varchar(128) default NULL COMMENT '报告模板文件名',
  PRIMARY KEY  (`id`),
  KEY `name` (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='试卷';

DROP TABLE IF EXISTS `quiz_evaluation`;
CREATE TABLE `quiz_evaluation` (
  `id` int(11) NOT NULL,
  `quizid` int(11) NOT NULL,
  `categoryId` int(8) NOT NULL COMMENT '维度ID',
  `categoryName` varchar(255) NOT NULL default '' COMMENT '维度名称',
  `minScore` int(10) NOT NULL,
  `maxScore` int(10) NOT NULL,
  `evaluation` text COMMENT '结果评价',
  `suggestion` text COMMENT '建议',
  `type` varchar(16) default '' COMMENT '个人报告还是团体报告,single or team?',
  `healthStatus` varchar(16) default '' COMMENT '健康状况',
  `explanation` text COMMENT '解释',
  `feature` text COMMENT '特征（关键词）'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='试题评价标准';

DROP TABLE IF EXISTS `quiz_item`;
CREATE TABLE `quiz_item` (
  `id` int(10) NOT NULL,
  `question` text NOT NULL,
  `lieFlag` int(1) NOT NULL,
  `optionJson` varchar(512) NOT NULL default '' COMMENT '{选项内容;选项维度;选项分值}',
  `createBy` varchar(512) default NULL,
  `createAt` varchar(512) default NULL,
  `updateBy` varchar(512) default NULL,
  `updateAt` varchar(512) default NULL,
  `quizId` int(10) default NULL COMMENT '对应的问卷ID'
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='题目';


DROP TABLE IF EXISTS `company`;
CREATE TABLE `company` (
	`id` INT(11) NOT NULL,
	`name` VARCHAR(256) NOT NULL,
	`code` VARCHAR(4) NOT NULL,
	`description` VARCHAR(2048)
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
INSERT INTO auth_user VALUES (1,'admin','超级管理员','d033e22ae348aeb5660fc2140aec35850c4da997',NULL,NULL,NULL,1,NULL,0,0,0,0,0,0,NULL,NULL,NULL);
INSERT INTO sys_tblids VALUES ('auth_user', 2);
