function saludar() {
    const mensaje = document.getElementById("mensaje");
    mensaje.textContent = "¡Hola desde JavaScript! Todo anda bien 😎";
}
async function cargarClientes() {
    const response = await fetch("http://localhost:4567/clientes")
    const data = await response.json();

    const contenedor = document.getElementById("mensaje");
    contenedor.innerHTML = "";

    data.forEach(cliente => {
        const div = document.createElement("div");
        div.textContent = `Cliente: ${cliente.nombre} - DNI: ${cliente.DNI}`;
        contenedor.appendChild(div);
    });
}
