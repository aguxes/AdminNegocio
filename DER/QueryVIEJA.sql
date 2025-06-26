CREATE DATABASE AdminNegocio;
GO

USE AdminNegocio;
GO

CREATE TABLE clientes (
    id bigint primary key identity(1,1),
    nombre nvarchar(255) not null,
    email nvarchar(255),
    telefono bigint,
    localidad nvarchar(255),
    apellido nvarchar(255),
    dni bigint
);

CREATE TABLE proveedores (
    id bigint primary key identity(1,1),
    nombre nvarchar(255) not null,
    contacto nvarchar(255),
    telefono nvarchar(255)
);

CREATE TABLE compras (
    id bigint primary key identity(1,1),
    proveedor_id bigint foreign key references proveedores(id),
    fecha datetime default getdate(),
    total float not null,
    notas nvarchar(max)
);

CREATE TABLE productos (
    id bigint primary key identity(1,1),
    nombre nvarchar(255) not null,
    descripcion nvarchar(max),
    precioUnitario int not null,
    costo int not null,
    stock int not null,
    unidadMedida nvarchar(255)
);

CREATE TABLE detalles_compras (
    id bigint primary key identity(1,1),
    compra_id bigint foreign key references compras(id),
    producto_id bigint foreign key references productos(id),
    cantidad int not null,
    precio_unitario float not null
);

CREATE TABLE empleados (
    id bigint primary key identity(1,1),
    nombre nvarchar(255) not null,
    puesto nvarchar(255) not null,
    email nvarchar(255),
    telefono nvarchar(255)
);

CREATE TABLE empresa (
    id bigint primary key identity(1,1),
    nombre nvarchar(255),
    direccion nvarchar(255),
    telefono nvarchar(255),
    email nvarchar(255),
    logo varbinary(max)
);

CREATE TABLE historial_precios (
    id bigint primary key identity(1,1),
    producto_id bigint foreign key references productos(id),
    fecha datetime default getdate(),
    precio_venta float not null,
    precio_compra float not null
);

CREATE TABLE usuarios (
    id bigint primary key identity(1,1),
    usuario nvarchar(255) not null unique,
    clave nvarchar(255) not null,
    rol nvarchar(255) not null,
    empleado_id bigint foreign key references empleados(id)
);

CREATE TABLE ventas (
    id bigint primary key identity(1,1),
    cliente_id bigint foreign key references clientes(id),
    fecha datetime default getdate(),
    total float not null,
    empleado_id bigint foreign key references empleados(id),
    medio_pago nvarchar(255) not null,
    notas nvarchar(max)
);

CREATE TABLE detalles_ventas (
    id bigint primary key identity(1,1),
    venta_id bigint foreign key references ventas(id),
    producto_id bigint foreign key references productos(id),
    cantidad int not null,
    precio_unitario float not null
);