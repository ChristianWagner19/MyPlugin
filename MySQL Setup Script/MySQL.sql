CREATE database minecraft_myplugin;
USE minecraft_myplugin;
CREATE TABLE `users` (
  `uniqueId` varchar(255) NOT NULL,
  `username` varchar(255) DEFAULT NULL,
  `woodworking` int NOT NULL DEFAULT '1',
  `mining` int NOT NULL DEFAULT '1',
  `vitality` int NOT NULL DEFAULT '1',
  `woodworkingexp` decimal(5,2) NOT NULL DEFAULT '1.00',
  `miningexp` decimal(5,2) NOT NULL DEFAULT '1.00',
  `vitalityexp` decimal(5,2) NOT NULL DEFAULT '1.00',
  `runregen` tinyint NOT NULL DEFAULT '0',
  `regenTaskID` int DEFAULT '-1',
  `mobskilled` varchar(500) DEFAULT NULL,
  `oresmined` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`uniqueId`)
)
