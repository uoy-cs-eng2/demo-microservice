# For Practical 5C - runs on first boot of MariaDB container

# Sets up an extra database
create database extra_database;

# Creates a user with all privileges on this database, with a certain password
grant all privileges on extra_database.* to 'youruser'@'%' identified by 'yoursecret';
