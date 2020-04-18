insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ('vlada', '$2a$10$Ybft/Pph5.11ESjhvMCWnuyWliLcGlsKRRTPxbTtwlE00j31OZdf6', 'Vladimir', 'Cvetanovic', 'Novi Sad', 'Srbija', 'dovla@gmail.com', false, true, 'ADMIN');
insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ('vukasin', '$2a$10$3u5vuAP6GogQOyE5JQMTOOBIVB874JoCxtcE2s15H1JBVidraPUCW', 'Vukasin', 'Jovic', 'Novi Sad', 'Srbija', 'vukasin@gmail.com', false, true, 'EDITOR');
insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ('nikola', '$2a$04$TwATfW3iqzDzzrAKvT06fepL/hZ4CuhYwyFhsaEIeV0UuxO26QFqS', 'nikola', 'majstorovic', 'Novi Sad', 'Srbija', 'nikola@gmail.com', false, true, 'AUTHOR');

insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ('lazic', '$2a$10$LiTcSs1SsKYHcAjAQLejqe/VPI4YsxkHLEC8OaujOR6ShPM81sV4q', 'Milan', 'Lazic', 'Novi Sad', 'Srbija', 'lazic@gmail.com', false, true, 'REVIEWER');
insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ('djordjevic', '$2a$10$XbOz9hcDf83PMdfm1JeHRO9JwcMCtalZVC1oiCnbhT/0sVF4A8snC', 'Nikola', 'Djordjevic', 'Novi Sad', 'Srbija', 'djordjevic@gmail.com', false, true, 'REVIEWER');
insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ('malencic', '$2a$10$B/xUXG0aVTMB5Ppcvx6dO.ZdCYL.nSSNk01KWK2h7Wzo6ph9gJlu2', 'Nikola', 'Malencic', 'Novi Sad', 'Srbija', 'malencic@gmail.com', false, true, 'REVIEWER');

insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ('milica', '$2a$10$iHLUEnk5gYMa50EclC.cEu0UTBIM2wyNAloS/59yC.MN/cAe88NeS', 'Milica', 'Makaric', 'Novi Sad', 'Srbija', 'milica@gmail.com', false, true, 'EDITOR');
insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ('andrijana', '$2a$10$TWPesMnqNm66Z9vNd/b5UudKvezOzPiuYGjI36MvonEzfvUaN8FRq', 'Andrijana', 'Jeremic', 'Novi Sad', 'Srbija', 'andrijana@gmail.com', false, true, 'EDITOR');


insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type)
values ('marko', '$2a$10$W8fc03eWKZ3wBK/IN4.TaOaFA/cjG.G/9z79qnOqXmZv6ByTvv1kG', 'Marko', 'Stevanov', 'Novi Knezevac', 'Srbija', 'marko@gmail.com', false, true, 'AUTHOR');
--insert into user (username, password, first_name, last_name, city, country, email, is_reviewer, is_active, type, magazine_id)
--values ('fedor', '$2a$10$IAbZueonfhyWHGKtSCtDtuSkxN6qbiHj4tmCKvMUjSvWi8otZIIQq', 'Fedor', 'Markovljev', 'Novi Sad', 'Srbija', 'fedor@gmail.com', false, true, 'EDITOR', 1);

insert into role values (1, 'ROLE_USER');
insert into role values (2, 'ROLE_ADMIN');
insert into role values (3, 'ROLE_EDITOR');
insert into role values (4, 'ROLE_REVIEWER');
insert into role values (5, 'ROLE_AUTHOR');


insert into magazine (id, name, issn, billing_type, is_active, editor_id, is_registered) values (1, 'Naucni kutak', '111', 'READERS', true, 'milica', false);
insert into magazine (id, name, issn, billing_type, is_active, editor_id, is_registered) values (2, 'Nauka danas', '222', 'AUTHORS', true, 'andrijana', false);
insert into magazine (id, name, issn, billing_type, is_active, editor_id, is_registered, seller_id) values (3, 'Savremena psihologija', '333', 'AUTHORS', true, 'vukasin', true, 1);

