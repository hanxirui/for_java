/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50716
 Source Host           : localhost
 Source Database       : link_empty

 Target Server Type    : MySQL
 Target Server Version : 50716
 File Encoding         : utf-8

 Date: 02/16/2017 17:24:57 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_app_message`
-- ----------------------------
DROP TABLE IF EXISTS `t_app_message`;
CREATE TABLE `t_app_message` (
  `id` varchar(64) NOT NULL,
  `school_code` varchar(64) DEFAULT NULL COMMENT '学校编码',
  `user_group` varchar(64) DEFAULT NULL COMMENT '用户组',
  `receivers` varchar(2000) DEFAULT NULL COMMENT '接收人',
  `type` varchar(2) DEFAULT NULL COMMENT '消息类型',
  `title` varchar(64) DEFAULT NULL COMMENT '消息标题',
  `body` varchar(512) DEFAULT NULL COMMENT '消息内容',
  `expired_time` int(15) DEFAULT NULL COMMENT '结束时间',
  `action_type` tinyint(1) DEFAULT NULL COMMENT '操作类型:0-无;1-url;2-内部服务',
  `button_name` varchar(32) DEFAULT NULL COMMENT '按钮名称',
  `action_url` varchar(128) DEFAULT NULL COMMENT '按钮访问的URL',
  `app_name` varchar(64) DEFAULT NULL COMMENT '应用ID',
  `time` int(15) DEFAULT NULL COMMENT '平台收到消息的时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='应用消息';

SET FOREIGN_KEY_CHECKS = 1;
