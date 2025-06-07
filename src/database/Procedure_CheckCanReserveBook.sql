DELIMITER //

CREATE PROCEDURE Procedure_CheckCanReserveBook(
    IN p_account_id INT,
    IN p_book_id INT,
    OUT can_reserve INT
)
BEGIN
    DECLARE has_unreturned_book INT;

    SELECT COUNT(*) INTO has_unreturned_book
    FROM BorrowEntry be
    JOIN UserAccount ua ON be.BorrowerID = ua.BorrowerID
    WHERE ua.AccountID = p_account_id 
      AND be.BookID = p_book_id 
      AND be.ReturnDate IS NULL;

    IF has_unreturned_book > 0 THEN
        SET can_reserve = 0;
    ELSE
        SET can_reserve = 1;
    END IF;
END //

DELIMITER ;
