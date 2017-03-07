/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50716
 Source Host           : localhost
 Source Database       : link

 Target Server Type    : MySQL
 Target Server Version : 50716
 File Encoding         : utf-8

 Date: 02/18/2017 10:57:03 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_app_msg_setting`
-- ----------------------------
DROP TABLE IF EXISTS `t_app_msg_setting`;
CREATE TABLE `t_app_msg_setting` (
  `userId` bigint(20) NOT NULL,
  `appId` varchar(64) NOT NULL,
  `receive` int(2) DEFAULT '0' COMMENT '接收消息类型,目前没有使用,只要表里面有记录表示不接收消息.',
  PRIMARY KEY (`userId`,`appId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
