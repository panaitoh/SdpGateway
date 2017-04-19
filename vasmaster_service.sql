-- MySQL dump 10.13  Distrib 5.7.16, for Linux (x86_64)
--
-- Host: localhost    Database: vasmaster_service
-- ------------------------------------------------------
-- Server version	5.7.16-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `accounts`
--

DROP TABLE IF EXISTS `accounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `accounts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `spid` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `spid` (`spid`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `activate_service_view`
--

DROP TABLE IF EXISTS `activate_service_view`;
/*!50001 DROP VIEW IF EXISTS `activate_service_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `activate_service_view` AS SELECT 
 1 AS `id`,
 1 AS `service_id`,
 1 AS `ssan`,
 1 AS `criteria`,
 1 AS `spid`,
 1 AS `password`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `correlator`
--

DROP TABLE IF EXISTS `correlator`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `correlator` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `correlator` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7004423 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `deactivate_service_view`
--

DROP TABLE IF EXISTS `deactivate_service_view`;
/*!50001 DROP VIEW IF EXISTS `deactivate_service_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `deactivate_service_view` AS SELECT 
 1 AS `id`,
 1 AS `service_id`,
 1 AS `ssan`,
 1 AS `criteria`,
 1 AS `spid`,
 1 AS `password`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `endpoints`
--

DROP TABLE IF EXISTS `endpoints`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endpoints` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `endpoint_name` varchar(50) DEFAULT NULL,
  `url` varchar(256) DEFAULT NULL,
  `interface_name` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `service_type` varchar(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `account_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `endpoint_name` (`endpoint_name`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `endpoints_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `inbox`
--

DROP TABLE IF EXISTS `inbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inbox` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(1024) DEFAULT NULL,
  `sprevid` varchar(50) DEFAULT NULL,
  `sender_address` varchar(20) NOT NULL,
  `linkid` varchar(50) DEFAULT NULL,
  `date_received` datetime DEFAULT NULL,
  `trace_unique_id` varchar(50) DEFAULT NULL,
  `correlator` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '0',
  `service_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `inbox_ibfk_1` (`service_id`),
  CONSTRAINT `inbox_ibfk_1` FOREIGN KEY (`service_id`) REFERENCES `services` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=251 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `inbox_view`
--

DROP TABLE IF EXISTS `inbox_view`;
/*!50001 DROP VIEW IF EXISTS `inbox_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `inbox_view` AS SELECT 
 1 AS `id`,
 1 AS `spid`,
 1 AS `message`,
 1 AS `sender_address`,
 1 AS `date_received`,
 1 AS `status`,
 1 AS `ssan`,
 1 AS `service_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `outbox`
--

DROP TABLE IF EXISTS `outbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `outbox` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(1024) DEFAULT NULL,
  `sender_address` varchar(20) DEFAULT NULL,
  `link_id` varchar(50) DEFAULT NULL,
  `sent_at` datetime DEFAULT NULL,
  `trace_unique_id` varchar(50) DEFAULT NULL,
  `correlator` varchar(50) DEFAULT NULL,
  `request_identifier` varchar(50) DEFAULT NULL,
  `delivery_status` varchar(50) DEFAULT NULL,
  `delivery_date` datetime DEFAULT NULL,
  `subreq_id` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `service_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `correlator` (`correlator`),
  KEY `service_id` (`service_id`),
  CONSTRAINT `outbox_ibfk_1` FOREIGN KEY (`service_id`) REFERENCES `services` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `outbox_view`
--

DROP TABLE IF EXISTS `outbox_view`;
/*!50001 DROP VIEW IF EXISTS `outbox_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `outbox_view` AS SELECT 
 1 AS `id`,
 1 AS `spid`,
 1 AS `message`,
 1 AS `sender_address`,
 1 AS `sent_at`,
 1 AS `delivery_status`,
 1 AS `delivery_date`,
 1 AS `status`,
 1 AS `ssan`,
 1 AS `service_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_name` varchar(50) DEFAULT NULL,
  `product_type` varchar(20) DEFAULT NULL,
  `product_code` varchar(50) DEFAULT NULL,
  `charge` varchar(20) DEFAULT NULL,
  `subscription_keyword` varchar(50) DEFAULT NULL,
  `unsubscription_keyword` varchar(50) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  `welcome_message` varchar(256) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `service_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `product_name` (`product_name`),
  UNIQUE KEY `product_code` (`product_code`),
  UNIQUE KEY `subscription_keyword` (`subscription_keyword`),
  UNIQUE KEY `unsubscription_keyword` (`unsubscription_keyword`),
  KEY `service_id` (`service_id`),
  CONSTRAINT `products_ibfk_1` FOREIGN KEY (`service_id`) REFERENCES `services` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `products_view`
--

DROP TABLE IF EXISTS `products_view`;
/*!50001 DROP VIEW IF EXISTS `products_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `products_view` AS SELECT 
 1 AS `id`,
 1 AS `product_name`,
 1 AS `product_type`,
 1 AS `product_code`,
 1 AS `charge`,
 1 AS `subscription_keyword`,
 1 AS `unsubscription_keyword`,
 1 AS `pstatus`,
 1 AS `welcome_message`,
 1 AS `service_id`,
 1 AS `serv_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `send_sms_view`
--

DROP TABLE IF EXISTS `send_sms_view`;
/*!50001 DROP VIEW IF EXISTS `send_sms_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `send_sms_view` AS SELECT 
 1 AS `id`,
 1 AS `message`,
 1 AS `sender_address`,
 1 AS `link_id`,
 1 AS `service_id`,
 1 AS `ssan`,
 1 AS `spid`,
 1 AS `password`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `send_subscription_view`
--

DROP TABLE IF EXISTS `send_subscription_view`;
/*!50001 DROP VIEW IF EXISTS `send_subscription_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `send_subscription_view` AS SELECT 
 1 AS `msisdn`,
 1 AS `service_id`,
 1 AS `product_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Temporary view structure for view `service_view`
--

DROP TABLE IF EXISTS `service_view`;
/*!50001 DROP VIEW IF EXISTS `service_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `service_view` AS SELECT 
 1 AS `id`,
 1 AS `service_id`,
 1 AS `ssan`,
 1 AS `criteria`,
 1 AS `correlator`,
 1 AS `status`,
 1 AS `service_type`,
 1 AS `account_id`,
 1 AS `spid`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `services`
--

DROP TABLE IF EXISTS `services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `services` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `service_id` varchar(50) DEFAULT NULL,
  `ssan` varchar(20) DEFAULT NULL,
  `criteria` varchar(50) DEFAULT NULL,
  `correlator` varchar(50) DEFAULT NULL,
  `status` varchar(20) DEFAULT '1',
  `service_type` varchar(20) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `account_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `service_id` (`service_id`),
  UNIQUE KEY `criteria` (`criteria`),
  UNIQUE KEY `correlator` (`correlator`),
  KEY `account_id` (`account_id`),
  CONSTRAINT `services_ibfk_1` FOREIGN KEY (`account_id`) REFERENCES `accounts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Temporary view structure for view `subscription_view`
--

DROP TABLE IF EXISTS `subscription_view`;
/*!50001 DROP VIEW IF EXISTS `subscription_view`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE VIEW `subscription_view` AS SELECT 
 1 AS `id`,
 1 AS `msisdn`,
 1 AS `update_type`,
 1 AS `product_name`,
 1 AS `subscription_keyword`,
 1 AS `charge`,
 1 AS `update_time`,
 1 AS `service_id`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `subscriptions`
--

DROP TABLE IF EXISTS `subscriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscriptions` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(20) DEFAULT NULL,
  `user_type` int(11) DEFAULT NULL,
  `service_list` varchar(50) DEFAULT NULL,
  `update_type` int(11) DEFAULT NULL,
  `update_time` varchar(50) DEFAULT NULL,
  `update_desc` varchar(50) DEFAULT NULL,
  `effective_time` varchar(20) DEFAULT NULL,
  `expiry_time` varchar(20) DEFAULT NULL,
  `unsubscription_date` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT NULL,
  `deleted_at` datetime DEFAULT NULL,
  `product_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `subscriptions_ibfk_1` (`product_id`),
  CONSTRAINT `subscriptions_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Final view structure for view `activate_service_view`
--

/*!50001 DROP VIEW IF EXISTS `activate_service_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `activate_service_view` AS select `s`.`id` AS `id`,`s`.`service_id` AS `service_id`,`s`.`ssan` AS `ssan`,`s`.`criteria` AS `criteria`,`a`.`spid` AS `spid`,`a`.`password` AS `password` from (`services` `s` join `accounts` `a` on((`s`.`account_id` = `a`.`id`))) where (isnull(`s`.`deleted_at`) and isnull(`a`.`deleted_at`) and (`s`.`status` = 'STATUS_PENDING')) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `deactivate_service_view`
--

/*!50001 DROP VIEW IF EXISTS `deactivate_service_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `deactivate_service_view` AS select `s`.`id` AS `id`,`s`.`service_id` AS `service_id`,`s`.`ssan` AS `ssan`,`s`.`criteria` AS `criteria`,`a`.`spid` AS `spid`,`a`.`password` AS `password` from (`services` `s` join `accounts` `a` on((`s`.`account_id` = `a`.`id`))) where (isnull(`s`.`deleted_at`) and isnull(`a`.`deleted_at`) and (`s`.`status` = 'STOP_PENDING')) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `inbox_view`
--

/*!50001 DROP VIEW IF EXISTS `inbox_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `inbox_view` AS select `i`.`id` AS `id`,`a`.`spid` AS `spid`,`i`.`message` AS `message`,`i`.`sender_address` AS `sender_address`,`i`.`date_received` AS `date_received`,`i`.`status` AS `status`,`s`.`ssan` AS `ssan`,`s`.`id` AS `service_id` from ((`inbox` `i` join `services` `s` on((`i`.`service_id` = `s`.`id`))) join `accounts` `a` on((`s`.`account_id` = `a`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `outbox_view`
--

/*!50001 DROP VIEW IF EXISTS `outbox_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `outbox_view` AS select `o`.`id` AS `id`,`a`.`spid` AS `spid`,`o`.`message` AS `message`,`o`.`sender_address` AS `sender_address`,`o`.`sent_at` AS `sent_at`,`o`.`delivery_status` AS `delivery_status`,`o`.`delivery_date` AS `delivery_date`,`o`.`status` AS `status`,`s`.`ssan` AS `ssan`,`o`.`service_id` AS `service_id` from ((`outbox` `o` join `services` `s` on((`o`.`service_id` = `s`.`id`))) join `accounts` `a` on((`s`.`account_id` = `a`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `products_view`
--

/*!50001 DROP VIEW IF EXISTS `products_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `products_view` AS select `p`.`id` AS `id`,`p`.`product_name` AS `product_name`,`p`.`product_type` AS `product_type`,`p`.`product_code` AS `product_code`,`p`.`charge` AS `charge`,`p`.`subscription_keyword` AS `subscription_keyword`,`p`.`unsubscription_keyword` AS `unsubscription_keyword`,`p`.`status` AS `pstatus`,`p`.`welcome_message` AS `welcome_message`,`s`.`service_id` AS `service_id`,`s`.`id` AS `serv_id` from (`products` `p` join `services` `s` on((`p`.`service_id` = `s`.`id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `send_sms_view`
--

/*!50001 DROP VIEW IF EXISTS `send_sms_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `send_sms_view` AS select `o`.`id` AS `id`,`o`.`message` AS `message`,`o`.`sender_address` AS `sender_address`,`o`.`link_id` AS `link_id`,`s`.`service_id` AS `service_id`,`s`.`ssan` AS `ssan`,`a`.`spid` AS `spid`,`a`.`password` AS `password` from ((`outbox` `o` join `services` `s` on((`o`.`service_id` = `s`.`id`))) join `accounts` `a` on((`s`.`account_id` = `a`.`id`))) where ((`o`.`status` = 'STATUS_PENDING') and isnull(`s`.`deleted_at`) and isnull(`a`.`deleted_at`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `send_subscription_view`
--

/*!50001 DROP VIEW IF EXISTS `send_subscription_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `send_subscription_view` AS select `s`.`msisdn` AS `msisdn`,`p`.`service_id` AS `service_id`,`s`.`product_id` AS `product_id` from (`subscriptions` `s` join `products` `p` on((`s`.`product_id` = `p`.`id`))) where (`s`.`update_type` = 1) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `service_view`
--

/*!50001 DROP VIEW IF EXISTS `service_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `service_view` AS select `s`.`id` AS `id`,`s`.`service_id` AS `service_id`,`s`.`ssan` AS `ssan`,`s`.`criteria` AS `criteria`,`s`.`correlator` AS `correlator`,`s`.`status` AS `status`,`s`.`service_type` AS `service_type`,`s`.`account_id` AS `account_id`,`a`.`spid` AS `spid` from (`services` `s` join `accounts` `a` on((`s`.`account_id` = `a`.`id`))) where (isnull(`a`.`deleted_at`) and isnull(`s`.`deleted_at`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `subscription_view`
--

/*!50001 DROP VIEW IF EXISTS `subscription_view`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `subscription_view` AS select `sub`.`id` AS `id`,`sub`.`msisdn` AS `msisdn`,`sub`.`update_type` AS `update_type`,`p`.`product_name` AS `product_name`,`p`.`subscription_keyword` AS `subscription_keyword`,`p`.`charge` AS `charge`,`sub`.`update_time` AS `update_time`,`s`.`service_id` AS `service_id` from ((`subscriptions` `sub` join `products` `p` on((`p`.`id` = `sub`.`product_id`))) join `services` `s` on((`s`.`id` = `p`.`service_id`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-01-18 22:52:30
