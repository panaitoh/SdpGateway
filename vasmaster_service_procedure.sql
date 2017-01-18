-- MySQL dump 10.13  Distrib 5.7.16, for Linux (x86_64)
--
-- Host: localhost    Database: vasmaster_service
-- ------------------------------------------------------
-- Server version	5.7.16-0ubuntu0.16.04.1
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping routines for database 'vasmaster_service'
--
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spDeliveryReport`(
IN traceUniqueId VARCHAR(50), 
IN correlator VARCHAR(50), 
IN deliveryStatus VARCHAR(100), 
IN time_stamp VARCHAR(20))
BEGIN
SET @query = CONCAT("UPDATE outbox SET delivery_status='",deliveryStatus,"', delivery_date='",time_stamp,"',trace_unique_id='",traceUniqueId,"' WHERE correlator='",correlator,"'");
    
PREPARE pstmt_delivery FROM @query;
EXECUTE pstmt_delivery ;
DEALLOCATE PREPARE pstmt_delivery;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spOutbox`()
BEGIN
SELECT *  FROM inbox;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spSaveInbox`(
    IN message TEXT, 
    IN mobile VARCHAR(20), 
    IN linkid VARCHAR(50), 
    IN date_time VARCHAR(20), 
    IN traceUniqueId VARCHAR(50),
    IN correlator VARCHAR(20),
IN service VARCHAR(20)
    )
BEGIN
SET @serviceId :=0;
    
SET @servSQL = CONCAT("SELECT id INTO @serviceId FROM services WHERE service_id ='", service, "' limit 1 ");
PREPARE pstmt_service FROM @servSQL;
EXECUTE pstmt_service ;
DEALLOCATE PREPARE pstmt_service;
    
    INSERT INTO inbox(message, sender_address, linkid, date_received,trace_unique_id, correlator,service_id)
    VALUES(message, mobile, linkid, date_time, traceUniqueId, correlator, @serviceId);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spSaveSubscription`(
IN msisdn VARCHAR(20), 
IN userid_type INT,
IN product VARCHAR(20),
IN updateType INT,
IN updateTime VARCHAR(50),
IN updateDesc VARCHAR(50),
IN effectiveTime NVARCHAR(50),
IN expiryTime NVARCHAR(50)
)
BEGIN
SET @productId :=0;
    
SET @prodSQL = CONCAT("SELECT id INTO @productId FROM products WHERE product_code = '", product, "'  limit 1 ");
PREPARE pstmt_product FROM @prodSQL;
EXECUTE pstmt_product ;
DEALLOCATE PREPARE pstmt_product;
    
    INSERT INTO subscriptions(msisdn,user_type,update_type,update_time,update_desc,effective_time,expiry_time,product_id) 
    VALUES(msisdn,userid_type,updateType,updateTime,updateDesc,effectiveTime,expiryTime, @productId);

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `spUpdateSubcription`(
IN msisdn VARCHAR(20),
IN product VARCHAR(50),
IN updateType INT,
IN updateTime VARCHAR(50),
IN updateDesc VARCHAR(50))
BEGIN
SET @productId :=0;
    
SET @prodSQL = CONCAT("SELECT id INTO @productId FROM products WHERE product_code = '", product, "'  limit 1 ");
PREPARE pstmt_product FROM @prodSQL;
EXECUTE pstmt_product ;
DEALLOCATE PREPARE pstmt_product;
    
SET @query = CONCAT("UPDATE subscriptions SET update_type='",updateType,"', 
    update_desc='",updateDesc,"',unsubscription_date='",updateTime,"' WHERE product_id='",@productId,"' AND msisdn ='", msisdn,"'");

PREPARE pstmt_updateSubscription FROM @query;
EXECUTE pstmt_updateSubscription ;
DEALLOCATE PREPARE pstmt_updateSubscription;
    
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-01-18 22:52:04
