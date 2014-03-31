CREATE DATABASE  IF NOT EXISTS `vasmaster` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `vasmaster`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: vasmaster
-- ------------------------------------------------------
-- Server version	5.5.32

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
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `spid` int(11) NOT NULL,
  `password` varchar(200) NOT NULL,
  `date_created` datetime NOT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (1,601399,'vasmaster','2014-03-17 00:00:00',0),(2,601288,'Martin2006','2014-03-04 00:00:00',1),(3,601245,'123456','2014-03-12 00:00:00',0),(4,602345,'1234','2014-03-13 00:00:00',0),(5,601234,'Helloworld','2014-03-05 00:00:00',0),(6,601235,'kiloo','2014-03-12 00:00:00',0);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `auto_messages`
--

DROP TABLE IF EXISTS `auto_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `auto_messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `option` varchar(45) NOT NULL,
  `response` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `auto_messages`
--

LOCK TABLES `auto_messages` WRITE;
/*!40000 ALTER TABLE `auto_messages` DISABLE KEYS */;
INSERT INTO `auto_messages` VALUES (1,'service_response_addition','Thank you for registering for this service'),(2,'service_response_deletion','You have been unsubscribed successfully'),(3,'service_response_modification','Yor subscriptionhas been modified');
/*!40000 ALTER TABLE `auto_messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inbox`
--

DROP TABLE IF EXISTS `inbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inbox` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountid` int(11) NOT NULL,
  `serviceid` int(11) NOT NULL,
  `sprevid` varchar(45) DEFAULT NULL,
  `message` text NOT NULL,
  `sender_address` varchar(45) NOT NULL,
  `linkid` varchar(45) DEFAULT NULL,
  `date_received` datetime DEFAULT NULL,
  `sms_service_activation_number` varchar(45) DEFAULT NULL,
  `traceuniqueid` varchar(45) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `inbox_accountid_idx` (`accountid`),
  KEY `inbox_serviceid_idx` (`serviceid`),
  CONSTRAINT `inbox_accountid` FOREIGN KEY (`accountid`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `inbox_serviceid` FOREIGN KEY (`serviceid`) REFERENCES `services` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inbox`
--

LOCK TABLES `inbox` WRITE;
/*!40000 ALTER TABLE `inbox` DISABLE KEYS */;
INSERT INTO `inbox` VALUES (24,1,2,'ekubai','This is a test message','254722123456','07161722430758000009','2012-07-02 19:00:00','1234','404090102591207161422430892004',0),(25,1,2,'ekubai','This is a test message','254722123456','07161722430758000009','2012-07-02 19:00:00','1234','404090102591207161422430892004',0),(26,1,2,'ekubai','This is a test message','254722123456','07161722430758000009','2012-07-02 19:00:00','1234','404090102591207161422430892004',0),(27,1,2,'ekubai','This is a test message','254722123456','07161722430758000009','2012-07-02 19:00:00','1234','404090102591207161422430892004',0),(28,1,2,'ekubai','This is a test message','254722123456','07161722430758000009','2012-07-02 19:00:00','1234','404090102591207161422430892004',0),(29,1,2,'ekubai','This is a test message','254722123456','07161722430758000009','2012-07-02 19:00:00','1234','404090102591207161422430892004',0),(30,1,2,'ekubai','This is a test message','254722123456','07161722430758000009','2012-07-02 19:00:00','1234','404090102591207161422430892004',0),(31,1,2,'ekubai','new message','254722123456','07161722430758000009','2012-07-02 19:00:00','1234','404090102591207161422430892004',0),(32,1,2,'ekubai','This is a test message','254722123456','07161722430758000009','2012-07-02 19:00:00','1234','404090102591207161422430892004',0),(33,1,2,'ekubai','This is a test message','254722123456','07161722430758000009','2012-07-02 19:00:00','1234','404090102591207161422430892004',0),(34,1,2,'ekubai','This is a test message','254722123456','07161722430758000009','2012-07-02 19:00:00','1234','404090102591207161422430892004',0);
/*!40000 ALTER TABLE `inbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `outbox`
--

DROP TABLE IF EXISTS `outbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `outbox` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountid` int(11) DEFAULT NULL,
  `message` text,
  `sender_address` varchar(45) DEFAULT NULL,
  `oa` varchar(45) DEFAULT NULL,
  `fa` varchar(45) DEFAULT NULL,
  `linkid` varchar(100) DEFAULT NULL,
  `serviceid` int(11) DEFAULT NULL,
  `sent_date` datetime DEFAULT NULL,
  `correlator` varchar(100) DEFAULT NULL,
  `request_identifier` varchar(45) DEFAULT NULL,
  `delivery_status` varchar(45) DEFAULT NULL,
  `delivery_date` datetime DEFAULT NULL,
  `subreqid` varchar(45) DEFAULT NULL,
  `traceuniqueid` varchar(45) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `outbox_accountid_idx` (`accountid`),
  KEY `outbox_serviceid_idx` (`serviceid`),
  CONSTRAINT `outbox_accountid` FOREIGN KEY (`accountid`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `outbox_serviceid` FOREIGN KEY (`serviceid`) REFERENCES `services` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `outbox`
--

LOCK TABLES `outbox` WRITE;
/*!40000 ALTER TABLE `outbox` DISABLE KEYS */;
INSERT INTO `outbox` VALUES (2,1,'Message','254726387742','254726387742','254726387742','010101010187',2,'2014-03-20 00:00:00','12344',NULL,'DeliveredToTerminal','2014-03-16 23:34:50','11111111111111','504021503311009040428550001002',0),(3,1,'Test message','254726387742','254726387742','254726387742','93939393',2,'2014-03-23 00:00:00','12345',NULL,'DeliveredToTerminal','2014-03-16 23:34:50','11111111111111','504021503311009040428550001002',0);
/*!40000 ALTER TABLE `outbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `services`
--

DROP TABLE IF EXISTS `services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `services` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountid` int(11) NOT NULL,
  `serviceid` varchar(45) NOT NULL,
  `endpoint` varchar(100) NOT NULL,
  `notify_interface_name` varchar(100) NOT NULL,
  `delivery_interface_name` varchar(100) NOT NULL,
  `sms_service_activation_number` varchar(100) NOT NULL,
  `criteria` varchar(50) DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `start_stop_endpoint` varchar(100) DEFAULT NULL,
  `local_endpoint` varchar(100) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0',
  `date_activated` datetime DEFAULT NULL,
  `date_deactivated` datetime DEFAULT NULL,
  `correlator` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `account_id_idx` (`accountid`),
  CONSTRAINT `account_id` FOREIGN KEY (`accountid`) REFERENCES `account` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `services`
--

LOCK TABLES `services` WRITE;
/*!40000 ALTER TABLE `services` DISABLE KEYS */;
INSERT INTO `services` VALUES (2,1,'6013992000001491','http://localhost:8080/sdpgateway/services','notifyreception','deliveryReception','1234',NULL,'2014-03-24 00:00:00',NULL,'http://localhost:8080/sdpgateway/delivery',0,NULL,NULL,NULL),(3,1,'6013992000001442','http://localhost:8080/sdpgateway/services','notifyreception','deliveryReceiption','1234',NULL,NULL,NULL,'http://localhost:8080/sdpgateway/delivery',0,NULL,NULL,NULL);
/*!40000 ALTER TABLE `services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subscription_data`
--

DROP TABLE IF EXISTS `subscription_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `subscription_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(20) NOT NULL,
  `user_type` int(11) DEFAULT NULL,
  `product_code` varchar(100) DEFAULT NULL,
  `serviceid` int(11) DEFAULT NULL,
  `accountid` int(11) DEFAULT NULL,
  `service_list` varchar(50) DEFAULT NULL,
  `update_type` int(11) DEFAULT NULL,
  `update_time` varchar(20) DEFAULT NULL,
  `update_desc` varchar(20) DEFAULT NULL,
  `effective_time` datetime DEFAULT NULL,
  `expiry_time` varchar(45) DEFAULT NULL,
  `unsubscription_date` datetime DEFAULT NULL,
  `status` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `subscription_date_service_id_idx` (`serviceid`),
  KEY `subscription_data_accountiid_idx` (`accountid`),
  CONSTRAINT `subscription_data_accountiid` FOREIGN KEY (`accountid`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `subscription_date_service_id` FOREIGN KEY (`serviceid`) REFERENCES `services` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription_data`
--

LOCK TABLES `subscription_data` WRITE;
/*!40000 ALTER TABLE `subscription_data` DISABLE KEYS */;
INSERT INTO `subscription_data` VALUES (2,'254721214848',0,'MDSP2000052892',3,1,'601399200000144',1,'2012-06-21 23:21:35','Addition','2012-06-22 00:13:11','2012-08-22 00:13:11',NULL,0);
/*!40000 ALTER TABLE `subscription_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary table structure for view `view_inbox`
--

DROP TABLE IF EXISTS `view_inbox`;
/*!50001 DROP VIEW IF EXISTS `view_inbox`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `view_inbox` (
  `id` tinyint NOT NULL,
  `spid` tinyint NOT NULL,
  `serviceid` tinyint NOT NULL,
  `message` tinyint NOT NULL,
  `sender_address` tinyint NOT NULL,
  `linkid` tinyint NOT NULL,
  `date_received` tinyint NOT NULL,
  `sms_service_activation_number` tinyint NOT NULL,
  `traceuniqueid` tinyint NOT NULL,
  `status` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `view_outbox`
--

DROP TABLE IF EXISTS `view_outbox`;
/*!50001 DROP VIEW IF EXISTS `view_outbox`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `view_outbox` (
  `id` tinyint NOT NULL,
  `spid` tinyint NOT NULL,
  `password` tinyint NOT NULL,
  `message` tinyint NOT NULL,
  `sender_address` tinyint NOT NULL,
  `oa` tinyint NOT NULL,
  `fa` tinyint NOT NULL,
  `linkid` tinyint NOT NULL,
  `serviceid` tinyint NOT NULL,
  `sms_service_activation_number` tinyint NOT NULL,
  `endpoint` tinyint NOT NULL,
  `local_endpoint` tinyint NOT NULL,
  `delivery_interface_name` tinyint NOT NULL,
  `notify_interface_name` tinyint NOT NULL,
  `sent_date` tinyint NOT NULL,
  `correlator` tinyint NOT NULL,
  `status` tinyint NOT NULL,
  `request_identifier` tinyint NOT NULL,
  `delivery_status` tinyint NOT NULL,
  `delivery_date` tinyint NOT NULL,
  `subreqid` tinyint NOT NULL,
  `traceuniqueid` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `view_services`
--

DROP TABLE IF EXISTS `view_services`;
/*!50001 DROP VIEW IF EXISTS `view_services`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `view_services` (
  `id` tinyint NOT NULL,
  `spid` tinyint NOT NULL,
  `password` tinyint NOT NULL,
  `serviceid` tinyint NOT NULL,
  `endpoint` tinyint NOT NULL,
  `notify_interface_name` tinyint NOT NULL,
  `delivery_interface_name` tinyint NOT NULL,
  `local_endpoint` tinyint NOT NULL,
  `start_stop_endpoint` tinyint NOT NULL,
  `sms_service_activation_number` tinyint NOT NULL,
  `criteria` tinyint NOT NULL,
  `date_created` tinyint NOT NULL,
  `date_activated` tinyint NOT NULL,
  `date_deactivated` tinyint NOT NULL,
  `status` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Temporary table structure for view `view_subscription_data`
--

DROP TABLE IF EXISTS `view_subscription_data`;
/*!50001 DROP VIEW IF EXISTS `view_subscription_data`*/;
SET @saved_cs_client     = @@character_set_client;
SET character_set_client = utf8;
/*!50001 CREATE TABLE `view_subscription_data` (
  `id` tinyint NOT NULL,
  `msisdn` tinyint NOT NULL,
  `user_type` tinyint NOT NULL,
  `product_code` tinyint NOT NULL,
  `serviceid` tinyint NOT NULL,
  `spid` tinyint NOT NULL,
  `service_list` tinyint NOT NULL,
  `update_type` tinyint NOT NULL,
  `update_time` tinyint NOT NULL,
  `update_desc` tinyint NOT NULL,
  `effective_time` tinyint NOT NULL,
  `expiry_time` tinyint NOT NULL,
  `unsubscription_date` tinyint NOT NULL,
  `status` tinyint NOT NULL,
  `endpoint` tinyint NOT NULL,
  `notify_interface_name` tinyint NOT NULL,
  `delivery_interface_name` tinyint NOT NULL,
  `sms_service_activation_number` tinyint NOT NULL,
  `criteria` tinyint NOT NULL,
  `date_created` tinyint NOT NULL
) ENGINE=MyISAM */;
SET character_set_client = @saved_cs_client;

