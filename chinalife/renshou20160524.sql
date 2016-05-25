/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50711
 Source Host           : localhost
 Source Database       : renshou

 Target Server Type    : MySQL
 Target Server Version : 50711
 File Encoding         : utf-8

 Date: 05/24/2016 06:57:49 AM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `biz_record`
-- ----------------------------
DROP TABLE IF EXISTS `biz_record`;
CREATE TABLE `biz_record` (
  `id` int(11) NOT NULL,
  `yaoyue_num` varchar(255) DEFAULT NULL COMMENT '邀约客户数',
  `daohui_num` varchar(255) DEFAULT NULL COMMENT '到会客户数',
  `receive_num` varchar(30) DEFAULT NULL COMMENT '当日回收件数',
  `receive_baofei` varchar(30) DEFAULT NULL COMMENT '当日回收保费',
  `daohuilv` varchar(30) DEFAULT NULL COMMENT '到会率',
  `qiandan_num` varchar(30) DEFAULT NULL COMMENT '签单件数',
  `qiandanlv` varchar(30) DEFAULT NULL COMMENT '签单率',
  `huishoulv` varchar(30) DEFAULT NULL COMMENT '回收率',
  `kehujingli` varchar(30) DEFAULT NULL COMMENT '客户经理',
  `qiandan_baofei` varchar(255) DEFAULT NULL COMMENT '当日签单保费',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `bizplatform`
-- ----------------------------
DROP TABLE IF EXISTS `bizplatform`;
CREATE TABLE `bizplatform` (
  `id` int(11) NOT NULL,
  `title` varchar(60) DEFAULT NULL,
  `url` varchar(60) DEFAULT NULL,
  `class` varchar(60) DEFAULT NULL,
  `start` int(13) DEFAULT NULL,
  `end` int(13) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `claim_record`
-- ----------------------------
DROP TABLE IF EXISTS `claim_record`;
CREATE TABLE `claim_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idcardnum` varchar(30) DEFAULT NULL COMMENT '客户身份证号',
  `reason` varchar(600) DEFAULT NULL COMMENT '投诉原因',
  `insuranceid` varchar(30) DEFAULT NULL COMMENT '涉及保单',
  `firstaccount` varchar(30) DEFAULT NULL COMMENT '客户经理',
  `firstcontent` varchar(600) DEFAULT NULL COMMENT '处理内容',
  `firstclaim` varchar(10) DEFAULT NULL COMMENT '赔偿金额',
  `firsttime` varchar(10) DEFAULT NULL COMMENT '处理时间',
  `secondaccount` varchar(30) DEFAULT NULL COMMENT '区域经理',
  `secondcontent` varchar(30) DEFAULT NULL COMMENT '处理内容',
  `secondtime` varchar(30) DEFAULT NULL COMMENT '处理时间',
  `secondclaim` varchar(30) DEFAULT NULL COMMENT '赔偿金额',
  `thirdaccount` varchar(30) DEFAULT NULL COMMENT '部门经理',
  `thirdcontent` varchar(30) DEFAULT NULL COMMENT '处理内容',
  `thirdclaim` varchar(30) DEFAULT NULL COMMENT '赔偿金额',
  `thirdtime` varchar(30) DEFAULT NULL COMMENT '处理时间',
  `fourthaccount` varchar(30) DEFAULT NULL COMMENT '经理室',
  `fourthcontent` varchar(30) DEFAULT NULL COMMENT '处理内容',
  `fourthclaim` varchar(30) DEFAULT NULL COMMENT '赔偿金额',
  `fourthtime` varchar(30) DEFAULT NULL COMMENT '赔偿时间',
  `claimtime` varchar(30) DEFAULT NULL COMMENT '投诉时间',
  `name` varchar(30) DEFAULT NULL COMMENT '客户名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `customer_basic`
-- ----------------------------
DROP TABLE IF EXISTS `customer_basic`;
CREATE TABLE `customer_basic` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `idcardnum` varchar(30) DEFAULT NULL COMMENT '身份证号',
  `type` int(2) DEFAULT NULL COMMENT '类型  1-原始；2-自营新拓；3-渠道新拓',
  `birthday` varchar(30) DEFAULT NULL COMMENT '生日',
  `wedding_day` varchar(30) DEFAULT NULL COMMENT '结婚纪念日',
  `kehujingli` varchar(30) DEFAULT NULL COMMENT '客户经理',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别',
  `laiyuan` varchar(30) DEFAULT NULL COMMENT '初始来源',
  `note` varchar(600) DEFAULT NULL COMMENT '爱好及特点',
  `phone` varchar(30) DEFAULT NULL,
  `addr` varchar(30) DEFAULT NULL,
  `daorurenyuan` varchar(30) DEFAULT NULL COMMENT '导入人员',
  PRIMARY KEY (`id`),
  KEY `tuijianren` (`type`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `customer_basic`
-- ----------------------------
BEGIN;
INSERT INTO `customer_basic` VALUES ('1', '全冠清', '120', '2', '2016-05-08', '2016-04-26', '11200000', '1', '2', '1', null, '天朝', null), ('2', '乔丹', '121', '1', '2016-05-04', '2016-05-04', '81000280', '1', null, '打篮球', null, '天津市', null), ('3', '钟灵', '122', '1', '2015-04-01', '2016-05-04', '11200467', '0', null, '玩', null, '万仇谷', null), ('4', '木婉清', '123', '1', '2016-05-26', '2016-05-25', '11200467', '0', null, '打篮球', null, '的', null), ('5', '穆念慈', '124', '1', '2011-05-05', '2016-05-04', '11200467', '0', null, '是', null, '12', null), ('6', '123', '125', '2', '2016-05-09', '2016-05-09', '81000280', '1', '3', '123', null, '123', null), ('7', 'qwe', '126', '1', '2016-05-10', '2016-05-10', '11233432', '1', null, 'qwe', null, 'qwe', null);
COMMIT;

-- ----------------------------
--  Table structure for `customer_extras`
-- ----------------------------
DROP TABLE IF EXISTS `customer_extras`;
CREATE TABLE `customer_extras` (
  `id` int(11) NOT NULL,
  `idcardnum` varchar(30) DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(30) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(30) DEFAULT NULL COMMENT '手机',
  `car_band` varchar(30) DEFAULT NULL COMMENT '车品牌',
  `car_num` varchar(30) DEFAULT NULL COMMENT '车牌号',
  `addr` varchar(30) DEFAULT NULL COMMENT '住址',
  `empcode` varchar(30) DEFAULT NULL COMMENT '维护人',
  `insert_date` varchar(30) DEFAULT NULL COMMENT '维护日期',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `customer_extras`
-- ----------------------------
BEGIN;
INSERT INTO `customer_extras` VALUES ('1', '120123456789', '13311234321', '13311234321', 'qq', '津A123456', '天津市和平区', 'zhang', null);
COMMIT;

-- ----------------------------
--  Table structure for `employee`
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `role` int(1) DEFAULT NULL COMMENT '职务',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `code` varchar(30) DEFAULT NULL COMMENT '工号',
  `idcardnum` varchar(30) DEFAULT NULL COMMENT '身份证号',
  `orgname` varchar(30) DEFAULT NULL COMMENT '所属公司',
  `orgcode` varchar(30) DEFAULT NULL COMMENT '公司代码',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别',
  `phone` varchar(30) DEFAULT NULL COMMENT '电话',
  `jointime` varchar(30) DEFAULT NULL COMMENT '入司时间',
  `managercode` varchar(30) DEFAULT NULL COMMENT '直接上级',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `employee`
-- ----------------------------
BEGIN;
INSERT INTO `employee` VALUES ('8', 'admin', '1', 'b594510740d2ac4261c1b2fe87850d08', 'admin', '1', '分公司', '120000', '0', '1', '2016-05-01', '0'), ('14', '夏菁菁', '1', '4671e0100ba8dcbcf3a148f7297e817f', '11200467', '1', '第三营业部（二支）', '120232', '0', '13602084421', '2007-05-01', '0'), ('15', '刘芯羽', '12', 'cca3e61bc2810d7458c69c3574262341', '81000280', '123343232112121212', '第三营业部（二支）', '120232', '0', '13820302201', '2015-04-02', '11200467'), ('16', '刘伟', '3', 'c0fb40f16c803ddbd06d609da268be8d', '11200000', '123456123432123456', '第三营业部（二支）', '120232', '1', '13212187953', '2007-05-01', '11200467'), ('17', '刘旭辉', '2', '64808adfdfbfa8a0d5dcc7dba84906f1', '11234321', '123421121212121212', '第二营业部（一支）', '120231', '1', '13512876032', '2003-05-01', '0'), ('18', '李广杰', '1', '3124ff1b700d643695db85cf4519c3e1', '11233432', '12312345667777777', '分公司', '120000', '1', '13820385717', '1991-05-01', '0'), ('19', '李超', '7', 'ffb9decafc5b6d12fe653bee56b75d8f', '81000000', '123454343434343434', '第三营业部（二支）', '120232', '1', '1234', '2016-05-03', '11200467');
COMMIT;

-- ----------------------------
--  Table structure for `insurance_record`
-- ----------------------------
DROP TABLE IF EXISTS `insurance_record`;
CREATE TABLE `insurance_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `baoxiandanhao` varchar(30) DEFAULT NULL COMMENT '保单号',
  `toubaodanhao` varchar(30) DEFAULT NULL COMMENT '投保单号',
  `yewuyuandaima` varchar(30) DEFAULT NULL COMMENT '业务员代码',
  `yewuyuanxingming` varchar(30) DEFAULT NULL COMMENT '业务员姓名',
  `xianzhongmingcheng` varchar(30) DEFAULT NULL COMMENT '险种名称',
  `baodanzhuangtai` varchar(30) DEFAULT NULL COMMENT '保单状态',
  `toubaoriqi` varchar(30) DEFAULT NULL COMMENT '投保日期',
  `shengxiaoriqi` varchar(30) DEFAULT NULL COMMENT '生效日期',
  `jibenbaoe` varchar(30) DEFAULT NULL COMMENT '基本保额',
  `jibenbaofei` varchar(30) DEFAULT NULL COMMENT '基本保费',
  `toubaorenxingming` varchar(30) DEFAULT NULL COMMENT '投保人姓名',
  `toubaorenxingbie` varchar(30) DEFAULT NULL COMMENT '投保人性别',
  `toubaorenshenfenzhenghao` varchar(30) DEFAULT NULL COMMENT '投保人身份证号',
  `toubaorenshoujihao` varchar(30) DEFAULT NULL COMMENT '投保人手机号',
  `toubaorentongxundizhi` varchar(30) DEFAULT NULL COMMENT '投保人通讯地址',
  `toubaorenzhiye` varchar(30) DEFAULT NULL COMMENT '投保人职业',
  `beibaoxianrenxingming` varchar(30) DEFAULT NULL COMMENT '被保人姓名',
  `beibaoxianrenxingbie` varchar(30) DEFAULT NULL COMMENT '被保险人性别',
  `beibaoxianrenshenfenzhenghao` varchar(30) DEFAULT NULL COMMENT '被保险人身份证号',
  `beibaoxianrenshoujihao` varchar(30) DEFAULT NULL COMMENT '被保险人手机号',
  `beibaoxianrentongxundizhi` varchar(30) DEFAULT NULL COMMENT '被保险人通讯地址',
  `beibaoxianrenzhiye` varchar(30) DEFAULT NULL COMMENT '被保险人职业',
  `beibaoxianrenyutoubaorenguanxi` varchar(30) DEFAULT NULL COMMENT '被保险人与投保人关系',
  `shouyirenxingming` varchar(30) DEFAULT NULL COMMENT '受益人姓名',
  `shouyirenxingbie` varchar(30) DEFAULT NULL COMMENT '受益人性别',
  `shouyirenshenfenzhenghao` varchar(30) DEFAULT NULL COMMENT '受益人身份证号',
  `shouyishunxu` varchar(30) DEFAULT NULL COMMENT '受益顺序',
  `shouyifene` varchar(30) DEFAULT NULL COMMENT '受益份额',
  `shouyirenyutoubaorenguanxi` varchar(30) DEFAULT NULL COMMENT '受益人与投保人关系',
  `jiaofeiqi` varchar(30) DEFAULT NULL COMMENT '缴费期',
  `baoxianqi` varchar(30) DEFAULT NULL COMMENT '保险期',
  `jiaofeiyinhang` varchar(30) DEFAULT NULL COMMENT '缴费银行',
  `jiaofeizhanghao` varchar(30) DEFAULT NULL COMMENT '缴费账号',
  `chushilaiyuan` varchar(30) DEFAULT NULL COMMENT '初始来源',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `insurance_record`
-- ----------------------------
BEGIN;
INSERT INTO `insurance_record` VALUES ('1', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', '123', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', '11', null), ('2', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', '121', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', 'SS11', null), ('3', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', '122', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', '12', null), ('4', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', '124', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', '13', null);
COMMIT;

-- ----------------------------
--  Table structure for `login_record`
-- ----------------------------
DROP TABLE IF EXISTS `login_record`;
CREATE TABLE `login_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `empcode` varchar(60) NOT NULL COMMENT '登录人员编号',
  `ip` varchar(60) DEFAULT NULL COMMENT 'IP地址',
  `date` datetime DEFAULT NULL COMMENT '登录时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=174 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `login_record`
-- ----------------------------
BEGIN;
INSERT INTO `login_record` VALUES ('1', 'admin', '127.0.0.1', '2016-04-20 18:10:58'), ('2', 'admin', '127.0.0.1', '2016-04-21 23:26:39'), ('3', 'admin', '127.0.0.1', '2016-04-24 22:03:23'), ('4', 'admin', '127.0.0.1', '2016-04-24 22:11:18'), ('5', 'admin', '127.0.0.1', '2016-04-24 22:42:32'), ('6', 'admin', '127.0.0.1', '2016-04-24 22:44:39'), ('7', 'admin', '127.0.0.1', '2016-04-24 22:46:53'), ('8', 'admin', '127.0.0.1', '2016-04-25 20:24:54'), ('9', 'admin', '127.0.0.1', '2016-04-25 20:36:15'), ('10', 'admin', '127.0.0.1', '2016-04-25 20:49:08'), ('11', 'admin', '127.0.0.1', '2016-04-25 20:55:52'), ('12', 'admin', '127.0.0.1', '2016-04-25 21:09:26'), ('13', 'admin', '127.0.0.1', '2016-04-25 21:13:10'), ('14', 'admin', '127.0.0.1', '2016-04-25 21:17:37'), ('15', 'admin', '127.0.0.1', '2016-04-25 22:27:21'), ('16', 'admin', '127.0.0.1', '2016-04-26 16:47:06'), ('17', 'admin', '127.0.0.1', '2016-04-26 16:49:56'), ('18', 'admin', '127.0.0.1', '2016-04-26 17:27:41'), ('19', 'admin', '127.0.0.1', '2016-04-26 20:48:17'), ('20', 'admin', '127.0.0.1', '2016-04-26 20:51:02'), ('21', 'admin', '127.0.0.1', '2016-04-26 21:18:52'), ('22', 'admin', '127.0.0.1', '2016-04-26 21:29:46'), ('23', 'admin', '127.0.0.1', '2016-04-26 22:01:07'), ('24', 'admin', '127.0.0.1', '2016-04-26 22:05:20'), ('25', 'admin', '127.0.0.1', '2016-04-26 22:18:35'), ('26', 'admin', '127.0.0.1', '2016-04-26 22:24:41'), ('27', 'admin', '127.0.0.1', '2016-04-26 22:26:26'), ('28', 'admin', '127.0.0.1', '2016-04-26 22:33:15'), ('29', 'admin', '127.0.0.1', '2016-04-28 10:49:39'), ('30', 'admin', '127.0.0.1', '2016-04-28 22:24:16'), ('31', 'admin', '127.0.0.1', '2016-04-30 14:48:42'), ('32', 'admin', '127.0.0.1', '2016-04-30 14:49:31'), ('33', 'admin', '127.0.0.1', '2016-04-30 14:51:56'), ('34', 'admin', '127.0.0.1', '2016-04-30 15:23:02'), ('35', 'admin', '127.0.0.1', '2016-04-30 15:25:22'), ('36', 'admin', '127.0.0.1', '2016-04-30 19:27:03'), ('37', 'admin', '127.0.0.1', '2016-04-30 19:48:32'), ('38', 'admin', '127.0.0.1', '2016-04-30 20:32:41'), ('39', 'admin', '127.0.0.1', '2016-04-30 20:37:56'), ('40', 'admin', '127.0.0.1', '2016-04-30 20:47:11'), ('41', 'xf', '127.0.0.1', '2016-04-30 20:49:52'), ('42', 'xf', '127.0.0.1', '2016-04-30 21:14:43'), ('43', 'admin', '127.0.0.1', '2016-05-01 06:58:28'), ('44', 'admin', '127.0.0.1', '2016-05-01 06:59:57'), ('45', 'mrf', '127.0.0.1', '2016-05-01 07:58:31'), ('46', 'admin', '127.0.0.1', '2016-05-01 07:58:52'), ('47', 'admin', '127.0.0.1', '2016-05-01 08:01:43'), ('48', 'xf', '127.0.0.1', '2016-05-01 14:29:20'), ('49', 'xf', '127.0.0.1', '2016-05-01 14:32:16'), ('50', 'admin', '127.0.0.1', '2016-05-01 14:36:56'), ('51', 'zqs', '127.0.0.1', '2016-05-01 14:39:24'), ('52', 'zqs', '127.0.0.1', '2016-05-01 14:42:03'), ('53', 'zqs', '127.0.0.1', '2016-05-01 14:44:07'), ('54', 'zqs', '127.0.0.1', '2016-05-01 14:52:44'), ('55', 'zqs', '127.0.0.1', '2016-05-01 14:55:36'), ('56', 'admin', '127.0.0.1', '2016-05-01 14:59:33'), ('57', 'zqs', '127.0.0.1', '2016-05-01 17:34:07'), ('58', 'zqs', '127.0.0.1', '2016-05-01 18:53:49'), ('59', 'admin', '127.0.0.1', '2016-05-02 11:14:00'), ('60', 'zqs', '127.0.0.1', '2016-05-02 11:18:42'), ('61', 'admin', '127.0.0.1', '2016-05-03 23:22:35'), ('62', 'admin', '127.0.0.1', '2016-05-04 12:32:58'), ('63', 'zqs', '127.0.0.1', '2016-05-04 12:34:32'), ('64', 'gt', '127.0.0.1', '2016-05-04 12:35:41'), ('65', 'gt', '127.0.0.1', '2016-05-04 12:37:01'), ('66', 'gt', '127.0.0.1', '2016-05-04 12:41:25'), ('67', 'gt', '127.0.0.1', '2016-05-04 12:43:37'), ('68', 'zqs', '127.0.0.1', '2016-05-04 13:05:19'), ('69', 'zqs', '127.0.0.1', '2016-05-04 13:05:19'), ('70', 'zqs', '127.0.0.1', '2016-05-04 22:33:44'), ('71', 'zqs', '127.0.0.1', '2016-05-04 22:36:46'), ('72', 'zqs', '127.0.0.1', '2016-05-04 22:42:30'), ('73', 'zqs', '127.0.0.1', '2016-05-04 22:43:16'), ('74', 'zqs', '127.0.0.1', '2016-05-04 22:44:57'), ('75', 'zqs', '127.0.0.1', '2016-05-04 22:55:50'), ('76', 'zqs', '127.0.0.1', '2016-05-04 23:01:19'), ('77', 'zqs', '127.0.0.1', '2016-05-04 23:09:22'), ('78', 'zqs', '127.0.0.1', '2016-05-04 23:11:18'), ('79', 'zqs', '127.0.0.1', '2016-05-05 15:14:43'), ('80', 'zqs', '127.0.0.1', '2016-05-05 15:35:26'), ('81', 'zqs', '127.0.0.1', '2016-05-05 15:44:53'), ('82', 'zqs', '127.0.0.1', '2016-05-05 16:56:17'), ('83', 'zqs', '127.0.0.1', '2016-05-05 20:17:53'), ('84', 'zqs', '127.0.0.1', '2016-05-05 20:26:27'), ('85', 'zqs', '127.0.0.1', '2016-05-05 20:53:33'), ('86', 'zqs', '127.0.0.1', '2016-05-05 21:26:48'), ('87', 'zqs', '127.0.0.1', '2016-05-05 21:40:04'), ('88', 'zqs', '127.0.0.1', '2016-05-05 21:42:22'), ('89', 'zqs', '127.0.0.1', '2016-05-05 21:44:32'), ('90', 'zqs', '127.0.0.1', '2016-05-05 21:49:59'), ('91', 'zqs', '127.0.0.1', '2016-05-06 06:27:49'), ('92', 'zqs', '127.0.0.1', '2016-05-06 06:31:34'), ('93', 'zqs', '127.0.0.1', '2016-05-06 06:42:33'), ('94', 'zqs', '127.0.0.1', '2016-05-06 06:44:01'), ('95', 'zqs', '127.0.0.1', '2016-05-06 06:47:31'), ('96', 'gt', '127.0.0.1', '2016-05-06 06:47:45'), ('97', 'zqs', '127.0.0.1', '2016-05-06 08:49:18'), ('98', 'zqs', '127.0.0.1', '2016-05-06 09:47:14'), ('99', 'zqs', '127.0.0.1', '2016-05-06 11:59:33'), ('100', 'zqs', '127.0.0.1', '2016-05-08 04:13:42'), ('101', 'zqs', '127.0.0.1', '2016-05-08 04:34:17'), ('102', 'zqs', '127.0.0.1', '2016-05-08 04:35:48'), ('103', 'zqs', '127.0.0.1', '2016-05-08 04:37:55'), ('104', 'zqs', '127.0.0.1', '2016-05-08 04:43:51'), ('105', 'zqs', '127.0.0.1', '2016-05-08 05:01:40'), ('106', 'zqs', '127.0.0.1', '2016-05-08 05:07:12'), ('107', 'zqs', '127.0.0.1', '2016-05-08 05:21:48'), ('108', 'zqs', '127.0.0.1', '2016-05-08 05:59:50'), ('109', 'zqs', '127.0.0.1', '2016-05-08 20:23:23'), ('110', 'zqs', '127.0.0.1', '2016-05-08 20:25:11'), ('111', 'zqs', '127.0.0.1', '2016-05-08 20:45:06'), ('112', 'zqs', '127.0.0.1', '2016-05-08 21:27:06'), ('113', 'zqs', '127.0.0.1', '2016-05-08 21:40:29'), ('114', 'zqs', '127.0.0.1', '2016-05-08 21:41:19'), ('115', 'admin', '127.0.0.1', '2016-05-08 21:49:54'), ('116', 'zqs', '127.0.0.1', '2016-05-08 21:57:20'), ('117', 'admin', '127.0.0.1', '2016-05-08 22:17:23'), ('118', 'admin', '127.0.0.1', '2016-05-08 22:25:29'), ('119', 'zqs', '127.0.0.1', '2016-05-08 22:26:11'), ('120', 'zqs', '127.0.0.1', '2016-05-08 22:30:12'), ('121', 'zqs', '127.0.0.1', '2016-05-09 20:32:16'), ('122', 'zqs', '127.0.0.1', '2016-05-09 21:04:18'), ('123', 'zqs', '127.0.0.1', '2016-05-09 21:04:56'), ('124', 'zqs', '127.0.0.1', '2016-05-10 05:48:52'), ('125', 'zqs', '127.0.0.1', '2016-05-10 05:55:00'), ('126', 'zqs', '127.0.0.1', '2016-05-10 06:31:19'), ('127', 'zqs', '127.0.0.1', '2016-05-10 06:50:02'), ('128', 'zqs', '127.0.0.1', '2016-05-10 06:52:22'), ('129', 'zqs', '127.0.0.1', '2016-05-10 07:00:20'), ('130', 'zqs', '127.0.0.1', '2016-05-10 07:04:34'), ('131', 'zqs', '127.0.0.1', '2016-05-10 08:54:34'), ('132', 'zqs', '127.0.0.1', '2016-05-10 09:12:42'), ('133', 'zqs', '127.0.0.1', '2016-05-10 09:16:06'), ('134', 'zqs', '127.0.0.1', '2016-05-10 09:16:54'), ('135', 'zqs', '127.0.0.1', '2016-05-10 09:18:39'), ('136', 'zqs', '127.0.0.1', '2016-05-10 09:20:29'), ('137', 'zqs', '127.0.0.1', '2016-05-10 09:22:45'), ('138', 'zqs', '127.0.0.1', '2016-05-10 14:21:56'), ('139', 'zqs', '127.0.0.1', '2016-05-10 14:38:45'), ('140', 'zqs', '127.0.0.1', '2016-05-10 15:15:14'), ('141', 'zqs', '127.0.0.1', '2016-05-10 15:25:30'), ('142', 'zqs', '127.0.0.1', '2016-05-10 15:32:49'), ('143', 'admin', '127.0.0.1', '2016-05-10 22:46:04'), ('144', 'admin', '127.0.0.1', '2016-05-10 22:56:03'), ('145', 'admin', '127.0.0.1', '2016-05-10 22:56:55'), ('146', 'admin', '127.0.0.1', '2016-05-10 22:57:47'), ('147', 'admin', '127.0.0.1', '2016-05-11 20:37:36'), ('148', 'admin', '127.0.0.1', '2016-05-21 21:06:11'), ('149', 'admin', '127.0.0.1', '2016-05-21 21:09:26'), ('150', 'admin', '127.0.0.1', '2016-05-21 21:10:40'), ('151', 'admin', '127.0.0.1', '2016-05-21 21:26:54'), ('152', 'admin', '127.0.0.1', '2016-05-21 22:17:43'), ('153', 'admin', '127.0.0.1', '2016-05-21 22:34:24'), ('154', 'admin', '127.0.0.1', '2016-05-21 22:36:22'), ('155', 'admin', '127.0.0.1', '2016-05-22 21:25:04'), ('156', 'admin', '127.0.0.1', '2016-05-22 21:55:19'), ('157', 'admin', '127.0.0.1', '2016-05-23 07:08:23'), ('158', '81000280', '127.0.0.1', '2016-05-23 07:14:28'), ('159', 'admin', '127.0.0.1', '2016-05-23 07:14:38'), ('160', '11200467', '127.0.0.1', '2016-05-23 07:15:02'), ('161', 'admin', '127.0.0.1', '2016-05-23 07:56:27'), ('162', 'admin', '127.0.0.1', '2016-05-23 09:13:28'), ('163', 'admin', '127.0.0.1', '2016-05-23 09:44:37'), ('164', 'admin', '127.0.0.1', '2016-05-23 10:37:51'), ('165', 'admin', '127.0.0.1', '2016-05-23 12:55:42'), ('166', 'admin', '127.0.0.1', '2016-05-23 13:11:16'), ('167', 'admin', '127.0.0.1', '2016-05-23 13:15:12'), ('168', '11200467', '127.0.0.1', '2016-05-23 13:16:28'), ('169', '81000280', '127.0.0.1', '2016-05-23 13:18:05'), ('170', 'admin', '127.0.0.1', '2016-05-23 13:20:15'), ('171', '11200467', '127.0.0.1', '2016-05-23 13:20:57'), ('172', 'admin', '127.0.0.1', '2016-05-23 20:52:20'), ('173', 'admin', '127.0.0.1', '2016-05-23 22:25:49');
COMMIT;

-- ----------------------------
--  Table structure for `org`
-- ----------------------------
DROP TABLE IF EXISTS `org`;
CREATE TABLE `org` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `code` varchar(30) DEFAULT NULL COMMENT '机构号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `org`
-- ----------------------------
BEGIN;
INSERT INTO `org` VALUES ('1', '大港', '120109'), ('2', '汉沽', '120108'), ('3', '滨海银保', '120107'), ('5', '东丽', '120110'), ('6', '西青', '120111'), ('7', '津南', '120112'), ('8', '北辰', '120113'), ('9', '宁河', '120221'), ('10', '武清', '120222'), ('11', '静海', '120223'), ('12', '宝坻', '120224'), ('13', '蓟县', '120225'), ('14', '第二营业部（一支）', '120231'), ('15', '第三营业部（二支）', '120232'), ('16', '和平支公司（理财）', '120235'), ('17', '第一营业部（三支）', '120238'), ('18', '分公司', '120000');
COMMIT;

-- ----------------------------
--  Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `level` int(2) NOT NULL COMMENT '级别',
  `name` varchar(60) NOT NULL COMMENT '职务',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `role`
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES ('1', '5', '分公司银保部'), ('2', '4', '经理室'), ('3', '3', '部门经理'), ('4', '3', '内勤'), ('5', '2', '初级区域经理'), ('6', '2', '中级区域经理'), ('7', '2', '高级区域经理'), ('8', '2', '组经理'), ('9', '2', '处经理'), ('10', '2', '部经理'), ('11', '1', '见习客户经理'), ('12', '1', '初级客户经理'), ('13', '1', '中级客户经理'), ('14', '1', '高级客户经理'), ('15', '1', '见习保险规划师'), ('16', '1', '初级保险规划师'), ('17', '1', '中级保险规划师'), ('18', '1', '高级保险规划师');
COMMIT;

-- ----------------------------
--  Table structure for `service_record`
-- ----------------------------
DROP TABLE IF EXISTS `service_record`;
CREATE TABLE `service_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idcardnum` varchar(30) DEFAULT NULL COMMENT '客户',
  `servicetime` varchar(30) DEFAULT NULL COMMENT '服务时间',
  `content` varchar(600) DEFAULT NULL COMMENT '服务内容',
  `empcode` varchar(30) DEFAULT NULL COMMENT '客户经理',
  `name` varchar(30) DEFAULT NULL COMMENT '客户名称',
  `type` varchar(1) DEFAULT NULL COMMENT '服务性质',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `service_record`
-- ----------------------------
BEGIN;
INSERT INTO `service_record` VALUES ('1', '120123456789', '2016-03-04', '送雨伞', '11200000', '全冠清', '0'), ('2', '123', '2016-04-19', '1231', '11200000', '乔丹', '1'), ('3', '12', '2016-05-10', '说说', '11200000', '穆念慈', '1');
COMMIT;

-- ----------------------------
--  Table structure for `visit_record`
-- ----------------------------
DROP TABLE IF EXISTS `visit_record`;
CREATE TABLE `visit_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `empcode` varchar(30) DEFAULT NULL COMMENT '客户经理',
  `visitTime` varchar(30) DEFAULT NULL COMMENT '拜访时间',
  `idcardnum` varchar(30) DEFAULT NULL COMMENT '客户',
  `content` varchar(600) DEFAULT NULL COMMENT '拜访内容',
  `name` varchar(30) DEFAULT NULL COMMENT '客户名称',
  `type` varchar(1) DEFAULT NULL COMMENT '拜访性质',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `visit_record`
-- ----------------------------
BEGIN;
INSERT INTO `visit_record` VALUES ('1', '11200000', '2016-04-25', '120123456789', '上门拜访', '全冠清', '1'), ('2', '11200000', '2016-05-02', '120123456789', '讲解', '全冠清', '0'), ('3', '11200000', '2016-05-04', '120123456789', '开单', '全冠清', '0');
COMMIT;

-- ----------------------------
--  Function structure for `getChildList`
-- ----------------------------
DROP FUNCTION IF EXISTS `getChildList`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `getChildList`(parentcode varchar(30)) RETURNS varchar(2000) CHARSET utf8
BEGIN
DECLARE sTemp VARCHAR(2000);
DECLARE sTempChd VARCHAR(2000);

SET sTemp = '$';
SET sTempChd = parentcode;

WHILE sTempChd is not NULL DO
SET sTemp = CONCAT(sTemp,',',sTempChd);
SELECT group_concat(code) INTO sTempChd FROM employee where FIND_IN_SET(managercode,sTempChd)>0;
END WHILE;
return sTemp;
END
 ;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
