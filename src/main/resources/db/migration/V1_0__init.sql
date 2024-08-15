create table limits (
	id bigserial primary key,
	client_id bigint not null unique,
	current_limit numeric(38,2),
	reserved_sum numeric(38,2),
	daily_limit numeric(38,2)
);


create table settings(
  id bigserial primary key,
  key varchar(50) unique,
  value varchar(255)
);

insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (1, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (2, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (3, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (4, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (5, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (6, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (7, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (8, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (9, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (10, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (11, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (12, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (13, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (14, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (15, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (16, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (17, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (18, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (19, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (20, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (21, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (22, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (23, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (24, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (25, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (26, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (27, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (28, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (29, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (30, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (31, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (32, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (33, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (34, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (35, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (36, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (37, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (38, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (39, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (40, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (41, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (42, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (43, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (44, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (45, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (46, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (47, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (48, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (49, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (50, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (51, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (52, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (53, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (54, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (55, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (56, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (57, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (58, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (59, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (60, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (61, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (62, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (63, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (64, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (65, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (66, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (67, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (68, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (69, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (70, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (71, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (72, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (73, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (74, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (75, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (76, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (77, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (78, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (79, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (80, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (81, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (82, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (83, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (84, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (85, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (86, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (87, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (88, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (89, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (90, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (91, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (92, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (93, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (94, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (95, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (96, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (97, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (98, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (99, 10000, 0, 10000);
insert into limits (client_id, current_limit, reserved_sum, daily_limit) values (100, 10000, 0, 10000);
