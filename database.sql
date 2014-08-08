CREATE DATABASE  IF NOT EXISTS `vasmaster` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `vasmaster`;
-- MySQL dump 10.13  Distrib 5.6.13, for Win32 (x86)
--
-- Host: localhost    Database: vasmaster
-- ------------------------------------------------------
-- Server version	5.5.36

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
  `date_edited` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES (7,601033,'iSw824CoL','2014-06-12 00:00:00',0,'2014-06-12 00:00:00');
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `assigned_services`
--

DROP TABLE IF EXISTS `assigned_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `assigned_services` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `userId` int(10) NOT NULL,
  `productid` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `userid_idx` (`userId`),
  KEY `assingnedproductid_idx` (`productid`),
  CONSTRAINT `assingnedproductid` FOREIGN KEY (`productid`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `userid` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `assigned_services`
--

LOCK TABLES `assigned_services` WRITE;
/*!40000 ALTER TABLE `assigned_services` DISABLE KEYS */;
INSERT INTO `assigned_services` VALUES (1,2,2),(2,1,3),(3,1,1),(4,1,2);
/*!40000 ALTER TABLE `assigned_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `attemptedlogin`
--

DROP TABLE IF EXISTS `attemptedlogin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `attemptedlogin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `datetime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `attemptedlogin`
--

LOCK TABLES `attemptedlogin` WRITE;
/*!40000 ALTER TABLE `attemptedlogin` DISABLE KEYS */;
/*!40000 ALTER TABLE `attemptedlogin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bulk`
--

DROP TABLE IF EXISTS `bulk`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bulk` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `message` varchar(1000) NOT NULL,
  `filename` varchar(50) NOT NULL,
  `scheduledDate` datetime NOT NULL,
  `productId` int(11) NOT NULL,
  `addedBy` varchar(50) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `productID_idx` (`productId`),
  CONSTRAINT `productID` FOREIGN KEY (`productId`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bulk`
--

LOCK TABLES `bulk` WRITE;
/*!40000 ALTER TABLE `bulk` DISABLE KEYS */;
INSERT INTO `bulk` VALUES (2,'Web-Application Installation Permissions\r\nThe directory you unpack the application WAR into should not be writable by the Tomcat user (i.e. jira-tomcat in the examples above). Again, the simplest method to do this is to unpack the WAR as root.','DataSyncService-wsdl-soapui-project.xml','2014-07-10 09:07:12',2,'admin',1),(8,'ExtJS Tutorial\r\nWelcome to extjs-tutorial.com. Learn JavaScript application framework ExtJS 4 step by step. It starts from basics to expert level','Jacky.pdf','2014-07-10 10:07:50',1,'admin',1),(9,'Emergency maintenance from 4:30pm today','test.csv','2014-07-18 10:07:41',4,'admin',1),(10,'jjjjjjijijijijii','malawi.csv','2014-07-18 10:07:49',2,'admin',1),(11,'The test message','vasmaster.txt','2014-07-18 10:07:13',2,'admin',1);
/*!40000 ALTER TABLE `bulk` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `content`
--

DROP TABLE IF EXISTS `content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `content` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productCode` varchar(50) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  `dateadded` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `scheduledDate` datetime DEFAULT NULL,
  `addedby` varchar(45) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `content`
--

LOCK TABLES `content` WRITE;
/*!40000 ALTER TABLE `content` DISABLE KEYS */;
INSERT INTO `content` VALUES (1,'MDSP2000052892','The latest musisc in town The latest musisc in town The latest musisc in town The latest musisc in town The latest musisc in town','2014-06-11 21:00:00','2014-06-12 00:00:00','mato',1),(2,'mpo089898','This is test message','2014-06-26 17:27:58','2013-07-28 00:00:00',NULL,1),(3,'mpo089898','Juacali new song','2014-06-26 17:34:39','2013-07-29 00:00:00',NULL,1),(5,'mpo089898','Nonini ni nini','2014-06-26 17:44:18','2013-07-30 00:00:00',NULL,1),(6,'mpo089898','Eko dyda','2014-06-26 17:46:12','2013-04-27 00:00:00',NULL,1),(7,'123445','Cord rally in Eldoret under way','2014-07-27 20:35:04','2013-07-23 00:00:00',NULL,1),(8,'MDSP2000052892','Try to click the Add more info or Add more phone numbers button to enable/disable the additional fields. Also, look at state of the Validate button based on the validity of additional fields.','2014-07-30 08:11:00','2014-06-30 09:06:44',NULL,1),(9,'MDSP2000052892','When you receive a pull request, the first thing to do is review the set of proposed changes. Pull requests are tightly integrated with the underlying git repository, so you can see exactly what commits would be merged should the request be accepted:','2014-07-30 08:13:42','2014-12-05 10:06:36',NULL,0),(10,'mpo089898','Test sms','2014-07-28 20:21:03','2014-12-05 10:06:47',NULL,0),(11,'mpo089898','Test sms 2','2014-06-30 08:22:03','2014-07-27 23:15:45',NULL,1),(14,'MDSP2000052892','This is a test message. ','2014-07-06 12:50:55','2014-07-07 09:07:19',NULL,1);
/*!40000 ALTER TABLE `content` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contenttosend`
--

DROP TABLE IF EXISTS `contenttosend`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `contenttosend` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `serviceid` int(10) DEFAULT NULL,
  `accountid` int(10) DEFAULT NULL,
  `msisdn` varchar(20) DEFAULT NULL,
  `message` varchar(1000) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contenttosend`
--

LOCK TABLES `contenttosend` WRITE;
/*!40000 ALTER TABLE `contenttosend` DISABLE KEYS */;
/*!40000 ALTER TABLE `contenttosend` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `endpoint`
--

DROP TABLE IF EXISTS `endpoint`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `endpoint` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `endpointName` varchar(20) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `interfacename` varchar(100) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `endpoint`
--

LOCK TABLES `endpoint` WRITE;
/*!40000 ALTER TABLE `endpoint` DISABLE KEYS */;
INSERT INTO `endpoint` VALUES (1,'sdp','http://eclipse.org/m2e/download/','',1),(2,'deliverysms','http://eclipse.org/m2e/download/','notifySmsDeliveryReceipt',1),(3,'notification','http://196.201.216.14:8310/SmsNotificationManagerService/services/SmsNotificationManager','',1),(4,'notifysms','http://localhost/send',NULL,NULL);
/*!40000 ALTER TABLE `endpoint` ENABLE KEYS */;
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
  `senderAddress` varchar(45) NOT NULL,
  `linkid` varchar(45) DEFAULT NULL,
  `dateReceived` datetime DEFAULT NULL,
  `traceUniqueid` varchar(45) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0',
  `correlator` varchar(20) DEFAULT NULL,
  `smsType` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `inbox_accountid_idx` (`accountid`),
  KEY `inbox_serviceid_idx` (`serviceid`),
  CONSTRAINT `inbox_accountid` FOREIGN KEY (`accountid`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `inbox_serviceid` FOREIGN KEY (`serviceid`) REFERENCES `services` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inbox`
--

LOCK TABLES `inbox` WRITE;
/*!40000 ALTER TABLE `inbox` DISABLE KEYS */;
/*!40000 ALTER TABLE `inbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `music_category`
--

DROP TABLE IF EXISTS `music_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `music_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `music_category`
--

LOCK TABLES `music_category` WRITE;
/*!40000 ALTER TABLE `music_category` DISABLE KEYS */;
INSERT INTO `music_category` VALUES (1,'Bongo','2014-07-13 00:00:00'),(2,'Genge','2014-07-14 00:00:00'),(3,'RnB','2014-07-14 00:00:00'),(4,'Ragga','2014-07-14 00:00:00');
/*!40000 ALTER TABLE `music_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `music_downloads`
--

DROP TABLE IF EXISTS `music_downloads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `music_downloads` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `msisdn` varchar(20) DEFAULT NULL,
  `songId` int(10) NOT NULL,
  `url` varchar(100) NOT NULL,
  `productCode` int(10) NOT NULL,
  `buyDate` datetime DEFAULT NULL,
  `downloadDate` datetime DEFAULT NULL,
  `status` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `music_downloads`
--

LOCK TABLES `music_downloads` WRITE;
/*!40000 ALTER TABLE `music_downloads` DISABLE KEYS */;
INSERT INTO `music_downloads` VALUES (52,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:06',NULL,1),(53,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:06',NULL,1),(54,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:06',NULL,1),(55,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:06',NULL,1),(56,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:06',NULL,1),(57,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:06',NULL,1),(58,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:06',NULL,1),(59,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:06',NULL,1),(60,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:06',NULL,1),(61,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:06',NULL,1),(62,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:06',NULL,1),(63,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:06',NULL,1),(64,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:07',NULL,1),(65,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:07',NULL,1),(66,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:07',NULL,1),(67,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 21:29:07',NULL,1),(68,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(69,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(70,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(71,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(72,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(73,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(74,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(75,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(76,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(77,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(78,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(79,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(80,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(81,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(82,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(83,'254723292663',2,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663',2,'2014-07-20 23:26:34',NULL,0),(84,'254726387742',4,'localhost:8080/Vasmaster/downloadMusic?songid=4&msisdn=254726387742',3,'2014-07-20 23:38:52',NULL,0);
/*!40000 ALTER TABLE `music_downloads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `musician`
--

DROP TABLE IF EXISTS `musician`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `musician` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) NOT NULL,
  `dateAdded` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `musician`
--

LOCK TABLES `musician` WRITE;
/*!40000 ALTER TABLE `musician` DISABLE KEYS */;
INSERT INTO `musician` VALUES (1,'Nonini','2014-07-01 00:00:00'),(2,'Jua cali','2014-07-19 10:07:00'),(3,'Amani','2014-07-01 00:00:00'),(4,'Madtracks','2014-07-03 00:00:00');
/*!40000 ALTER TABLE `musician` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `outbox`
--

DROP TABLE IF EXISTS `outbox`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `outbox` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `accountid` int(11) NOT NULL,
  `message` text NOT NULL,
  `senderAddress` varchar(45) NOT NULL,
  `oa` varchar(45) DEFAULT NULL,
  `fa` varchar(45) DEFAULT NULL,
  `linkid` varchar(100) DEFAULT NULL,
  `serviceid` int(11) NOT NULL,
  `sentDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `correlator` varchar(100) DEFAULT NULL,
  `requestIdentifier` varchar(45) DEFAULT NULL,
  `deliveryStatus` varchar(45) DEFAULT NULL,
  `deliveryDate` datetime DEFAULT NULL,
  `subreqid` varchar(45) DEFAULT NULL,
  `traceuniqueid` varchar(45) DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0',
  `sms_type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `outbox_accountid_idx` (`accountid`),
  KEY `outbox_serviceid_idx` (`serviceid`),
  CONSTRAINT `outbox_accountid` FOREIGN KEY (`accountid`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `outbox_serviceid` FOREIGN KEY (`serviceid`) REFERENCES `services` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `outbox`
--

LOCK TABLES `outbox` WRITE;
/*!40000 ALTER TABLE `outbox` DISABLE KEYS */;
INSERT INTO `outbox` VALUES (1,7,'','254723292663','254723292663','254723292663',NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(2,7,'','254723292663','254723292663','254723292663',NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(3,7,'','254723292663','254723292663','254723292663',NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(4,7,'','254723292663','254723292663','254723292663',NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(5,7,'nullYour are subscribed to news','254723292663','254723292663','254723292663',NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(6,7,'Your are subscribed to news','254723292663','254723292663','254723292663',NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(7,7,'You are now subscribed to news. To unsubscribe dial *100#','254723292663','254723292663','254723292663',NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(8,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(9,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(10,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(11,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(12,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(13,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(14,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(15,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(16,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(17,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(18,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(19,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(20,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(21,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(22,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(23,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(24,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(25,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(26,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(27,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(28,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(29,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(30,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(31,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(32,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(33,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(34,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(35,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(36,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(37,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(38,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(39,7,'localhost:8080/Vasmaster/downloadMusic?songid=2&msisdn=254723292663','254723292663',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(40,7,'This is test message','254726387742',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(41,7,'Juacali new song','254726387742',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(42,7,'Nonini ni nini','254726387742',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(43,7,'Eko dyda','254726387742',NULL,NULL,NULL,7,'2014-07-27 20:14:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(44,7,'This is test message','254726387742',NULL,NULL,NULL,7,'2014-07-27 20:15:10',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(45,7,'Juacali new song','254726387742',NULL,NULL,NULL,7,'2014-07-27 20:15:10',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(46,7,'Nonini ni nini','254726387742',NULL,NULL,NULL,7,'2014-07-27 20:15:10',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(47,7,'Eko dyda','254726387742',NULL,NULL,NULL,7,'2014-07-27 20:15:10',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(48,7,'Test sms 2','254726387742',NULL,NULL,NULL,7,'2014-07-27 20:15:49',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(49,7,'You are now subscribed to news. To unsubscribe dial *100#','254723292663','254723292663','254723292663',NULL,7,'2014-07-30 12:52:16',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(50,7,'You are now subscribed to news. To unsubscribe dial *100#','254723292663','254723292663','254723292663',NULL,7,'2014-07-30 12:53:37',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(51,7,'You are now subscribed to news. To unsubscribe dial *100#','254723292663','254723292663','254723292663',NULL,7,'2014-07-30 12:54:09',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(52,7,'You are now subscribed to news. To unsubscribe dial *100#','254723292663','254723292663','254723292663',NULL,7,'2014-07-30 20:48:20',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL),(53,7,'You are now subscribed to news. To unsubscribe dial *100#','254723292663','254723292663','254723292663',NULL,7,'2014-07-30 20:49:05',NULL,NULL,NULL,NULL,NULL,NULL,0,NULL);
/*!40000 ALTER TABLE `outbox` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `serviceId` int(10) DEFAULT NULL,
  `productType` varchar(50) DEFAULT NULL,
  `productName` varchar(50) DEFAULT NULL,
  `productCode` varchar(50) DEFAULT NULL,
  `charge` int(10) DEFAULT NULL,
  `keyword` varchar(50) DEFAULT NULL,
  `unsubKeyword` varchar(50) DEFAULT NULL,
  `status` tinyint(1) NOT NULL DEFAULT '0',
  `bulkid` tinyint(1) DEFAULT '0',
  `assigned` tinyint(1) DEFAULT '0',
  `welcome` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,7,'On demand','music','MDSP2000052892',10,'music','stop',0,0,0,NULL),(2,7,'On demand','news','MDSP2000052891',10,'news','stop',0,1,0,'You are now subscribed to news. To unsubscribe dial *100#'),(3,4,'Subscription','Bible verses','mpo089898',10,'bible','stop',0,0,0,NULL),(4,7,'On demand','Kplc','MOOOOOOOO900',10,'kplc','kplc',0,1,0,'You are now subscribed to,');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'admin'),(2,'user'),(3,'manager'),(4,'Super');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `scheduled_music`
--

DROP TABLE IF EXISTS `scheduled_music`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduled_music` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `productCode` int(10) NOT NULL,
  `songid` int(10) DEFAULT NULL,
  `sendDate` datetime NOT NULL,
  `status` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `smProductcode_idx` (`productCode`),
  KEY `songID_idx` (`songid`),
  CONSTRAINT `smProductcode` FOREIGN KEY (`productCode`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `songID` FOREIGN KEY (`songid`) REFERENCES `songs` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `scheduled_music`
--

LOCK TABLES `scheduled_music` WRITE;
/*!40000 ALTER TABLE `scheduled_music` DISABLE KEYS */;
INSERT INTO `scheduled_music` VALUES (1,1,1,'2014-07-20 18:20:20',1),(2,2,2,'2014-07-20 20:20:20',1),(3,1,3,'2014-07-22 03:07:07',0),(4,3,4,'2014-07-17 09:07:57',1),(5,4,5,'2014-07-18 10:07:30',0);
/*!40000 ALTER TABLE `scheduled_music` ENABLE KEYS */;
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
  `smsServiceActivationNumber` varchar(100) NOT NULL,
  `criteria` varchar(50) DEFAULT NULL,
  `dateCreated` datetime DEFAULT NULL,
  `status` tinyint(4) DEFAULT '0',
  `dateActivated` datetime DEFAULT NULL,
  `dateDeactivated` datetime DEFAULT NULL,
  `correlator` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `account_id_idx` (`accountid`),
  CONSTRAINT `account_id` FOREIGN KEY (`accountid`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `services`
--

LOCK TABLES `services` WRITE;
/*!40000 ALTER TABLE `services` DISABLE KEYS */;
INSERT INTO `services` VALUES (7,7,'6010332000002828','1317',NULL,NULL,1,NULL,NULL,NULL);
/*!40000 ALTER TABLE `services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `song_services`
--

DROP TABLE IF EXISTS `song_services`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `song_services` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `songId` int(10) NOT NULL,
  `productCode` int(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `song_serviceId_idx` (`songId`),
  KEY `product_code_services_idx` (`productCode`),
  CONSTRAINT `product_code_services` FOREIGN KEY (`productCode`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `song_serviceId` FOREIGN KEY (`songId`) REFERENCES `songs` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `song_services`
--

LOCK TABLES `song_services` WRITE;
/*!40000 ALTER TABLE `song_services` DISABLE KEYS */;
INSERT INTO `song_services` VALUES (1,1,1),(2,1,2),(3,1,1),(4,3,4),(5,2,3),(6,2,4);
/*!40000 ALTER TABLE `song_services` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `songs`
--

DROP TABLE IF EXISTS `songs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `songs` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `categoryId` int(10) NOT NULL,
  `musicianId` int(10) NOT NULL,
  `songName` varchar(100) NOT NULL,
  `dateAdded` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `songcategory_idx` (`categoryId`),
  KEY `productcode_idx` (`musicianId`),
  CONSTRAINT `productcode` FOREIGN KEY (`musicianId`) REFERENCES `products` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `songcategory` FOREIGN KEY (`categoryId`) REFERENCES `music_category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `songs`
--

LOCK TABLES `songs` WRITE;
/*!40000 ALTER TABLE `songs` DISABLE KEYS */;
INSERT INTO `songs` VALUES (1,1,2,'Judy Boucher  - That Night We Met.mp3','2014-07-02 02:07:57'),(2,1,2,'Judy Boucher  - That Night We Met.mp3','2014-07-02 02:07:57'),(3,2,1,'Judy Boucher  - That Night We Met.mp3','2014-07-20 00:00:00'),(4,1,2,'13 Track 13.mp3','2014-07-10 09:07:54'),(5,2,3,'AtlanticStarr - You Deserve The Best.mp3','2014-07-19 10:07:46');
/*!40000 ALTER TABLE `songs` ENABLE KEYS */;
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
  `content_status` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `subscription_date_service_id_idx` (`serviceid`),
  KEY `subscription_data_accountiid_idx` (`accountid`),
  CONSTRAINT `subscription_data_accountiid` FOREIGN KEY (`accountid`) REFERENCES `account` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `subscription_date_service_id` FOREIGN KEY (`serviceid`) REFERENCES `services` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subscription_data`
--

LOCK TABLES `subscription_data` WRITE;
/*!40000 ALTER TABLE `subscription_data` DISABLE KEYS */;
INSERT INTO `subscription_data` VALUES (31,'254723292663',0,'MDSP2000052891',7,7,'6010332000002828',2,'2012-06-21 23:21:35','Addition','2012-06-22 00:13:11','2012-08-22 00:13:11','2012-06-21 23:21:35',0,1);
/*!40000 ALTER TABLE `subscription_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(200) NOT NULL,
  `dateCreated` datetime NOT NULL,
  `firstName` varchar(50) DEFAULT NULL,
  `secondName` varchar(50) DEFAULT NULL,
  `otherName` varchar(50) DEFAULT NULL,
  `company` varchar(100) DEFAULT NULL,
  `role` int(11) DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '0',
  `message` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `role_idx` (`role`),
  CONSTRAINT `role` FOREIGN KEY (`role`) REFERENCES `roles` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'admin','Admin12345','2014-06-12 00:00:00','Martin','Lugaliki','A','SMK',1,1,NULL),(2,'verah','Kemunto','2014-06-12 00:00:00','Verah','Kemunto','Ntenga','VNK',3,1,NULL),(4,'denge','Kevinn2014','2014-06-12 00:00:00','Kevin','Denge','A','Craft Solution',2,1,NULL),(5,'janet','Kanini','2014-07-10 00:00:00','Janet','Kanini','','Inkane Technologies',2,1,NULL),(6,'grace','Lugaliki','2014-07-10 00:00:00','Grace','Lugaliki','','Grace Inc',1,0,NULL),(7,'rosy','Rosy12345','2014-07-11 00:00:00','Rosy','Rosy','','Kcb',2,0,NULL);
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userservices`
--

DROP TABLE IF EXISTS `userservices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userservices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serviceid` int(10) NOT NULL,
  `dateAssigned` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userservices`
--

LOCK TABLES `userservices` WRITE;
/*!40000 ALTER TABLE `userservices` DISABLE KEYS */;
/*!40000 ALTER TABLE `userservices` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-08-08  6:37:56
