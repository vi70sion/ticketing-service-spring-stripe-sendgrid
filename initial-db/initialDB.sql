CREATE DATABASE  IF NOT EXISTS `ticketing_service` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `ticketing_service`;
-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: localhost    Database: ticketing_service
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `discounts`
--

DROP TABLE IF EXISTS `discounts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `discounts` (
  `name` varchar(20) NOT NULL,
  `discount` int DEFAULT NULL,
  `used` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `discounts`
--

LOCK TABLES `discounts` WRITE;
/*!40000 ALTER TABLE `discounts` DISABLE KEYS */;
INSERT INTO `discounts` VALUES ('ATOSTOGOS20',20,0),('NUOLAIDA15',15,0),('RUDUO10',10,1);
/*!40000 ALTER TABLE `discounts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `email_logs`
--

DROP TABLE IF EXISTS `email_logs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `email_logs` (
  `id` int NOT NULL AUTO_INCREMENT,
  `email_to` varchar(255) NOT NULL,
  `subject` varchar(255) NOT NULL,
  `status` varchar(50) NOT NULL,
  `error_message` text,
  `sent_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `email_logs`
--

LOCK TABLES `email_logs` WRITE;
/*!40000 ALTER TABLE `email_logs` DISABLE KEYS */;
/*!40000 ALTER TABLE `email_logs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event_reviews`
--

DROP TABLE IF EXISTS `event_reviews`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event_reviews` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `event_id` bigint NOT NULL,
  `user_name` varchar(45) NOT NULL,
  `rating` int NOT NULL,
  `comment` text,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `event_reviews_chk_1` CHECK ((`rating` between 1 and 5))
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event_reviews`
--

LOCK TABLES `event_reviews` WRITE;
/*!40000 ALTER TABLE `event_reviews` DISABLE KEYS */;
INSERT INTO `event_reviews` VALUES (1,2,'dgdfg',4,'dfgd','2024-09-09 07:00:00');
/*!40000 ALTER TABLE `event_reviews` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `events`
--

DROP TABLE IF EXISTS `events`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `events` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `description` varchar(1000) DEFAULT NULL,
  `event_time` datetime DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `image_url` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `events`
--

LOCK TABLES `events` WRITE;
/*!40000 ALTER TABLE `events` DISABLE KEYS */;
INSERT INTO `events` VALUES (1,'Lemon Joy | Kaunas','„Lemon Joy“ koncerte skambės kertiniai grupės hitai „Mylėt tave taip beprotiška ir keista“, „Kažkada“, „Pamiršk mane“, „Karolis“, „Be atšvaitų“, „Ylang Ylang“, „Ne apie mus (Koks pašėlęs tempas)“ ir daugelis kitų puikiai žinomų dainų.','2024-09-09 20:00:00',34.00,'https://i.ibb.co/mXwJgbW/lemonjoy.jpg'),(2,'A lyga: FK \'\'Žalgiris\'\' - Gargždų \'\'Banga\'\'','Rudenį „Žalgiris“ pradeda dvikova su Gargždų „Bangos“ komanda. „Topsport A lygos“ rungtynės tarp „Žalgirio“ ir „Bangos“ bus žaidžiamos rugsėjo 1 d. (sekmadienį) nuo 16 val. LFF stadione (Stadiono g. 2 / Liepkalnio g. 13, Vilnius)','2024-09-14 19:00:00',10.00,'https://i.ibb.co/wBmBBZW/zalgirisbanga.jpg'),(3,'Gurtam DevConf','Gurtam DevConf yra debiutinė Gurtam konferencija, skirta IT specialistams, kurie kasdien sprendžia praktines technologijų problemas. Konferencijos tikslas - palengvinti žinių ir patirties dalijimąsi tarp pirmaujančių daiktų interneto, sudėtingų sistemų ir realaus laiko duomenų sprendimų kūrėjų. ','2024-09-10 10:00:00',61.20,'https://i.ibb.co/YjNQjMk/gurtam.jpg');
/*!40000 ALTER TABLE `events` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `newsletters_users`
--

DROP TABLE IF EXISTS `newsletters_users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `newsletters_users` (
  `email` varchar(50) NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `newsletters_users`
--

LOCK TABLES `newsletters_users` WRITE;
/*!40000 ALTER TABLE `newsletters_users` DISABLE KEYS */;
INSERT INTO `newsletters_users` VALUES ('aaa@mail.com'),('antanas@gmail.com'),('evaldas@mail.lt'),('juozas@mail.lt'),('juozas111@mail.lt'),('juozas222@mail.lt'),('juozas2555522@mail.lt'),('juozas33222@mail.lt'),('juozas33444222@mail.lt'),('naujasvardas@mail.com'),('vardas@mail.com');
/*!40000 ALTER TABLE `newsletters_users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `event_id` bigint DEFAULT NULL,
  `user_email` varchar(50) DEFAULT NULL,
  `price` decimal(10,2) DEFAULT NULL,
  `discount_name` varchar(50) DEFAULT NULL,
  `order_total` decimal(10,2) DEFAULT NULL,
  `payment_status` tinyint(1) DEFAULT NULL,
  `charge_id` varchar(45) DEFAULT NULL,
  `uuid` binary(16) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,2,'vardas@mail.com',10.00,'',10.00,0,NULL,NULL),(2,1,'antanas@outlook.com',34.00,'NUOLAIDA15',34.00,0,NULL,_binary '\�A�|SEAħ\">)��\�'),(3,1,'antanas@outlook.com',34.00,'NUOLAIDA15',34.00,0,NULL,_binary 'u҈/�KY��XF\Z�'),(4,1,'antanas@outlook.com',34.00,'NUOLAIDA15',28.90,0,NULL,_binary '<)�\�QHG��\�\n�\":��'),(5,1,'antanas@outlook.com',34.00,'NUOLAIDA15',28.90,0,NULL,_binary 'eYHq�&Et�Px\r7\�n('),(6,1,'vardas2@mail.com',34.00,'NUOLAIDA15',28.90,0,NULL,_binary '�@�B\�M��U�h~�\�\�'),(7,1,'antanas@outlook.com',34.00,'NUOLAIDA15',28.90,0,NULL,_binary '�\��RL۔�\�(|9G'),(8,1,'antanas@outlook.com',34.00,'NUOLAIDA15',28.90,0,NULL,_binary '�T[޶xI��^�O%�A'),(9,2,'saulius@gmail.com',10.00,'NUOLAIDA15',8.50,0,NULL,_binary '򰇹,F����\�`ip	'),(10,2,'saulius@gmail.com',10.00,'ruduo10',9.00,0,NULL,_binary '�f�\�{@Ku��1\�v�ϑ'),(11,2,'saulius@gmail.com',10.00,'',10.00,0,NULL,_binary '�Z\�AG*�u�\r4mg'),(12,1,'antanas@outlook.com',34.00,'NUOLAIDA15',28.90,0,NULL,_binary '\��\�\�r�E����Ɵ6�'),(13,1,'antanas@outlook.com',34.00,'',34.00,0,NULL,_binary ':h�`�Br��ǻ0i<�'),(14,2,'vardas2@mail.com',10.00,'',10.00,1,NULL,_binary 'w�\�\\Y�H˥@5�l�\�'),(15,3,'jurgis@gmail.com',61.20,'RUDUO10',55.08,1,NULL,_binary '7\�O�B��t\�\��?�,'),(20,1,'evaldas.lupeikis@gmail.com',34.00,'',34.00,0,'ch_3PwnD7BsNoGKJEE71QjBFawR',_binary '�\�y�j�@��G�\�A�u'),(21,1,'evaldas.lupeikis@gmail.com',34.00,'',34.00,0,'ch_3PwnH5BsNoGKJEE72y7bJ3nl',_binary '��2.�\�L����l\�T�'),(22,2,'evaldas.lupeikis@gmail.com',10.00,'',10.00,1,'ch_3PwoMzBsNoGKJEE73DDaqmSX',_binary 'C+)%�?G�=c\�o�='),(23,2,'evaldas.lupeikis@gmail.com',10.00,'RUDUO10',9.00,0,'ch_3Px9jZBsNoGKJEE70xCZjtd0',_binary '�/\Z\�EN��\�飾^��');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL,
  `name` text,
  `password` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'vardas1@mail.com','123456'),(2,'vardas2@mail.com','qwerty'),(3,'evaldas.lupeikis@gmail.com','123456');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-09-15 11:11:45
