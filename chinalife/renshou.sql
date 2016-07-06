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

 Date: 07/06/2016 15:35:01 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `bank_record`
-- ----------------------------
DROP TABLE IF EXISTS `bank_record`;
CREATE TABLE `bank_record` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `bankname` varchar(30) DEFAULT NULL COMMENT '银行名称',
  `bankcode` varchar(30) DEFAULT NULL COMMENT '银行代码',
  `zhihangname` varchar(30) DEFAULT NULL COMMENT '支行',
  `zhihangcode` varchar(30) DEFAULT NULL COMMENT '支行代码',
  `wangdianname` varchar(30) DEFAULT NULL COMMENT '网点',
  `wangdiancode` varchar(30) DEFAULT NULL COMMENT '网点代码',
  `mzhuanguanyuan` varchar(30) DEFAULT NULL COMMENT '专管员',
  `mzhuanguanyuancode` varchar(30) DEFAULT NULL COMMENT '专管员代码',
  `szhuanguanyuan` varchar(30) DEFAULT NULL COMMENT '专管员',
  `szhuanguanyuancode` varchar(30) DEFAULT NULL COMMENT '专管员代码',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1001 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `biz_record`
-- ----------------------------
DROP TABLE IF EXISTS `biz_record`;
CREATE TABLE `biz_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `yaoyue_num` varchar(30) DEFAULT NULL COMMENT '邀约客户数',
  `daohui_num` varchar(30) DEFAULT NULL COMMENT '到会客户数',
  `receive_num` varchar(30) DEFAULT NULL COMMENT '当日回收件数',
  `receive_baofei` varchar(30) DEFAULT NULL COMMENT '当日回收保费',
  `daohuilv` varchar(30) DEFAULT NULL COMMENT '到会率',
  `qiandan_num` varchar(30) DEFAULT NULL COMMENT '签单件数',
  `qiandanlv` varchar(30) DEFAULT NULL COMMENT '签单率',
  `huishoulv` varchar(30) DEFAULT NULL COMMENT '回收率',
  `kehujingli` varchar(30) DEFAULT NULL COMMENT '客户经理',
  `qiandan_baofei` varchar(30) DEFAULT NULL COMMENT '当日签单保费',
  `riqi` varchar(30) DEFAULT NULL COMMENT '日期',
  `bizplat_title` varchar(60) DEFAULT NULL COMMENT '业务平台',
  `bizplat_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `bizplatform`
