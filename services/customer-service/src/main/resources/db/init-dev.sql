INSERT INTO dbo.customer (first_name, last_name, email, phone) VALUES ('John', 'Doe', 'john.doe@example.com', '1234567890');

INSERT INTO dbo.food_items (customer_id, food_item_id, preference_type, priority_level, created_date, updated_count) VALUES (1, 1, 'PREFERRED', 'ONE', '2024-06-26', 3);
INSERT INTO dbo.food_items (customer_id, food_item_id, preference_type, priority_level, created_date, updated_count) VALUES (1, 2, 'DISLIKED', 'FIVE', '2024-06-26', 4);

INSERT INTO dbo.customer (first_name, last_name, email, phone, premium_level) VALUES ('Jane', 'Doe', 'jane.doe@example.com', '0987654321', 'BASIC');
INSERT INTO dbo.food_items (customer_id, food_item_id, preference_type, priority_level, created_date, updated_count) VALUES (2, 1, 'PREFERRED', 'FIVE', '2024-06-26', 1);
INSERT INTO dbo.food_items (customer_id, food_item_id, preference_type, priority_level, created_date, updated_count) VALUES (2, 2, 'DISLIKED', 'ONE', '2024-06-26', 0);
INSERT INTO dbo.food_allergies (customer_id, food_allergy_id) VALUES (2, 1);
