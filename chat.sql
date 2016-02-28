-- phpMyAdmin SQL Dump
-- version 4.4.15.2
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Feb 04, 2016 at 02:03 PM
-- Server version: 5.5.44-MariaDB
-- PHP Version: 5.4.16

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `chat`
--

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(10) NOT NULL,
  `name` varchar(50) COLLATE utf16_unicode_ci NOT NULL,
  `username` varchar(20) COLLATE utf16_unicode_ci NOT NULL,
  `password` varchar(20) COLLATE utf16_unicode_ci NOT NULL,
  `email` varchar(50) COLLATE utf16_unicode_ci NOT NULL,
  `gender` varchar(20) COLLATE utf16_unicode_ci NOT NULL,
  `status` varchar(20) COLLATE utf16_unicode_ci NOT NULL
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf16 COLLATE=utf16_unicode_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `username`, `password`, `email`, `gender`, `status`) VALUES
(1, 'mhmd fawzy', 'm', '1', 'mhmd@me.com', 'Male', 'available'),
(2, 'mhmdfawzy', 'mhmdf', '1111111111', 'mhmd@fgsdf.com', 'Male', 'offline'),
(5, 'ahmed', 'ahmed', '1111111111', 'sdf@rsdf.com', 'Male', 'offline'),
(6, 'mostafatolbe', 'tolba', '1111111111', 'tdfg@fgdhf.com', 'Male', 'offline'),
(13, 'dfdgdtghtfgyu', 'gouda', '1111111111', 'ddgf@tgf.com', 'Male', 'offline'),
(14, 'kuguygb', 'nnnn', '1111111111', 'kuy@k.jug.com', 'Female', 'offline'),
(15, 'aaaaaaaaaaaaaaaaaa', 'h', '1111111111', 'h@h.com', 'Male', 'offline'),
(16, 'bbbbbbbb', 'b', '1111111111', 'b@b.com', 'Male', 'offline'),
(17, 'kkkkkkkkkk', 'kkk', '111111', 'k@s.com', 'Male', 'offline'),
(18, 'm', 'mm', '111111', 'm@m.com', 'Male', 'offline'),
(19, 'mmm', 'mmm', '111111', 'm@m.com', 'Male', 'offline'),
(23, 'mmmm', 's', '111111', 'n@n.com', 'Male', 'offline'),
(24, 'kubhuk', 'q', '111111', 'q@q.com', 'Male', 'offline'),
(25, 'mas', 'fawzy', '111111', 'f@f.com', 'Male', 'offline'),
(26, 'qadi', 'qadi', '111111', 'q@q.com', 'Male', 'offline'),
(27, 'abdo', 'abdo', '111111', 'a@a.com', 'Male', 'offline');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(10) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=28;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
