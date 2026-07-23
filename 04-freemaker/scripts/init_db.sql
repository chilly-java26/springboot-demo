-- 1. 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `book_db` 
  DEFAULT CHARACTER SET utf8mb4 
  DEFAULT COLLATE utf8mb4_general_ci;

USE `book_db`;

-- 2. 创建图书表
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '书名',
  `author` varchar(50) DEFAULT NULL COMMENT '作者',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='图书表';

-- 3. 插入两条初始测试数据（方便启动后查看）
INSERT INTO `book` (`name`, `author`, `price`) VALUES 
('Spring Boot 实战', 'Craig Walls', 69.00),
('Java 并发编程实战', 'Brian Goetz', 89.00);