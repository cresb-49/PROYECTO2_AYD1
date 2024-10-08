-- Elimina la base de datos y la vuelve a crear
DROP DATABASE IF EXISTS proyecto2_ayd1;
CREATE DATABASE proyecto2_ayd1;
-- Creamos el usuario si no existe
CREATE USER IF NOT EXISTS 'usuarioAyd1Proyecto2' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON proyecto2_ayd1.* TO 'usuarioAyd1Proyecto2';
FLUSH PRIVILEGES;
