DROP DATABASE IF EXISTS CustomEmailClient;

CREATE DATABASE CustomEmailClient;

USE CustomEmailClient;

CREATE TABLE email
(
    email_id    INT NOT NULL AUTO_INCREMENT,
    sender_id   INT NOT NULL,
    subject     NVARCHAR(255) NOT NULL,
    mailbox     NVARCHAR(10) NOT NULL,
    mark        NVARCHAR(10) NOT NULL,
    date_time   DATETIME NOT NULL,
    body        LONGTEXT CHARACTER SET utf8 NOT NULL,

    PRIMARY KEY (email_id)
);

CREATE TABLE user
(
    user_id             INT NOT NULL AUTO_INCREMENT,
    first_name          NVARCHAR(255) NOT NULL,
    last_name           NVARCHAR(255) NOT NULL,
    email_address       NVARCHAR(255) NOT NULL,
    is_in_address_book  BOOLEAN NOT NULL DEFAULT 0,

    PRIMARY KEY (user_id)
);

CREATE TABLE email_user
(
    email_id    INT NOT NULL,
    user_id     INT NOT NULL,

    PRIMARY KEY (email_id, user_id),

    FOREIGN KEY (email_id)  REFERENCES email (email_id),
    FOREIGN KEY (user_id)   REFERENCES user (user_id)
);



-- Set up values for email table

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (2, 'No action required', 'Inbox', 'UNMARKED', '2018-02-12 08:09:40', 'Don''t mind me, I''m just testing');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (3, 'Prima Aprilis', 'Inbox', 'UNMARKED', '2018-04-01 08:00:00', 'April Fools!');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (4, 'Whatever', 'Inbox', 'UNMARKED', '2018-05-07 21:58:13', 'Yeah, whatever');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (5, 'Una bella ragazza', 'Inbox', 'MARKED', '2018-05-07 21:59:01', 'Buongiorno,

Ho incontrato una bella ragazza!

I migliori saluti,
Crazy Figo');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (6, 'Thank you for signing up!', 'Inbox', 'UNMARKED', '2018-05-17 17:10:46', 'Hello Simple,

Thank you for signing up at https://stackoverflow.com/
Hopefully you''ll find here an answer to your every question!

Cheers,
Stack Overflow Team');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (7, 'Stop spam', 'Deleted', 'UNMARKED', '2018-06-06 11:06:19', 'Hello,

Please stop spaming.

Best regards,
Tester');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (9, 'Special wish accepted', 'Saved', 'UNMARKED', '2018-06-06 21:06:50', 'Reservation number: 45685197895
Your special wishes regarding this reservation has been accepted!

Enjoy your stay!
Booking.com Team');
INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (8, 'E-mail removal', 'Inbox', 'UNMARKED', '2018-06-06 21:06:50', 'Hello,

I kindly ask you to remove this e-mail.

Best regards,
Michael Green');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (10, 'Meeting', 'Inbox', 'MARKED', '2018-08-10 08:09:40', 'Hello,

How are you? Will you attend our meeting?

Best regards,
Bill Gates');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (11, 'Yet another unnecessary e-mail', 'Inbox', 'UNMARKED', '2018-08-11 09:10:41', 'CONGRATULATIONS! YOU ARE OUR 500 CLIENT! GET YOUR FREE IPHONE NOW!!!');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (12, 'Trending', 'Inbox', 'MARKED', '2018-08-19 19:12:43', 'Hello Simple,

Take a look what''s currently trending in Technology!
https://trending.com/technology

Cheers!
Tech Support');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (13, 'E-mail removal', 'Inbox', 'UNMARKED', '2018-09-11 23:10:13', 'Hello,

Please remove this e-mail.Please remove this e-mail.Please remove this e-mail.Please remove this e-mail.Please remove this e-mail.Please remove this e-mail.Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.
Please remove this e-mail.

Best regards,
Some Employee');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (14, 'Open to new job opportunities?', 'Inbox', 'MARKED', '2018-11-30 11:10:09', 'Hello,

I would like to present you our cool offers. Are you currently looking for a new job?
If so, please don''t hesitate to contact me.

Regards,
Caroline Smith');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (15, 'Don''t mind me, just testing long e-mails', 'Inbox', 'UNREAD', '2018-12-10 09:19:44', 'Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails
Don''t mind me, just testing long e-mails');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (1, 'Please change your surname', 'Sent', 'UNMARKED', '2018-12-11 11:34:17', 'Hello,

Please change your surname so people won''t confuse us.

Best regards,
Simple Receiver');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (1, 'Spam stop', 'Draft', 'UNMARKED', '2018-12-11 11:36:49', 'Hello');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (17, 'SPAM', 'Inbox', 'UNREAD', '2018-12-12 12:12:12', 'SPAM
SPAM');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (18, 'Did you hear about Louie Miller?', 'Inbox', 'UNREAD', '2018-12-31 07:08:51', 'He disappeared, babe!');

INSERT INTO email (sender_id, subject, mailbox, mark, date_time, body)
VALUES (1, '', 'Draft', 'UNMARKED', '2019-02-14 20:13:10', '');



-- Set up values for user table

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Simple', 'User', 'simple.user@yahoo.com', false);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Another', 'Tester', 'another.tester@gmail.com', false);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Prima', 'Aprilis', 'prima@aprilis.pl', false);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Whatever', '', 'whatever@yahoo.com', true);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Crazy', 'Figo', 'crazy.figo@italiano.it', true);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Stack', 'Overflow', 'simple.user@yahoo.com', false);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Tester', '', 'test@test.com', true);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Michael', 'Green', 'michael.green@gmail.com', true);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Customer', 'Service', 'customer.service@booking.com', true);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Bill', 'Gates', 'bill.gates@microsoft.com', true);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Special', 'Offers', 'special.offers@spam.com', false);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Tech', 'Support', 'techsupport@company.com', true);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Some', 'Employee', 'some.employee@company.com', true);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Caroline', 'Smith', 'caroline.smith@recruitments.com', true);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Long', 'Emails', 'long.emails@gmail.com', true);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Another', 'Receiver', 'another.receiver@yahoo.com', true);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Spam', '', 'spamer@spam.com', false);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('Mack', 'the Knife', 'mack.knife@jazz.com', true);

INSERT INTO user (first_name, last_name, email_address, is_in_address_book)
VALUES ('', '', 'no.name@o2.pl', false);



-- Set up values for email_user table

INSERT INTO email_user (email_id, user_id)
VALUES (1, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (2, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (3, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (4, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (5, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (6, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (6, 16);

INSERT INTO email_user (email_id, user_id)
VALUES (7, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (8, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (9, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (10, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (11, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (12, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (13, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (14, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (15, 16);

INSERT INTO email_user (email_id, user_id)
VALUES (16, 7);

INSERT INTO email_user (email_id, user_id)
VALUES (17, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (17, 16);

INSERT INTO email_user (email_id, user_id)
VALUES (18, 1);

INSERT INTO email_user (email_id, user_id)
VALUES (19, 19);