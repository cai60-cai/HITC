

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_article
-- ----------------------------
DROP TABLE IF EXISTS `t_article`;
CREATE TABLE `t_article`  (
  `article_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文章id',
  `article_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章标题',
  `article_abstract` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章摘要',
  `article_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文章封面',
  `article_type` int(11) NOT NULL COMMENT '文章分类',
  `article_rate` float DEFAULT NULL COMMENT '文章评级',
  `article_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '文章详情',
  `article_belong_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发布者用户id',
  `article_file_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '依赖包文件id',
  `article_download_point` int(11) DEFAULT 0 COMMENT '下载所需积分',
  `article_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '文章状态,0、未审核 1、已通过 2、未通过',
  `article_is_recommend` tinyint(1) DEFAULT 0 COMMENT '是否官方推荐',
  `article_is_index` tinyint(1) DEFAULT 0 COMMENT '是否展示在首页,1是,0不是',
  `article_is_self` tinyint(1) DEFAULT 0 COMMENT '是否原创文章，0、不是，1、是',
  `original_link` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '非原创，原文链接',
  `create_time` datetime(0) DEFAULT NULL COMMENT '文章创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '文章修改时间',
  `article_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '备注描述',
  PRIMARY KEY (`article_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_article
-- ----------------------------


-- ----------------------------
-- Table structure for t_article_collection
-- ----------------------------
DROP TABLE IF EXISTS `t_article_collection`;
CREATE TABLE `t_article_collection`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `article_id` int(11) NOT NULL COMMENT '文章id',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `collection_time` datetime(0) DEFAULT NULL COMMENT '收藏时间',
  `belong_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文章所属用户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `t_article_collection_article_id_user_id`(`article_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章收藏表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_article_comment
-- ----------------------------
DROP TABLE IF EXISTS `t_article_comment`;
CREATE TABLE `t_article_comment`  (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论id',
  `article_id` int(11) NOT NULL COMMENT '评论id',
  `comment_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '评论内容',
  `comment_from` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论人',
  `comment_to` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论对象',
  `parent_comment_id` int(11) DEFAULT NULL COMMENT '父级评论id',
  `comment_time` datetime(0) DEFAULT NULL COMMENT '评论时间',
  `comment_essence` tinyint(1) DEFAULT NULL COMMENT '是否精华、1是、0不是',
  `top` tinyint(1) DEFAULT 0 COMMENT '置顶',
  PRIMARY KEY (`comment_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章评论表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_article_comment_like
-- ----------------------------
DROP TABLE IF EXISTS `t_article_comment_like`;
CREATE TABLE `t_article_comment_like`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `comment_id` int(11) NOT NULL COMMENT '评论id',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `belong_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属用户',
  `like_time` datetime(0) DEFAULT NULL COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `t_article_comment_like_comment_id_user_id`(`comment_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章评论点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_article_draft
-- ----------------------------
DROP TABLE IF EXISTS `t_article_draft`;
CREATE TABLE `t_article_draft`  (
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '草稿id，userId',
  `article_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文章id',
  `article_abstract` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文章摘要',
  `article_cover` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文章封面',
  `article_type` int(11) DEFAULT NULL COMMENT '文章分类',
  `importance` int(11) DEFAULT 0 COMMENT '评级',
  `article_detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '文章详情',
  `article_tags` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文章标签',
  `create_time` datetime(0) DEFAULT NULL COMMENT '草稿创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '草稿修改时间',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章草稿表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_article_like
-- ----------------------------
DROP TABLE IF EXISTS `t_article_like`;
CREATE TABLE `t_article_like`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `article_id` int(11) NOT NULL COMMENT '文章id',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `belong_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文章所属用户id',
  `like_time` datetime(0) DEFAULT NULL COMMENT '点赞时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `t_article_like_article_id_user_id`(`article_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_article_read
-- ----------------------------
DROP TABLE IF EXISTS `t_article_read`;
CREATE TABLE `t_article_read`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `article_id` int(11) NOT NULL COMMENT '文章id',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `read_time` datetime(0) DEFAULT NULL COMMENT '阅读时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `t_article_read_article_id_user_id`(`article_id`, `user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章阅读表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_article_read
-- ----------------------------
INSERT INTO `t_article_read` VALUES (1, 2, '67489e02c9b44da6aa33735ee9beb4d5', '2024-02-26 15:10:01');

-- ----------------------------
-- Table structure for t_article_search_flow
-- ----------------------------
DROP TABLE IF EXISTS `t_article_search_flow`;
CREATE TABLE `t_article_search_flow`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `article_id` int(11) NOT NULL COMMENT '文章id',
  `search_time` datetime(0) DEFAULT NULL COMMENT '搜索时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章搜索次数记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_article_tag`;
CREATE TABLE `t_article_tag`  (
  `article_id` int(11) NOT NULL COMMENT '文章id',
  `tag_id` int(11) NOT NULL COMMENT '标签id',
  PRIMARY KEY (`article_id`, `tag_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文章-标签关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_article_tag
-- ----------------------------
INSERT INTO `t_article_tag` VALUES (2, 1);

-- ----------------------------
-- Table structure for t_external_link
-- ----------------------------
DROP TABLE IF EXISTS `t_external_link`;
CREATE TABLE `t_external_link`  (
  `external_link_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `external_link_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接名称',
  `external_link_link` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接地址',
  `external_link_icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接图标',
  `external_link_blank` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '_blank' COMMENT '链接打开方式',
  `external_link_status` tinyint(1) DEFAULT 0 COMMENT '状态,0、待审核1、已通过',
  `external_link_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '所属用户',
  `external_link_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) DEFAULT NULL COMMENT '链接创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '链接修改时间',
  PRIMARY KEY (`external_link_id`) USING BTREE,
  UNIQUE INDEX `t_external_link__index_name_unique`(`external_link_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 15 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '友情链接表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_external_link
-- ----------------------------
INSERT INTO `t_external_link` VALUES (1, '学生工作网', 'http://xgb.hit.edu.cn/', '', '_blank', 1, '67489e02c9b44da6aa33735ee9beb4d5', '学生工作网', '2024-5-23 19:44:55', '2024-05-23 21:36:24');
INSERT INTO `t_external_link` VALUES (3, '今日哈工大', 'https://today.hit.edu.cn', '', '_blank', 1, '67489e02c9b44da6aa33735ee9beb4d5', '今日哈工大', '2024-5-23 19:44:55', NULL);
INSERT INTO `t_external_link` VALUES (11, '工大圈子', '127.0.0.1:3000/index', '', '_blank', 1, '67489e02c9b44da6aa33735ee9beb4d5', '工大圈子主页', '2024-5-07 19:03:35', NULL);
INSERT INTO `t_external_link` VALUES (13, '电子邮箱', 'https://mail.hit.edu.cn/', '', '_blank', 1, '67489e02c9b44da6aa33735ee9beb4d5', '电子邮箱', '2024-05-29 22:25:34', NULL);
INSERT INTO `t_external_link` VALUES (14, '学校官网', 'http://www.hit.edu.cn/', '', '_blank', 1, '67489e02c9b44da6aa33735ee9beb4d5', '学校官网', '2024-05-23 21:32:39', NULL);

-- ----------------------------
-- Table structure for t_feedback
-- ----------------------------
DROP TABLE IF EXISTS `t_feedback`;
CREATE TABLE `t_feedback`  (
  `feedback_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '反馈id',
  `feedback_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '反馈类型',
  `feedback_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '反馈内容',
  `feedback_images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '反馈图片',
  `feedback_status` tinyint(1) DEFAULT 0 COMMENT '状态，1、已处理 0、未处理',
  `feedback_user` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '反馈用户',
  `feedback_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户接收邮箱',
  `feedback_time` datetime(0) DEFAULT NULL COMMENT '反馈时间',
  PRIMARY KEY (`feedback_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户反馈表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_feedback_reply
-- ----------------------------
DROP TABLE IF EXISTS `t_feedback_reply`;
CREATE TABLE `t_feedback_reply`  (
  `reply_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `feedback_id` int(11) NOT NULL COMMENT '反馈id',
  `reply_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '回复内容',
  `admin_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '处理人',
  `reply_time` datetime(0) DEFAULT NULL COMMENT '处理时间',
  PRIMARY KEY (`reply_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户反馈回复表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_file_path
-- ----------------------------
DROP TABLE IF EXISTS `t_file_path`;
CREATE TABLE `t_file_path`  (
  `file_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文件id',
  `file_orign_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '源文件名',
  `file_real_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件真实路径',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小',
  `file_access_url` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '文件访问路径',
  `file_structure` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '文件结构',
  `create_date` datetime(0) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`file_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件资源表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_log_info
-- ----------------------------
DROP TABLE IF EXISTS `t_log_info`;
CREATE TABLE `t_log_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '日志id',
  `param` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '入参',
  `response` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '出参',
  `opera_result` tinyint(1) DEFAULT NULL COMMENT '操作结果,1、成功 2、失败',
  `opera_method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作接口方法名',
  `opera_error_why` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '失败原因描述',
  `opera_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作说明',
  `opera_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '操作人用户id',
  `opera_time` datetime(0) DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_log_info
-- ----------------------------

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`  (
  `menu_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '菜单id',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单名称',
  `menu_path` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单路径',
  `menu_component` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '菜单组件',
  `menu_redirect` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '重定向地址，在面包屑中点击会重定向去的地址',
  `menu_hidden` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否在侧边栏显示,1、不显示、0、显示',
  `menu_always_show` tinyint(1) NOT NULL DEFAULT 1 COMMENT '一直显示根路由,1、显示，0、不显示',
  `menu_meta` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '开发者配置项',
  `menu_parant_id` int(11) DEFAULT 1 COMMENT '父菜单id',
  PRIMARY KEY (`menu_id`) USING BTREE,
  UNIQUE INDEX `t_menu__index_name_unique`(`menu_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 47 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '菜单权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES (13, 'system', '/system', '', '', 0, 0, '{\"icon\":\"el-icon-setting\",\"title\":\"系统管理\"}', -1);
INSERT INTO `t_menu` VALUES (14, 'systemrole', '/system/role', '', '', 0, 0, '{\"breadcrumb\":false,\"affix\":false,\"icon\":\"el-icon-s-custom\",\"title\":\"角色管理\"}', 13);
INSERT INTO `t_menu` VALUES (15, 'systemuser', '/system/user', '', '', 0, 0, '{\"icon\":\"el-icon-user\",\"title\":\"用户管理\"}', 13);
INSERT INTO `t_menu` VALUES (16, 'systemmenu', '/system/menu', '', '', 0, 0, '{\"icon\":\"el-icon-menu\",\"title\":\"菜单管理\"}', 13);
INSERT INTO `t_menu` VALUES (26, 'mainManage', '/main', '', '/nav', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-s-home\",\"title\":\"首页管理\",\"noCahce\":false}', -1);
INSERT INTO `t_menu` VALUES (27, 'navManage', '/main/nav', '', '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-link\",\"title\":\"导航链接管理\",\"noCahce\":false}', 26);
INSERT INTO `t_menu` VALUES (28, '/main/friend_link', '/main/friend_link', '', '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-paperclip\",\"title\":\"友情链接管理\",\"noCahce\":false}', 26);
INSERT INTO `t_menu` VALUES (29, 'searchTagManage', '/main/tag', '', '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-collection-tag\",\"title\":\"搜索标签管理\",\"noCahce\":false}', 26);
INSERT INTO `t_menu` VALUES (30, 'technologyType', '/main/technology', NULL, '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-s-grid\",\"title\":\"技术分类管理\",\"noCahce\":false}', 26);
INSERT INTO `t_menu` VALUES (31, 'taskManage', '/system/task', NULL, '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-stopwatch\",\"title\":\"任务管理\",\"noCahce\":false}', 13);
INSERT INTO `t_menu` VALUES (32, 'blog', '/blog', NULL, '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-orange\",\"title\":\"博客管理\",\"noCahce\":false}', -1);
INSERT INTO `t_menu` VALUES (33, 'blog-examine', '/blog/examine', NULL, '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-s-check\",\"title\":\"博客审核\",\"noCahce\":false}', 32);
INSERT INTO `t_menu` VALUES (34, 'logManage', '/log/operalog', NULL, '', 0, 0, '{\"breadcrumb\":false,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-document-add\",\"title\":\"操作日志管理\",\"noCahce\":false}', 43);
INSERT INTO `t_menu` VALUES (35, 'notice', '/blog/notice', NULL, '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"gonggao\",\"title\":\"公告管理\",\"noCahce\":false}', 13);
INSERT INTO `t_menu` VALUES (36, 'blog-manage', '/blog/manage', NULL, '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-place\",\"title\":\"博客管理\",\"noCahce\":false}', 32);
INSERT INTO `t_menu` VALUES (37, 'about_system_info', '/system/info', NULL, '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-s-tools\",\"title\":\"关于本站\",\"noCahce\":false}', 13);
INSERT INTO `t_menu` VALUES (38, 'notice-add-etit', '/blog/editNotice/:id?', NULL, '', 1, 0, '{\"breadcrumb\":false,\"activeMenu\":\"/blog/notice\",\"affix\":false,\"icon\":\"\",\"title\":\"编辑公告\",\"noCahce\":false}', 32);
INSERT INTO `t_menu` VALUES (39, 'other', '/other', NULL, '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-menu\",\"title\":\"其他管理\",\"noCahce\":false}', -1);
INSERT INTO `t_menu` VALUES (40, 'question', '/other/question', NULL, '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-postcard\",\"title\":\"相关问题管理\",\"noCahce\":false}', 39);
INSERT INTO `t_menu` VALUES (41, 'feedback', '/other/feedback', NULL, '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"email\",\"title\":\"反馈管理\",\"noCahce\":false}', 39);
INSERT INTO `t_menu` VALUES (42, 'report', '/other/report', NULL, '', 0, 0, '{\"breadcrumb\":true,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"email\",\"title\":\"举报管理\",\"noCahce\":false}', 39);
INSERT INTO `t_menu` VALUES (43, 'log', '/log', NULL, '', 0, 0, '{\"breadcrumb\":false,\"activeMenu\":\"\",\"affix\":false,\"icon\":\"el-icon-document-add\",\"title\":\"日志管理\",\"noCahce\":false}', -1);

-- ----------------------------
-- Table structure for t_nav_link
-- ----------------------------
DROP TABLE IF EXISTS `t_nav_link`;
CREATE TABLE `t_nav_link`  (
  `nav_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '链接id',
  `nav_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '链接名称',
  `nav_link` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '链接/路由地址',
  `nav_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '链接类型, 1、路由 2、链接',
  `nav_icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '链接图标',
  `nav_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '鼠标移入说明',
  `nav_must_login` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否需要登录查看',
  `nav_remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  `nav_order` int(11) NOT NULL COMMENT '链接顺序',
  PRIMARY KEY (`nav_id`) USING BTREE,
  UNIQUE INDEX `t_nav_link__index_name_unique`(`nav_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'nav-top链接表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_nav_link
-- ----------------------------
INSERT INTO `t_nav_link` VALUES (3, '关于本站', '/system/info', 1, 'el-icon-more-outline', '关于本站', 0, '', 1);
INSERT INTO `t_nav_link` VALUES (4, '发布', '/article/publish', 1, 'el-icon-chat-dot-square', '发布', 1, '', 4);
INSERT INTO `t_nav_link` VALUES (9, '用户反馈', '/fallback', 1, 'el-icon-s-comment', '用户反馈', 1, '', 3);
INSERT INTO `t_nav_link` VALUES (10, '工大聊天室', 'http://127.0.0.1:3002/#/', 2, '', '工大聊天室', 0, '', 2);
INSERT INTO `t_nav_link` VALUES (11, '今日哈工大', 'https://today.hit.edu.cn', 2, '', '今日哈工大', 0, '', 5);

-- ----------------------------
-- Table structure for t_notice_info
-- ----------------------------
DROP TABLE IF EXISTS `t_notice_info`;
CREATE TABLE `t_notice_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '公告id',
  `notice_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '公告标题',
  `notice_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '公告内容',
  `is_push` tinyint(1) DEFAULT 0 COMMENT '是否首页轮询推送',
  `publish_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '发布人用户id',
  `publish_time` datetime(0) DEFAULT NULL COMMENT '发布时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '公告信息表' ROW_FORMAT = Dynamic;


-- ----------------------------
-- Records of t_notice_info
-- ----------------------------
INSERT INTO `t_notice_info` VALUES (2, '欢迎使用工大圈子', '#### 欢迎公告\n欢迎大家加入工大圈子！在这里，您可以发布动态、分享生活、结交朋友。我们致力于为您提供一个自由交流的平台。', 1, '67489e02c9b44da6aa33735ee9beb4d5', '2024-05-10 09:30:00', NULL);
INSERT INTO `t_notice_info` VALUES (1, '首次运营公告', '#### 首次运营通知\n工大圈子正式上线运营！为了确保大家有一个良好的使用体验，我们将在接下来的几周内持续优化平台功能，欢迎大家积极参与并提供宝贵意见。', 1, '67489e02c9b44da6aa33735ee9beb4d5', '2024-06-01 09:00:00', NULL);
INSERT INTO `t_notice_info` VALUES (3, '使用注意事项', '#### 注意事项\n1. 请勿发布违法违规内容，保持友善和谐的交流氛围。\n2. 请保护好个人隐私，不要随意泄露个人信息。\n3. 如果遇到任何问题或有任何建议，请通过反馈功能联系我们。', 1, '67489e02c9b44da6aa33735ee9beb4d5', '2024-06-01 10:00:00', NULL);
INSERT INTO `t_notice_info` VALUES (4, '举报和反馈', '#### 举报和反馈\n为了维护平台的健康发展，我们鼓励用户积极举报不良信息和行为。您可以通过平台内的举报功能进行反馈，我们将及时处理。感谢您的支持和理解。', 1, '67489e02c9b44da6aa33735ee9beb4d5', '2024-06-15 10:30:00', NULL);
INSERT INTO `t_notice_info` VALUES (5, '平台运营政策', '#### 平台运营政策\n为了确保平台的正常运营，我们制定了一系列管理政策，涵盖用户注册、内容发布、信息安全等方面。详细政策内容请参阅我们的用户协议和隐私政策。', 1, '67489e02c9b44da6aa33735ee9beb4d5', '2024-06-16 11:00:00', NULL);
INSERT INTO `t_notice_info` VALUES (6, '功能介绍', '#### 功能介绍\n工大圈子为您提供了丰富的功能，包括动态发布、评论互动、好友添加等。我们将不断更新和完善功能，提升用户体验。', 1, '67489e02c9b44da6aa33735ee9beb4d5', '2024-06-16 11:30:00', NULL);


# -- ----------------------------
# -- Records of t_point_trade_flow
# -- ----------------------------
# INSERT INTO `t_point_trade_flow` VALUES (1, '67489e02c9b44da6aa33735ee9beb4d5', 1, 1, 2, '文章审核通过', '2024-02-26 15:08:33');


-- ----------------------------
-- Table structure for t_realtion_question
-- ----------------------------
DROP TABLE IF EXISTS `t_realtion_question`;
CREATE TABLE `t_realtion_question`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `question` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '问题',
  `question_answer` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '答案',
  `is_show` tinyint(1) DEFAULT 0 COMMENT '是否展示',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '问题回答表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_realtion_question
-- ----------------------------
INSERT INTO `t_realtion_question` VALUES (1, '如何注册新用户?', '未登录情况：\n1. 点击有上角登录按钮。\n2. 在登录框下点击`注册新用户>>`按钮。\n3. 填写基本信息，通过邮箱验证码进行注册。\n4. 注册成功后即可登陆了系统。\n5. 新用户会强制提醒更新信息哦。\n\n已登录情况：\n1. 先退出登录，操作同上', 1, '2024-05-14 12:23:52');
INSERT INTO `t_realtion_question` VALUES (2, '如何更改头像', '首先登录自己的账号，如无账号，先进行用户注册\n1、点击进入个人中心\n2、点击上传头像', 1, '2024-05-14 12:33:45');

-- ----------------------------
-- Table structure for t_report
-- ----------------------------
DROP TABLE IF EXISTS `t_report`;
CREATE TABLE `t_report`  (
  `report_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'report_id',
  `report_type` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '举报类型',
  `report_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '举报描述',
  `report_images` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '举报图片',
  `report_status` tinyint(4) DEFAULT 0 COMMENT '状态,0、待处理，1、已处理',
  `report_target` tinyint(4) NOT NULL DEFAULT 1 COMMENT '举报对象,1、资源博客,2、评论',
  `report_target_id` int(11) NOT NULL COMMENT '举报对象id,report_target=1时为articleId,report_target=2时为commentId',
  `report_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '举报人id',
  `report_time` datetime(0) DEFAULT NULL COMMENT '举报时间',
  `report_handle_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '举报处理描述',
  `report_handle_admin` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '处理人id',
  `report_handle_result` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '举报处理结果',
  `report_handle_time` datetime(0) DEFAULT NULL COMMENT '举报处理时间',
  PRIMARY KEY (`report_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户举报表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `role_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色名',
  `role_desc` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`role_id`) USING BTREE,
  UNIQUE INDEX `t_role__index_name_unique`(`role_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, 'super_admin', '超级管理员');
INSERT INTO `t_role` VALUES (2, 'admin', '管理员');
INSERT INTO `t_role` VALUES (3, 'user', '用户');
INSERT INTO `t_role` VALUES (4, 'sys_view', '观察者');
INSERT INTO `t_role` VALUES (5, 'role_examine', '博客审核员');

-- ----------------------------
-- Table structure for t_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_role_menu`;
CREATE TABLE `t_role_menu`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `menu_id` int(11) NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 170 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_role_menu
-- ----------------------------
INSERT INTO `t_role_menu` VALUES (151, 5, 32);
INSERT INTO `t_role_menu` VALUES (152, 5, 33);
INSERT INTO `t_role_menu` VALUES (153, 5, 36);
INSERT INTO `t_role_menu` VALUES (161, 2, 13);
INSERT INTO `t_role_menu` VALUES (162, 2, 35);
INSERT INTO `t_role_menu` VALUES (163, 2, 26);
INSERT INTO `t_role_menu` VALUES (164, 2, 29);
INSERT INTO `t_role_menu` VALUES (165, 2, 39);
INSERT INTO `t_role_menu` VALUES (166, 2, 41);
INSERT INTO `t_role_menu` VALUES (167, 2, 42);
INSERT INTO `t_role_menu` VALUES (168, 2, 43);
INSERT INTO `t_role_menu` VALUES (169, 2, 34);

-- ----------------------------
-- Table structure for t_scheduled
-- ----------------------------
DROP TABLE IF EXISTS `t_scheduled`;
CREATE TABLE `t_scheduled`  (
  `task_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '任务id',
  `task_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务key值（使用bean名称）',
  `task_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务名称',
  `task_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '任务描述',
  `task_cron` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '任务表达式',
  `task_status` tinyint(1) DEFAULT 0 COMMENT '状态(0.禁用; 1.启用)',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`task_id`) USING BTREE,
  UNIQUE INDEX `uniqu_task_key`(`task_key`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务配置表' ROW_FORMAT = Dynamic;

# -- ----------------------------
# -- Records of t_scheduled
# -- ----------------------------
# INSERT INTO `t_scheduled` VALUES (1, 'taskTest', '测试任务', '11111', '*/5 * * * * ?', 0, '2022-11-16 17:42:32', '2023-11-04 17:56:32');
# INSERT INTO `t_scheduled` VALUES (2, 'hotSearchArticleTask', '文章热搜Task', '同步热搜文章', '0 0 0 * * ?', 1, '2022-12-04 20:00:17', NULL);
# INSERT INTO `t_scheduled` VALUES (3, 'pointOrderStatusTask', '订单任务状态回溯Task', '订单任务状态回溯Task', '0 */5 * * * ?', 1, '2023-10-04 21:40:17', NULL);
# INSERT INTO `t_scheduled` VALUES (4, 'userAuthScheduledTask', '用户权限状态定时任务', '用户权限状态定时任务', '*/30 * * * * ?', 1, '2023-11-03 17:59:09', NULL);
# INSERT INTO `t_scheduled` VALUES (5, 'clearInvalidDataTask', '清理过期定时任务日志', '清理过期定时任务日志', '0 0 */12 * * ?', 1, '2023-11-04 17:53:18', NULL);

-- ----------------------------
-- Table structure for t_scheduled_log
-- ----------------------------
DROP TABLE IF EXISTS `t_scheduled_log`;
CREATE TABLE `t_scheduled_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `task_id` int(11) NOT NULL COMMENT '任务id',
  `execute_time` datetime(0) DEFAULT NULL COMMENT '执行时间',
  `execute_status` tinyint(1) DEFAULT 0 COMMENT '执行状态,1成功,0失败',
  `execute_desc` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '定时任务日志记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_system_info
-- ----------------------------
DROP TABLE IF EXISTS `t_system_info`;
CREATE TABLE `t_system_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sys_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '站点标题',
  `sys_graph` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '站点说明',
  `sys_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '站点说明',
  `sys_weixin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '站点微信',
  `sys_wenxin_public` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信公众号',
  `sys_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '站点联系邮箱',
  `sys_logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '站点logo',
  `sys_logo_title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '站点logo说明',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '站点系统表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_system_info
-- ----------------------------

INSERT INTO `t_system_info` VALUES (1, '工大圈子', '规格严格，功夫到家', '### 系统背景\n工大圈子是一个社交平台，旨在为用户提供一个分享生活、交流思想、结识朋友的场所。平台采用前后端分离的架构，确保用户体验流畅、安全。\n\n### 关于系统\n#### 系统地址\n- 前台系统: [127.0.0.1:3000/index](127.0.0.1:3000)\n- 管理系统: [127.0.0.1:9527](127.0.0.1:9527)\n#### 用户注册\n- 普通用户可在前台页面直接注册！\n\n### 联系我们\n- 邮箱：support@gongdaquan.com\n', '/upload/20240226/touxiang.jpg', '/upload/20240226/touxiang.jpg', 'caishenghao040115@163.com', '/upload/20240226/67489e02c9b44da6aa33735ee9beb4d520240226150935.png', '规格严格，功夫到家');
-- ----------------------------
-- Table structure for t_system_message
-- ----------------------------
DROP TABLE IF EXISTS `t_system_message`;
CREATE TABLE `t_system_message`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息id、主键id',
  `receive_user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '通知用户id、为空则为全部通知',
  `message_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '通知内容',
  `create_time` datetime(0) DEFAULT NULL COMMENT '通知创建时间',
  `is_read` tinyint(1) NOT NULL DEFAULT 0 COMMENT '已读标记',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_tag
-- ----------------------------
DROP TABLE IF EXISTS `t_tag`;
CREATE TABLE `t_tag`  (
  `tag_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '标签id',
  `tag_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`tag_id`) USING BTREE,
  UNIQUE INDEX `t_tag__index_name_unique`(`tag_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '标签表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_tag
-- ----------------------------

-- 观点交流
INSERT INTO `t_tag` VALUES (1, '观点交流', NULL);
-- 学校生活
INSERT INTO `t_tag` VALUES (2, '学校生活', NULL);
-- 学术讨论
INSERT INTO `t_tag` VALUES (3, '学术讨论', NULL);
-- 活动通知
INSERT INTO `t_tag` VALUES (4, '活动通知', NULL);
-- 心情随笔
INSERT INTO `t_tag` VALUES (5, '心情随笔', NULL);
-- 娱乐文化
INSERT INTO `t_tag` VALUES (6, '娱乐文化', NULL);
-- 校园新闻
INSERT INTO `t_tag` VALUES (7, '校园新闻', NULL);
-- 技术分享
INSERT INTO `t_tag` VALUES (8, '技术分享', NULL);
-- 校园活动
INSERT INTO `t_tag` VALUES (9, '校园活动', NULL);
-- 社交互动
INSERT INTO `t_tag` VALUES (10, '社交互动', NULL);
-- 校园资讯
INSERT INTO `t_tag` VALUES (11, '校园资讯', NULL);
-- 学习心得
INSERT INTO `t_tag` VALUES (12, '学习心得', NULL);
-- 校园文化
INSERT INTO `t_tag` VALUES (13, '校园文化', NULL);
-- 校友交流
INSERT INTO `t_tag` VALUES (14, '校友交流', NULL);
-- 校园风景
INSERT INTO `t_tag` VALUES (15, '校园风景', NULL);
-- 校园政治
INSERT INTO `t_tag` VALUES (16, '竞赛组队', NULL);

INSERT INTO `t_tag` VALUES (17, '二手交易', NULL);
INSERT INTO `t_tag` VALUES (18, '社团交流', NULL);




-- ----------------------------
-- Table structure for t_technology_type
-- ----------------------------
DROP TABLE IF EXISTS `t_technology_type`;
CREATE TABLE `t_technology_type`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分类id',
  `type_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名',
  `type_parent_id` int(11) NOT NULL DEFAULT -1 COMMENT '父id, -1表示一级',
  `type_route` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '分类链接',
  `type_icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '图标',
  `type_del` tinyint(1) NOT NULL DEFAULT 0 COMMENT '0、未删除，1、已删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `t_technology_type__index_name_unique`(`type_name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 48 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '技术分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_technology_type
-- ----------------------------
INSERT INTO `t_technology_type` VALUES (1, '竞赛组队', -1, NULL, 'el-icon-user-solid', 0);
INSERT INTO `t_technology_type` VALUES (2, '校级赛事', 1, '', '', 0);
INSERT INTO `t_technology_type` VALUES (3, '国家级赛事', 1, NULL, NULL, 0);

INSERT INTO `t_technology_type` VALUES (17, '二手交易', -1, '', 'el-icon-shopping-cart-2', 0);
INSERT INTO `t_technology_type` VALUES (18, '健身器械', 17, '', '', 0);
INSERT INTO `t_technology_type` VALUES (19, '电脑外设', 17, '', '', 0);
INSERT INTO `t_technology_type` VALUES (20, '二手课本', 17, '', '', 0);

INSERT INTO `t_technology_type` VALUES (22, '校园服务', -1, '', 'el-icon-phone-outline', 0);
INSERT INTO `t_technology_type` VALUES (23, '技术分享', 22, '', '', 0);
INSERT INTO `t_technology_type` VALUES (24, '校园资讯', 22, '', '', 0);
INSERT INTO `t_technology_type` VALUES (25, '校园风景', 22, '', '', 0);
INSERT INTO `t_technology_type` VALUES (26, '家教信息', 22, '', '', 0);

INSERT INTO `t_technology_type` VALUES (27, '校园新闻', -1, '', 'el-icon-s-comment', 0);
INSERT INTO `t_technology_type` VALUES (28, '校园公告通知', 27, '', '', 0);
INSERT INTO `t_technology_type` VALUES (29, '校园政策解读', 27, '', '', 0);
INSERT INTO `t_technology_type` VALUES (30, '校园新闻报道', 27, '', '', 0);
INSERT INTO `t_technology_type` VALUES (31, '校园安全警示', 27, '', '', 0);


INSERT INTO `t_technology_type` VALUES (32, '学术新闻', -1, '', 'el-icon-reading', 0);
INSERT INTO `t_technology_type` VALUES (33, '新闻第一跳', 32, '', '', 0);
INSERT INTO `t_technology_type` VALUES (34, '校友访谈', 32, '', '', 0);
INSERT INTO `t_technology_type` VALUES (35, '学术交流', 32, '', '', 0);
INSERT INTO `t_technology_type` VALUES (36, '校园集市', 32, '', '', 0);


INSERT INTO `t_technology_type` VALUES (37, '公告通知', -1, '', 'el-icon-date', 0);
INSERT INTO `t_technology_type` VALUES (38, '考试安排', 37, '', '', 0);
INSERT INTO `t_technology_type` VALUES (39, '放假通知', 37, '', '', 0);
INSERT INTO `t_technology_type` VALUES (40, '招生通知', 37, '', '', 0);
INSERT INTO `t_technology_type` VALUES (41, '毕业安排', 37, '', '', 0);


INSERT INTO `t_technology_type` VALUES (42, '政策解读', -1, '', 'el-icon-collection-tag', 0);
INSERT INTO `t_technology_type` VALUES (43, '学习政策', 42, '', '', 0);
INSERT INTO `t_technology_type` VALUES (44, '住宿管理', 42, '', '', 0);
INSERT INTO `t_technology_type` VALUES (45, '奖助学金', 42, '', '', 0);
INSERT INTO `t_technology_type` VALUES (46, '考勤政策', 42, '', '', 0);


INSERT INTO `t_technology_type` VALUES (47, '安全警示', -1, '', 'el-icon-warning-outline', 0);
INSERT INTO `t_technology_type` VALUES (48, '交通安全', 47, '', '', 0);
INSERT INTO `t_technology_type` VALUES (49, '实验室安全', 47, '', '', 0);
INSERT INTO `t_technology_type` VALUES (50, '饮食安全', 47, '', '', 0);
INSERT INTO `t_technology_type` VALUES (51, '个人安全', 47, '', '', 0);

-- 删除现有表（如果存在）
DROP TABLE IF EXISTS `friend_request`;
DROP TABLE IF EXISTS `friend`;
DROP TABLE IF EXISTS `t_user`;
DROP TABLE IF EXISTS `messages`;

-- 创建 t_user 表
CREATE TABLE `t_user`  (
                           `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
                           `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
                           `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
                           `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '头像地址',
                           `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '真实姓名',
                           `phone` varchar(11) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '手机号',
                           `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '邮箱地址',
                           `create_time` datetime(0) NOT NULL COMMENT '用户注册时间',
                           `update_time` datetime(0) DEFAULT NULL COMMENT '用户修改时间',
                           `autograph` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户签名',
                           `user_status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '用户状态,1、正常 2、锁定',
                           `point` int(11) DEFAULT 0 COMMENT '用户积分',
                           `sex` tinyint(1) DEFAULT NULL COMMENT '性别,1、男 2、女',
                           `user_type` tinyint(1) DEFAULT 1 COMMENT '用户类型,1、新用户 2、老用户',
                           PRIMARY KEY (`user_id`) USING BTREE,
                           UNIQUE INDEX `t_user__index_username_unique`(`user_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Compact;

-- 创建 friend_request 表
CREATE TABLE `friend_request` (
                                  `id` INT AUTO_INCREMENT PRIMARY KEY,
                                  `sender_id` varchar(50) NOT NULL,
                                  `receiver_id` varchar(50) NOT NULL,
                                  `accepted` BOOLEAN DEFAULT FALSE,
                                  `sender_username` VARCHAR(255),
                                  FOREIGN KEY (`sender_id`) REFERENCES `t_user`(`user_id`),
                                  FOREIGN KEY (`receiver_id`) REFERENCES `t_user`(`user_id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;

-- 创建 friend 表
CREATE TABLE `friend` (
                          `id` INT AUTO_INCREMENT PRIMARY KEY,
                          `user_id` varchar(50) NOT NULL,
                          `friend_id` varchar(50) NOT NULL,
                          FOREIGN KEY (`user_id`) REFERENCES `t_user`(`user_id`),
                          FOREIGN KEY (`friend_id`) REFERENCES `t_user`(`user_id`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci;



-- 创建 messages 表，并添加外键约束
CREATE TABLE messages (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          sender_id VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                          receiver_id VARCHAR(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                          content TEXT,
                          timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (sender_id) REFERENCES t_user(user_id),
                          FOREIGN KEY (receiver_id) REFERENCES t_user(user_id)
);


-- ----------------------------
-- Records of t_user
-- ----------------------------

INSERT INTO `t_user` VALUES ('67489e02c9b44da6aa33735ee9beb4d5', 'admin', '$2a$10$.ri8.lj6hubQ04raQs69ouL/rTovDK4oHkzks9PQwdiRJVIVdPzxW', '/upload/20240226/touxiang.jpg', '工大圈子运营中心', '13663888134', 'caishenghao040115@163.com', '2024-03-21 19:10:38', '2024-03-26 15:08:57', '加油，未来可期！', 1, 1473, 1, 2);
INSERT INTO `t_user` VALUES ('67489e02c9b44da6aa33735ee9beb4d7', 'user', '$2a$10$Z.A0C7vE5m956cL4xf8akuYNXRqhhotw0WT2M5ol9L.892h7f44OK', '/upload/20240226/67489e02c9b44da6aa33735ee9beb4d720240226150854.jpg', 'Java程序员', '14793254595', '14793254595@163.com', '2024-5-18 09:28:20', '2024-05-26 14:43:53', 'IT码农工人!', 1, 521, 1, 2);
-- ----------------------------
-- Table structure for t_user_auth
-- ----------------------------
DROP TABLE IF EXISTS `t_user_auth`;
CREATE TABLE `t_user_auth`  (
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `upload_auth` tinyint(1) DEFAULT 1 COMMENT '文件上传权限',
  `comment_auth` tinyint(1) DEFAULT 1 COMMENT '发言权限',
  `reward_auth` tinyint(1) DEFAULT 1 COMMENT '打赏功能权限',
  `push_article_auth` tinyint(1) DEFAULT 1 COMMENT '文章发布权限',
  `apply_external_auth` tinyint(1) DEFAULT 1 COMMENT '申请友链权限',
  `feedback_auth` tinyint(1) DEFAULT 1 COMMENT '用户反馈权限',
  `report_auth` tinyint(1) DEFAULT 1 COMMENT '用户举报权限',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_auth
-- ----------------------------
INSERT INTO `t_user_auth` VALUES ('2692ebab93474c23b397e2024aa0be18', 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `t_user_auth` VALUES ('2cdd3fbb40d347638130026e511ebd5d', 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `t_user_auth` VALUES ('384297486ac14b5dbca708e0491c0dd3', 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `t_user_auth` VALUES ('39cd382a7f094c47800a135cd59c4423', 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `t_user_auth` VALUES ('3b9a9185f2054ce8b40efddb8249ae0b', 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `t_user_auth` VALUES ('4298421471ec42649f32de7132d69c71', 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `t_user_auth` VALUES ('5ed17b0bf3ed4491a8f1ee1bfa26591f', 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `t_user_auth` VALUES ('67489e02c9b44da6aa33735ee9beb4d5', 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `t_user_auth` VALUES ('67489e02c9b44da6aa33735ee9beb4d7', 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `t_user_auth` VALUES ('68218b3007484b4c9d3cb8fd38c57d56', 1, 1, 1, 1, 1, 0, 1);
INSERT INTO `t_user_auth` VALUES ('777ec370bd4a4213bdf4ed891574dc1a', 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `t_user_auth` VALUES ('cb65dfac4711478c9842861be638c625', 1, 1, 1, 1, 1, 1, 1);
INSERT INTO `t_user_auth` VALUES ('f3c9734108274f2ebfd0b244abef549e', 1, 1, 1, 1, 1, 1, 1);

-- ----------------------------
-- Table structure for t_user_auth_scheduled
-- ----------------------------
DROP TABLE IF EXISTS `t_user_auth_scheduled`;
CREATE TABLE `t_user_auth_scheduled`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `upload_auth` tinyint(1) DEFAULT NULL COMMENT '文件上传权限',
  `comment_auth` tinyint(1) DEFAULT NULL COMMENT '发言权限',
  `reward_auth` tinyint(1) DEFAULT NULL COMMENT '打赏功能权限',
  `push_article_auth` tinyint(1) DEFAULT NULL COMMENT '文章发布权限',
  `apply_external_auth` tinyint(1) DEFAULT NULL COMMENT '申请友链权限',
  `feedback_auth` tinyint(1) DEFAULT NULL COMMENT '用户反馈权限',
  `report_auth` tinyint(1) DEFAULT NULL COMMENT '用户举报权限',
  `start_time` datetime(0) DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime(0) DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户权限禁止定时表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_user_login_log
-- ----------------------------
DROP TABLE IF EXISTS `t_user_login_log`;
CREATE TABLE `t_user_login_log`  (
  `log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `login_mode` tinyint(1) NOT NULL DEFAULT 1 COMMENT '登录模式',
  `login_time` datetime(0) NOT NULL COMMENT '用户登录时间',
  `login_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录ip',
  `login_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '登录地址',
  PRIMARY KEY (`log_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户登录流水表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_user_login_log
-- ----------------------------
INSERT INTO `t_user_login_log` VALUES (1, '67489e02c9b44da6aa33735ee9beb4d5', 1, '2024-02-26 15:03:31', '127.0.0.1', NULL);
INSERT INTO `t_user_login_log` VALUES (2, '67489e02c9b44da6aa33735ee9beb4d5', 2, '2024-02-26 15:04:29', '127.0.0.1', NULL);

-- ----------------------------
-- Table structure for t_user_reward
-- ----------------------------
DROP TABLE IF EXISTS `t_user_reward`;
CREATE TABLE `t_user_reward`  (
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `weixin_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '微信收款码',
  `weixin_border_color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '#409eff' COMMENT '微信边框颜色',
  `zhifubao_img` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '支付宝收款码',
  `zhifubao_border_color` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT '#409eff' COMMENT '支付宝边框颜色',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户打赏配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
INSERT INTO `t_user_role` VALUES (22, '67489e02c9b44da6aa33735ee9beb4d7', 3);
INSERT INTO `t_user_role` VALUES (26, '67489e02c9b44da6aa33735ee9beb4d6', 2);
INSERT INTO `t_user_role` VALUES (27, '67489e02c9b44da6aa33735ee9beb4d6', 3);
INSERT INTO `t_user_role` VALUES (33, '5ed17b0bf3ed4491a8f1ee1bfa26591f', 4);
INSERT INTO `t_user_role` VALUES (34, '5ed17b0bf3ed4491a8f1ee1bfa26591f', 1);
INSERT INTO `t_user_role` VALUES (35, '68218b3007484b4c9d3cb8fd38c57d56', 3);
INSERT INTO `t_user_role` VALUES (36, '4298421471ec42649f32de7132d69c71', 3);
INSERT INTO `t_user_role` VALUES (37, 'f3c9734108274f2ebfd0b244abef549e', 3);
INSERT INTO `t_user_role` VALUES (44, '2cdd3fbb40d347638130026e511ebd5d', 1);
INSERT INTO `t_user_role` VALUES (45, '2cdd3fbb40d347638130026e511ebd5d', 3);
INSERT INTO `t_user_role` VALUES (46, '2cdd3fbb40d347638130026e511ebd5d', 4);
INSERT INTO `t_user_role` VALUES (54, '67489e02c9b44da6aa33735ee9beb4d5', 1);
INSERT INTO `t_user_role` VALUES (55, '39cd382a7f094c47800a135cd59c4423', 1);
INSERT INTO `t_user_role` VALUES (56, '39cd382a7f094c47800a135cd59c4423', 3);
INSERT INTO `t_user_role` VALUES (57, 'cb65dfac4711478c9842861be638c625', 3);
INSERT INTO `t_user_role` VALUES (58, '384297486ac14b5dbca708e0491c0dd3', 3);
INSERT INTO `t_user_role` VALUES (59, '777ec370bd4a4213bdf4ed891574dc1a', 3);
INSERT INTO `t_user_role` VALUES (61, '2692ebab93474c23b397e2024aa0be18', 2);
INSERT INTO `t_user_role` VALUES (62, '2692ebab93474c23b397e2024aa0be18', 5);
INSERT INTO `t_user_role` VALUES (63, '3b9a9185f2054ce8b40efddb8249ae0b', 2);
INSERT INTO `t_user_role` VALUES (64, '3b9a9185f2054ce8b40efddb8249ae0b', 5);

-- ----------------------------
-- Table structure for t_user_setting
-- ----------------------------
DROP TABLE IF EXISTS `t_user_setting`;
CREATE TABLE `t_user_setting`  (
  `user_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `open_reward` tinyint(1) DEFAULT 0 COMMENT '转发功能,0、未开启 1、已开启',
  `show_point` tinyint(1) DEFAULT 1 COMMENT '显示积分余额,0、不显示 1、显示',
  `receive_email_notice` tinyint(1) DEFAULT 1 COMMENT '是否接受邮件通知',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户设置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_user_setting
-- ----------------------------
INSERT INTO `t_user_setting` VALUES ('2692ebab93474c23b397e2024aa0be18', 0, 1, 1);
INSERT INTO `t_user_setting` VALUES ('2cdd3fbb40d347638130026e511ebd5d', 0, 1, 1);
INSERT INTO `t_user_setting` VALUES ('384297486ac14b5dbca708e0491c0dd3', 0, 1, 1);
INSERT INTO `t_user_setting` VALUES ('39cd382a7f094c47800a135cd59c4423', 0, 1, 0);
INSERT INTO `t_user_setting` VALUES ('3b9a9185f2054ce8b40efddb8249ae0b', 0, 1, 1);
INSERT INTO `t_user_setting` VALUES ('4298421471ec42649f32de7132d69c71', 0, 1, 1);
INSERT INTO `t_user_setting` VALUES ('5ed17b0bf3ed4491a8f1ee1bfa26591f', 0, 1, 1);
INSERT INTO `t_user_setting` VALUES ('67489e02c9b44da6aa33735ee9beb4d5', 1, 1, 1);
INSERT INTO `t_user_setting` VALUES ('67489e02c9b44da6aa33735ee9beb4d7', 0, 0, 1);
INSERT INTO `t_user_setting` VALUES ('68218b3007484b4c9d3cb8fd38c57d56', 0, 1, 1);
INSERT INTO `t_user_setting` VALUES ('777ec370bd4a4213bdf4ed891574dc1a', 0, 1, 1);
INSERT INTO `t_user_setting` VALUES ('cb65dfac4711478c9842861be638c625', 0, 1, 1);
INSERT INTO `t_user_setting` VALUES ('f3c9734108274f2ebfd0b244abef549e', 0, 1, 1);

SET FOREIGN_KEY_CHECKS = 1;

