-- ----------------------------
-- Table structure for `ai_user`
-- ----------------------------
DROP TABLE IF EXISTS `ai_user`;
CREATE TABLE `ai_user` (
                           `id` bigint(18) NOT NULL AUTO_INCREMENT,
                           `login_name` varchar(25) NOT NULL,
                           `user_name` varchar(55) NOT NULL,
                           `password` varchar(25) NOT NULL,
                           `email` varchar(55) NOT NULL,
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of ai_user
-- ----------------------------
INSERT INTO `ai_user` VALUES ('1', 'admin', '林**', '******', 'answer_ljm@163.com');
INSERT INTO `ai_user` VALUES ('2', 'answer', '杨**', '******', '1072594307@qq.com');

-- ----------------------------
-- Table structure for `ai_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `ai_user_role`;
CREATE TABLE `ai_user_role` (
                                `id` bigint(18) NOT NULL AUTO_INCREMENT,
                                `role_name` varchar(25) NOT NULL,
                                `role_descript` varchar(55) NOT NULL,
                                `user_id` bigint(18) NOT NULL,
                                `role_status` tinyint(1) DEFAULT NULL,
                                PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of ai_user_role
-- ----------------------------
INSERT INTO `ai_user_role` VALUES ('1', 'admin', '超级管理员', '1', '1');
INSERT INTO `ai_user_role` VALUES ('2', 'answer', '普通管理员', '2', '1');
INSERT INTO `ai_user_role` VALUES ('3', 'develop', '开发者权限', '2', '1');
