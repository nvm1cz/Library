USE library_db;

CREATE TABLE students (
    studentNumber VARCHAR(100) NOT NULL PRIMARY KEY,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE book (
    bookTitle VARCHAR(100),
    author VARCHAR(100),
    bookType VARCHAR(100),
    image VARCHAR(500),
    date DATE,
    quantity INT DEFAULT 10
);

CREATE TABLE take (
    studentNumber VARCHAR(100) NOT NULL,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    bookTitle VARCHAR(100) NOT NULL,
    image VARCHAR(500) NOT NULL,
    date DATE DEFAULT NULL,
    checkReturn VARCHAR(100) NOT NULL
);

CREATE TABLE admin (
    username VARCHAR(100) NOT NULL PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);

INSERT INTO admin (username, password) VALUES ('admin', '123');

INSERT INTO students (studentNumber, firstName, lastName, password) VALUES 
('1', 'John', 'Doe', '1'),
('2', 'Jane', 'Smith', '1'),
('3', 'Mike', 'Johnson', '1'),
('4', 'Sarah', 'Williams', '1'),
('5', 'David', 'Brown', '1'),
('6', 'Emily', 'Davis', '1'),
('7', 'James', 'Miller', '1'),
('8', 'Emma', 'Wilson', '1'),
('9', 'Daniel', 'Taylor', '1'),
('10', 'Sophia', 'Anderson', '1'),
('11', 'Oliver', 'Thomas', '1');

INSERT INTO book (bookTitle, author, bookType, image, date, quantity)
VALUES 
('Programming Language', 'Thumbnail_01', 'Non-fiction', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\programming language book.jpg', '2025-01-01', 10),
('JavaFx', 'Thumbnail_02', 'Non-fiction', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\javafx tutorial book.jpg', '2024-12-15', 10),
('C# Tutorial', 'Thumbnail_03', 'Non-fiction', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\c#  tutorial book.jpg', '2024-12-16', 10),
('Java Tutorial', 'Thumbnail_04', 'Non-fiction', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\java tutorial.jpg', '2024-12-17', 10),
('Python Tutorial', 'Thumbnail_05', 'Non-fiction', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\python tutorial.jpg', '2024-05-12', 10),
('Data Structures and Algorithms', 'Robert Smith', 'Academic', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\dsa.jpg', '2024-06-15', 10),
('Web Development Basics', 'Sarah Johnson', 'Technical', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\web_dev.jpg', '2024-07-20', 10),
('Database Management Systems', 'Michael Brown', 'Academic', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\dbms.jpg', '2024-08-10', 10),
('Artificial Intelligence', 'David Wilson', 'Technical', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\ai.jpg', '2024-09-05', 10),
('Mobile App Development', 'Emily Davis', 'Technical', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\mobile_dev.jpg', '2024-10-15', 10),
('Software Engineering', 'James Anderson', 'Academic', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\software_eng.jpg', '2024-11-20', 10),
('Cloud Computing', 'Lisa Thompson', 'Technical', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\cloud.jpg', '2024-12-01', 10),
('Cybersecurity Fundamentals', 'Mark Taylor', 'Technical', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\security.jpg', '2025-01-10', 10),
('Machine Learning Basics', 'Jennifer White', 'Technical', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\ml.jpg', '2025-02-15', 10),
('Network Programming', 'Kevin Martin', 'Technical', 'C:\\Users\\Legion\\OneDrive\\Máy tính\\LibraryManagement\\Library\\src\\view\\image\\network.jpg', '2025-03-01', 10);
