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

 Date: 02/20/2017 10:06:05 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `t_memo`
-- ----------------------------
DROP TABLE IF EXISTS `t_memo`;
CREATE TABLE `t_memo` (
  `id` varchar(64) NOT NULL,
  `title` varchar(128) NOT NULL,
  `body` varchar(512) DEFAULT NULL,
  `userId` varchar(64) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
