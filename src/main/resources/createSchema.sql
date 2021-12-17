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

  CREATE TABLE `StockHolmes_dev`.`stocks` (
    `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(45) NULL,
    `ticker` VARCHAR(6) NOT NULL,
    `description` VARCHAR(45) NULL,
    `isActive` BOOLEAN NOT NULL,
    `listingDate` DATETIME NULL,
    `delistingDate` DATETIME NULL,
    `exchangeId` INT NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `ticker_UNIQUE` (`ticker` ASC) VISIBLE,
    INDEX `stock_exchangeId_FK_idx` (`exchangeId` ASC) VISIBLE,
    CONSTRAINT `stocks_exchangeId_FK`
      FOREIGN KEY (`exchangeId`)
      REFERENCES `StockHolmes_dev`.`exchanges` (`id`)
      ON DELETE NO ACTION
      ON UPDATE NO ACTION);
