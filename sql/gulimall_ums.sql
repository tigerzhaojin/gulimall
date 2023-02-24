/*
 Navicat MySQL Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50739
 Source Host           : localhost
 Source Database       : gulimall_ums

 Target Server Type    : MySQL
 Target Server Version : 50739
 File Encoding         : utf-8

 Date: 02/24/2023 15:42:01 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `ums_growth_change_history`
-- ----------------------------
DROP TABLE IF EXISTS `ums_growth_change_history`;
CREATE TABLE `ums_growth_change_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `member_id` bigint(20) DEFAULT NULL COMMENT 'member_id',
  `create_time` datetime DEFAULT NULL COMMENT 'create_time',
  `change_count` int(11) DEFAULT NULL COMMENT '�ı��ֵ������������',
  `note` varchar(0) COLLATE utf8_bin DEFAULT NULL COMMENT '��ע',
  `source_type` tinyint(4) DEFAULT NULL COMMENT '������Դ[0-���1-����Ա�޸�]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='�ɳ�ֵ�仯��ʷ��¼';

-- ----------------------------
--  Table structure for `ums_integration_change_history`
-- ----------------------------
DROP TABLE IF EXISTS `ums_integration_change_history`;
CREATE TABLE `ums_integration_change_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `member_id` bigint(20) DEFAULT NULL COMMENT 'member_id',
  `create_time` datetime DEFAULT NULL COMMENT 'create_time',
  `change_count` int(11) DEFAULT NULL COMMENT '�仯��ֵ',
  `note` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '��ע',
  `source_tyoe` tinyint(4) DEFAULT NULL COMMENT '��Դ[0->���1->����Ա�޸�;2->�]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='���ֱ仯��ʷ��¼';

-- ----------------------------
--  Table structure for `ums_member`
-- ----------------------------
DROP TABLE IF EXISTS `ums_member`;
CREATE TABLE `ums_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `level_id` bigint(20) DEFAULT NULL COMMENT '��Ա�ȼ�id',
  `username` char(64) COLLATE utf8_bin DEFAULT NULL COMMENT '�û���',
  `password` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '����',
  `nickname` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '�ǳ�',
  `mobile` varchar(20) COLLATE utf8_bin DEFAULT NULL COMMENT '�ֻ�����',
  `email` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '����',
  `header` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'ͷ��',
  `gender` tinyint(4) DEFAULT NULL COMMENT '�Ա�',
  `birth` date DEFAULT NULL COMMENT '����',
  `city` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '���ڳ���',
  `job` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'ְҵ',
  `sign` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '����ǩ��',
  `source_type` tinyint(4) DEFAULT NULL COMMENT '�û���Դ',
  `integration` int(11) DEFAULT NULL COMMENT '����',
  `growth` int(11) DEFAULT NULL COMMENT '�ɳ�ֵ',
  `status` tinyint(4) DEFAULT NULL COMMENT '����״̬',
  `create_time` datetime DEFAULT NULL COMMENT 'ע��ʱ��',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='��Ա';

-- ----------------------------
--  Table structure for `ums_member_collect_spu`
-- ----------------------------
DROP TABLE IF EXISTS `ums_member_collect_spu`;
CREATE TABLE `ums_member_collect_spu` (
  `id` bigint(20) NOT NULL COMMENT 'id',
  `member_id` bigint(20) DEFAULT NULL COMMENT '��Աid',
  `spu_id` bigint(20) DEFAULT NULL COMMENT 'spu_id',
  `spu_name` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'spu_name',
  `spu_img` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'spu_img',
  `create_time` datetime DEFAULT NULL COMMENT 'create_time',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='��Ա�ղص���Ʒ';

-- ----------------------------
--  Table structure for `ums_member_collect_subject`
-- ----------------------------
DROP TABLE IF EXISTS `ums_member_collect_subject`;
CREATE TABLE `ums_member_collect_subject` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `subject_id` bigint(20) DEFAULT NULL COMMENT 'subject_id',
  `subject_name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'subject_name',
  `subject_img` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT 'subject_img',
  `subject_urll` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '�url',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='��Ա�ղص�ר��';

-- ----------------------------
--  Table structure for `ums_member_level`
-- ----------------------------
DROP TABLE IF EXISTS `ums_member_level`;
CREATE TABLE `ums_member_level` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '�ȼ�����',
  `growth_point` int(11) DEFAULT NULL COMMENT '�ȼ���Ҫ�ĳɳ�ֵ',
  `default_status` tinyint(4) DEFAULT NULL COMMENT '�Ƿ�ΪĬ�ϵȼ�[0->���ǣ�1->��]',
  `free_freight_point` decimal(18,4) DEFAULT NULL COMMENT '���˷ѱ�׼',
  `comment_growth_point` int(11) DEFAULT NULL COMMENT 'ÿ�����ۻ�ȡ�ĳɳ�ֵ',
  `priviledge_free_freight` tinyint(4) DEFAULT NULL COMMENT '�Ƿ���������Ȩ',
  `priviledge_member_price` tinyint(4) DEFAULT NULL COMMENT '�Ƿ��л�Ա�۸���Ȩ',
  `priviledge_birthday` tinyint(4) DEFAULT NULL COMMENT '�Ƿ���������Ȩ',
  `note` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '��ע',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='��Ա�ȼ�';

-- ----------------------------
--  Table structure for `ums_member_login_log`
-- ----------------------------
DROP TABLE IF EXISTS `ums_member_login_log`;
CREATE TABLE `ums_member_login_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `member_id` bigint(20) DEFAULT NULL COMMENT 'member_id',
  `create_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  `ip` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'ip',
  `city` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT 'city',
  `login_type` tinyint(1) DEFAULT NULL COMMENT '��¼����[1-web��2-app]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='��Ա��¼��¼';

-- ----------------------------
--  Table structure for `ums_member_receive_address`
-- ----------------------------
DROP TABLE IF EXISTS `ums_member_receive_address`;
CREATE TABLE `ums_member_receive_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `member_id` bigint(20) DEFAULT NULL COMMENT 'member_id',
  `name` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '�ջ�������',
  `phone` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '�绰',
  `post_code` varchar(64) COLLATE utf8_bin DEFAULT NULL COMMENT '��������',
  `province` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT 'ʡ��/ֱϽ��',
  `city` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '����',
  `region` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '��',
  `detail_address` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '��ϸ��ַ(�ֵ�)',
  `areacode` varchar(15) COLLATE utf8_bin DEFAULT NULL COMMENT 'ʡ��������',
  `default_status` tinyint(1) DEFAULT NULL COMMENT '�Ƿ�Ĭ��',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='��Ա�ջ���ַ';

-- ----------------------------
--  Table structure for `ums_member_statistics_info`
-- ----------------------------
DROP TABLE IF EXISTS `ums_member_statistics_info`;
CREATE TABLE `ums_member_statistics_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `member_id` bigint(20) DEFAULT NULL COMMENT '��Աid',
  `consume_amount` decimal(18,4) DEFAULT NULL COMMENT '�ۼ����ѽ��',
  `coupon_amount` decimal(18,4) DEFAULT NULL COMMENT '�ۼ��Żݽ��',
  `order_count` int(11) DEFAULT NULL COMMENT '��������',
  `coupon_count` int(11) DEFAULT NULL COMMENT '�Ż�ȯ����',
  `comment_count` int(11) DEFAULT NULL COMMENT '������',
  `return_order_count` int(11) DEFAULT NULL COMMENT '�˻�����',
  `login_count` int(11) DEFAULT NULL COMMENT '��¼����',
  `attend_count` int(11) DEFAULT NULL COMMENT '��ע����',
  `fans_count` int(11) DEFAULT NULL COMMENT '��˿����',
  `collect_product_count` int(11) DEFAULT NULL COMMENT '�ղص���Ʒ����',
  `collect_subject_count` int(11) DEFAULT NULL COMMENT '�ղص�ר������',
  `collect_comment_count` int(11) DEFAULT NULL COMMENT '�ղص���������',
  `invite_friend_count` int(11) DEFAULT NULL COMMENT '�������������',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='��Աͳ����Ϣ';

SET FOREIGN_KEY_CHECKS = 1;
