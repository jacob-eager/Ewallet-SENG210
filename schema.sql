
CREATE TABLE IF NOT EXISTS `EWallet`.`users` (
  `username` VARCHAR(100) NOT NULL,
  `password` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`username`));

CREATE TABLE IF NOT EXISTS `EWallet`.`expense` (
  `expense_user` VARCHAR(100) NOT NULL,
  `expense_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `source` VARCHAR(200) NOT NULL,
  `amount` DECIMAL(10,2) NOT NULL,
  `yearly_frequency` SMALLINT UNSIGNED NOT NULL,
  PRIMARY KEY (`expense_id`),
  CONSTRAINT `fk_expense_user`
    FOREIGN KEY (`expense_user`)
    REFERENCES `EWallet`.`users` (`username`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

CREATE TABLE IF NOT EXISTS `EWallet`.`wage` (
  `wage_user` VARCHAR(100) NOT NULL,
  `wage_id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `source` VARCHAR(200) NULL,
  `amount` DECIMAL(10,2) NULL,
  `month` ENUM('January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December') NULL,
  PRIMARY KEY (`wage_id`),
  CONSTRAINT `fk_wage_user`
    FOREIGN KEY (`wage_user`)
    REFERENCES `EWallet`.`users` (`username`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);

