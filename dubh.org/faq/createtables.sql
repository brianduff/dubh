-- 
-- SQL Script to create tables for FAQbase
-- For MySQL 3.24
--
-- (C) 2001 Brian Duff
--

CREATE DATABASE IF NOT EXISTS faqbase;

USE faqbase;

CREATE TABLE IF NOT EXISTS users (
   id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY
,  email       VARCHAR(100) NOT NULL
,  password    VARCHAR(25) NOT NULL
,  forenames   VARCHAR(200)
,  surname     VARCHAR(100)
,  created     TIMESTAMP NOT NULL
,  type        ENUM("Normal", "Administrator") NOT NULL
);

CREATE TABLE IF NOT EXISTS categories (
   id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY
,  title       VARCHAR(100) NOT NULL
,  description TEXT
,  creator     INT NOT NULL
,  created     TIMESTAMP NOT NULL
,  parent_cat  INT
);

CREATE TABLE IF NOT EXISTS questions (
   id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY
,  category    INT NOT NULL
,  title       VARCHAR(100) NOT NULL
,  description TEXT
,  keywords    VARCHAR(100)
,  creator     INT NOT NULL
,  published   ENUM("Yes", "No") NOT NULL DEFAULT "No"
);

CREATE TABLE IF NOT EXISTS answers (
   id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY
,  question    INT NOT NULL
,  body        TEXT
,  creator     INT NOT NULL
,  created     TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS seealso (
   id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY
,  question    INT NOT NULL
,  url         VARCHAR(255)
,  text        VARCHAR(255)
);

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP
ON    faqbase.*
TO    web@'%'
IDENTIFIED BY 'web';

GRANT SELECT, INSERT, UPDATE, DELETE, CREATE, DROP
ON    faqbase.*
TO    web@localhost
IDENTIFIED BY 'web';
