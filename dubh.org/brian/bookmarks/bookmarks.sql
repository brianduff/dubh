--
-- SQL Script to create tables for the Bookmark database
-- For MySQL 3.24
--
-- (C) 2001 Brian Duff
--

CREATE DATABASE IF NOT EXISTS bookmarks;

USE bookmarks;


CREATE TABLE IF NOT EXISTS categories (
   id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY
,  title       VARCHAR(100) NOT NULL
,  parent_cat  INT
);

CREATE TABLE IF NOT EXISTS bookmarks (
   id          INT NOT NULL AUTO_INCREMENT PRIMARY KEY
,  title       VARCHAR(200) NOT NULL
,  url         VARCHAR(255) NOT NULL
,  category    INT
);