DELIMITER //

CREATE PROCEDURE AddOrUpdateBook(
    IN book_title VARCHAR(200),
    IN copies_to_add INT
)
BEGIN
    DECLARE existing_id INT;

    -- Kiểm tra sách đã tồn tại chưa
    SELECT BookID INTO existing_id
    FROM Book
    WHERE Title = book_title
    LIMIT 1;

    IF existing_id IS NOT NULL THEN
        -- Sách đã có: cập nhật số lượng
        UPDATE Book
        SET 
            TotalCopies = TotalCopies + copies_to_add,
            AvailableCopies = AvailableCopies + copies_to_add
        WHERE BookID = existing_id;
    ELSE
        -- Sách chưa có: thêm mới
        INSERT INTO Book (Title, TotalCopies, AvailableCopies)
        VALUES (book_title, copies_to_add, copies_to_add);
    END IF;
END //

DELIMITER ;
