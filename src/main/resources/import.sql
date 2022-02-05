INSERT INTO account (id, is_deleted, created_date, email, password, role, username) VALUES (1, 0, '2021-01-01', 'admin@gmail.com', '$2a$10$ssJg2MpBlaqyDWli4yzFJOLT9TSRZV.Jsxkdtv1fqhGmK2Tqzt0D6', 0, 'admin');

INSERT INTO customer_identity (id, is_deleted, created_date, address, first_name, identification_number, identity_category, last_name, account_id) VALUES (1, 0, '2021-01-01', 'Address', 'Admin', '12345678910', 0, 'Admin', 1);

INSERT INTO region (id, is_deleted, created_date, name) VALUES (1, 0, '2021-01-01', 'DKI Jakarta');
INSERT INTO region (id, is_deleted, created_date, name) VALUES (2, 0, '2021-01-01', 'Jawa Barat');

INSERT INTO city (id, is_deleted, created_date, name, region_id) VALUES (1, 0, '2021-01-01', 'Jakarta Pusat', 1);
INSERT INTO city (id, is_deleted, created_date, name, region_id) VALUES (2, 0, '2021-01-01', 'Jakarta Barat', 1);
INSERT INTO city (id, is_deleted, created_date, name, region_id) VALUES (3, 0, '2021-01-01', 'Jakarta Timur', 1);
INSERT INTO city (id, is_deleted, created_date, name, region_id) VALUES (4, 0, '2021-01-01', 'Jakarta Utara', 1);
INSERT INTO city (id, is_deleted, created_date, name, region_id) VALUES (5, 0, '2021-01-01', 'Jakarta Selatan', 1);
INSERT INTO city (id, is_deleted, created_date, name, region_id) VALUES (6, 0, '2021-01-01', 'Bandung', 2);

INSERT INTO company (id, is_deleted, name, city_id) VALUES (1, 0, 'Company Name', 1);

INSERT INTO contact_person (id, is_deleted, contact_number, first_name, last_name, position, company_id) VALUES (1, 0, '010101', 'FirstName', 'LastName', 'Manager', 1);
INSERT INTO hotel (id, is_deleted, about, name, city_id, company_id) VALUES (1, 0, 'about hotel', 'Hotel Name', 1, 1);

INSERT INTO room (id, is_deleted, about, number_of_room, price, room_type, hotel_id) VALUES (1, 0, 'about room', 3, 2000000, 'Deluxe', 1);

INSERT INTO booking (id, is_deleted, check_in_date, check_out_date, number_of_night, person_count, room_count, status, sub_total, booked_by, guest, room_id) VALUES (1, 0, '2021-01-10', '2021-01-15', 3, 2, 1, 0, 6000000, 1, 1, 1);
