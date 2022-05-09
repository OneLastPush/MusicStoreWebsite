/**
 * Author:  Frank Birikundavyi
 * Created: Feb 8, 2017
 */

-- DROP DATABASE IF EXISTS g2w17;
-- CREATE DATABASE g2w17;
-- 
-- GRANT ALL ON g2w17.* TO g2w17@"%" IDENTIFIED BY "MY83bizk";
-- GRANT ALL ON g2w17.* TO g2w17@"localhost" IDENTIFIED BY "MY83bizk";
-- 
USE g2w17;

DROP TABLE IF EXISTS banner_ad;
DROP TABLE IF EXISTS rss;
DROP TABLE IF EXISTS survey_choices;
DROP TABLE IF EXISTS survey_questions;
DROP TABLE IF EXISTS songwriter_songs;
DROP TABLE IF EXISTS invoice_details_album;
DROP TABLE IF EXISTS invoice_details_songs;
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS songs;
DROP TABLE IF EXISTS albums;
DROP TABLE IF EXISTS labels;
DROP TABLE IF EXISTS songwriters;
DROP TABLE IF EXISTS artists;
DROP TABLE IF EXISTS invoices;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS genres;

CREATE TABLE genres(
	id INT AUTO_INCREMENT,
	genre_name VARCHAR(255) NOT NULL UNIQUE,
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE users(
	id INT AUTO_INCREMENT,
	title VARCHAR(10),
	pword VARCHAR(255) NOT NULL,
	fname VARCHAR(100) NOT NULL,
	lname VARCHAR(100) NOT NULL,
	company_name VARCHAR(255),
	address_1 VARCHAR(100) NOT NULL,
	address_2 VARCHAR(100),
	city VARCHAR(100) NOT NULL,
	province VARCHAR(100) NOT NULL,
	country VARCHAR(100) NOT NULL,
	postal_code VARCHAR(7) NOT NULL,
	home_number VARCHAR(15),
	cell_number VARCHAR(15),
	email VARCHAR(255) NOT NULL UNIQUE,
	last_genre_searched INT,
	admin_privilege BOOLEAN DEFAULT false,
	PRIMARY KEY (id),
	FOREIGN KEY (last_genre_searched) REFERENCES genres(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE invoices(
	id INT AUTO_INCREMENT,
	client_id INT NOT NULL,
	sale_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	net_value DECIMAL(5,2) NOT NULL,
	pst DECIMAL(5,2),
	gst DECIMAL(5,2),
	hst DECIMAL(5,2),
	gross_value DECIMAL(5,2) NOT NULL,
	removed BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (id),
	FOREIGN KEY (client_id) REFERENCES users(id) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE artists(
	id INT AUTO_INCREMENT,
	artist VARCHAR(255) UNIQUE NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE songwriters(
	id INT AUTO_INCREMENT,
	songwriter VARCHAR(255) UNIQUE NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE labels(
	id INT AUTO_INCREMENT,
	label_name VARCHAR(255) UNIQUE NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE albums(
	id INT AUTO_INCREMENT,
	album_title VARCHAR(255) NOT NULL,
	release_date DATE NOT NULL,
	artist_id INT NOT NULL,
	recording_label INT NOT NULL,
	album_cover VARCHAR(255) NOT NULL,
	track_count INT NOT NULL,
	date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	cost_price DECIMAL(5,2) NOT NULL,
	list_price DECIMAL(5,2) NOT NULL,
	sale_price DECIMAL(5,2),
	removal_status BOOLEAN DEFAULT FALSE,
	removal_date TIMESTAMP,
	FOREIGN KEY (artist_id) REFERENCES artists(id) ON UPDATE CASCADE,
	FOREIGN KEY (recording_label) REFERENCES labels(id) ON UPDATE CASCADE,
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE songs(
	id INT AUTO_INCREMENT,
	album_id INT NOT NULL,
	track_title VARCHAR(255) NOT NULL,
	artist_id INT NOT NULL,
	song_length TIME DEFAULT '0000',
	track_number INT NOT NULL,
	genre_id INT NOT NULL,
	cost_price DECIMAL(5,2) NOT NULL,
	list_price DECIMAL(5,2) NOT NULL,
	sale_price DECIMAL(5,2),
	date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	single_song BOOLEAN DEFAULT FALSE,
	removal_status BOOLEAN DEFAULT FALSE,
	removal_date TIMESTAMP,
	PRIMARY KEY (id),
	FOREIGN KEY (album_id) REFERENCES albums(id) ON UPDATE CASCADE,
	FOREIGN KEY (genre_id) REFERENCES genres(id) ON UPDATE CASCADE,
	FOREIGN KEY (artist_id) REFERENCES artists(id) ON UPDATE CASCADE
	) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE reviews(
	id INT AUTO_INCREMENT,
	inventory_id INT NOT NULL,
	client_id INT NOT NULL,
	rating INT NOT NULL,
	review_text VARCHAR(400) NOT NULL,
	approval_status BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (id),
	FOREIGN KEY (inventory_id) REFERENCES songs(id) ON UPDATE CASCADE,
	FOREIGN KEY (client_id) REFERENCES users(id) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE invoice_details_songs(
  	id INT AUTO_INCREMENT,
	sale_id INT NOT NULL,
    inventory_id INT NOT NULL,
	FOREIGN KEY (sale_id) REFERENCES invoices(id) ON UPDATE CASCADE,
	FOREIGN KEY (inventory_id) REFERENCES songs(id) ON UPDATE CASCADE,
	sold_price DECIMAL(5,2) NOT NULL,
	removed BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE invoice_details_album(
	id INT AUTO_INCREMENT,
  	sale_id INT NOT NULL,
    inventory_id INT NOT NULL,
	FOREIGN KEY (sale_id) REFERENCES invoices(id) ON UPDATE CASCADE,
	FOREIGN KEY (inventory_id) REFERENCES albums(id) ON UPDATE CASCADE,
	sold_price DECIMAL(5,2) NOT NULL,
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE songwriter_songs(
	id INT AUTO_INCREMENT,
	song_id INT NOT NULL,
	songwriter_id  INT NOT NULL,
	FOREIGN KEY (song_id) REFERENCES songs(id) ON UPDATE CASCADE,
	FOREIGN KEY (songwriter_id) REFERENCES songwriters(id) ON UPDATE CASCADE,
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE survey_questions(
	id INT AUTO_INCREMENT,
	question varchar(255) UNIQUE NOT NULL,
	current_question BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE survey_choices(
 	id INT AUTO_INCREMENT,
	question_id INT NOT NULL,
  	choice varchar(25) NOT NULL,
  	vote INT DEFAULT '0',
	FOREIGN KEY (question_id) REFERENCES survey_questions(id) ON UPDATE CASCADE,
  	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE rss(
	id INT AUTO_INCREMENT,
	feed VARCHAR(255) UNIQUE NOT NULL,
	current_rss BOOLEAN DEFAULT FALSE,
	PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE banner_ad(
	id INT AUTO_INCREMENT,
	company VARCHAR(255) NOT NULL,
	banner_pic VARCHAR(255) NOT NULL,
	display BOOLEAN DEFAULT FALSE,
PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Add artist, Songwriter, labels, genre
-- Then album
--  then tracks
-- add users
-- then invoices & reviews
-- 

-- Songwriters 
insert into songwriters (songwriter) values ('Cook');
insert into songwriters (songwriter) values ('Jones');
insert into songwriters (songwriter) values ('Rotten');
insert into songwriters (songwriter) values ('Vicious');

insert into songwriters (songwriter) values ('Matlock');
insert into songwriters (songwriter) values ('Strummer');
insert into songwriters (songwriter) values ('Simonon');
insert into songwriters (songwriter) values ('Tommy Ramone');
insert into songwriters (songwriter) values ('Dee Dee Ramone');
insert into songwriters (songwriter) values ('Bob Hilliard');
insert into songwriters (songwriter) values ('David Mann');
insert into songwriters (songwriter) values ('Barney Bigard');
insert into songwriters (songwriter) values ('Duke Ellington');
insert into songwriters (songwriter) values ('Irving Mills');
insert into songwriters (songwriter) values ('Richard Rodgers');
insert into songwriters (songwriter) values ('Lorenz Hart');
insert into songwriters (songwriter) values ('Hoagy Carmichael');
insert into songwriters (songwriter) values ('Eddie DeLange');
insert into songwriters (songwriter) values ('Jimmy Van Heusen');
insert into songwriters (songwriter) values ('Howard Dietz');
insert into songwriters (songwriter) values ('Arthur Schwartz');
insert into songwriters (songwriter) values ('Paul James');
insert into songwriters (songwriter) values ('Kay Swift');
insert into songwriters (songwriter) values ('Einar Aaron Swan');
insert into songwriters (songwriter) values ('Cole Porter');
insert into songwriters (songwriter) values ('Yip Harburg');
insert into songwriters (songwriter) values ('Alec Wilder');
insert into songwriters (songwriter) values ('Harold Arlen');
insert into songwriters (songwriter) values ('Ted Koehler');
insert into songwriters (songwriter) values ('Gus Kahn');
insert into songwriters (songwriter) values ('Matty Malneck');
insert into songwriters (songwriter) values ('Frank Signorelli');
insert into songwriters (songwriter) values ('Sol Parker');
insert into songwriters (songwriter) values ('Henry W. Sanicola');
insert into songwriters (songwriter) values ('Jr.');
insert into songwriters (songwriter) values ('Frank Sinatra');
insert into songwriters (songwriter) values ('Jagger');
insert into songwriters (songwriter) values ('Richards');
insert into songwriters (songwriter) values ('James Moore');
insert into songwriters (songwriter) values ('Mick Taylor');
insert into songwriters (songwriter) values ('Robert Johnson');
insert into songwriters (songwriter) values ('Page');
insert into songwriters (songwriter) values ('Plant');
insert into songwriters (songwriter) values ('Bonham');
insert into songwriters (songwriter) values ('Memphis Minnie');
insert into songwriters (songwriter) values ('Townshend');
insert into songwriters (songwriter) values ('Entwistle');
insert into songwriters (songwriter) values ('Felix Mendelssohn');
insert into songwriters (songwriter) values ('Philippe Gaubert');
insert into songwriters (songwriter) values ('Maurice Ravel');
insert into songwriters (songwriter) values ('George Gershwin');
insert into songwriters (songwriter) values ('Jules Massenet');
-- artist
insert into artists (artist) values ('Sex Pistols');
insert into artists (artist) values ('The Clash');
insert into artists (artist) values ('Ramones');
insert into artists (artist) values ('Thelonious Monk');
insert into artists (artist) values ('John Coltrane');
insert into artists (artist) values ('Frank Sinatra');
insert into artists (artist) values ('The Seatbelts');
insert into artists (artist) values ('The Rolling Stones');
insert into artists (artist) values ('Led Zeppelin');
insert into artists (artist) values ('The Who');
insert into artists (artist) values ('Andrew von Oeyen');
insert into artists (artist) values ('The Weeknd');
-- label
insert into labels (label_name) values ('Virgin');
insert into labels (label_name) values ('CBS');
insert into labels (label_name) values ('Sire');
insert into labels (label_name) values ('Columbia/Legacy');
insert into labels (label_name) values ('Impulse');
insert into labels (label_name) values ('Capitol Records');
insert into labels (label_name) values ('Victor Entertainment');
insert into labels (label_name) values ('Rolling Stones Records');
insert into labels (label_name) values ('Atlantic Records');
insert into labels (label_name) values ('Track Records/Decca Records');
insert into labels (label_name) values ('Warner Classics, Warner Music UK Ltd');
insert into labels (label_name) values ('XO & Republic Records');

-- Genre
INSERT INTO `genres`( `genre_name`) VALUES ('Jazz');
INSERT INTO `genres`( `genre_name`) VALUES ('Pop');
INSERT INTO `genres`( `genre_name`) VALUES ('Classical');
INSERT INTO `genres`( `genre_name`) VALUES ('Punk');
INSERT INTO `genres`( `genre_name`) VALUES ('Rock');

-- Album
INSERT INTO `albums`(`id`,`album_title`, `release_date`,`artist_id`, `recording_label` ,`track_count`, `date_added`, `cost_price`, `list_price`, `sale_price`, `album_cover`) VALUES 
(1,"Never Mind the Bollocks, Here's the Sex Pistols",'1997-10-28',1,1,11,NOW(),2.2,10,7,'https://upload.wikimedia.org/wikipedia/en/thumb/4/4c/Never_Mind_the_Bollocks%2C_Here%27s_the_Sex_Pistols.png/220px-Never_Mind_the_Bollocks%2C_Here%27s_the_Sex_Pistols.png'),
(2,"London Calling",'1979-12-14',2,2,19,NOW(),3.8,12,10,'https://upload.wikimedia.org/wikipedia/en/thumb/0/00/TheClashLondonCallingalbumcover.jpg/220px-TheClashLondonCallingalbumcover.jpg'), 
(3,"Ramones",'1976-2-1',3,3,14,NOW(),0.2,1,0.7,'https://upload.wikimedia.org/wikipedia/en/thumb/0/0e/Ramones_-_Blitzkrieg_Bop_cover.jpg/220px-Ramones_-_Blitzkrieg_Bop_cover.jpg'),
(4,"Monk's Dream",'1963-1-1',4,4,12,NOW(),2.2,10,7,'https://upload.wikimedia.org/wikipedia/en/e/ed/Monks_Dream_by_Thelonious.jpg'),
(5,"Ballads (Original recording remastered)",'2008-2-19',5,5,8,NOW(),2.09,9.49,6.69,'https://upload.wikimedia.org/wikipedia/en/0/0a/Coltraneballads.jpg'),
(6,"In The Wee Small Hours",'1998-5-14',6,6,16,NOW(),2.16,9.82,6.89,'https://upload.wikimedia.org/wikipedia/en/1/12/Wee_small_hours_album_cover_high_definition.jpg'),
(7,"Cowboy Bebop (Original Soundtrack)",'1998-5-21',7,7,17,NOW(),3.8,11.99,9.99,'https://images-na.ssl-images-amazon.com/images/I/41V6S7NGC6L.jpg'),
(8,"Exile on Main St.",'1972-5-12',8,8,18,NOW(),3.65,10.99,8.75,'https://upload.wikimedia.org/wikipedia/en/thumb/c/ca/ExileMainSt.jpg/220px-ExileMainSt.jpg'),
(9,"Led Zeppelin IV",'1971-8-11',9,9,8,NOW(),1.97,7.99,5.95,'https://upload.wikimedia.org/wikipedia/en/2/26/Led_Zeppelin_-_Led_Zeppelin_IV.jpg'), 
(10,"Who's Next",'1971-8-27',10,10,9,NOW(),2.06,8.49,6.49,'https://upload.wikimedia.org/wikipedia/en/4/44/Whosnext.jpg'),
(11,"Saint-Saens, Ravel & Gershwin: Piano concertos",'2017-1-13',11,11,8,NOW(),3.7,14.29,12.49,'http://static.fnac-static.com/multimedia/Images/FR/NR/67/a5/81/8496487/1507-1.jpg'),
(12,"Starboy",'2016-11-25',12,12,18,NOW(),3.47,11.99,9.99,'https://upload.wikimedia.org/wikipedia/en/thumb/3/39/The_Weeknd_-_Starboy.png/220px-The_Weeknd_-_Starboy.png');


-- Insert mock songs
INSERT INTO `songs` (`id`, `album_id`, `track_title`, `artist_id`, `song_length`, `track_number`, `genre_id`, `cost_price`, `list_price`, `sale_price`) VALUES
(1, 1, 'Holiday in the Sun', 1, '03:22:00', 1, 4, '0.20', '1.00', '0.70'),
(2, 1, 'Liar', 1, '02:41:00', 2, 4, '0.20', '1.00', '0.70'),
(3, 1, 'No Feelings', 1, '02:53:00', 3, 4, '0.20', '1.00', '0.70'),
(4, 1, 'God Save the Queen', 1, '03:20:00', 4, 4, '0.20', '1.00', '0.70'),
(5, 1, 'Problems', 1, '04:11:00', 5, 4, '0.20', '1.00', '0.70'),
(6, 1, 'Seventeen', 1, '02:02:00', 6, 4, '0.20', '1.00', '0.70'),
(7, 1, 'Anarchy in the UK', 1, '03:32:00', 7, 4, '0.20', '1.00', '0.70'),
(8, 1, 'Bodies', 1, '03:03:00', 8, 4, '0.20', '1.00', '0.70'),
(9, 1, 'Pretty Vacant', 1, '03:18:00', 9, 4, '0.20', '1.00', '0.70'),
(10, 1, 'New York', 1, '03:07:00', 10, 4, '0.20', '1.00', '0.70'),
(11, 1, 'E.M.I', 1, '03:10:00', 11, 4, '0.20', '1.00', '0.70'),
(12, 2, 'London Calling', 2, '03:19:00', 1, 4, '0.20', '1.00', '0.70'),
(13, 2, 'Brand New Cadillac (written and originally performed by Vince Taylor)', 2, '02:09:00', 2, 4, '0.20', '1.00', '0.70'),
(14, 2, 'Jimmy Jazz', 2, '03:52:00', 3, 4, '0.20', '1.00', '0.70'),
(15, 2, 'Hateful', 2, '02:45:00', 4, 4, '0.20', '1.00', '0.70'),
(16, 2, 'Rudie Can''t Fail', 2, '03:26:00', 5, 4, '0.20', '1.00', '0.70'),
(17, 2, 'Spanish Bombs', 2, '03:19:00', 6, 4, '0.20', '1.00', '0.70'),
(18, 2, 'The Right Profile', 2, '03:56:00', 7, 4, '0.20', '1.00', '0.70'),
(19, 2, 'Lost in the Supermarket', 2, '03:47:00', 8, 4, '0.20', '1.00', '0.70'),
(20, 2, 'Clampdown', 2, '03:49:00', 9, 4, '0.20', '1.00', '0.70'),
(21, 2, 'The Guns of Brixton (written by Paul Simonon)', 2, '03:07:00', 10, 4, '0.20', '1.00', '0.70'),
(22, 2, 'Wrong ''Em Boyo (written by Clive Alphonso; originally performed by the Rulers; including Stagger Lee)', 2, '03:10:00', 11, 4, '0.20', '1.00', '0.70'),
(23, 2, 'Death or Glory', 2, '03:55:00', 12, 4, '0.20', '1.00', '0.70'),
(24, 2, 'Koka Kola', 2, '01:46:00', 13, 4, '0.20', '1.00', '0.70'),
(25, 2, 'The Card Cheat (written by Strummer, Jones, Simonon & Topper Headon)', 2, '03:51:00', 14, 4, '0.20', '1.00', '0.70'),
(26, 2, 'Lover''s Rock', 2, '04:01:00', 15, 4, '0.20', '1.00', '0.70'),
(27, 2, 'Four Horsemen', 2, '02:56:00', 16, 4, '0.20', '1.00', '0.70'),
(28, 2, 'I''m Not Down', 2, '03:00:00', 17, 4, '0.20', '1.00', '0.70'),
(29, 2, 'Revolution Rock (written by Jackie Edwards, Danny Ray; originally performed by Danny Ray and the Revolutionaries)', 2, '05:37:00', 18, 4, '0.20', '1.00', '0.70'),
(30, 2, 'Train in Vain', 2, '03:09:00', 19, 4, '0.20', '1.00', '0.70'),
(32, 3, 'Monk''s Dream (Take 8)', 4, '06:25:00', 1, 1, '0.20', '1.00', '0.70'),
(33, 3, 'Body and Soul', 4, '04:28:00', 2, 1, '0.20', '1.00', '0.70'),
(34, 3, 'Bright Mississippi (Take 1)', 4, '08:36:00', 3, 1, '0.20', '1.00', '0.70'),
(35, 3, 'Five Spot Blues (Album Version)', 4, '03:14:00', 4, 1, '0.20', '1.00', '0.70'),
(36, 3, 'Blue Bolivar Blues (Take 2)', 4, '07:30:00', 5, 1, '0.20', '1.00', '0.70'),
(37, 3, 'Just a Gigolo', 4, '02:27:00', 6, 1, '0.20', '1.00', '0.70'),
(38, 3, 'Bye-Ya (Album Version)', 4, '05:22:00', 7, 1, '0.20', '1.00', '0.70'),
(39, 3, 'Sweet and Lovely (Album Version)', 4, '07:52:00', 8, 1, '0.20', '1.00', '0.70'),
(40, 3, 'Monk''s Dream (Take 3)', 4, '05:14:00', 9, 1, '0.20', '1.00', '0.70'),
(41, 3, 'Body and Soul (Take 1)', 4, '05:05:00', 10, 1, '0.20', '1.00', '0.70'),
(42, 3, 'Bright Mississippi (Take 3)', 4, '10:19:00', 11, 1, '0.20', '1.00', '0.70'),
(43, 3, 'Blue Bolivar Blues (Take 1)', 4, '06:12:00', 12, 1, '0.20', '1.00', '0.70'),
(44, 4, 'Say It (Over And Over Again)', 5, '04:15:00', 1, 1, '0.26', '1.29', '0.95'),
(45, 4, 'You Don''t Know What Love Is', 5, '05:12:00', 2, 1, '0.26', '1.29', '0.95'),
(46, 4, 'Too Young To Go Steady', 5, '04:21:00', 3, 1, '0.26', '1.29', '0.95'),
(47, 4, 'All Or Nothing At All', 5, '03:35:00', 4, 1, '0.26', '1.29', '0.95'),
(48, 4, 'I Wish I Knew', 5, '04:51:00', 5, 1, '0.26', '1.29', '0.95'),
(49, 4, 'What''s New', 5, '03:44:00', 6, 1, '0.26', '1.29', '0.95'),
(50, 4, 'It''s Easy To Remember', 5, '02:45:00', 7, 1, '0.26', '1.29', '0.95'),
(51, 4, 'Nancy (With The Laughing Face)', 5, '03:10:00', 8, 1, '0.26', '1.29', '0.95'),
(52, 5, 'In the Wee Small Hours of the Morning', 6, '03:00:00', 1, 1, '0.26', '1.29', '0.95'),
(53, 5, 'Mood Indigo', 6, '03:30:00', 2, 1, '0.26', '1.29', '0.95'),
(54, 5, 'Glad to Be Unhappy', 6, '02:35:00', 3, 1, '0.26', '1.29', '0.95'),
(55, 5, 'I Get Along With You Very Well', 6, '03:42:00', 4, 1, '0.26', '1.29', '0.95'),
(56, 5, 'Deep in a Dream', 6, '02:48:00', 5, 1, '0.26', '1.29', '0.95'),
(57, 5, 'I See Your Face Before Me', 6, '03:23:00', 6, 1, '0.26', '1.29', '0.95'),
(58, 5, 'Can''t We Be Friends?', 6, '02:48:00', 7, 1, '0.26', '1.29', '0.95'),
(59, 5, 'When Your Lover Has Gone', 6, '03:09:00', 8, 1, '0.26', '1.29', '0.95'),
(60, 5, 'What Is This Thing Called Love?', 6, '02:34:00', 9, 1, '0.26', '1.29', '0.95'),
(61, 5, 'Last Night When We Were Young', 6, '03:17:00', 10, 1, '0.26', '1.29', '0.95'),
(62, 5, 'I''ll Be Around', 6, '02:59:00', 11, 1, '0.26', '1.29', '0.95'),
(63, 5, 'Ill Wind', 6, '03:46:00', 12, 1, '0.26', '1.29', '0.95'),
(64, 5, 'It Never Entered My Mind', 6, '02:41:00', 13, 1, '0.26', '1.29', '0.95'),
(65, 5, 'Dancing on the Ceiling', 6, '02:57:00', 14, 1, '0.26', '1.29', '0.95'),
(66, 5, 'I''ll Never Be the Same', 6, '03:05:00', 15, 1, '0.26', '1.29', '0.95'),
(67, 5, 'This Love of Mine', 6, '03:33:00', 16, 1, '0.26', '1.29', '0.95'),
(69, 8, 'Rocks Off', 8, '04:32:00', 1, 5, '0.26', '1.29', '0.90'),
(70, 8, 'Rip This Joint', 8, '02:23:00', 2, 5, '0.26', '1.29', '0.90'),
(71, 8, 'Shake Your Hips', 8, '02:59:00', 3, 5, '0.26', '1.29', '0.90'),
(72, 8, 'Casino Boogie', 8, '03:33:00', 4, 5, '0.26', '1.29', '0.90'),
(73, 8, 'Tumbling Dice', 8, '03:45:00', 5, 5, '0.26', '1.29', '0.90'),
(74, 8, 'Sweet Virginia', 8, '04:27:00', 6, 5, '0.26', '1.29', '0.90'),
(75, 8, 'Torn and Frayed', 8, '04:17:00', 7, 5, '0.26', '1.29', '0.90'),
(76, 8, 'Sweet Black Angel', 8, '02:54:00', 8, 5, '0.26', '1.29', '0.90'),
(77, 8, 'Loving Cup', 8, '04:25:00', 9, 5, '0.26', '1.29', '0.90'),
(78, 8, 'Happy', 8, '03:04:00', 10, 5, '0.26', '1.29', '0.90'),
(79, 8, 'Turd on the Run', 8, '02:36:00', 11, 5, '0.26', '1.29', '0.90'),
(80, 8, 'Ventilator Blues', 8, '03:24:00', 12, 5, '0.26', '1.29', '0.90'),
(81, 8, 'I Just Want to See His Face', 8, '02:52:00', 13, 5, '0.26', '1.29', '0.90'),
(82, 8, 'Let It Loose', 8, '05:16:00', 14, 5, '0.26', '1.29', '0.90'),
(83, 8, 'All Down the Line', 8, '03:49:00', 15, 5, '0.26', '1.29', '0.90'),
(84, 8, 'Stop Breaking Down', 8, '04:34:00', 16, 5, '0.26', '1.29', '0.90'),
(85, 8, 'Shine a Light', 8, '04:14:00', 17, 5, '0.26', '1.29', '0.90'),
(86, 8, 'Soul Survivor', 8, '03:49:00', 18, 5, '0.26', '1.29', '0.90'),
(87, 9, 'Black Dog', 9, '04:54:00', 1, 5, '0.26', '1.29', '0.90'),
(88, 9, 'Rock and Roll', 9, '03:40:00', 2, 5, '0.26', '1.29', '0.90'),
(89, 9, 'The Battle of Evermore', 9, '05:51:00', 3, 5, '0.26', '1.29', '0.90'),
(90, 9, 'Stairway to Heaven', 9, '08:02:00', 4, 5, '0.26', '1.29', '0.90'),
(91, 9, 'Misty Mountain Hop', 9, '04:38:00', 5, 5, '0.26', '1.29', '0.90'),
(92, 9, 'Four Sticks', 9, '04:44:00', 6, 5, '0.26', '1.29', '0.90'),
(93, 9, 'Going to California', 9, '03:31:00', 7, 5, '0.26', '1.29', '0.90'),
(94, 9, 'When the Levee Breaks', 9, '07:07:00', 8, 5, '0.26', '1.29', '0.90'),
(95, 10, 'Baba O''Riley', 10, '05:08:00', 1, 5, '0.26', '1.29', '0.90'),
(96, 10, 'Bargain', 10, '05:34:00', 2, 5, '0.26', '1.29', '0.90'),
(97, 10, 'Love Ain''t for Keeping', 10, '02:10:00', 3, 5, '0.26', '1.29', '0.90'),
(98, 10, 'My Wife', 10, '03:41:00', 4, 5, '0.26', '1.29', '0.90'),
(99, 10, 'The Song Is Over', 10, '06:14:00', 5, 5, '0.26', '1.29', '0.90'),
(100, 10, 'Getting in Tune', 10, '04:50:00', 6, 5, '0.26', '1.29', '0.90'),
(101, 10, 'Going Mobile', 10, '03:42:00', 7, 5, '0.26', '1.29', '0.90'),
(102, 10, 'Behind Blue Eyes', 10, '03:42:00', 8, 5, '0.26', '1.29', '0.90'),
(103, 10, 'Won''t Get Fooled Again', 10, '08:32:00', 9, 5, '0.26', '1.29', '0.90'),
(104, 11, 'Andante Sostenuto', 11, '11:31:00', 1, 3, '0.50', '1.74', '1.25'),
(105, 11, 'Allegro scherzando', 11, '05:59:00', 2, 3, '0.50', '1.74', '1.25'),
(106, 11, 'Presto', 11, '06:28:00', 3, 3, '0.50', '1.74', '1.25'),
(107, 11, 'Allegramente', 11, '08:37:00', 4, 3, '0.50', '1.74', '1.25'),
(108, 11, 'Adagio assai', 11, '09:23:00', 5, 3, '0.50', '1.74', '1.25'),
(109, 11, 'Presto', 11, '04:05:00', 6, 3, '0.50', '1.74', '1.25'),
(110, 11, 'Second Rhapsody', 11, '14:42:00', 7, 3, '2.28', '3.75', '3.49'),
(111, 11, 'Meditation de Tha', 11, '05:17:00', 8, 3, '0.50', '1.74', '1.25'),
(112, 12, 'Starboy', 12, '03:50:00', 1, 2, '0.39', '1.49', '0.99'),
(113, 12, 'Party Monster', 12, '04:09:00', 2, 2, '0.39', '1.49', '0.99'),
(114, 12, 'False Alarm', 12, '03:40:00', 3, 2, '0.39', '1.49', '0.99'),
(115, 12, 'Reminder', 12, '03:38:00', 4, 2, '0.39', '1.49', '0.99'),
(116, 12, 'Rockin''', 12, '03:52:00', 5, 2, '0.39', '1.49', '0.99'),
(117, 12, 'Secrets', 12, '04:25:00', 6, 2, '0.39', '1.49', '0.99'),
(118, 12, 'True color', 12, '03:26:00', 7, 2, '0.39', '1.49', '0.99'),
(119, 12, 'Stargirl Interlude', 12, '01:51:00', 8, 2, '0.39', '1.49', '0.99'),
(120, 12, 'Sidewalks', 12, '03:51:00', 9, 2, '0.39', '1.49', '0.99'),
(121, 12, 'Six Feet Under', 12, '03:57:00', 10, 2, '0.39', '1.49', '0.99'),
(122, 12, 'Love to lay', 12, '03:43:00', 11, 2, '0.39', '1.49', '0.99'),
(123, 12, 'A lovely night', 12, '03:40:00', 12, 2, '0.39', '1.49', '0.99'),
(124, 12, 'Attention', 12, '03:17:00', 13, 2, '0.39', '1.49', '0.99'),
(125, 12, 'Ordinary life', 12, '03:41:00', 14, 2, '0.39', '1.49', '0.99'),
(126, 12, 'Nothing without you', 12, '03:18:00', 15, 2, '0.39', '1.49', '0.99'),
(127, 12, 'All I know', 12, '05:21:00', 16, 2, '0.39', '1.49', '0.99'),
(128, 12, 'Die for you', 12, '04:20:00', 17, 2, '0.39', '1.49', '0.99'),
(129, 12, 'I feel it comming', 12, '04:29:00', 18, 2, '0.39', '1.49', '0.99');

-- users = Mock data

insert into users (title, pword, fname, lname, company_name, address_1, address_2, city, province, country, postal_code, home_number, cell_number, email) 
values 
(null, 'J3wlAR', 'Kimberly', 'Weaver', 'Goldner, Bahringer and Witting', '0112 North Point', null, 'Jeffersonville', 'Indiana', 'United States', '47134', null, '1-(339)322-9386', 'kweaver0@alexa.com'),
(null, '6hq5MN', 'Kelly', 'Reed', 'Gusikowski LLC', '62606 Hanson Trail', null, 'Las Vegas', 'Nevada', 'United States', '89115', '1-(702)756-8037', '1-(310)136-8835', 'kreed1@google.es'),
('Honorable', 'EKHavg94ZHGf', 'Phillip', 'Spencer', 'Watsica, Roob and Hayes', '8 Michigan Way', '76301 Darwin Park', 'San Antonio', 'Texas', 'United States', '78230', null, '1-(775)340-5844', 'pspencer2@diigo.com'),
 ('Dr', 'o6VIfD0', 'John', 'Burns', 'Runolfsson, Zieme and Bahringer', '48 Hagan Place', null, 'Houston', 'Texas', 'United States', '77070', null, '1-(325)937-9810', 'jburns3@list-manage.com'),
(null, '1LHZQpqC', 'Jessica', 'Stone', 'Nolan, Zemlak and Welch', '4365 Stephen Pass', '4640 Thackeray Trail', 'Sacramento', 'California', 'United States', '94280', '1-(916)377-2725', '1-(412)106-3690', 'jstone4@spiegel.de'),
 ('Dr', 'uitwA7Vh', 'Deborah', 'Flores', 'Lueilwitz-Kunze', '5 Commercial Street', '58551 Commercial Alley', 'Naples', 'Florida', 'United States', '33963', '1-(239)950-2179', null, 'dflores5@macromedia.com'),
(null, 'SDh7CzZLxook', 'Albert', 'Hill', 'Mayert-Krajcik', '8 Trailsway Avenue', null, 'Saint Paul', 'Minnesota', 'United States', '55108', '1-(651)209-3131', '1-(202)202-0352', 'ahill6@tuttocitta.it'),
('Rev', 'n0OZGdnBqrF5', 'Anthony', 'Jackson', 'Nader, Feeney and Streich', '304 Crowley Drive', '973 Burrows Lane', 'Miami', 'Florida', 'United States', '33124', null, '1-(330)202-5559', 'ajackson7@newsvine.com'),
 (null, 't655hoevoB', 'Anne', 'Cruz', 'Botsford, O''Reilly and Keeling', '1 Sherman Park', '53 Starling Junction', 'Philadelphia', 'Pennsylvania', 'United States', '19131', '1-(215)360-3013', '1-(512)938-7334', 'acruz8@scribd.com'),
('Dr', '77vIJ6i7Dc', 'Michael', 'Burns', 'Murazik, Howe and Mraz', '624 Lakeland Point', null, 'Tampa', 'Florida', 'United States', '33615', '1-(813)636-9339', null, 'mburns9@tinypic.com'),
('Ms', 'Bt8foJeHq', 'Pamela', 'Coleman', 'Kiehn-Bernier', '683 Memorial Plaza', '60 Barby Center', 'Rochester', 'New York', 'United States', '14619', '1-(585)546-9983', null, 'pcolemana@trellian.com'),
 ('Ms', 'QiPZd7bfFZdd', 'George', 'Anderson', 'Jacobs-Oberbrunner', '14 Marcy Parkway', '319 Pawling Park', 'Abilene', 'Texas', 'United States', '79699', '1-(325)187-7904', '1-(623)232-4183', 'gandersonb@taobao.com'),
('Rev', '4TLUqDbDwp6', 'Christina', 'Little', 'Collier and Sons', '31 Laurel Road', null, 'Merrifield', 'Virginia', 'United States', '22119', '1-(571)760-4668', '1-(505)134-6441', 'clittlec@trellian.com'),
('Mr', 'h1GKM7y217W', 'Cheryl', 'Ellis', 'Upton-Little', '5698 Petterle Hill', null, 'Saint Louis', 'Missouri', 'United States', '63116', null, '1-(907)800-0000', 'cellisd@chronoengine.com'), (null, 'ujpkswbM7lzJ', 'Karen', 'Perkins', 'Hettinger LLC', '7059 Morning Terrace', '8440 Anhalt Place', 'Winter Haven', 'Florida', 'United States', '33884', null, null, 'kperkinse@yolasite.com'), ('Honorable', 'RpaE0Bl', 'Alice', 'Ward', 'Wunsch-Powlowski', '7666 Starling Plaza', null, 'Bronx', 'New York', 'United States', '10464', '1-(917)791-8229', '1-(303)377-4604', 'awardf@printfriendly.com'),
 ('Rev', 'e4fcAegiCKQR', 'Andrea', 'Martin', 'Stehr and Sons', '62 Hooker Way', null, 'Sacramento', 'California', 'United States', '95823', '1-(916)717-3929', '1-(202)605-9832', 'amarting@irs.gov'),
 (null, 'wZxL3TduKhm', 'Alan', 'Day', 'Quigley-Kovacek', '2705 Dorton Plaza', '87 Vahlen Parkway', 'Plano', 'Texas', 'United States', '75074', '1-(214)959-3482', '1-(717)326-9814', 'adayh@spiegel.de'),
 ('Ms', 'oDhG3tH8', 'Nicole', 'Holmes', 'Torp-Murazik', '13134 Granby Junction', '15527 Grim Park', 'Reno', 'Nevada', 'United States', '89505', '1-(775)983-8576', '1-(260)414-8310', 'nholmesi@slashdot.org'),
 ('Dr', 'WRIyaLL', 'Debra', 'Fuller', 'Crooks-Gerhold', '38354 Farmco Way', '66709 Maywood Lane', 'Anderson', 'Indiana', 'United States', '46015', '1-(765)360-8539', '1-(763)574-6127', 'dfullerj@usgs.gov');

INSERT INTO invoices(id,client_id,sale_date,net_value,pst,gst,hst,gross_value)
VALUES
(1,5,'2016-12-17',7,0.7,0.35,0,8.05),
(2,10,'2016-11-29',1.65,0.08,0.16,0,1.89),
(3,7,'2016-12-26',8.63,0.43,0.86,0,9.92),
(4,8,'2017-02-07',18.39,0,0,2.21,20.60),
(5,2,'2016-11-28',5.95,0,0,0.60,6.55),
(6,19,'2016-10-14',0.90,0,0,0.09,0.99);


INSERT INTO invoice_details_songs (sale_id,inventory_id,sold_price)
VALUES 
(2,17,0.7),
(2,58,0.95),
(3,20,0.7),
(3,120,0.99),
(3,124,0.99),
(4,37,0.7),
(4,24,0.7),
(6,90,0.9);

INSERT INTO invoice_details_album(sale_id,inventory_id,sold_price)
VALUES
(1,1,7),
(3,9,5.95),
(4,12,9.99),
(4,4,7),
(5,9,5.95);
INSERT INTO survey_questions(question,current_question) 
VALUES ('What is your favorite genre?',TRUE);

INSERT INTO survey_choices(question_id,choice, vote) VALUES (1,'Punk',0), (1,'Rock',1), (1,'Pop',2);

--  First invoice contains: 
-- 1 album (1) Never Mind the Bollocks”, “Here's the Sex Pistols 
-- net_value = 7.00 
-- gross_value = 8.05
-- gst = 0.35
-- pst = 0.70
-- hst = 0
-- 
-- Second invoice contains:
-- 2 tracks (17) Spanish Bombs & (58) Can't We Be Friends?
-- 1.65 sale 
-- 0.46 cost
-- net_value = 1.65
-- gross_value = 1.89
-- gst = 0.08
-- pst = 0.16
-- hst = 0
-- 
-- third invoice contains:
-- 1 album 
-- (9) Led Zepplin 5.95
-- 3 songs 
-- (20) Clampdown 0.70
-- (120) Sidewalks 0.99
-- (124) Attention 0.99
-- 2.68
-- net_value = 8.63
-- gst =  0.43
-- pst = 0.86
-- hst = 0
-- gross_value = 9.92
-- 
-- fourth invoice contains
-- 2 albums
-- (12)Starboy 9.99
-- (4) Monk's Dream 7.00
-- 2 tracks 
-- (24) Koka kola 0.70
-- (37) Just a Gigolo 0.70
-- net_value = 18.39
-- gst = 0
-- pst = 0
-- hst = 2.21
-- gross_value = 20.60
-- 
-- fifth invoice contains:
-- 1 album 
-- (9) Led Zepplin 5.95
-- net_value = 5.95
-- gst =  0
-- pst = 0
-- hst = 0.60
-- gross_value = 6.55
-- 
-- 
-- sixth invoice contains:
-- 1 track 
-- (90) Stairway to Heaven
-- net_value = 0.90
-- gst =  0
-- pst = 0
-- hst = 0.09
-- gross_value = 0.99


insert into reviews (id, inventory_id, client_id, rating, review_text) 
values (1, 10, 10, 5, 'Sed ante. Vivamus tortor. Duis mattis egestas metus.'),
(2, 8, 8, 5, 'Morbi non quam nec dui luctus rutrum. Nulla tellus. In sagittis dui vel nisl. Duis ac nibh. Fusce lacus purus, aliquet at, feugiat non, pretium quis, lectus.'),
(3, 6, 1, 3, 'Pellentesque eget nunc.'),
 (4, 10, 9, 3, 'In blandit ultrices enim. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Proin interdum mauris non ligula pellentesque ultrices.'),
 (5, 5, 7, 3, 'Lorem ipsum dolor sit amet, consectetuer adipiscing elit.'),
(6, 7, 1, 4, 'Sed sagittis. Nam congue, risus semper porta volutpat, quam pede lobortis ligula, sit amet eleifend pede libero quis orci. Nullam molestie nibh in lectus.'),
 (7, 6, 8, 3, 'Donec ut mauris eget massa tempor convallis. Nulla neque libero, convallis eget, eleifend luctus, ultricies eu, nibh. Quisque id justo sit amet sapien dignissim vestibulum. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nulla dapibus dolor vel est.'),
 (8, 8, 5, 5, 'In congue.'),
 (9, 5, 1, 3, 'Aenean auctor gravida sem. Praesent id massa id nisl venenatis lacinia. Aenean sit amet justo.'),
 (10, 2, 2, 1, 'Integer ac neque. Duis bibendum. Morbi non quam nec dui luctus rutrum.');

insert into rss (feed, current_rss) 
VALUES ('http://rss.cbc.ca/lineup/arts.xml', true),
('https://twitrss.me/twitter_user_to_rss/?user=omniprof', false);

insert into banner_ad (company, banner_pic, display) 
VALUES ('http://www.gibson.com/', 'resources/banners/20170408043910-GibsonBanner.jpg', true),
('http://www.google.com/', 'resources/banners/20170408043910-GoogleBanner.jpg', false) ;
