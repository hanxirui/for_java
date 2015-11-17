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

 Date: 11/17/2015 21:12:02 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_sys_scheduler_params`
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_scheduler_params`;
CREATE TABLE `t_sys_scheduler_params` (
  `c_schd_id` varchar(50) NOT NULL COMMENT 'scheduler id',
  `c_param_key` varchar(500) NOT NULL COMMENT 'scheduler 参数key',
  `c_param_val` varchar(2000) NOT NULL COMMENT 'scheduler 参数value'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='应用程序调度参数表';

SET FOREIGN_KEY_CHECKS = 1;
