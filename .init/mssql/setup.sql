-- Helper function to create database if not exists
CREATE PROCEDURE CreateDatabaseIfNotExists @DatabaseName NVARCHAR(128)
AS
BEGIN
    IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = @DatabaseName)
        BEGIN
            EXEC('CREATE DATABASE ' + @DatabaseName);
            PRINT 'Database ' + @DatabaseName + ' has been created.';
        END
    ELSE
        BEGIN
            PRINT 'Database ' + @DatabaseName + ' already exists.';
        END
END
GO

EXEC CreateDatabaseIfNotExists 'customer';
EXEC CreateDatabaseIfNotExists 'product';
EXEC CreateDatabaseIfNotExists 'order';
EXEC CreateDatabaseIfNotExists 'payment';

-- Cleanup
DROP PROCEDURE CreateDatabaseIfNotExists;
GO

DECLARE @MSSQL_USER NVARCHAR(50) = '$(MSSQL_USER)';
DECLARE @MSSQL_PASSWORD NVARCHAR(50) = '$(MSSQL_PASSWORD)';

-- Create login
DECLARE @CreateLoginSQL NVARCHAR(MAX) = N'CREATE LOGIN [' + @MSSQL_USER + N'] WITH PASSWORD = N' + QUOTENAME(@MSSQL_PASSWORD, '''') + N';';
EXEC sp_executesql @CreateLoginSQL;
GO

-- Grant sysadmin role to user
DECLARE @GrantSysAdminSQL NVARCHAR(MAX) = N'ALTER SERVER ROLE sysadmin ADD MEMBER [' + @MSSQL_USER + N'];';
EXEC sp_executesql @GrantSysAdminSQL;
GO

-- Create users and grant permissions for each database
DECLARE @DB_NAME NVARCHAR(50);
DECLARE @SQL NVARCHAR(MAX);

DECLARE DB_CURSOR CURSOR FOR
    SELECT name
    FROM sys.databases
    WHERE name IN ('customer', 'product', 'order', 'payment');

OPEN DB_CURSOR;
FETCH NEXT FROM DB_CURSOR INTO @DB_NAME;

DECLARE @MSSQL_USER NVARCHAR(50) = '$(MSSQL_USER)';
WHILE @@FETCH_STATUS = 0
    BEGIN
        SET @SQL = '
    USE [' + @DB_NAME + '];
    CREATE USER [' + @MSSQL_USER + '] FOR LOGIN [' + @MSSQL_USER + '];
    EXEC sp_addrolemember ''db_owner'', [' + @MSSQL_USER + '];';

        IF @DB_NAME = 'sonar'
            BEGIN
                SET @SQL = @SQL + 'ALTER DATABASE sonar COLLATE Latin1_General_CS_AS;';
            END

        EXEC sp_executesql @SQL;
        FETCH NEXT FROM DB_CURSOR INTO @DB_NAME;
    END

CLOSE DB_CURSOR;
DEALLOCATE DB_CURSOR;
GO