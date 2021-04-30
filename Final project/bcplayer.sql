-- phpMyAdmin SQL Dump
-- version 5.0.4
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 30, 2021 at 04:17 AM
-- Server version: 10.4.17-MariaDB
-- PHP Version: 8.0.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bcplayer`
--

-- --------------------------------------------------------

--
-- Table structure for table `words`
--

CREATE TABLE `words` (
  `ID` int(11) NOT NULL,
  `word` varchar(25) NOT NULL,
  `characters` int(11) NOT NULL,
  `available` varchar(10) NOT NULL DEFAULT 'Yes'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `words`
--

INSERT INTO `words` (`ID`, `word`, `characters`, `available`) VALUES
(1, 'abs', 3, 'No'),
(2, 'ace', 3, 'No'),
(3, 'yum', 3, 'Yes'),
(4, 'mud', 3, 'Yes'),
(5, 'sun', 3, 'No'),
(6, 'try', 3, 'No'),
(7, 'pet', 3, 'No'),
(8, 'ion', 3, 'Yes'),
(9, 'bun', 3, 'No'),
(10, 'red', 3, 'No'),
(11, 'reed', 4, 'No'),
(12, 'each', 4, 'Yes'),
(13, 'gage', 4, 'Yes'),
(14, 'inch', 4, 'Yes'),
(15, 'keel', 4, 'Yes'),
(16, 'cage', 4, 'No'),
(17, 'able', 4, 'Yes'),
(18, 'ache', 4, 'Yes'),
(19, 'gave', 4, 'Yes'),
(20, 'kids', 4, 'Yes'),
(21, 'cabin', 5, 'Yes'),
(22, 'easel', 5, 'Yes'),
(23, 'ghost', 5, 'No'),
(24, 'lasts', 5, 'No'),
(25, 'abide', 5, 'Yes'),
(26, 'eagle', 5, 'Yes'),
(27, 'gains', 5, 'No'),
(28, 'label', 5, 'No'),
(29, 'gears', 5, 'Yes'),
(30, 'cadet', 5, 'Yes'),
(31, 'Minh', 4, 'Yes'),
(32, 'four', 4, 'Yes');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `words`
--
ALTER TABLE `words`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `words`
--
ALTER TABLE `words`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
