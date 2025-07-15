USE master;
GO

IF DB_ID('AdminNegocio') IS NOT NULL
	DROP DATABASE Admincito;
GO

CREATE DATABASE AdminNegocio;
GO

USE AdminNegocio;
GO

--Tablas de detalle
CREATE TABLE Generos (
	idSexo INT PRIMARY KEY,
	descripcion VARCHAR(15) NOT NULL
);

CREATE TABLE Paises (
	idPais INT PRIMARY KEY,
	nombrePais VARCHAR(30) NOT NULL
);

CREATE TABLE TipoUsers (
	tipo INT PRIMARY KEY,
	descripcion VARCHAR(20) NOT NULL
);

CREATE TABLE TiposClientes (
	tipo INT PRIMARY KEY,
	descripcion VARCHAR(30) NOT NULL
);

CREATE TABLE Roles (
	Rol INT PRIMARY KEY,
	descripcion VARCHAR(30) NOT NULL
);

CREATE TABLE MedidasProd (
	unidadMedida INT PRIMARY KEY,
	descripcion VARCHAR(30) NOT NULL
);

CREATE TABLE CategoriasProd (
	Categoria INT PRIMARY KEY,
	descripcion VARCHAR(50) NOT NULL
);

CREATE TABLE Monedas (
	idMoneda INT PRIMARY KEY,
	descripcion VARCHAR(20) NOT NULL,
	TasaCambio FLOAT NOT NULL
);

-- Tablas principales
CREATE TABLE Persona (
	DNI INT PRIMARY KEY,
	nombre VARCHAR(25) NOT NULL,
	apellido VARCHAR(25) NOT NULL,
	genero INT NOT NULL,
	nacionalidad INT NOT NULL,
	FOREIGN KEY (genero) REFERENCES Generos(idSexo),
	FOREIGN KEY (nacionalidad) REFERENCES Paises(idPais)
);

CREATE TABLE Correos (
	idPersona INT PRIMARY KEY, 
	correo VARCHAR(60) NOT NULL,
	FOREIGN KEY (idPersona) REFERENCES Persona(DNI) ON UPDATE CASCADE
);

CREATE TABLE Telefonos (
	idPersona INT PRIMARY KEY,
	telefono INT NOT NULL,
	FOREIGN KEY (idPersona) REFERENCES Persona(DNI) ON UPDATE CASCADE
);

CREATE TABLE Usuario (
	DNI INT PRIMARY KEY,
	nombreUsuario VARCHAR(20) NOT NULL,
	contrasenias VARCHAR(20) NOT NULL,
	idTipo INT NOT NULL,
	FOREIGN KEY (DNI) REFERENCES Persona(DNI) ON UPDATE CASCADE,
	FOREIGN KEY (idTipo) REFERENCES TipoUsers(tipo)
);

CREATE TABLE Cliente (
	ID INT IDENTITY(1,1) PRIMARY KEY,
	DNI INT NOT NULL UNIQUE,
	idTipo INT NOT NULL,
	fechAlta DATETIME NOT NULL,
	cantCompras INT NOT NULL,
	FOREIGN KEY (DNI) REFERENCES Persona(DNI) ON UPDATE CASCADE,
	FOREIGN KEY (idTipo) REFERENCES TiposClientes(tipo)
);

CREATE TABLE Empleado (
	ID INT IDENTITY(1,1) PRIMARY KEY,
	DNI INT NOT NULL UNIQUE,
	idRol INT NOT NULL,
	Sueldo FLOAT NULL,
	Vacaciones BIT NOT NULL DEFAULT 0,
	Faltas INT NULL,
	FechaIngreso DATETIME NOT NULL,
	FechaEgreso DATETIME NOT NULL,
	Activo BIT NOT NULL DEFAULT 0,
	FOREIGN KEY (DNI) REFERENCES Persona(DNI) ON UPDATE CASCADE,
	FOREIGN KEY (idRol) REFERENCES Roles(Rol)
);

CREATE TABLE Producto (
	idProducto INT PRIMARY KEY,
	nombre VARCHAR(40) NOT NULL,
	precio FLOAT NOT NULL,
	costo FLOAT NULL,
	stock INT NULL,
	idMedida INT NOT NULL,
	idCategoria INT NOT NULL,
	fechAlta DATETIME NOT NULL,
	fechaBaja DATETIME NOT NULL,
	FOREIGN KEY (idMedida) REFERENCES MedidasProd(unidadMedida),
	FOREIGN KEY (idCategoria) REFERENCES CategoriasProd(Categoria)
);

CREATE TABLE FormaDePagos (
	idPago INT PRIMARY KEY,
	moneda INT NOT NULL,
	descripcion VARCHAR(30) NOT NULL,
	FOREIGN KEY (moneda) REFERENCES Monedas(idMoneda)
);

CREATE TABLE Venta (
	nFactura INT IDENTITY(1,1) NOT NULL,
	idProd INT NOT NULL,
	idE INT NOT NULL,
	idC INT NOT NULL,
	fecha DATETIME NOT NULL,
	formaDePago INT NOT NULL,
	subtotal FLOAT NOT NULL,
	total FLOAT NOT NULL,
	CONSTRAINT PK_Venta PRIMARY KEY (nFactura, idProd),
	FOREIGN KEY (idProd) REFERENCES Producto(idProducto),
	FOREIGN KEY (idE) REFERENCES Empleado(ID) ON UPDATE CASCADE,
	FOREIGN KEY (idC) REFERENCES Cliente(ID),
	FOREIGN KEY (formaDePago) REFERENCES FormaDePagos(idPago)
);
