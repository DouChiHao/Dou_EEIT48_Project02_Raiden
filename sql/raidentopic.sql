-- phpMyAdmin SQL Dump
-- version 5.1.2
-- https://www.phpmyadmin.net/
--
-- 主機： localhost:3306
-- 產生時間： 2022-08-25 20:41:32
-- 伺服器版本： 5.7.24
-- PHP 版本： 8.0.1

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- 資料庫: `raidentopic`
--

-- --------------------------------------------------------

--
-- 資料表結構 `member`
--

CREATE TABLE `member` (
  `id` int(10) NOT NULL,
  `account` varchar(100) CHARACTER SET armscii8 NOT NULL,
  `passwd` varchar(255) CHARACTER SET armscii8 NOT NULL,
  `usermail` varchar(100) CHARACTER SET armscii8 DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `member`
--

INSERT INTO `member` (`id`, `account`, `passwd`, `usermail`) VALUES
(1, 'John', '123456', 'abc@gmail.com'),
(2, 'Dou', '456789', '123test@gmail.com'),
(4, 'Max', 'abc', '123@gmail.com'),
(5, 'Sheep789', '789', '123@gmail.com'),
(6, 'DD', '111', '123@gmail.com'),
(7, 'Fukka', '0505', '123@gmail.com');

-- --------------------------------------------------------

--
-- 資料表結構 `scorerecord`
--

CREATE TABLE `scorerecord` (
  `id` int(10) NOT NULL,
  `account` varchar(100) CHARACTER SET armscii8 NOT NULL,
  `gamescore` int(255) NOT NULL,
  `playtime` varchar(100) CHARACTER SET armscii8 NOT NULL,
  `endshot` longblob,
  `inserttime` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 傾印資料表的資料 `scorerecord`
--

INSERT INTO `scorerecord` (`id`, `account`, `gamescore`, `playtime`, `endshot`, `inserttime`) VALUES
(1, 'John', 167, '30', NULL, '2022-08-24'),
(2, 'John', 268, '47.6', NULL, '2022-08-24'),
(3, 'Dou', 113, '3', NULL, '2022-08-25'),
(4, 'Dou', 391, '51', NULL, '2022-08-25'),
(5, 'Max', 154, '24', NULL, '2022-08-25'),
(6, 'Max', 166, '26', NULL, '2022-08-25'),
(7, 'Max', 166, '26', NULL, '2022-08-25'),
(8, 'John', 1647, '87', NULL, '2022-08-25'),
(9, 'Sheep789', 234, '34', NULL, '2022-08-25'),
(10, 'Sheep789', 370, '50', NULL, '2022-08-25'),
(11, 'Sheep789', 370, '50', NULL, '2022-08-25'),
(12, 'DD', 445, '55', NULL, '2022-08-25'),
(13, 'DD', 245, '35', NULL, '2022-08-25'),
(14, 'DD', 291, '41', NULL, '2022-08-25'),
(15, 'DD', 179, '29', NULL, '2022-08-25'),
(16, 'DD', 403, '53', NULL, '2022-08-25'),
(17, 'Sheep789', 1680, '90', NULL, '2022-08-25'),
(18, 'Dou', 525, '65', NULL, '2022-08-25'),
(19, 'Dou', 388, '48', NULL, '2022-08-25'),
(20, 'Fukka', 67, '17', NULL, '2022-08-25'),
(21, 'Fukka', 89, '19', NULL, '2022-08-25'),
(22, 'Fukka', 54, '14', NULL, '2022-08-25'),
(23, 'Fukka', 80, '20', NULL, '2022-08-25'),
(24, 'Fukka', 133, '23', NULL, '2022-08-25'),
(25, 'Fukka', 166, '26', NULL, '2022-08-25'),
(26, 'Fukka', 899, '69', NULL, '2022-08-25'),
(27, 'John', 98, '18', NULL, '2022-08-25'),
(28, 'John', 1455, '85', NULL, '2022-08-25'),
(29, 'John', 258, '38', NULL, '2022-08-26');

--
-- 已傾印資料表的索引
--

--
-- 資料表索引 `member`
--
ALTER TABLE `member`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `account` (`account`);

--
-- 資料表索引 `scorerecord`
--
ALTER TABLE `scorerecord`
  ADD PRIMARY KEY (`id`),
  ADD KEY `account` (`account`);

--
-- 在傾印的資料表使用自動遞增(AUTO_INCREMENT)
--

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `member`
--
ALTER TABLE `member`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- 使用資料表自動遞增(AUTO_INCREMENT) `scorerecord`
--
ALTER TABLE `scorerecord`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- 已傾印資料表的限制式
--

--
-- 資料表的限制式 `scorerecord`
--
ALTER TABLE `scorerecord`
  ADD CONSTRAINT `scorerecord_ibfk_1` FOREIGN KEY (`account`) REFERENCES `member` (`account`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
