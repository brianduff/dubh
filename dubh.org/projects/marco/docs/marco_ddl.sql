--
-- SQL Script to create tables for the Bookmark database
-- For MySQL 3.24
--
-- (C) 2001 Brian Duff
--

CREATE DATABASE IF NOT EXISTS marco;

USE marco;

CREATE TABLE IF NOT EXISTS users (
   id                INT NOT NULL AUTO_INCREMENT PRIMARY KEY
,  forenames         VARCHAR(255)
,  surname           VARCHAR(255)
,  email             VARCHAR(255) NOT NULL
,  password          VARCHAR(20)  NOT NULL
,  is_administrator  ENUM("Yes", "No") NOT NULL DEFAULT "No"
);

CREATE TABLE IF NOT EXISTS categories (
   id                INT NOT NULL AUTO_INCREMENT PRIMARY KEY
,  title             VARCHAR(100) NOT NULL
,  parent_cat        INT
,  user              INT
,  is_published      ENUM("Yes", "No") NOT NULL DEFAULT "No"
);

CREATE TABLE IF NOT EXISTS bookmarks (
   id                INT NOT NULL AUTO_INCREMENT PRIMARY KEY
,  title             VARCHAR(200) NOT NULL
,  url               VARCHAR(255) NOT NULL
,  category          INT
,  user              INT
);