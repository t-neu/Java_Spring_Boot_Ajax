SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

CREATE DATABASE IF NOT EXISTS `sample` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `sample`;

CREATE TABLE `email_opened` (
  `id` int(11) NOT NULL,
  `username` varchar(60) NOT NULL,
  `type` varchar(12) NOT NULL,
  `sent_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `opened_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `opened` tinyint(4) NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `password_recovery` (
  `id` int(11) NOT NULL,
  `username` varchar(45) NOT NULL,
  `pass_key` varchar(60) NOT NULL,
  `creation` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `users` (
  `username` varchar(60) NOT NULL,
  `email` varchar(60) NOT NULL,
  `password` varchar(60) NOT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT '1'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `user_roles` (
  `user_role_id` int(11) NOT NULL,
  `username` varchar(60) NOT NULL,
  `role` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `zips` (
  `zipcode` varchar(8) NOT NULL DEFAULT '',
  `abr` varchar(19) DEFAULT NULL,
  `latitude` varchar(10) DEFAULT NULL,
  `longitude` varchar(10) DEFAULT NULL,
  `city` varchar(28) DEFAULT NULL,
  `state` varchar(21) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `email_opened`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_username_idx` (`username`);

ALTER TABLE `persistent_logins`
  ADD PRIMARY KEY (`series`);

ALTER TABLE `users`
  ADD PRIMARY KEY (`username`);

ALTER TABLE `user_roles`
  ADD KEY `fk_username` (`username`);

ALTER TABLE `zips`
  ADD PRIMARY KEY (`zipcode`);

ALTER TABLE `email_opened`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

ALTER TABLE `email_opened`
  ADD CONSTRAINT `fk_username_eo` FOREIGN KEY (`username`) REFERENCES `users` (`username`);

ALTER TABLE `user_roles`
  ADD CONSTRAINT `fk_username_ur` FOREIGN KEY (`username`) REFERENCES `users` (`username`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
