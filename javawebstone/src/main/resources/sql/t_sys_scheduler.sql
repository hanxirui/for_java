/*
 Navicat Premium Data Transfer

 Source Server         : 117.21.225.211
 Source Server Type    : MySQL
 Source Server Version : 50626
 Source Host           : 117.21.225.211
 Source Database       : snake

 Target Server Type    : MySQL
 Target Server Version : 50626
 File Encoding         : utf-8

 Date: 11/17/2015 21:11:54 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_sys_scheduler`
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_scheduler`;
CREATE TABLE `t_sys_scheduler` (
  `C_ID` varchar(50) NOT NULL COMMENT '主键',
  `C_START_TIME` datetime DEFAULT NULL COMMENT '开始时间',
  `C_END_TIME` datetime DEFAULT NULL COMMENT '结束时间',
  `C_INTERVAL` int(10) DEFAULT NULL COMMENT '间隔',
  `C_INTERVAL_UNIT` varchar(10) DEFAULT NULL COMMENT '间隔单位',
  `C_JOB_CLASSNAME` varchar(500) DEFAULT NULL COMMENT 'Job类名',
  `C_CYCLE_TYPE` varchar(10) DEFAULT NULL COMMENT '循环类型',
  `C_SEC` smallint(2) DEFAULT NULL COMMENT '触发秒',
  `C_MIN` smallint(2) DEFAULT NULL COMMENT '触发分钟',
  `C_HOUR` smallint(2) DEFAULT NULL COMMENT '触发小时',
  `C_DAY` smallint(2) DEFAULT NULL COMMENT '触发天',
  `C_LASTDAYOFMONTH` tinyint(1) DEFAULT '0' COMMENT '否是当月最后一天,两个值0是、1否',
  `C_WEEK_OPTS` bigint(20) DEFAULT NULL COMMENT '周操作符',
  `C_MONTH_OPTS` bigint(20) DEFAULT NULL COMMENT '月操作符',
  `C_CRON_EXP` varchar(200) DEFAULT NULL COMMENT 'cron表达式',
  PRIMARY KEY (`C_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用程序调度表';

SET FOREIGN_KEY_CHECKS = 1;
