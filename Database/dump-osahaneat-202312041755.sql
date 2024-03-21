drop database IF EXISTS osahaneat;
create database osahaneat;
use osahaneat;

SET time_zone = "+07:00";
--
-- Table structure for table `restaurant`
--

DROP TABLE IF EXISTS `restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `restaurant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `subtitle` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `is_free_ship` tinyint(1) NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `open_date` varchar(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `restaurant`
--

LOCK TABLES `restaurant` WRITE;
/*!40000 ALTER TABLE `restaurant` DISABLE KEYS */;
INSERT INTO `restaurant` VALUES (1,'Burger King','American, Fast Food','It is one of the most iconic and well-recognizable fast food restaurants out there which offers really amazing food and drinks.','burgerking.png',1,'Irving St, San Francisco, California','18:30 PM'),(2,'Pizza Hut','American, Fast Food','It is one of the most iconic and well-recognizable fast food restaurants out there which offers really amazing food and drinks.','pizzahut.png',1,'Irving St, San Francisco, California','20:30 PM'),(3, 'Pho 24', 'Vietnamese Cuisine', 'Pho 24 is a popular chain of restaurants specializing in Vietnamese pho and other local dishes.', 'pho24.jpg', 1, '123 Nguyen Van A Street, District 1, Ho Chi Minh City', '01:00 PM'),(4, 'The Deck Saigon', 'Riverside Dining', 'The Deck Saigon offers a unique dining experience with a picturesque view of the Saigon River and a diverse menu.', 'the-deck-saigon.jpg', 0, '38 Nguyen U Di, Thao Dien, District 2, Ho Chi Minh City', '02:30 PM'),(5, 'Namo Italian Restaurant', 'Italian Delights', 'Namo Italian Restaurant serves authentic Italian cuisine with a menu featuring a variety of pasta, pizza, and more.', 'namo-italian.jpg', 1, '74/6 Hai Ba Trung Street, District 1, Ho Chi Minh City', '05:00 AM');
/*!40000 ALTER TABLE `restaurant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (1,'ROLE_ADMIN'),(2,'ROLE_USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `pass` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `fullname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `role_id` int NOT NULL,
  `phone` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_user_role_id` (`role_id`),
  CONSTRAINT `fk_user_role_id` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'nguyenvana@gmail.com','$2y$10$DbYLnzX.OqiSnJAgL9bFX.YVvta2lQtiwcTGH/.K4rMzqYmXdu0fa','Nguyen Van A','2023-11-30 03:15:31',1,'0123456789','Nhà Bè'),(2,'nguyenvanb@gmail.com','$2y$10$DbYLnzX.OqiSnJAgL9bFX.YVvta2lQtiwcTGH/.K4rMzqYmXdu0fa','Nguyen Van B','2023-11-30 03:15:31',2,'012345679',NULL),(5,'nguyenvanc@gmail.com','$2y$10$DbYLnzX.OqiSnJAgL9bFX.YVvta2lQtiwcTGH/.K4rMzqYmXdu0fa','Nguyen Van C','2023-11-30 03:15:31',2,'012345679',NULL),(6,'vanthong@example.com', '$2a$10$xyz', 'Van Thong', '2023-12-04 10:30:00', 2, '0123456789', 'Quang Ngai'),(7, 'janesmith@example.com', '$2a$10$abc', 'Jane Smith', '2023-12-05 11:45:00', 1, '0123456789', 'USA'),(8, 'alexjones@example.com', '$2a$10$mno', 'Alex Jones', '2023-12-06 14:00:00', 2, '0123456789', 'Spain'),(11,'huybadao323@gmail.com','$2a$10$lhtphHSXwJr1CNLFiGDISOlTt/ZghcIT4QDFFbaDaNDutJZ0jE4mG','Huong Quang Huy','2023-12-03 10:13:33',2,'0123456789',NULL),(12,'dragonelga90@gmail.com','$2a$10$IDF26Kxyg2Q4MCYqvyqE7u1/U.BaSpltB4lz6/4lwr5ipxip0zqei','Gia Huy','2023-12-03 05:40:14',2,'0000000000','TP HCM');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
--
-- Table structure for table `category`
--

DROP TABLE IF EXISTS `category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `category` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name_cate` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `category`
--

LOCK TABLES `category` WRITE;
/*!40000 ALTER TABLE `category` DISABLE KEYS */;
INSERT INTO `category` VALUES (1,'Asian Food'),(2,'Burger'),(3,'Pizza'),(4,'Seafood'),(5,'Vegetarian'),(6,'Desserts');
/*!40000 ALTER TABLE `category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `food`
--

DROP TABLE IF EXISTS `food`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `food` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `image` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `price` decimal(10,0) NOT NULL,
  `cate_id` int NOT NULL,
  `is_free_ship` tinyint(1) NOT NULL DEFAULT '0',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `res_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `food_FK` (`res_id`),
  KEY `fk_food_cate_id` (`cate_id`),
  CONSTRAINT `fk_food_cate_id` FOREIGN KEY (`cate_id`) REFERENCES `category` (`id`),
  CONSTRAINT `food_FK` FOREIGN KEY (`res_id`) REFERENCES `restaurant` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `food`
--

LOCK TABLES `food` WRITE;
/*!40000 ALTER TABLE `food` DISABLE KEYS */;
INSERT INTO `food` VALUES (1,'Special Burger','food2.jpg',55000,1,1,'Served with basmati rice or bulgur pilaf, skewered with grilled vegetables',1),(2,'Spicy Na Thai Pizza','food1.jpg',150000,3,1,'Served with basmati rice or bulgur pilaf, skewered with grilled vegetables',1),(3,'Tandoori','food3.jpg',50000,3,1,'Served with basmati rice or bulgur pilaf, skewered with grilled vegetables',2),(4,'Special Thali','food4.jpg',250000,3,1,'Served with basmati rice or bulgur pilaf, skewered with grilled vegetables',2),(5,'Sandwich','food6.jpg',60000,1,1,'Served with basmati rice or bulgur pilaf, skewered with grilled vegetables',2),(6,'Vegetarian Delight', 'food5.jpg', 75000, 5, 0, 'A delightful vegetarian option with a mix of fresh veggies and herbs.', 5),(7,'Mango Sticky Rice Dessert', 'food7.jpg', 45000, 6, 1, 'A sweet and delicious Thai dessert made with sticky rice and fresh mango.', 4),(8,'Paneer Tikka', 'food8.jpg', 80000, 6, 1, 'Paneer cubes marinated and grilled to perfection, served with mint chutney.', 4),(9,'Grilled Shrimp', 'food9.jpg', 120000, 4, 1, 'Freshly grilled shrimp served with garlic butter sauce.', 3),(10, 'Pho Ga', 'food10.jpg', 60000, 1, 1, 'Delicious chicken pho with aromatic broth and fresh herbs.', 3);
/*!40000 ALTER TABLE `food` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menu_restaurant`
--

DROP TABLE IF EXISTS `menu_restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menu_restaurant` (
  `cate_id` int NOT NULL,
  `res_id` int NOT NULL,
  PRIMARY KEY (`cate_id`,`res_id`),
  KEY `fk_menu_restaurant_res_id` (`res_id`),
  CONSTRAINT `fk_menu_restaurant_cate_id` FOREIGN KEY (`cate_id`) REFERENCES `category` (`id`),
  CONSTRAINT `fk_menu_restaurant_res_id` FOREIGN KEY (`res_id`) REFERENCES `restaurant` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menu_restaurant`
--

LOCK TABLES `menu_restaurant` WRITE;
/*!40000 ALTER TABLE `menu_restaurant` DISABLE KEYS */;
INSERT INTO `menu_restaurant` VALUES (1,1),(3,1),(1,2),(4,3),(6,4),(5,5);
/*!40000 ALTER TABLE `menu_restaurant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `res_id` int NOT NULL,
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_order_user_id` (`user_id`),
  KEY `orders_FK` (`res_id`),
  CONSTRAINT `fk_order_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `orders_FK` FOREIGN KEY (`res_id`) REFERENCES `restaurant` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (1,1,'2023-12-02 13:38:28',1,'TPHCM'),(2,1,'2023-12-02 14:33:29',2,'TPHCM'),(3,2,'2023-12-03 06:33:41',1,'TPHCM'),(4,2,'2023-12-03 06:43:14',1,'TPHCM');
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_item`
--

DROP TABLE IF EXISTS `order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_item` (
  `order_id` int NOT NULL,
  `food_id` int NOT NULL,
  `price` decimal(10,0) NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`order_id`,`food_id`),
  KEY `fk_order_item_food_id` (`food_id`),
  CONSTRAINT `fk_order_item_food_id` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`),
  CONSTRAINT `fk_order_item_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_item`
--

LOCK TABLES `order_item` WRITE;
/*!40000 ALTER TABLE `order_item` DISABLE KEYS */;
INSERT INTO `order_item` VALUES (1,1,50000,1),(1,2,150000,1),(2,3,50000,1),(2,4,50000,1),(2,5,50000,1),(3,7,150000,3),(3,8,150000,2),(4,3,100000,1),(4,5,80000,2);
/*!40000 ALTER TABLE `order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `password_reset_token`
--

DROP TABLE IF EXISTS `password_reset_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `password_reset_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `expiry_date_time` datetime NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `PasswordResetToken_FK` (`user_id`),
  CONSTRAINT `PasswordResetToken_FK` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `password_reset_token`
--

LOCK TABLES `password_reset_token` WRITE;
/*!40000 ALTER TABLE `password_reset_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `password_reset_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rating_restaurant`
--

DROP TABLE IF EXISTS `rating_restaurant`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rating_restaurant` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `res_id` int NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `rate_point` int NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_rating_restaurant_user_id` (`user_id`),
  KEY `fk_rating_restaurant_res_id` (`res_id`),
  CONSTRAINT `fk_rating_restaurant_res_id` FOREIGN KEY (`res_id`) REFERENCES `restaurant` (`id`),
  CONSTRAINT `fk_rating_restaurant_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rating_restaurant`
--

LOCK TABLES `rating_restaurant` WRITE;
/*!40000 ALTER TABLE `rating_restaurant` DISABLE KEYS */;
INSERT INTO `rating_restaurant` VALUES (1,1,1,'Good',4),(2,1,1,'Not bad',5),(3,1,2,'Delicious',5),(4,1,1,'Burger king so good',5),(5,12,1,'Not Gooo',4),(6,5,2,'Good',5),(7,6,2,'Very Good',5);
/*!40000 ALTER TABLE `rating_restaurant` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cart`
--

DROP TABLE IF EXISTS `cart`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `food_id` int NOT NULL,
  `quantity` int NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  KEY `fk_cart_user_id` (`user_id`),
  KEY `fk_cart_food_id` (`food_id`),
  CONSTRAINT `fk_cart_food_id` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`),
  CONSTRAINT `fk_cart_user_id` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart`
--

LOCK TABLES `cart` WRITE;
/*!40000 ALTER TABLE `cart` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart` ENABLE KEYS */;
UNLOCK TABLES;

SET GLOBAL event_scheduler = ON;
CREATE EVENT delete_expired_tokens
ON SCHEDULE EVERY 1 MINUTE
DO DELETE FROM password_reset_token WHERE expiry_date_time < NOW();