-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 15, 2023 at 06:33 PM
-- Server version: 10.4.28-MariaDB
-- PHP Version: 8.2.4

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bd_portaventura`
--

-- --------------------------------------------------------

--
-- Table structure for table `asignacion`
--
-- Error reading structure for table bd_portaventura.asignacion: #1932 - Table &#039;bd_portaventura.asignacion&#039; doesn&#039;t exist in engine
-- Error reading data for table bd_portaventura.asignacion: #1064 - You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near &#039;FROM `bd_portaventura`.`asignacion`&#039; at line 1

--
-- Triggers `asignacion`
--
DELIMITER $$
CREATE TRIGGER `solapar_horarios` BEFORE INSERT ON `asignacion` FOR EACH ROW BEGIN
  DECLARE num_asignaciones INT;
  DECLARE f_inicio_horario DATETIME;
  DECLARE f_fin_horario DATETIME;
  SELECT fecha_inicio, fecha_fin INTO f_inicio_horario, f_fin_horario FROM horario WHERE id = NEW.idHorario;
  SELECT COUNT(*) INTO num_asignaciones FROM asignacion 
    INNER JOIN horario ON idHorario = id
    WHERE dniEmpleado = NEW.dniEmpleado  
      AND ((f_inicio_horario BETWEEN fecha_inicio AND fecha_fin - INTERVAL 1 SECOND) 
           OR (f_fin_horario BETWEEN fecha_inicio + INTERVAL 1 SECOND AND fecha_fin)
           OR (fecha_inicio BETWEEN f_inicio_horario + INTERVAL 1 SECOND AND f_fin_horario - INTERVAL 1 SECOND)
           OR (fecha_fin BETWEEN f_inicio_horario + INTERVAL 1 SECOND AND f_fin_horario - INTERVAL 1 SECOND));

  IF num_asignaciones > 0 THEN
    SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El empleado ya está asignado en otro horario en la misma fecha.';
  END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `atraccion`
--
-- Error reading structure for table bd_portaventura.atraccion: #1932 - Table &#039;bd_portaventura.atraccion&#039; doesn&#039;t exist in engine
-- Error reading data for table bd_portaventura.atraccion: #1064 - You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near &#039;FROM `bd_portaventura`.`atraccion`&#039; at line 1

-- --------------------------------------------------------

--
-- Table structure for table `empleado`
--
-- Error reading structure for table bd_portaventura.empleado: #1932 - Table &#039;bd_portaventura.empleado&#039; doesn&#039;t exist in engine
-- Error reading data for table bd_portaventura.empleado: #1064 - You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near &#039;FROM `bd_portaventura`.`empleado`&#039; at line 1

-- --------------------------------------------------------

--
-- Table structure for table `horario`
--
-- Error reading structure for table bd_portaventura.horario: #1932 - Table &#039;bd_portaventura.horario&#039; doesn&#039;t exist in engine
-- Error reading data for table bd_portaventura.horario: #1064 - You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near &#039;FROM `bd_portaventura`.`horario`&#039; at line 1

--
-- Triggers `horario`
--
DELIMITER $$
CREATE TRIGGER `repetir_horario` BEFORE INSERT ON `horario` FOR EACH ROW BEGIN
    DECLARE total INT;
    SELECT COUNT(*) INTO total
    FROM horario
    WHERE (fecha_inicio = NEW.fecha_inicio AND fecha_fin = NEW.fecha_fin)
    AND (nombreAtr = NEW.nombreAtr OR nombreRes = NEW.nombreRes);
    IF total > 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Horario ya creado';
    END IF;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `restaurante`
--
-- Error reading structure for table bd_portaventura.restaurante: #1932 - Table &#039;bd_portaventura.restaurante&#039; doesn&#039;t exist in engine
-- Error reading data for table bd_portaventura.restaurante: #1064 - You have an error in your SQL syntax; check the manual that corresponds to your MariaDB server version for the right syntax to use near &#039;FROM `bd_portaventura`.`restaurante`&#039; at line 1
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
