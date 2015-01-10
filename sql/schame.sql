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
	dept VARCHAR(32) NULL DEFAULT NULL,
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


# Dumping structure for table sys_tblids
DROP TABLE IF EXISTS `sys_tblids`;
CREATE TABLE IF NOT EXISTS `sys_tblids` (
  `name` varchar(32) NOT NULL,
  `maxid` bigint(20) NOT NULL,
  PRIMARY KEY (`name`),
  UNIQUE KEY `name` (`name`)
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


-- admin / admin
INSERT INTO auth_user VALUES (1,'admin','超级管理员','d033e22ae348aeb5660fc2140aec35850c4da997',NULL,NULL,NULL,1,NULL,0,0,0,0,0,NULL,NULL,NULL);

