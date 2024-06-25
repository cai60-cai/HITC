-- 创建 user 表
CREATE TABLE user (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL
);
ALTER TABLE user ADD COLUMN avatar VARCHAR(255);

-- 创建 friend_request 表
CREATE TABLE friend_request (
                                id INT AUTO_INCREMENT PRIMARY KEY,
                                sender_id INT NOT NULL,
                                receiver_id INT NOT NULL,
                                accepted BOOLEAN DEFAULT FALSE,
                                FOREIGN KEY (sender_id) REFERENCES user(id),
                                FOREIGN KEY (receiver_id) REFERENCES user(id)
);

-- 添加 sender_username 列
ALTER TABLE friend_request ADD COLUMN sender_username VARCHAR(255);

-- 创建 message 表
CREATE TABLE message (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         sender_id INT NOT NULL,
                         receiver_id INT NOT NULL,
                         content TEXT NOT NULL,
                         timestamp DATETIME NOT NULL,
                         FOREIGN KEY (sender_id) REFERENCES user(id),
                         FOREIGN KEY (receiver_id) REFERENCES user(id)
);

-- 创建 friend 表
CREATE TABLE friend (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        user_id INT NOT NULL,
                        friend_id INT NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES user(id),
                        FOREIGN KEY (friend_id) REFERENCES user(id)
);