-- ----------------------------
DROP TABLE IF EXISTS `bizplatform`;
CREATE TABLE `bizplatform` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(60) DEFAULT NULL,
  `zhishibaifang` varchar(1) DEFAULT NULL COMMENT '是否制式拜访',
  `caiye` varchar(4000) DEFAULT NULL COMMENT '彩页',
  `startdate` varchar(13) DEFAULT NULL COMMENT '开始时间',
  `enddate` varchar(13) DEFAULT NULL COMMENT '结束时间',
  `huashu` varchar(4000) DEFAULT NULL COMMENT '话术',
  `jishuziliao` varchar(4000) DEFAULT NULL COMMENT '技术资料',
  `others` varchar(4000) DEFAULT NULL COMMENT '其他',
  `empcode` varchar(30) DEFAULT NULL COMMENT '维护人员',
  `orgcode` varchar(30) DEFAULT NULL COMMENT '业务平台所属机构',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

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
  `cusname` varchar(30) DEFAULT NULL COMMENT '客户名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

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
  `empcode` varchar(30) DEFAULT NULL COMMENT '客户经理',
  `sex` varchar(2) DEFAULT NULL COMMENT '性别',
  `laiyuan` varchar(30) DEFAULT NULL COMMENT '初始来源',
  `note` varchar(600) DEFAULT NULL COMMENT '爱好及特点',
  `phone` varchar(30) DEFAULT NULL,
  `addr` varchar(30) DEFAULT NULL,
  `daorurenyuan` varchar(30) DEFAULT NULL COMMENT '导入人员',
  `leibie` varchar(30) DEFAULT NULL COMMENT '类别',
  `empname` varchar(30) DEFAULT NULL COMMENT '客户经理',
  `emporgcode` varchar(30) DEFAULT NULL COMMENT '机构编号',
  `emporgname` varchar(30) DEFAULT NULL COMMENT '机构',
  `oldorgname` varchar(30) DEFAULT NULL COMMENT '原机构',
  `oldorgcode` varchar(30) DEFAULT NULL COMMENT '原机构编号',
  `beizhu` varchar(1000) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idcardnum_index` (`idcardnum`)
) ENGINE=InnoDB AUTO_INCREMENT=358 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `customer_extras`
-- ----------------------------
DROP TABLE IF EXISTS `customer_extras`;
CREATE TABLE `customer_extras` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `idcardnum` varchar(30) DEFAULT NULL COMMENT '身份证号',
  `phone` varchar(30) DEFAULT NULL COMMENT '电话',
  `mobile` varchar(30) DEFAULT NULL COMMENT '手机',
  `car_band` varchar(30) DEFAULT NULL COMMENT '车品牌',
  `car_num` varchar(30) DEFAULT NULL COMMENT '车牌号',
  `addr` varchar(30) DEFAULT NULL COMMENT '住址',
  `empcode` varchar(30) DEFAULT NULL COMMENT '维护人',
  `insert_date` varchar(30) DEFAULT NULL COMMENT '维护日期',
  `empname` varchar(30) DEFAULT NULL COMMENT '维护人名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `gongzidan`
-- ----------------------------
DROP TABLE IF EXISTS `gongzidan`;
CREATE TABLE `gongzidan` (
  `xuhao` varchar(30) DEFAULT NULL COMMENT '序号',
  `bumen` varchar(30) DEFAULT NULL COMMENT '部门',
  `xingming` varchar(30) DEFAULT NULL COMMENT '姓名',
  `xingbie` varchar(30) DEFAULT NULL COMMENT '性别',
  `yinhangzhanghao` varchar(30) DEFAULT NULL COMMENT '银行账号',
  `shenfenzhenghao` varchar(30) DEFAULT NULL COMMENT '身份证号',
  `gonghao` varchar(30) DEFAULT NULL COMMENT '工号',
  `zhijijintie` varchar(30) DEFAULT NULL COMMENT '职级津贴',
  `xiaoshoujingyingjintie` varchar(30) DEFAULT NULL COMMENT '销售精英津贴',
  `xinrenbaohu` varchar(30) DEFAULT NULL COMMENT '新人保护/推荐津贴',
  `nianzijintie` varchar(30) DEFAULT NULL COMMENT '年资津贴',
  `guanlijintie` varchar(30) DEFAULT NULL COMMENT '管理津贴',
  `duanxianyongjin` varchar(30) DEFAULT NULL COMMENT '短险佣金',
  `qudaoyongjin` varchar(30) DEFAULT NULL COMMENT '渠道佣金',
  `ziyingyongjin` varchar(30) DEFAULT NULL COMMENT '自营佣金',
  `ziyingxuqi` varchar(30) DEFAULT NULL COMMENT '自营续期',
  `wangdianxuqi` varchar(30) DEFAULT NULL COMMENT '网点续期',
  `jiefa` varchar(30) DEFAULT NULL COMMENT '借发/补发',
  `xiaoji` varchar(30) DEFAULT NULL COMMENT '小计',
  `qitakoukuan` varchar(30) DEFAULT NULL COMMENT '其他扣款',
  `koukuanxiaoji` varchar(30) DEFAULT NULL COMMENT '扣款小计',
  `yingfaxiaoji` varchar(30) DEFAULT NULL COMMENT '应发小计',
  `fengognsijiangli` varchar(30) DEFAULT NULL COMMENT '分公司奖励',
  `jicengjiangli` varchar(30) DEFAULT NULL COMMENT '基层奖励',
  `qitabutie` varchar(30) DEFAULT NULL COMMENT '其他补贴',
  `jicengqitabutie` varchar(30) DEFAULT NULL COMMENT '其他补贴（基层承担）',
  `zhanyejintie` varchar(30) DEFAULT NULL COMMENT '展业津贴',
  `fengongsishiwujiangli` varchar(30) DEFAULT NULL COMMENT '分公司实物奖励',
  `jicengshiwujiangli` varchar(30) DEFAULT NULL COMMENT '基层公司实物奖励',
  `baoxianbaozhang` varchar(30) DEFAULT NULL COMMENT '保险保障',
  `yingfaheji` varchar(30) DEFAULT NULL COMMENT '应发合计',
  `weituoshebao` varchar(30) DEFAULT NULL COMMENT '委托社保',
  `chanshuohuikoukuan` varchar(30) DEFAULT NULL COMMENT '产说会扣款',
  `sandaikoukuanhechaoqikoukuan` varchar(30) DEFAULT NULL COMMENT '三代扣款和超期扣款',
  `gerensuodeshui` varchar(30) DEFAULT NULL COMMENT '个人所得税',
  `zengzhishui` varchar(30) DEFAULT NULL COMMENT '增值税',
  `chengjianshui` varchar(30) DEFAULT NULL COMMENT '城建税',
  `jiaoyufeifujia` varchar(30) DEFAULT NULL COMMENT '教育费附加',
  `difangjiaoyufeifujia` varchar(30) DEFAULT NULL COMMENT '地方教育费附加',
  `fanghongfei` varchar(30) DEFAULT NULL COMMENT '防洪费',
  `koufengongsishiwujiangli` varchar(30) DEFAULT NULL COMMENT '扣分公司实物奖励',
  `koujicenggongsishiwujiangli` varchar(30) DEFAULT NULL COMMENT '扣基层公司实物奖励',
  `koubaoxianbaozhang` varchar(30) DEFAULT NULL COMMENT '扣保险保障',
  `shouxianshifajine` varchar(30) DEFAULT NULL COMMENT '寿险实发金额',
  `caixianjixiao` varchar(30) DEFAULT NULL COMMENT '财险绩效',
  `koucaixianjixiao` varchar(30) DEFAULT NULL COMMENT '扣财险绩效',
  `shifaheji` varchar(30) DEFAULT NULL COMMENT '实发合计',
  `riqi` varchar(30) DEFAULT NULL COMMENT '工资日期'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
  `jigouhao` varchar(30) DEFAULT NULL COMMENT '机构号',
  `xianzhongdaima` varchar(30) DEFAULT NULL COMMENT '险种代码',
  `qudao` varchar(30) DEFAULT NULL COMMENT '渠道',
  `zhihang` varchar(30) DEFAULT NULL COMMENT '支行',
  `wangdian` varchar(30) DEFAULT NULL COMMENT '网点',
  `dianhua` varchar(30) DEFAULT NULL COMMENT '电话',
  `chushengriqi` varchar(30) DEFAULT NULL COMMENT '出生日期',
  `bumengjingli` varchar(30) DEFAULT NULL COMMENT '部门经理',
  `yuanzhuanguanyuangonghao` varchar(30) DEFAULT NULL COMMENT '原专管员工号',
  `yuanzhuanguanyuan` varchar(30) DEFAULT NULL COMMENT '原专管员',
  `xinfenpeirenyuangonghao` varchar(30) DEFAULT NULL COMMENT '新分配人员工号',
  `xinfenpeirenyuan` varchar(30) DEFAULT NULL COMMENT '新分配人员',
  `fafangshijian` varchar(30) DEFAULT NULL COMMENT '发放时间',
  `fafangbiaoshi` varchar(30) DEFAULT NULL COMMENT '发放标识',
  `beizhu` varchar(1000) DEFAULT NULL COMMENT '备注',
  `kaituoxindan` varchar(30) DEFAULT '分配' COMMENT '开拓新单',
  `tousujilu` varchar(30) DEFAULT '未投诉' COMMENT '投诉记录',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=372 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=474 DEFAULT CHARSET=utf8;

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
--  Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `level` int(2) NOT NULL COMMENT '级别',
  `name` varchar(60) NOT NULL COMMENT '职务',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;

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
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `visit_record`
-- ----------------------------
DROP TABLE IF EXISTS `visit_record`;
CREATE TABLE `visit_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `empcode` varchar(30) DEFAULT NULL COMMENT '客户经理',
  `visittime` varchar(30) DEFAULT NULL COMMENT '拜访时间',
  `idcardnum` varchar(30) DEFAULT NULL COMMENT '客户',
  `content` varchar(600) DEFAULT NULL COMMENT '拜访内容',
  `name` varchar(30) DEFAULT NULL COMMENT '客户名称',
  `type` varchar(1) DEFAULT NULL COMMENT '拜访性质',
  `xijie` varchar(30) DEFAULT NULL COMMENT '拜访细节',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

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

-- ----------------------------
--  Function structure for `getChildList_copy`
-- ----------------------------
DROP FUNCTION IF EXISTS `getChildList_copy`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `getChildList_copy`(parentcode varchar(30)) RETURNS varchar(2000) CHARSET utf8
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
