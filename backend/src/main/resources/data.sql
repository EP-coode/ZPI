INSERT INTO db_example.role (role_name, can_approve_post, can_create_post, can_read_post, post_per_day_limit)
VALUES
    ('ROLE_USER', false, true, true, 10),
    ('ROLE_ADMIN', true, true, true, -1);

--hasło: 123456
INSERT INTO db_example.user (user_id, avatar_url, email, email_confirmation_token, email_confirmed, password_hash,
                             refresh_token, student_status_confirmed, role_fk)
VALUES (1, '""', 'studentcommunityzpi@gmail.com', '""', true, '$2a$10$iEmh3dR5UGxLnVF0bv4cVeGnukOrDITftURUxVeHstQnCBeQhbUlK', '""', true, 'ROLE_ADMIN'),
       (2, '""', 'user@gmail.com', '""', true, '$2a$10$iEmh3dR5UGxLnVF0bv4cVeGnukOrDITftURUxVeHstQnCBeQhbUlK', '""', true, 'ROLE_USER');

INSERT INTO db_example.post_category_group (post_category_group_id, display_name, total_posts)
VALUES
    (1, 'Wydziały', 0),
    (2, 'Prowadzący', 0);

INSERT INTO db_example.post_category (post_category_id, display_name, total_posts,
                                      post_category_group_post_category_group_id)
VALUES
    (1, 'W-4N', 0, 1),
    (2, 'W-8', 0, 1),
    (3, 'W-11', 0, 1),
    (4, 'Matematycy', 0, 2),
    (5, 'Fizycy', 0, 2),
    (6, 'Chemicy', 0, 2);

INSERT INTO db_example.post (post_id, approve_time, creation_time, image_url, markdown_content, title, total_dislikes,
                             total_likes, approver_id_fk, post_category_id_fk, creator_id_fk)
VALUES
    (1, current_date, current_date, '""', '# Jakiś bardzo interesujący content
**Lorem Ipsum** is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
## pod tytuł
I tyle w temacie.', 'Tytuł 1', 10, 100, 1, 1, 1),
    (2, current_date, current_date - 1, '""', '# Jakiś bardzo interesujący content
**Lorem Ipsum** is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
## pod tytuł
I tyle w temacie.', 'Tytuł 2 (dłuższy tytuł)', 100, 1000, 1, 5, 1);