



DELIMITER //

CREATE PROCEDURE Procedure_ApproveReservation(IN reservation_id INT)
BEGIN
    DECLARE v_book_id INT;
    DECLARE v_account_id INT;
    DECLARE v_available_copies INT;
    DECLARE v_reservation_date DATETIME;

    -- Get book ID, account ID, and reservation date from reservation
    SELECT BookID, AccountID, ReservationDate INTO v_book_id, v_account_id, v_reservation_date
    FROM Reservation
    WHERE ReservationID = reservation_id AND Status = 'Pending';

    -- Get available copies
    SELECT AvailableCopies INTO v_available_copies
    FROM Book
    WHERE BookID = v_book_id;

    -- Only proceed if there are copies left
    IF v_available_copies > 0 THEN

        -- Approve this reservation
        UPDATE Reservation
        SET Status = 'Fulfilled'
        WHERE ReservationID = reservation_id;

        -- Cancel all other pending reservations for this book (requested after this one)
        UPDATE Reservation
        SET Status = 'Canceled'
        WHERE BookID = v_book_id
          AND Status = 'Pending'
          AND ReservationDate > v_reservation_date;

        -- Notify accepted user
        INSERT INTO Notification(AccountID, Message)
        VALUES (v_account_id, CONCAT('Your reservation for Book ID ', v_book_id, ' has been approved.'));

        -- Notify canceled users
        INSERT INTO Notification(AccountID, Message)
        SELECT AccountID, CONCAT('Your reservation for Book ID ', v_book_id, ' was canceled due to lack of copies.')
        FROM Reservation
        WHERE BookID = v_book_id AND Status = 'Canceled';

    END IF;
END //

DELIMITER ;