USE `prescription-db`;

CREATE TABLE IF NOT EXISTS prescriptions(
    id INTEGER NOT NULL AUTO_INCREMENT PRIMARY KEY,
    prescriptionId INTEGER NOT NULL UNIQUE,
    petId INTEGER NOT NULL,
    medication VARCHAR(45) NOT NULL,
    amount VARCHAR(45),
    datePrinted DATE,
    instructions VARCHAR(100)
)engine=InnoDB;