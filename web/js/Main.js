function saludar() {
    const mensaje = document.getElementById("mensaje");
    mensaje.textContent = "¡Hola desde JavaScript! Todo anda bien 😎";
}
async function cargarClientes() {
    const response = await fetch("http://localhost:4567/clientes");
    const data = await response.json();

    const contenedor = document.getElementById("mensaje");
    contenedor.innerHTML = "";

    data.forEach(cliente => {
        const tarjeta = document.createElement("div");
        tarjeta.classList.add("tarjeta");

        tarjeta.innerHTML = `
            <p><strong>ID:</strong> ${cliente.id}</p>
            <p><strong>DNI:</strong> ${cliente.DNI}</p>
            <p><strong>Nombre:</strong> ${cliente.nombre} ${cliente.apellido}</p>
            <p><strong>Tipo:</strong> ${cliente.tipo.descripcion}</p>
            <p><strong>Compras:</strong> ${cliente.cantCompras}</p>
            <p><strong>Teléfono:</strong> ${cliente.telefono?.telefono ?? 'No registrado'}</p>
        `;

        contenedor.appendChild(tarjeta);
    });
}


