USE [master]
GO

CREATE DATABASE [AdminNegocio]

USE [AdminNegocio]
GO

CREATE OR ALTER TABLE CategoriasProd(
	idCategoria INT PRIMARY KEY,
	descripcion varchar 50 NOT NULL);
	GO
 CREATE OR ALTER TABLE Cliente(
	ID INT,
	DNI INT NOT NULL,
	tipoCliente INT NOT NULL,
	fechAlta datetime NOT NULL,
	cantCompras INT NOT NULL
	CONSTRAINT PRIMARY KEY(ID, DNI));
	GO
	CREATE TABLE Correos(
	idPersona INT PRIMARY KEY,
	correo VARCHAR 60 NOT NULL);
	GO
	CREATE OR ALTER TABLE Empleado(
	DNI INT NOT NULL,
	Legajo varchar 25 NOT NULL,
	Rol int NOT NULL,
	Sueldo float NULL,
	Vacaciones bit NOT NULL,
	Faltas INT NULL,
	FechaIngreso datetime NOT NULL,
	Fechaegreso datetime NOT NULL,
	Activo bit NULL
	CONSTRAINT PRIMARY KEY(DNI, Legajo));
	GO

	CREATE OR ALTER TABLE FormaDePagos(
	idPago INT PRIMARY KEY,
	moneda INT NOT NULL,
	descripcion VARCHAR 30 NOT NULL);
	GO

	CREATE OR ALTER TABLE Generos(
	idSexo INT PRIMARY KEY,
	descripcion VARCHAR 15 NOT NULL);

	CREATE OR ALTER TABLE MedidasProd(
	idMedida INT PRIMARY KEY,
	descripcion VARCHAR 30 NOT NULL);

	CREATE OR ALTER TABLE Monedas(
	idMoneda INT PRIMARY KEY,
	descripcion varchar 20 NOT NULL,
	TasaCambio float NOT NULL);

	CREATE OR ALTER TABLE Paises(
	idPais INT PRIMARY KEY,
	nombrePais VARCHAR 30 NOT NULL);

	CREATE OR ALTER TABLE Persona(
	DNI INT PRIMARY KEY,
	nombre VARCHAR 25 NOT NULL,
	apellido VARCHAR 25 NOT NULL,
	genero INT NOT NULL,
	nacionalidad INT NOT NULL);

	CREATE OR ALTER TABLE Producto(
	idProducto INT PRIMARY KEY,
	nombre varchar 40 NOT NULL,
	precio float NOT NULL,
	costo float NULL,
	stock INT NULL,
	unidadMedida INT NOT NULL,
	Categoria INT NOT NULL,
	fechAlta datetime NOT NULL,
	fechaBaja datetime NOT NULL);

	CREATE OR ALTER TABLE Roles(
	idRol INT PRIMARY KEY,
	descripcion VARCHAR 30 NOT NULL);

	CREATE OR ALTER TABLE Telefonos(
	idPersona INT PRIMARY KEY,
	telefono INT NOT NULL);

	CREATE OR ALTER TABLE TiposClientes(
	idTipo INT PRIMARY KEY,
	descripcion VARCHAR 30 NOT NULL);

	CREATE OR ALTER TABLE TipoUsers(
	idTipo INT PRIMARY KEY,
	descripcion VARCHAR 20 NOT NULL);

	CREATE OR ALTER TABLE Usuario(
	DNI INT PRIMARY KEY,
	nombreUsuario VARCHAR 20 NOT NULL,
	contrasenias VARCHAR 20 NOT NULL,
	tipoUser INT NOT NULL);

	CREATE OR ALTER TABLE Venta(
	idVenta INT NOT NULL,
	idProd intT NULL,
	legajoE varchar](20) NOT NULL,
	dniCliente [int] NOT NULL,
	fecha datetime] NOT NULL,
	formaDePago [int] NOT NULL,
	subtotal float] NOT NULL,
	total float NOT NULL,