INSERT INTO db_example.role (
        role_name,
        can_approve_post,
        can_create_post,
        can_read_post,
        post_per_day_limit
    )
VALUES ('ROLE_USER', false, true, true, 10),
    ('ROLE_ADMIN', true, true, true, -1) ON DUPLICATE KEY
UPDATE role_name = role_name,
    can_approve_post=can_approve_post,
    can_create_post=can_create_post,
    can_read_post=can_read_post,
    post_per_day_limit=post_per_day_limit;
--update roles

--hasło: 123456
--on duplicate skip
INSERT INTO db_example.user (user_id, avatar_url, email, name, email_confirmation_token, email_confirmed, password_hash,
                             refresh_token, student_status_confirmed, role_fk)
VALUES (1, null, 'studentcommunityzpi@gmail.com', 'admin123', null, true, '$2a$10$iEmh3dR5UGxLnVF0bv4cVeGnukOrDITftURUxVeHstQnCBeQhbUlK', '""', true, 'ROLE_ADMIN'),
       (2, 'avatar_user@gmail.com.jpg', 'user@gmail.com', 'user123', null, true, '$2a$10$iEmh3dR5UGxLnVF0bv4cVeGnukOrDITftURUxVeHstQnCBeQhbUlK', '""', true, 'ROLE_USER')
ON DUPLICATE KEY UPDATE user_id=user_id;

INSERT INTO db_example.post_category_group (display_name, total_posts)
VALUES
    ('Wydziały', 1),
    ('Prowadzący', 2)
ON DUPLICATE KEY UPDATE display_name=display_name;

INSERT INTO db_example.post_category (display_name, total_posts,
                                      post_category_group_id_fk)
VALUES
    ('W-4N', 1, 'Wydziały'),
    ('W-8', 0, 'Wydziały'),
    ('W-11', 0, 'Wydziały'),
    ('Matematycy', 0, 'Prowadzący'),
    ('Fizycy', 2, 'Prowadzący'),
    ('Chemicy', 0, 'Prowadzący')
ON DUPLICATE KEY UPDATE display_name=display_name;

--@BLOCK
INSERT INTO db_example.post_tag (tag_name, total_posts) VALUES
    ('Jedzenie', 2),
    ('Jeremiasz', 1),
    ('Jarmark', 2)
ON DUPLICATE KEY UPDATE tag_name=tag_name;

INSERT INTO db_example.post (post_id, approve_time, creation_time, image_url, markdown_content, title, total_dislikes,
                             total_likes, approver_id_fk, post_category_id_fk, creator_id_fk)
VALUES
    (0, current_date, current_date, null, '# Jakiś bardzo interesujący content
**Lorem Ipsum** is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
## pod tytuł
I tyle w temacie.', 'Tytuł 1', 10, 100, 1, 'W-4N', 1),
    (1, current_date, current_date - 1, null, '# Jakiś bardzo interesujący content
**Lorem Ipsum** is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
## pod tytuł
I tyle w temacie.', 'Tytuł 2 (dłuższy tytuł)', 100, 1000, 1, 'Fizycy', 1)
,
    (2, current_date, current_date - 2, "post_1337_png", '# Jakiś bardzo interesujący content
**Lorem Ipsum** is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry''s standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book.
## pod tytuł
I tyle w temacie.', 'Tytuł 2 (dłuższy tytuł)', 100, 1000, 1, 'Fizycy', 1)
ON DUPLICATE KEY UPDATE post_id=post_id;

INSERT INTO db_example.post_post_tag (post_id, tag_name) VALUES
    (0,'Jedzenie'),
    (1,'Jeremiasz'),
    (2,'Jarmark'),
    (2,'Jedzenie'),
    (1,'Jarmark')
ON DUPLICATE KEY UPDATE post_id=post_id;
