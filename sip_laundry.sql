-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 06, 2023 at 08:45 PM
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

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`customer_id`, `name`, `phone`) VALUES
(36, 'sasa', '087546534211'),
(37, 'sinta', '087546534211'),
(38, 'sisi', '087546534211'),
(39, 'nana', '087546534211');

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

--
-- Dumping data for table `laundries`
--

INSERT INTO `laundries` (`laundry_id`, `unit`, `cost`, `name`, `IsExpress`) VALUES
(29, 'meter', 3000, 'korden', NULL),
(30, 'pcs', 7000, 'boneka', NULL),
(31, 'kilogram', 5000, 'baju', NULL),
(32, 'meter', 4000, 'karpet', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `options`
--

CREATE TABLE `options` (
  `option_id` int(11) NOT NULL,
  `key` varchar(100) NOT NULL,
  `value` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `transaction_id` int(11) NOT NULL,
  `transaction_date` datetime NOT NULL,
  `ritard` int(3) DEFAULT 0,
  `pickup_date` datetime NOT NULL,
  `status` enum('process','finish','taken') DEFAULT 'process',
  `payment_status` enum('paid','unpaid') NOT NULL,
  `amount` int(11) NOT NULL DEFAULT 0,
  `user_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`transaction_id`, `transaction_date`, `ritard`, `pickup_date`, `status`, `payment_status`, `amount`, `user_id`, `customer_id`) VALUES
(22, '2023-04-03 13:09:45', NULL, '2023-04-08 13:09:45', 'finish', 'paid', 18000, 8, 36),
(23, '2023-04-01 13:14:43', NULL, '2023-04-03 13:14:43', 'finish', 'unpaid', 14000, 19, 38),
(24, '2023-04-02 13:16:48', NULL, '2023-04-02 13:16:48', 'finish', 'unpaid', 8000, 20, 37),
(25, '2023-04-02 13:23:13', NULL, '2023-04-02 13:23:13', 'taken', 'unpaid', 20000, 20, 39);

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
-- Dumping data for table `transaction_details`
--

INSERT INTO `transaction_details` (`qty`, `subtotal`, `transaction_id`, `laundry_id`) VALUES
(1, 3000, 22, 29),
(3, 15000, 22, 31),
(2, 14000, 23, 30),
(2, 8000, 24, 32),
(4, 20000, 25, 31);

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
  `fullname` varchar(150) NOT NULL,
  `phone` varchar(15) NOT NULL,
  `password` varchar(100) NOT NULL,
  `address` mediumtext NOT NULL,
  `role` enum('admin','cashier') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `fullname`, `phone`, `password`, `address`, `role`) VALUES
(8, 'aldea', '3232333333', 'haikamu', 'jember', 'cashier'),
(19, 'sinta', '089787675654', 'kamu', 'jember', 'admin'),
(20, 'sinta', '089787675654', 'kamu', 'jember', 'admin'),
(21, 'sinta', '089787675654', 'kamu', 'jember', 'admin'),
(26, 'sinta', '089787675654', 'kamu', 'jember', 'admin'),
(31, 'sinta', '089787675654', 'kamu', 'jember', 'admin'),
(37, 'aldea', '3232333333', 'haikamu', 'jember', 'cashier'),
(38, 'sinta', '089787675654', 'kamu', 'jember', 'admin');

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
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=43;

--
-- AUTO_INCREMENT for table `laundries`
--
ALTER TABLE `laundries`
  MODIFY `laundry_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=33;

--
-- AUTO_INCREMENT for table `options`
--
ALTER TABLE `options`
  MODIFY `option_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `transaction_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=42;

--
-- AUTO_INCREMENT for table `verifications`
--
ALTER TABLE `verifications`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- Constraints for dumped tables
--

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
