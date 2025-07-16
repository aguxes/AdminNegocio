USE AdminNegocio
GO
SELECT P.DNI, P.nombre, P.apellido, g.descripcion AS Sexo, PA.nombrePais AS Nacionalidad, c.ID AS IdCliente FROM Persona P
INNER JOIN Cliente c ON c.DNI = P.DNI
INNER JOIN Paises PA ON PA.idPais = P.nacionalidad
INNER JOIN Generos g ON g.idSexo = P.genero
WHERE g.descripcion = 'Masculino'