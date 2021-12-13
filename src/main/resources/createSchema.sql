DROP DATABASE IF EXISTS StockHolmes_dev;

CREATE DATABASE IF NOT EXISTS StockHolmes_dev;

CREATE TABLE `StockHolmes_dev`.`exchanges` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(10) NOT NULL,
  `description` VARCHAR(80) NULL,
  `country` VARCHAR(45) NOT NULL,
  `currency` VARCHAR(10) NOT NULL,
  `start_hour` TIME NOT NULL,
  `end_hour` TIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE);