MERGE INTO MPA_RATING AS target
USING (SELECT 1 AS ID, 'G' AS NAME) AS source_table
ON (target.ID = source_table.ID OR target.NAME = source_table.NAME)
WHEN MATCHED
THEN UPDATE SET
target.NAME = source_table.NAME
WHEN NOT MATCHED
THEN INSERT (ID, NAME)
VALUES (source_table.ID, source_table.NAME);

MERGE INTO MPA_RATING AS target
USING (SELECT 2 AS ID, 'PG' AS NAME) AS source_table
ON (target.ID = source_table.ID OR target.NAME = source_table.NAME)
WHEN MATCHED
THEN UPDATE SET
target.NAME = source_table.NAME
WHEN NOT MATCHED
THEN INSERT (ID, NAME)
VALUES (source_table.ID, source_table.NAME);

MERGE INTO MPA_RATING AS target
USING (SELECT 3 AS ID, 'PG-13' AS NAME) AS source_table
ON (target.ID = source_table.ID OR target.NAME = source_table.NAME)
WHEN MATCHED
THEN UPDATE SET
target.NAME = source_table.NAME
WHEN NOT MATCHED
THEN INSERT (ID, NAME)
VALUES (source_table.ID, source_table.NAME);

MERGE INTO MPA_RATING AS target
USING (SELECT 4 AS ID, 'R' AS NAME) AS source_table
ON (target.ID = source_table.ID OR target.NAME = source_table.NAME)
WHEN MATCHED
THEN UPDATE SET
target.NAME = source_table.NAME
WHEN NOT MATCHED
THEN INSERT (ID, NAME)
VALUES (source_table.ID, source_table.NAME);

MERGE INTO MPA_RATING AS target
USING (SELECT 5 AS ID, 'NC-17' AS NAME) AS source_table
ON (target.ID = source_table.ID OR target.NAME = source_table.NAME)
WHEN MATCHED
THEN UPDATE SET
target.NAME = source_table.NAME
WHEN NOT MATCHED
THEN INSERT (ID, NAME)
VALUES (source_table.ID, source_table.NAME);

MERGE INTO GENRE AS target
USING (SELECT 1 AS ID, 'Комедия' AS NAME) AS source_table
ON (target.ID = source_table.ID OR target.NAME = source_table.NAME)
WHEN MATCHED
THEN UPDATE SET
target.NAME = source_table.NAME
WHEN NOT MATCHED
THEN INSERT (ID, NAME)
VALUES (source_table.ID, source_table.NAME);

MERGE INTO GENRE AS target
USING (SELECT 2 AS ID, 'Драма' AS NAME) AS source_table
ON (target.ID = source_table.ID OR target.NAME = source_table.NAME)
WHEN MATCHED
THEN UPDATE SET
target.NAME = source_table.NAME
WHEN NOT MATCHED
THEN INSERT (ID, NAME)
VALUES (source_table.ID, source_table.NAME);

MERGE INTO GENRE AS target
USING (SELECT 3 AS ID, 'Мультфильм' AS NAME) AS source_table
ON (target.ID = source_table.ID OR target.NAME = source_table.NAME)
WHEN MATCHED
THEN UPDATE SET
target.NAME = source_table.NAME
WHEN NOT MATCHED
THEN INSERT (ID, NAME)
VALUES (source_table.ID, source_table.NAME);

MERGE INTO GENRE AS target
USING (SELECT 4 AS ID, 'Триллер' AS NAME) AS source_table
ON (target.ID = source_table.ID OR target.NAME = source_table.NAME)
WHEN MATCHED
THEN UPDATE SET
target.NAME = source_table.NAME
WHEN NOT MATCHED
THEN INSERT (ID, NAME)
VALUES (source_table.ID, source_table.NAME);

MERGE INTO GENRE AS target
USING (SELECT 5 AS ID, 'Документальный' AS NAME) AS source_table
ON (target.ID = source_table.ID OR target.NAME = source_table.NAME)
WHEN MATCHED
THEN UPDATE SET
target.NAME = source_table.NAME
WHEN NOT MATCHED
THEN INSERT (ID, NAME)
VALUES (source_table.ID, source_table.NAME);

MERGE INTO GENRE AS target
USING (SELECT 6 AS ID, 'Боевик' AS NAME) AS source_table
ON (target.ID = source_table.ID OR target.NAME = source_table.NAME)
WHEN MATCHED
THEN UPDATE SET
target.NAME = source_table.NAME
WHEN NOT MATCHED
THEN INSERT (ID, NAME)
VALUES (source_table.ID, source_table.NAME);

MERGE INTO FRIENDSHIP_STATUS AS target
USING (SELECT 1 AS ID, 'Не подтверждена' AS NAME, 'Дружба ещё не подтверждена другим пользователем' AS DESCRIPTION) AS source_table
ON (target.ID = source_table.ID)
WHEN MATCHED
THEN UPDATE SET
target.NAME = source_table.NAME,
target.DESCRIPTION = source_table.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, NAME, DESCRIPTION)
VALUES (source_table.ID, source_table.NAME, source_table.DESCRIPTION);

MERGE INTO FRIENDSHIP_STATUS AS target
USING (SELECT 2 AS ID, 'Подтверждена' AS NAME, 'Дружба подтверждена обоими пользователями' AS DESCRIPTION) AS source_table
ON (target.ID = source_table.ID)
WHEN MATCHED
THEN UPDATE SET
target.NAME = source_table.NAME,
target.DESCRIPTION = source_table.DESCRIPTION
WHEN NOT MATCHED
THEN INSERT (ID, NAME, DESCRIPTION)
VALUES (source_table.ID, source_table.NAME, source_table.DESCRIPTION);