-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: mejoratuhp_db
-- ------------------------------------------------------
-- Server version	5.5.5-10.4.32-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `articulos_tienda`
--

DROP TABLE IF EXISTS `articulos_tienda`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `articulos_tienda` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(255) DEFAULT NULL,
  `precio` int(11) NOT NULL,
  `tipo` varchar(50) DEFAULT 'cosmetico',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `articulos_tienda`
--

LOCK TABLES `articulos_tienda` WRITE;
/*!40000 ALTER TABLE `articulos_tienda` DISABLE KEYS */;
INSERT INTO `articulos_tienda` VALUES (1,'Marco de Avatar Dorado','Un borde brillante para tu foto de perfil.',50,'avatar'),(2,'Tema Oscuro','Desbloquea el modo noche para la aplicación.',100,'tema'),(3,'Poción de Sabiduría','Duplica la XP de tu próxima misión.',150,'consumible'),(4,'Corona de Rey','Un accesorio exclusivo para usuarios nivel 10.',500,'avatar'),(5,'Fondo de Escritorio Gamer','Personaliza el fondo de la app.',75,'fondo');
/*!40000 ALTER TABLE `articulos_tienda` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historial_misiones`
--

DROP TABLE IF EXISTS `historial_misiones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `historial_misiones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) DEFAULT NULL,
  `mision_id` int(11) DEFAULT NULL,
  `fecha_completada` timestamp NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `mision_id` (`mision_id`),
  CONSTRAINT `historial_misiones_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `historial_misiones_ibfk_2` FOREIGN KEY (`mision_id`) REFERENCES `misiones` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historial_misiones`
--

LOCK TABLES `historial_misiones` WRITE;
/*!40000 ALTER TABLE `historial_misiones` DISABLE KEYS */;
INSERT INTO `historial_misiones` VALUES (1,2,4,'2025-12-04 03:57:08'),(2,1,4,'2025-12-04 03:59:58'),(16,2,36,'2025-12-04 05:06:38');
/*!40000 ALTER TABLE `historial_misiones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventario_usuario`
--

DROP TABLE IF EXISTS `inventario_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventario_usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `usuario_id` int(11) DEFAULT NULL,
  `articulo_id` int(11) DEFAULT NULL,
  `fecha_compra` datetime DEFAULT current_timestamp(),
  `es_equipado` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`id`),
  KEY `usuario_id` (`usuario_id`),
  KEY `articulo_id` (`articulo_id`),
  CONSTRAINT `inventario_usuario_ibfk_1` FOREIGN KEY (`usuario_id`) REFERENCES `usuarios` (`id`),
  CONSTRAINT `inventario_usuario_ibfk_2` FOREIGN KEY (`articulo_id`) REFERENCES `articulos_tienda` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventario_usuario`
--

LOCK TABLES `inventario_usuario` WRITE;
/*!40000 ALTER TABLE `inventario_usuario` DISABLE KEYS */;
INSERT INTO `inventario_usuario` VALUES (1,2,1,'2025-12-03 22:25:26',0),(2,2,2,'2025-12-03 22:37:41',0),(3,2,4,'2025-12-03 23:07:19',1),(4,2,5,'2025-12-03 23:08:25',0);
/*!40000 ALTER TABLE `inventario_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `misiones`
--

DROP TABLE IF EXISTS `misiones`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `misiones` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `titulo` varchar(150) NOT NULL,
  `descripcion` text DEFAULT NULL,
  `recompensa_xp` int(11) DEFAULT 50,
  `recompensa_oro` int(11) DEFAULT 10,
  `es_personalizada` tinyint(1) DEFAULT 0,
  `usuario_id` int(11) DEFAULT NULL,
  `tipo` varchar(20) DEFAULT 'salud',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `misiones`
--

LOCK TABLES `misiones` WRITE;
/*!40000 ALTER TABLE `misiones` DISABLE KEYS */;
INSERT INTO `misiones` VALUES (1,'Beber 2L de agua','Mantenerse hidratado todo el día',50,10,0,NULL,'salud'),(2,'Leer 10 páginas','Lectura de desarrollo personal',100,20,0,NULL,'salud'),(3,'Ejercicio 30 min','Actividad física moderada',150,30,0,NULL,'salud'),(4,'Hacer la tarea','Responsabilidad académica',50,10,0,NULL,'intelecto'),(5,'Beber un vaso de agua al despertar','Hidratación matutina',20,5,0,NULL,'salud'),(6,'Comer una fruta','Snack saludable',30,5,0,NULL,'salud'),(7,'Dormir 8 horas','Descanso reparador',100,20,0,NULL,'salud'),(8,'Caminar 15 minutos','Actividad ligera',50,10,0,NULL,'fuerza'),(9,'Hacer 10 flexiones','Fuerza básica',60,15,0,NULL,'fuerza'),(10,'Subir por las escaleras','Evita el elevador',40,10,0,NULL,'fuerza'),(11,'Leer un artículo interesante','Cultura general',50,10,0,NULL,'intelecto'),(12,'Estudiar 30 minutos','Enfoque profundo',100,25,0,NULL,'intelecto'),(13,'Aprender 5 palabras en otro idioma','Idiomas',60,15,0,NULL,'intelecto'),(14,'Meditar 5 minutos','Salud mental',40,10,0,NULL,'intelecto'),(15,'No usar el celular 1 hora antes de dormir','Higiene del sueño',80,20,0,NULL,'salud'),(16,'Ordenar el escritorio','Orden y limpieza',50,10,0,NULL,'intelecto'),(17,'Hacer estiramientos','Flexibilidad',30,5,0,NULL,'fuerza'),(18,'Cocinar una comida casera','Nutrición',120,30,0,NULL,'salud'),(19,'Escribir en un diario','Reflexión personal',70,15,0,NULL,'intelecto'),(36,'Ejercicio 30 min','Personalizada',150,30,1,2,'salud'),(37,'Beber 2L de agua','Personalizada',50,10,1,2,'salud');
/*!40000 ALTER TABLE `misiones` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuarios`
--

DROP TABLE IF EXISTS `usuarios`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuarios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `nivel` int(11) DEFAULT 1,
  `experiencia` int(11) DEFAULT 0,
  `monedas` int(11) DEFAULT 0,
  `fecha_registro` datetime DEFAULT current_timestamp(),
  PRIMARY KEY (`id`),
  UNIQUE KEY `correo` (`correo`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuarios`
--

LOCK TABLES `usuarios` WRITE;
/*!40000 ALTER TABLE `usuarios` DISABLE KEYS */;
INSERT INTO `usuarios` VALUES (1,'CRD','crd@gmail.com','12345',1,50,10,'2025-12-03 21:07:36'),(2,'Hola','hola@gmail.com','1111',4,90,75,'2025-12-03 21:19:00');
/*!40000 ALTER TABLE `usuarios` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-12-04  1:20:11
