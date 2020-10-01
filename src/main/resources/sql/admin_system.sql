/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : admin_system

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 01/10/2020 17:24:54
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for blog
-- ----------------------------
DROP TABLE IF EXISTS `blog`;
CREATE TABLE `blog` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `aid` bigint NOT NULL COMMENT '作者id',
  `content` text NOT NULL COMMENT '内容',
  `publish_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '发布时间',
  `modify_time` datetime DEFAULT NULL COMMENT '上次修改时间',
  `state` int DEFAULT '1' COMMENT '状态',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for blogview
-- ----------------------------
DROP TABLE IF EXISTS `blogview`;
CREATE TABLE `blogview` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `bid` int DEFAULT NULL,
  `timestamp` varchar(255) DEFAULT NULL,
  `uid` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blogview
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for candidate
-- ----------------------------
DROP TABLE IF EXISTS `candidate`;
CREATE TABLE `candidate` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `iid` bigint NOT NULL COMMENT '项目id',
  `uid` bigint NOT NULL COMMENT '竞选用户id',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `state` int DEFAULT '0' COMMENT '状态，0未被选择 1被选择',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of candidate
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for catalog
-- ----------------------------
DROP TABLE IF EXISTS `catalog`;
CREATE TABLE `catalog` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) NOT NULL COMMENT '名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of catalog
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `bid` bigint NOT NULL DEFAULT '0' COMMENT '所属的Blog',
  `rid` bigint DEFAULT '0' COMMENT '回复的comment的id',
  `aid` bigint NOT NULL COMMENT '留言者id',
  `content` text NOT NULL COMMENT '内容',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of comment
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for favour
-- ----------------------------
DROP TABLE IF EXISTS `favour`;
CREATE TABLE `favour` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `bid` bigint NOT NULL COMMENT 'Blog编号',
  `uid` bigint NOT NULL COMMENT '点赞用户编号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `bid_uid` (`bid`,`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of favour
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `aid` bigint NOT NULL COMMENT '创建者id',
  `cid` bigint NOT NULL COMMENT '分类',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `description` text NOT NULL COMMENT '描述',
  `file` varchar(100) DEFAULT NULL COMMENT '附件',
  `price` decimal(9,2) unsigned NOT NULL DEFAULT '0.00' COMMENT '价格',
  `state` int NOT NULL DEFAULT '1' COMMENT '标志，0关闭 1正在竞选 2竞选完成 ',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of item
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for itemview
-- ----------------------------
DROP TABLE IF EXISTS `itemview`;
CREATE TABLE `itemview` (
  `id` int NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `iid` int DEFAULT NULL,
  `timestamp` varchar(255) DEFAULT NULL,
  `uid` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of itemview
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for order_
-- ----------------------------
DROP TABLE IF EXISTS `order_`;
CREATE TABLE `order_` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `iid` bigint NOT NULL COMMENT '项目id',
  `aid` bigint NOT NULL COMMENT '订单创建人id',
  `uid` bigint NOT NULL COMMENT '交易对象id',
  `price` decimal(9,2) NOT NULL COMMENT '交易金额',
  `state` int NOT NULL DEFAULT '0' COMMENT '0创建未付款，1已付定金，2已确认支付，3已退款',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '上次修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `iid` (`iid`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `url` varchar(200) NOT NULL,
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of permission
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for picture
-- ----------------------------
DROP TABLE IF EXISTS `picture`;
CREATE TABLE `picture` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `bid` bigint NOT NULL COMMENT 'blogid',
  `url` varchar(200) NOT NULL COMMENT '地址',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of picture
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `rid` bigint NOT NULL,
  `pid` bigint NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for star
-- ----------------------------
DROP TABLE IF EXISTS `star`;
CREATE TABLE `star` (
  `id` int NOT NULL AUTO_INCREMENT,
  `add_time` varchar(255) DEFAULT NULL,
  `iid` int DEFAULT NULL,
  `uid` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of star
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '编号',
  `username` varchar(32) NOT NULL COMMENT '账号',
  `password` varchar(32) NOT NULL COMMENT '密码',
  `nickname` varchar(32) NOT NULL COMMENT '昵称',
  `salt` varchar(256) DEFAULT NULL COMMENT '盐',
  `gender` tinyint unsigned DEFAULT '1' COMMENT '性别，0女性 1男性',
  `college` varchar(256) DEFAULT NULL COMMENT '学校',
  `major` varchar(256) DEFAULT NULL COMMENT '专业',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机',
  `email` varchar(256) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(256) DEFAULT NULL COMMENT '头像',
  `state` int NOT NULL DEFAULT '1' COMMENT '标志，0不可用 1可用',
  `create_time` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT NULL COMMENT '上次修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `nickname` (`nickname`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'admin', 'admin', 'admin', NULL, 1, NULL, NULL, NULL, NULL, 'http://localhost:8888/default.jpeg', 1, '2020-10-01 17:23:55', NULL);
COMMIT;

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `uid` bigint NOT NULL,
  `rid` bigint NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
