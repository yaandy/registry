-- -----------------------------------------------------
-- Data for table `register`.`user`
-- -----------------------------------------------------
START TRANSACTION;
USE `register`;
INSERT INTO `register`.`user` (`id`, `first_name`, `last_name`, `username`, `phone`, `password`, `telegram_id`, `date_created`) VALUES (1, 'Володимир', 'Хуйналанін', 'us1@us.com', '0981791789', '$2a$10$UcMBkIzCgV9wV8n4WMF/EuQxbaYMG3XqnQowuYwpnhMHDj3hsSY5i', NULL, '2019-05-04 00:00:00');
INSERT INTO `register`.`user` (`id`, `first_name`, `last_name`, `username`, `phone`, `password`, `telegram_id`, `date_created`) VALUES (2, 'Петр', 'Поршонко', 'ad@ad.com', '0966666666', '$2a$10$UcMBkIzCgV9wV8n4WMF/EuQxbaYMG3XqnQowuYwpnhMHDj3hsSY5i', NULL, '2019-05-04 00:00:00');
INSERT INTO `register`.`user` (`id`, `first_name`, `last_name`, `username`, `phone`, `password`, `telegram_id`, `date_created`) VALUES (3, 'Ваван', 'Здублян', 'us2@us.com', '0677777985', '$2a$10$UcMBkIzCgV9wV8n4WMF/EuQxbaYMG3XqnQowuYwpnhMHDj3hsSY5i', NULL, '2019-05-04 00:00:00');
INSERT INTO `register`.`user` (`id`, `first_name`, `last_name`, `username`, `phone`, `password`, `telegram_id`, `date_created`) VALUES (4, 'Іван', 'Геодезист', 'geo@geo.com', '0932233226', '$2a$10$UcMBkIzCgV9wV8n4WMF/EuQxbaYMG3XqnQowuYwpnhMHDj3hsSY5i', NULL, '2019-05-04 00:00:00');
INSERT INTO `register`.`user` (`id`, `first_name`, `last_name`, `username`, `phone`, `password`, `telegram_id`, `date_created`) VALUES (5, 'Юра', 'Гостинський', 'jurag777@ukr.net ', '0673134311', '$2a$10$ePNCH.6Z0Uze7g0SaCfhhuiD6/Z8l8hvpl9r49sGMcUgJzvXXvuBS', NULL, '2019-05-04 00:00:00');
INSERT INTO `register`.`user` (`id`, `first_name`, `last_name`, `username`, `phone`, `password`, `telegram_id`, `date_created`) VALUES (6, 'Саша', 'Занчук', 'oleksandrzanchuk9@ukr.net', '0672907093', '$2a$10$2h..DUeZ8ZSASCJgtEFRneag3A84cezuBJ4LSc/Gt2rxBgQhPe.Wq', NULL, '2019-05-04 00:00:00');
INSERT INTO `register`.`user` (`id`, `first_name`, `last_name`, `username`, `phone`, `password`, `telegram_id`, `date_created`) VALUES (7, 'Юра', 'Ганжа', 'yuraganj@gmail.com ', '0681378559', '$2a$10$bA/A6OO9pleEzQXrUIape.RDKEyvHAk6.prm006S.atOWR0CJ/M1W', NULL, '2019-05-04 00:00:00');
INSERT INTO `register`.`user` (`id`, `first_name`, `last_name`, `username`, `phone`, `password`, `telegram_id`, `date_created`) VALUES (8, 'Ростік', 'Троняк', 'RostikTronyak777@ukr.net', '0977679850', '$2a$10$cO1yB9GYCiMC7tfac1ZutuVhRTYlsF8C1C6SJ/NQl04sHYigh2A22', NULL, '2019-05-04 00:00:00');

COMMIT;


-- -----------------------------------------------------
-- Data for table `register`.`authority`
-- -----------------------------------------------------
START TRANSACTION;
USE `register`;
INSERT INTO `register`.`authority` (`id`, `name`) VALUES (1, 'ROLE_ADMIN');
INSERT INTO `register`.`authority` (`id`, `name`) VALUES (2, 'ROLE_USER');
INSERT INTO `register`.`authority` (`id`, `name`) VALUES (3, 'ROLE_GEODEZ');

COMMIT;


-- -----------------------------------------------------
-- Data for table `register`.`customer`
-- -----------------------------------------------------
START TRANSACTION;
USE `register`;
INSERT INTO `register`.`customer` (`id`, `org_name`, `email`, `phone`, `region`, `adress`, `first_name`, `last_name`) VALUES (1, 'ФОП Гройсман', 'gr@mail.com', '0987777777', 'Lviv', 'qwerty', 'Іван', 'Гройсман');
INSERT INTO `register`.`customer` (`id`, `org_name`, `email`, `phone`, `region`, `adress`, `first_name`, `last_name`) VALUES (2, 'ПДА Тимошенко', 'timo@asd.com', '0981234565', 'Kyiv', 'zxcvbn', 'Галя', 'Тимошенко');

COMMIT;


