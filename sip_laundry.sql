-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 07, 2023 at 06:53 PM
-- Server version: 10.4.24-MariaDB
-- PHP Version: 8.1.6

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sip_laundry`
--

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `customer_id` int(11) NOT NULL,
  `name` varchar(150) NOT NULL,
  `phone` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `expense`
--

CREATE TABLE `expense` (
  `expense_id` int(11) NOT NULL,
  `name` varchar(30) NOT NULL,
  `expense_date` date NOT NULL,
  `amount` int(11) NOT NULL,
  `user_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `laundries`
--

CREATE TABLE `laundries` (
  `laundry_id` int(11) NOT NULL,
  `unit` enum('kilogram','meter','pcs') NOT NULL,
  `cost` int(11) NOT NULL,
  `name` varchar(150) NOT NULL,
  `IsExpress` tinyint(1) DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `options`
--

CREATE TABLE `options` (
  `option_id` int(11) NOT NULL,
  `key` varchar(100) NOT NULL,
  `value` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `options`
--

INSERT INTO `options` (`option_id`, `key`, `value`) VALUES
(15, 'alamat20', 'sumbersari'),
(17, 'alamat20', 'sumbersari'),
(19, 'alamat20', 'sumbersari'),
(21, 'alamat20', 'sumbersari'),
(23, 'alamat20', 'sumbersari'),
(25, 'alamat20', 'sumbersari'),
(27, 'alamat20', 'sumbersari'),
(29, 'alamat20', 'sumbersari'),
(31, 'alamat20', 'sumbersari'),
(33, 'alamat20', 'sumbersari'),
(35, 'alamat20', 'sumbersari'),
(37, 'alamat20', 'sumbersari'),
(39, 'alamat20', 'sumbersari'),
(41, 'alamat20', 'sumbersari'),
(43, 'alamat20', 'sumbersari'),
(45, 'alamat20', 'sumbersari'),
(47, 'alamat20', 'sumbersari'),
(49, 'alamat20', 'sumbersari'),
(51, 'alamat20', 'sumbersari'),
(53, 'alamat20', 'sumbersari');

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `transaction_id` int(11) NOT NULL,
  `transaction_date` datetime NOT NULL,
  `retard` int(11) NOT NULL DEFAULT 0,
  `pickup_date` datetime NOT NULL,
  `status` enum('process','finish','taken','canceled') DEFAULT 'process',
  `payment_status` enum('paid','unpaid','instalment') NOT NULL,
  `amount` int(11) NOT NULL DEFAULT 0,
  `user_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `transaction_details`
--

CREATE TABLE `transaction_details` (
  `qty` int(11) NOT NULL,
  `subtotal` int(11) NOT NULL DEFAULT 0,
  `transaction_id` int(11) NOT NULL,
  `laundry_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Triggers `transaction_details`
--
DELIMITER $$
CREATE TRIGGER `add_amount` AFTER INSERT ON `transaction_details` FOR EACH ROW UPDATE `transactions` SET `transactions`.`amount` = `transactions`.`amount` + NEW.`subtotal` WHERE `transactions`.`transaction_id` = NEW.`transaction_id`
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `del_amount` AFTER DELETE ON `transaction_details` FOR EACH ROW UPDATE `transactions` SET `transactions`.`amount` = `transactions`.`amount` - OLD.`subtotal` WHERE `transactions`.`transaction_id` = OLD.`transaction_id`
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `update_amount` AFTER UPDATE ON `transaction_details` FOR EACH ROW UPDATE `transactions` SET `transactions`.`amount` = `transactions`.`amount` - OLD.`subtotal` + NEW.`subtotal` WHERE `transactions`.`transaction_id` = OLD.`transaction_id`
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `username` varchar(20) NOT NULL,
  `fullname` varchar(150) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `password` varchar(100) NOT NULL,
  `address` mediumtext NOT NULL,
  `role` enum('admin','cashier') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `verifications`
--

CREATE TABLE `verifications` (
  `user_id` int(11) NOT NULL,
  `code` varchar(8) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customers`
--
ALTER TABLE `customers`
  ADD PRIMARY KEY (`customer_id`);

--
-- Indexes for table `expense`
--
ALTER TABLE `expense`
  ADD PRIMARY KEY (`expense_id`),
  ADD KEY `expense_ibfk_1` (`user_id`);

--
-- Indexes for table `laundries`
--
ALTER TABLE `laundries`
  ADD PRIMARY KEY (`laundry_id`);

--
-- Indexes for table `options`
--
ALTER TABLE `options`
  ADD PRIMARY KEY (`option_id`);

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`transaction_id`),
  ADD KEY `user_id` (`user_id`),
  ADD KEY `customer_id` (`customer_id`);

--
-- Indexes for table `transaction_details`
--
ALTER TABLE `transaction_details`
  ADD PRIMARY KEY (`transaction_id`,`laundry_id`),
  ADD KEY `transaction_details_ibfk_2` (`laundry_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

--
-- Indexes for table `verifications`
--
ALTER TABLE `verifications`
  ADD PRIMARY KEY (`user_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customers`
--
ALTER TABLE `customers`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=158;

--
-- AUTO_INCREMENT for table `expense`
--
ALTER TABLE `expense`
  MODIFY `expense_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `laundries`
--
ALTER TABLE `laundries`
  MODIFY `laundry_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=120;

--
-- AUTO_INCREMENT for table `options`
--
ALTER TABLE `options`
  MODIFY `option_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=55;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `transaction_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=107;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=181;

--
-- AUTO_INCREMENT for table `verifications`
--
ALTER TABLE `verifications`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=171;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `expense`
--
ALTER TABLE `expense`
  ADD CONSTRAINT `expense_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE;

--
-- Constraints for table `transactions`
--
ALTER TABLE `transactions`
  ADD CONSTRAINT `transactions_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `transactions_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `transaction_details`
--
ALTER TABLE `transaction_details`
  ADD CONSTRAINT `transaction_details_ibfk_1` FOREIGN KEY (`transaction_id`) REFERENCES `transactions` (`transaction_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `transaction_details_ibfk_2` FOREIGN KEY (`laundry_id`) REFERENCES `laundries` (`laundry_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `verifications`
--
ALTER TABLE `verifications`
  ADD CONSTRAINT `verifications_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
