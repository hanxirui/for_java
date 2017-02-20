/*
干净的数据
*/
CREATE DATABASE `stu`;

USE `stu`;

/*Table structure for table `department` */

DROP TABLE IF EXISTS `department`;

CREATE TABLE `department` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `department_name` VARCHAR(30) DEFAULT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=INNODB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;

/*Data for the table `department` */

INSERT  INTO `department`(`ID`,`department_name`) VALUES (16,'修改后的'),(17,'外语系'),(18,'经管系'),(19,'新闻系'),(20,'科学系'),(21,'生化系'),(27,'生物工程'),(28,'生物工程'),(29,'生物工程'),(30,'生物工程2'),(31,'生物工程3');

/*Table structure for table `order_info` */

DROP TABLE IF EXISTS `order_info`;

CREATE TABLE `order_info` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `city_name` varchar(150) DEFAULT NULL,
  `mobile` varchar(150) DEFAULT NULL,
  `address` varchar(600) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `order_info` */

/*Table structure for table `r_data_obj` */

DROP TABLE IF EXISTS `r_data_obj`;

CREATE TABLE `r_data_obj` (
  `do_id` int(11) NOT NULL AUTO_INCREMENT,
  `dt_id` int(11) NOT NULL,
  `group_name` varchar(50) NOT NULL,
  PRIMARY KEY (`do_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `r_data_obj` */

/*Table structure for table `r_data_permission` */

DROP TABLE IF EXISTS `r_data_permission`;

CREATE TABLE `r_data_permission` (
  `dp_id` int(11) NOT NULL AUTO_INCREMENT,
  `sr_id` int(11) NOT NULL,
  `expression_type` tinyint(4) NOT NULL,
  `column` varchar(50) NOT NULL,
  `equal` varchar(50) NOT NULL,
  `value` varchar(50) NOT NULL,
  `remark` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`dp_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `r_data_permission` */

/*Table structure for table `r_data_permission_role` */

DROP TABLE IF EXISTS `r_data_permission_role`;

CREATE TABLE `r_data_permission_role` (
  `dp_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  PRIMARY KEY (`dp_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `r_data_permission_role` */

/*Table structure for table `r_data_type` */

DROP TABLE IF EXISTS `r_data_type`;

CREATE TABLE `r_data_type` (
  `dt_id` int(11) NOT NULL AUTO_INCREMENT,
  `type_name` varchar(50) NOT NULL,
  PRIMARY KEY (`dt_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `r_data_type` */

/*Table structure for table `r_datatype_res` */

DROP TABLE IF EXISTS `r_datatype_res`;

CREATE TABLE `r_datatype_res` (
  `dt_id` int(11) NOT NULL,
  `sr_id` int(11) NOT NULL,
  PRIMARY KEY (`dt_id`,`sr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `r_datatype_res` */

/*Table structure for table `r_group` */

DROP TABLE IF EXISTS `r_group`;

CREATE TABLE `r_group` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户组ID',
  `group_name` varchar(50) NOT NULL COMMENT '用户组名称',
  `parent_id` int(11) DEFAULT NULL COMMENT '父ID',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组表';

/*Data for the table `r_group` */

/*Table structure for table `r_group_role` */

DROP TABLE IF EXISTS `r_group_role`;

CREATE TABLE `r_group_role` (
  `group_id` int(11) NOT NULL COMMENT '用户组ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`group_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组角色表';

/*Data for the table `r_group_role` */

/*Table structure for table `r_group_user` */

DROP TABLE IF EXISTS `r_group_user`;

CREATE TABLE `r_group_user` (
  `group_id` int(11) NOT NULL COMMENT '用户组ID',
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  PRIMARY KEY (`group_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户组成员表';

/*Data for the table `r_group_user` */

/*Table structure for table `r_role` */

DROP TABLE IF EXISTS `r_role`;

CREATE TABLE `r_role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(50) NOT NULL COMMENT '角色名',
  `role_type` tinyint(4) NOT NULL COMMENT '角色类型,1个人,2用户组',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色表';

/*Data for the table `r_role` */

insert  into `r_role`(`role_id`,`role_name`,`role_type`) values (1,'超级管理员',1);

/*Table structure for table `r_role_permission` */

DROP TABLE IF EXISTS `r_role_permission`;

CREATE TABLE `r_role_permission` (
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `sf_id` int(11) NOT NULL COMMENT '系统功能ID',
  PRIMARY KEY (`role_id`,`sf_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限角色表';

/*Data for the table `r_role_permission` */

insert  into `r_role_permission`(`role_id`,`sf_id`) values (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17);

/*Table structure for table `r_sys_function` */

DROP TABLE IF EXISTS `r_sys_function`;

CREATE TABLE `r_sys_function` (
  `sf_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `sr_id` int(11) NOT NULL COMMENT 'r_sys_res主键',
  `operate_code` varchar(50) NOT NULL COMMENT '权限代码',
  `operate_name` varchar(50) NOT NULL COMMENT '权限名称',
  `url` varchar(1000) DEFAULT NULL COMMENT 'URL',
  PRIMARY KEY (`sf_id`,`operate_code`,`sr_id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COMMENT='系统功能表';

/*Data for the table `r_sys_function` */

insert  into `r_sys_function`(`sf_id`,`sr_id`,`operate_code`,`operate_name`,`url`) values (1,2,'btn_addUser','添加用户','addRUser.do'),(2,2,'grid_setRole','设置角色','listAllRRole.do,listTreeRole.do,listUserRRole.do,addUserRole.do,delUserRole.do'),(3,2,'grid_resetPwsd','重置密码','resetUserPassword.do'),(4,2,'html_search','查询','listRUser.do'),(5,3,'html_search','查询','listRRole.do'),(6,3,'grid_setRight','设置权限','listMenuByTabId.do, listRolePermissionByRoleId.do, listTopMenu.do,listRoleRelationInfo.do, addRolePermission.do, delRolePermission.do'),(7,3,'grid_del','删除角色','delRRole.do'),(8,3,'btn_add','添加角色','addRRole.do'),(9,4,'html_search','查询','listTopMenu.do,listMenuByTabId.do,listSysFunctionBySrId.do, addTopMenu.do'),(10,4,'html_addSysFun','添加操作权限','addSysFunction.do'),(11,4,'grid_auth','授权','listTreeRole.do, listAllRRole.do,addRolePermission.do,delRolePermission.do'),(12,4,'grid_del','删除操作权限','delRSysFunction.do'),(13,4,'opt_node','操作节点','addRSysRes.do,updateRSysRes.do,delRSysRes.do'),(14,5,'html_search','查询','listAllGroup.do,listRGroupUser.do,listRoleByGroupId.do'),(15,5,'opt_node','操作节点','addRGroup.do,updateRGroup.do,delRGroup.do'),(16,5,'opt_groupUser','操作用户组成员','listGroupNoAddUser.do,addGroupUser.do,delRGroupUser.do'),(17,5,'opt_groupRole','操作用户组角色','addRGroupRole.do,delRGroupRole.do');

/*Table structure for table `r_sys_res` */

DROP TABLE IF EXISTS `r_sys_res`;

CREATE TABLE `r_sys_res` (
  `sr_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '资源ID',
  `parent_id` int(11) DEFAULT NULL COMMENT '父ID',
  `res_name` varchar(50) NOT NULL COMMENT '资源名称',
  `tab_id` int(11) DEFAULT NULL COMMENT 'r_sys_res_tab表外键',
  `url` varchar(50) DEFAULT NULL COMMENT 'URL',
  PRIMARY KEY (`sr_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='系统资源表';

/*Data for the table `r_sys_res` */

insert  into `r_sys_res`(`sr_id`,`parent_id`,`res_name`,`tab_id`,`url`) values (1,0,'权限管理',1,NULL),(2,1,'用户管理',1,'permission/user.jsp'),(3,1,'角色管理',1,'permission/role.jsp'),(4,1,'资源管理',1,'permission/resMgr.jsp'),(5,1,'用户组管理',1,'permission/group.jsp');

/*Table structure for table `r_sys_res_tab` */

DROP TABLE IF EXISTS `r_sys_res_tab`;

CREATE TABLE `r_sys_res_tab` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `tab_name` varchar(50) DEFAULT NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='系统分类表';

/*Data for the table `r_sys_res_tab` */

insert  into `r_sys_res_tab`(`id`,`tab_name`) values (1,'系统管理');

/*Table structure for table `r_user` */

DROP TABLE IF EXISTS `r_user`;

CREATE TABLE `r_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(200) NOT NULL COMMENT '用户名',
  `password` varchar(200) NOT NULL COMMENT '密码',
  `state` tinyint(4) NOT NULL COMMENT '状态,1启用,0禁用',
  `add_time` datetime NOT NULL COMMENT '添加时间',
  `last_login_date` datetime DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='用户表';

/*Data for the table `r_user` */

insert  into `r_user`(`user_id`,`username`,`password`,`state`,`add_time`,`last_login_date`) values (1,'admin','1000:8dc4c0d4e2d3585d3d47ed593c96a1bf0430247e09b09ccf:e6a6747ce9296cb6a67c95d278a4ab4cf37aa9a41801cc5f',1,'2016-05-06 14:06:17','2016-05-06 14:23:37');

/*Table structure for table `r_user_role` */

DROP TABLE IF EXISTS `r_user_role`;

CREATE TABLE `r_user_role` (
  `user_id` int(11) NOT NULL COMMENT '用户ID',
  `role_id` int(11) NOT NULL COMMENT '角色ID',
  `role_type` tinyint(4) NOT NULL COMMENT '角色类型,1个人,2用户组',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户角色表';

/*Data for the table `r_user_role` */

insert  into `r_user_role`(`user_id`,`role_id`,`role_type`) values (1,1,1);

/*Table structure for table `student` */

DROP TABLE IF EXISTS `student`;

CREATE TABLE `student` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(32) NOT NULL,
  `POLITICS_STATUS` INT(11) NOT NULL DEFAULT '1',
  `NATIONALITY` VARCHAR(20) NOT NULL DEFAULT '',
  `STU_NO` VARCHAR(64) NOT NULL,
  `GENDER` TINYINT(4) DEFAULT NULL,
  `DEPARTMENT` INT(11) DEFAULT NULL,
  `ADDRESS` VARCHAR(128) DEFAULT NULL,
  `MOBILE` VARCHAR(20) NOT NULL DEFAULT '',
  `REGIST_DATE` DATETIME DEFAULT NULL,
  `BIRTHDAY` DATETIME DEFAULT NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=INNODB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

/*Data for the table `student` */

INSERT  INTO `student`(`ID`,`NAME`,`POLITICS_STATUS`,`NATIONALITY`,`STU_NO`,`GENDER`,`DEPARTMENT`,`ADDRESS`,`MOBILE`,`REGIST_DATE`,`BIRTHDAY`) VALUES (1,'李四',1,'汉族','NO000001',1,16,'CN','000013398761567','2011-12-02 00:00:00','2012-10-17 00:00:00'),(2,'张三',1,'汉族','NO000002',1,16,'北京市朝阳区广顺北大街33号院1号楼福码大厦','13398761567','2011-12-02 12:11:00',NULL),(3,'张三',1,'汉族','NO000003',0,16,'北京市朝阳区广顺北大街33号院1号楼福码大厦','13398761567','2011-12-02 00:00:00',NULL),(4,'张三',1,'汉族','NO000004',1,16,'北京市朝阳区广顺北大街33号院1号楼福码大厦','13398761567','2011-12-02 00:00:00','2012-10-19 00:00:00'),(5,'张三',1,'汉族','NO000005',0,16,'北京市朝阳区广顺北大街33号院1号楼福码大厦','013398761567','2011-12-02 00:00:00',NULL),(6,'bbb',2,'汉族','NO000006',1,16,'北京市朝阳区广顺北大街33号院1号楼福码大厦','0013398761567','2010-02-16 00:00:00','2013-01-08 00:00:00'),(7,'张三',1,'汉族','NO000007',1,17,'北京市朝阳区广顺北大街33号院1号楼福码大厦','13398761567','2012-01-18 00:00:00','2012-10-06 00:00:00'),(8,'张三1',1,'汉族','NO0000011',1,16,'北京市朝阳区广顺北大街33号院1号楼福码大厦','13398761567','2011-12-02 00:00:00','2012-10-05 00:00:00'),(9,'张三2',1,'汉族','NO0000012',1,16,'北京市朝阳区广顺北大街33号院1号楼福码大厦','13398761567','2011-12-02 00:00:00','2014-06-12 00:00:00'),(10,'张三3',1,'汉族','NO0000013',0,16,'浙江省杭州市','013398761567','2011-12-02 00:00:00','2012-10-16 00:00:00'),(11,'张三4',1,'汉族','NO0000014',1,18,'河南省开封市','13398761567','2011-12-02 00:00:00',NULL),(13,'张三6',1,'汉族','NO0000016',1,16,'河南省濮阳市','13398761567','2011-12-02 00:00:00','2012-10-26 00:00:00'),(14,'张三7',1,'汉族','NO0000017',1,16,'湖北省武汉市','13398761567','2011-12-02 00:00:00',NULL),(17,'张克',1,'汉族','NO00000013',0,16,'湖南','13398761567','2012-01-04 00:00:00','2012-10-15 00:00:00'),(18,'张克4',0,'汉族','NO00000014',1,19,'湖南4','13398761567','2012-01-29 00:00:00','2012-10-18 00:00:00'),(19,'张克45',2,'汉族','NO00000015',0,16,'湖南5','13398761567','2012-02-14 00:00:00','2012-10-08 00:00:00'),(20,'JIM',1,'汉族','NO0000001',1,16,'USA','13398761567','2011-12-02 00:00:00','2013-12-12 00:00:00'),(21,'JIM',1,'汉族','NO0000001',1,16,'USA','013398761567','2011-12-02 00:00:00','2008-10-15 00:00:00'),(22,'JIM',1,'汉族','NO0000001',1,16,'USA','0013398761567','2011-12-02 00:00:00','2013-01-07 00:00:00'),(23,'JIM',2,'汉族','NO0000001',1,16,'USA','00013398761567','2011-12-02 00:00:00','2013-01-07 00:00:00'),(24,'JIM',2,'汉族','NO0000001',1,16,'USA','0013398761567','2011-12-02 00:00:00','2012-10-01 00:00:00'),(25,'JIM',1,'汉族','NO0000001',1,17,'USA','13398761567','2011-12-02 00:00:00','2012-10-12 00:00:00'),(26,'JIM',2,'汉族','NO0000001',1,16,'USA','13398761567','2011-12-02 00:00:00','2012-10-18 00:00:00'),(27,'JIM',1,'汉族','NO0000001',1,18,'USA','13398761567','2011-12-02 00:00:00','2012-10-19 00:00:00'),(30,'JIM',1,'汉族','NO0000001',1,16,'USA','013398761567','2011-12-02 00:00:00','2013-01-07 00:00:00'),(31,'JIM',1,'汉族','NO0000001',1,16,'USA','13398761567','2011-12-02 00:00:00','2012-10-16 00:00:00'),(33,'JIM',1,'汉族','NO0000001',1,16,'USA','13398761567','2011-12-02 00:00:00','2012-10-27 00:00:00'),(34,'JIM',1,'汉族','NO0000001',1,16,'USA','13398761567','2011-12-02 00:00:00','2012-10-27 00:00:00'),(35,'JIM',1,'汉族','NO0000001',1,16,'USA','013398761567','2011-12-02 00:00:00','2012-10-27 00:00:00'),(36,'Jus',0,'回族','NO0000001',0,16,'UK1wer','13398761560','2012-09-24 00:00:00','2012-09-28 00:00:00'),(37,'安布雷拉',1,'汉族','NO00909',0,21,'USA','13398761567','2012-04-27 00:00:00','2012-08-07 00:00:00'),(38,'赵六',2,'汉族','NO20120924',1,NULL,'上海市黄浦区福建中路225号中悦大厦503室（人民广场福州路上海书城斜对面）','000','2012-09-21 00:00:00','2012-09-14 00:00:00'),(39,'钱七1',1,'汉族','NO2013121301',1,16,'UK1','234234','2013-12-17 00:00:00','2013-11-30 00:00:00'),(40,'周杰伦',1,'汉族','NO20140506001',0,16,'阿斯顿飞','13777777777','2014-06-06 00:00:00','2014-06-12 00:00:00'),(41,'周杰伦',2,'汉族','NO20140506002',1,16,'北京市朝阳区广顺北大街33号院1号楼福码大厦','13777777777','2014-06-01 00:00:00','2014-06-12 00:00:00'),(42,'刘德华',0,'汉族','NO20140506003',0,16,'台北','13777777777','2014-06-10 00:00:00','2013-11-07 00:00:00');


/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
