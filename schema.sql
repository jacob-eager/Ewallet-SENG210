
CREATE SCHEMA EWallet;

CREATE TABLE EWallet.users (
  username VARCHAR(100) NOT NULL,
  password VARCHAR(200) NOT NULL,
  PRIMARY KEY (username)
);

CREATE TABLE EWallet.expense (
  expense_user VARCHAR(100) NOT NULL,
  expense_id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  source VARCHAR(200) NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  yearly_frequency SMALLINT NOT NULL,
  PRIMARY KEY (expense_id),
  FOREIGN KEY (expense_user)
    REFERENCES EWallet.users(username)
    ON DELETE CASCADE
);

CREATE TABLE EWallet.wage (
  wage_user VARCHAR(100) NOT NULL,
  wage_id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
  source VARCHAR(200),
  amount DECIMAL(10,2),
  month VARCHAR(20),
  PRIMARY KEY (wage_id),
  FOREIGN KEY (wage_user)
    REFERENCES EWallet.users(username)
    ON DELETE CASCADE
);

