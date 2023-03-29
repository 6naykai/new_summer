CREATE TABLE new_th_info (
   device_id VARCHAR(20) PRIMARY KEY,
   temperature VARCHAR(20) NOT NULL,
   humidity VARCHAR(20) NOT NULL,
   recode_date DATETIME NOT NULL
);

CREATE TABLE device (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    device_id VARCHAR(20) NOT NULL
);

CREATE INDEX user_id_index ON device(user_id);

CREATE TABLE th_info (
    id INT PRIMARY KEY AUTO_INCREMENT,
    temperature VARCHAR(20) NOT NULL,
    humidity VARCHAR(20) NOT NULL
);

CREATE TABLE file_resource_info (
    id INT PRIMARY KEY AUTO_INCREMENT,
    file_name VARCHAR(200) NOT NULL,
    file_type ENUM('.txt','.pdf','.png','.jpeg','.doc','.docx') NOT NULL,
    upload_time DATETIME NOT NULL,
    download_times INT NOT NULL
);

CREATE TABLE user_info (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(200) UNIQUE NOT NULL,
    password VARCHAR(200) NOT NULL,
    create_date DATETIME NOT NULL,
    update_date DATETIME NOT NULL,
    level ENUM('0','1')
);

CREATE TABLE essay(
id int primary key auto_increment,
my_project varchar(200) not null,
my_assay varchar(200) not null,
owner varchar(20) not null,
score int not null
);

CREATE TABLE black_list (
id int primary key auto_increment,
ban_user_id int
);