insert into science_paper (id, title, key_term, paper_abstract, price, currency, magazine_id) values (1, 'Mape uma', 'Kljucni pojam', 'Abstrakt', 10, 'USD', 3);
insert into science_paper (id, title, key_term, paper_abstract, price, currency, magazine_id) values (2, 'Kako prihvatiti odgovornost?', 'Kljucni pojam', 'Abstrakt', 20, 'USD', 3);

insert into science_paper (id, title, key_term, paper_abstract, price, currency, magazine_id) values (3, 'Matematicka analiza', 'Kljucni pojam', 'Abstrakt', 20, 'USD', 1);
insert into science_paper (id, title, key_term, paper_abstract, price, currency, magazine_id) values (4, 'Algoritmi', 'Kljucni pojam', 'Abstrakt', 20, 'USD', 1);

insert into science_paper (id, title, key_term, paper_abstract, price, currency, magazine_id) values (5, 'Prepoznavanja objekata - Python', 'Kljucni pojam', 'Abstrakt', 20, 'USD', 2);
insert into science_paper (id, title, key_term, paper_abstract, price, currency, magazine_id) values (6, 'Globalno zagrevanje', 'Kljucni pojam', 'Abstrakt', 20, 'USD', 2);


insert into science_field values (1, 'Matematika');
insert into science_field values (2, 'Informatika');
insert into science_field values (3, 'Automatika');
insert into science_field values (4, 'Biologija');
insert into science_field values (5, 'Psihologija');

insert into magazine_sciencefield values (1,1);
insert into magazine_sciencefield values (1,2);
insert into magazine_sciencefield values (1,3);
insert into magazine_sciencefield values (2,1);
insert into magazine_sciencefield values (2,3);
insert into magazine_sciencefield values (3,5);



/* admin privilegije */
insert into privilege values (1, 'SET_REVIEWER_TASK');
insert into privilege values (2, 'SET_REVIEWER');

insert into roles_privileges values (2,1);
insert into roles_privileges values (2,2);

insert into user_roles values ('vlada',2);
insert into user_roles values ('vukasin', 3);
insert into user_roles values ('lazic', 4);
insert into user_roles values ('djordjevic', 4);
insert into user_roles values ('malencic', 4);
insert into user_roles values ('milica', 3);
insert into user_roles values ('andrijana', 3);
insert into user_roles values ('marko', 5);
--insert into user_roles values ('fedor', 3);

insert into user_sciencefields values ('vukasin', 1);
insert into user_sciencefields values ('vukasin', 2);
insert into user_sciencefields values ('vukasin', 3);

insert into user_sciencefields values ('lazic', 1);
insert into user_sciencefields values ('lazic', 4);
insert into user_sciencefields values ('lazic', 5);

insert into user_sciencefields values ('djordjevic', 1);
insert into user_sciencefields values ('djordjevic', 2);

insert into user_sciencefields values ('malencic', 1);
insert into user_sciencefields values ('malencic', 2);
insert into user_sciencefields values ('malencic', 3);
insert into user_sciencefields values ('malencic', 4);

insert into user_sciencefields values ('milica', 1);
insert into user_sciencefields values ('milica', 3);

insert into user_sciencefields values ('andrijana', 3);
insert into user_sciencefields values ('andrijana', 4);
insert into user_sciencefields values ('andrijana', 5);

insert into user_sciencefields values ('marko', 1);
insert into user_sciencefields values ('marko', 2);
insert into user_sciencefields values ('marko', 3);

--insert into user_sciencefields values ('fedor', 1);
--insert into user_sciencefields values ('fedor', 3);
--insert into user_sciencefields values ('fedor', 5);


--insert into memberships (magazine_id, author_username) values (2, 'marko');
insert into membership (end_date,magazine_id, author_username) values ('2020-05-03 10:08:02',2, 'nikola');