-- -----------------------------------------------------
-- Data for table `register`.`contract`
-- -----------------------------------------------------
START TRANSACTION;
USE `register`;
INSERT INTO `register`.`contract` (`id`, `customer_id`, `region`, `district`, `village_council`, `order_type`, `order_status`, `is_finished`, `stage`, `total_price`, `payed_amount`, `registered`, `updated`, `created_by`, `assigned_to`, `contract_identifier`, `square`) VALUES (1, 1, 'Львів', 'Самбір', 'Задністрянська', 'Технічна документація(Паї)', 'Прийнято', false, 'Зйомка', 2500, 1000, '2019-01-01', '2019-02-02', 1, 1, NULL, NULL);
INSERT INTO `register`.`contract` (`id`, `customer_id`, `region`, `district`, `village_council`, `order_type`, `order_status`, `is_finished`, `stage`, `total_price`, `payed_amount`, `registered`, `updated`, `created_by`, `assigned_to`, `contract_identifier`, `square`) VALUES (2, 2, 'Городок', 'Свиняни', 'Цюрипинська', 'Технічна документація(Паї)', 'Прийнято', true, 'Зйомка', 3000, 1500, '2019-02-02', '2019-03-03', 1, 1, NULL, NULL);
INSERT INTO `register`.`contract` (`id`, `customer_id`, `region`, `district`, `village_council`, `order_type`, `order_status`, `is_finished`, `stage`, `total_price`, `payed_amount`, `registered`, `updated`, `created_by`, `assigned_to`, `contract_identifier`, `square`) VALUES (3, 1, 'Городок', 'Ставчани', 'Задупинська', 'Технічна документація(Паї)', 'Прийнято', false, 'Зйомка', 3000, 1500, '2019-03-03', '2019-04-04', 3, 3, NULL, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `register`.`stage`
-- -----------------------------------------------------
START TRANSACTION;
USE `register`;
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (1, 'Зйомка', NULL, 10);
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (2, 'Погодження в замовника', NULL, 10);
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (3, 'Погодження в інших установах', NULL, 10);
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (4, 'Погодження меж', NULL, 10);
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (5, 'Походження архітектури', NULL, 10);
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (6, 'Заява 6-зем', NULL, 10);
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (7, 'Екстериторіальність', NULL, 10);
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (8, 'Накладки', NULL, 10);
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (9, 'Реєстрація', NULL, 10);
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (10, 'Громадські слухання', NULL, 10);
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (11, 'Розробка проекту', NULL, 10);
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (12, 'Експертиза', NULL, 10);
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (13, 'Затверджено', NULL, 10);
INSERT INTO `register`.`stage` (`id`, `label`, `comment`, `max_in_progress`) VALUES (14, 'Опрацювання геодезичних вимірів', NULL, 10);

COMMIT;


-- -----------------------------------------------------
-- Data for table `register`.`status`
-- -----------------------------------------------------
START TRANSACTION;
USE `register`;
INSERT INTO `register`.`status` (`id`, `label`, `comment`) VALUES (1, 'Прийнято', NULL);
INSERT INTO `register`.`status` (`id`, `label`, `comment`) VALUES (2, 'В Роботі', NULL);
INSERT INTO `register`.`status` (`id`, `label`, `comment`) VALUES (3, 'Виправляється', NULL);
INSERT INTO `register`.`status` (`id`, `label`, `comment`) VALUES (4, 'Очікує рішення', NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `register`.`contract_type`
-- -----------------------------------------------------
START TRANSACTION;
USE `register`;
INSERT INTO `register`.`contract_type` (`id`, `label`, `comment`) VALUES (1, 'Проект', NULL);
INSERT INTO `register`.`contract_type` (`id`, `label`, `comment`) VALUES (2, 'Технічна документація(Паї)', NULL);
INSERT INTO `register`.`contract_type` (`id`, `label`, `comment`) VALUES (3, 'Технічна документація (встановлення)', NULL);
INSERT INTO `register`.`contract_type` (`id`, `label`, `comment`) VALUES (4, 'Технічна документація(Інвентаризація)', NULL);
INSERT INTO `register`.`contract_type` (`id`, `label`, `comment`) VALUES (5, 'Геодезія', NULL);
INSERT INTO `register`.`contract_type` (`id`, `label`, `comment`) VALUES (6, 'Геологія', NULL);
INSERT INTO `register`.`contract_type` (`id`, `label`, `comment`) VALUES (7, 'СЕО', NULL);
INSERT INTO `register`.`contract_type` (`id`, `label`, `comment`) VALUES (8, 'Ген план', NULL);
INSERT INTO `register`.`contract_type` (`id`, `label`, `comment`) VALUES (9, 'ПДП', NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `register`.`user_authority`
-- -----------------------------------------------------
START TRANSACTION;
USE `register`;
INSERT INTO `register`.`user_authority` (`user_id`, `authority_id`) VALUES (1, '2');
INSERT INTO `register`.`user_authority` (`user_id`, `authority_id`) VALUES (2, '1');
INSERT INTO `register`.`user_authority` (`user_id`, `authority_id`) VALUES (3, '2');
INSERT INTO `register`.`user_authority` (`user_id`, `authority_id`) VALUES (4, '3');
INSERT INTO `register`.`user_authority` (`user_id`, `authority_id`) VALUES (5, '2');
INSERT INTO `register`.`user_authority` (`user_id`, `authority_id`) VALUES (6, '2');
INSERT INTO `register`.`user_authority` (`user_id`, `authority_id`) VALUES (7, '2');
INSERT INTO `register`.`user_authority` (`user_id`, `authority_id`) VALUES (8, '2');

COMMIT;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