--
-- Final view structure for view `view_inbox`
--

/*!50001 DROP TABLE IF EXISTS `view_inbox`*/;
/*!50001 DROP VIEW IF EXISTS `view_inbox`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_inbox` AS select `i`.`id` AS `id`,`a`.`spid` AS `spid`,`s`.`serviceid` AS `serviceid`,`i`.`message` AS `message`,`i`.`sender_address` AS `sender_address`,`i`.`linkid` AS `linkid`,`i`.`date_received` AS `date_received`,`i`.`sms_service_activation_number` AS `sms_service_activation_number`,`i`.`traceuniqueid` AS `traceuniqueid`,`i`.`status` AS `status` from ((`inbox` `i` join `account` `a`) join `services` `s`) where ((`i`.`accountid` = `a`.`id`) and (`i`.`serviceid` = `s`.`id`)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_outbox`
--

/*!50001 DROP TABLE IF EXISTS `view_outbox`*/;
/*!50001 DROP VIEW IF EXISTS `view_outbox`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_outbox` AS select `o`.`id` AS `id`,`a`.`spid` AS `spid`,`a`.`password` AS `password`,`o`.`message` AS `message`,`o`.`sender_address` AS `sender_address`,`o`.`oa` AS `oa`,`o`.`fa` AS `fa`,`o`.`linkid` AS `linkid`,`s`.`serviceid` AS `serviceid`,`s`.`sms_service_activation_number` AS `sms_service_activation_number`,`s`.`endpoint` AS `endpoint`,`s`.`local_endpoint` AS `local_endpoint`,`s`.`delivery_interface_name` AS `delivery_interface_name`,`s`.`notify_interface_name` AS `notify_interface_name`,`o`.`sent_date` AS `sent_date`,`o`.`correlator` AS `correlator`,`o`.`status` AS `status`,`o`.`request_identifier` AS `request_identifier`,`o`.`delivery_status` AS `delivery_status`,`o`.`delivery_date` AS `delivery_date`,`o`.`subreqid` AS `subreqid`,`o`.`traceuniqueid` AS `traceuniqueid` from ((`outbox` `o` join `services` `s`) join `account` `a`) where ((`o`.`accountid` = `a`.`id`) and (`o`.`serviceid` = `s`.`id`) and (`s`.`status` = 0) and (`a`.`status` = 0)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_services`
--

/*!50001 DROP TABLE IF EXISTS `view_services`*/;
/*!50001 DROP VIEW IF EXISTS `view_services`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_services` AS select `s`.`id` AS `id`,`a`.`spid` AS `spid`,`a`.`password` AS `password`,`s`.`serviceid` AS `serviceid`,`s`.`endpoint` AS `endpoint`,`s`.`notify_interface_name` AS `notify_interface_name`,`s`.`delivery_interface_name` AS `delivery_interface_name`,`s`.`local_endpoint` AS `local_endpoint`,`s`.`start_stop_endpoint` AS `start_stop_endpoint`,`s`.`sms_service_activation_number` AS `sms_service_activation_number`,`s`.`criteria` AS `criteria`,`s`.`date_created` AS `date_created`,`s`.`date_activated` AS `date_activated`,`s`.`date_deactivated` AS `date_deactivated`,`s`.`status` AS `status` from (`services` `s` join `account` `a`) where ((`s`.`accountid` = `a`.`id`) and (`a`.`status` = 0) and (`s`.`status` = 0)) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `view_subscription_data`
--

/*!50001 DROP TABLE IF EXISTS `view_subscription_data`*/;
/*!50001 DROP VIEW IF EXISTS `view_subscription_data`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8 */;
/*!50001 SET character_set_results     = utf8 */;
/*!50001 SET collation_connection      = utf8_general_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `view_subscription_data` AS select `sd`.`id` AS `id`,`sd`.`msisdn` AS `msisdn`,`sd`.`user_type` AS `user_type`,`sd`.`product_code` AS `product_code`,`s`.`serviceid` AS `serviceid`,`a`.`spid` AS `spid`,`sd`.`service_list` AS `service_list`,`sd`.`update_type` AS `update_type`,`sd`.`update_time` AS `update_time`,`sd`.`update_desc` AS `update_desc`,`sd`.`effective_time` AS `effective_time`,`sd`.`expiry_time` AS `expiry_time`,`sd`.`unsubscription_date` AS `unsubscription_date`,`sd`.`status` AS `status`,`s`.`endpoint` AS `endpoint`,`s`.`notify_interface_name` AS `notify_interface_name`,`s`.`delivery_interface_name` AS `delivery_interface_name`,`s`.`sms_service_activation_number` AS `sms_service_activation_number`,`s`.`criteria` AS `criteria`,`s`.`date_created` AS `date_created` from ((`subscription_data` `sd` join `services` `s`) join `account` `a`) where ((`sd`.`serviceid` = `s`.`id`) and (`sd`.`accountid` = `a`.`id`)) */;
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

-- Dump completed on 2014-03-31 16:42:30
