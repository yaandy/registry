-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema register
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `register` ;

-- -----------------------------------------------------
-- Schema register
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `register` DEFAULT CHARACTER SET utf8 ;
USE `register` ;

-- -----------------------------------------------------
-- Table `register`.`Roles`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `register`.`Roles` ;

CREATE TABLE IF NOT EXISTS `register`.`Roles` (
  `roleId` INT NOT NULL,
  `roleName` VARCHAR(45) NULL,
  PRIMARY KEY (`roleId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `register`.`Users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `register`.`Users` ;

CREATE TABLE IF NOT EXISTS `register`.`Users` (
  `userId` INT NOT NULL,
  `firstName` VARCHAR(45) NOT NULL,
  `lastName` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `phone` VARCHAR(10) NOT NULL,
  `roleId` INT NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`userId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `register`.`Customer`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `register`.`Customer` ;

CREATE TABLE IF NOT EXISTS `register`.`Customer` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `email` VARCHAR(20) NULL,
  `phone` VARCHAR(10) NULL,
  `region` VARCHAR(10) NULL,
  `adress` VARCHAR(45) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `register`.`Register`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `register`.`Register` ;

CREATE TABLE IF NOT EXISTS `register`.`Register` (
  `id` INT NOT NULL,
  `customerId` INT NULL,
  `createdBy` INT NULL,
  `orderType` VARCHAR(45) NULL,
  `orderStatus` VARCHAR(45) NULL,
  `isFinished` TINYINT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Data for table `register`.`Roles`
-- -----------------------------------------------------
START TRANSACTION;
USE `register`;
INSERT INTO `register`.`Roles` (`roleId`, `roleName`) VALUES (0, 'admin');
INSERT INTO `register`.`Roles` (`roleId`, `roleName`) VALUES (1, 'operator');

COMMIT;


-- -----------------------------------------------------
-- Data for table `register`.`Users`
-- -----------------------------------------------------
START TRANSACTION;
USE `register`;
INSERT INTO `register`.`Users` (`userId`, `firstName`, `lastName`, `email`, `phone`, `roleId`, `password`) VALUES (1, 'Хуйналанін', 'Володимир', 'huynalanin@gmail.com', '0981791789', 1, '0000');
INSERT INTO `register`.`Users` (`userId`, `firstName`, `lastName`, `email`, `phone`, `roleId`, `password`) VALUES (2, 'Порошенко', 'Петро', 'pporoh@ap.com', '0966666666', 0, '1111');

COMMIT;


-- -----------------------------------------------------
-- Data for table `register`.`Customer`
-- -----------------------------------------------------
START TRANSACTION;
USE `register`;
INSERT INTO `register`.`Customer` (`id`, `name`, `email`, `phone`, `region`, `adress`) VALUES (1, 'Гройсман', 'gr@mail.com', '0987777777', 'Lviv', 'qwerty');
INSERT INTO `register`.`Customer` (`id`, `name`, `email`, `phone`, `region`, `adress`) VALUES (2, 'Тимошенко', 'timo@asd.com', '0981234565', 'Kyiv', 'zxcvbn');

COMMIT;


-- -----------------------------------------------------
-- Data for table `register`.`Register`
-- -----------------------------------------------------
START TRANSACTION;
USE `register`;
INSERT INTO `register`.`Register` (`id`, `customerId`, `createdBy`, `orderType`, `orderStatus`, `isFinished`) VALUES (1, 0, 1, 'план', 'прийнято', NULL);
INSERT INTO `register`.`Register` (`id`, `customerId`, `createdBy`, `orderType`, `orderStatus`, `isFinished`) VALUES (2, 1, 1, 'інше', 'прийнято', NULL);

COMMIT;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
