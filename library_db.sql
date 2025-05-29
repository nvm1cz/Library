USE library_db;

CREATE TABLE students (
    studentNumber VARCHAR(100) NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);

INSERT INTO students (studentNumber, password) VALUES ('1234', '1234');


CREATE TABLE book (
    bookTitle VARCHAR(100),
    author VARCHAR(100),
    bookType VARCHAR(100),
    image VARCHAR(500),
    date DATE
);

INSERT INTO book (bookTitle, author, bookType, image, date)
VALUES 
('Programming Language', 'Thumbnail_01', 'Non-fiction', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\programming language book.jpg', '2025-01-01'),
('JavaFx', 'Thumbnail_02', 'Non-fiction', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\javafx tutorial book.jpg', '2024-12-15'),
('C\# Tutorial', 'Thumbnail_03', 'Non-fiction', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\c\#  tutorial book.jpg', '2024-12-16'),
('Java Tutorial', 'Thumbnail_04', 'Non-fiction', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\java tutorial.jpg', '2024-12-17'),
('Python Tutorial', 'Thumbnail_05', 'Non-fiction', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\python tutorial.jpg', '2024-05-12');

CREATE TABLE take (
    studentNumber VARCHAR(100) NOT NULL,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    gender VARCHAR(100) NOT NULL,
    bookTitle VARCHAR(100) NOT NULL,
    image VARCHAR(500) NOT NULL,
    date DATE DEFAULT NULL,
    checkReturn VARCHAR(100) NOT NULL
);

CREATE TABLE admin (
    username VARCHAR(100) NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);

INSERT INTO admin (username, password) VALUES ('admin', 'admin123');