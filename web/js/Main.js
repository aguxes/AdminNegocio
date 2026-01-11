const API_URL = 'http://localhost:4567/api';

// --- Navegación ---
function cargarVista(vista) {
    document.querySelectorAll('.sidebar a').forEach(a => a.classList.remove('active'));
    const activeLink = document.getElementById('link-' + vista);
    if (activeLink) activeLink.classList.add('active');

    const container = document.getElementById('app-container');
    const title = document.getElementById('page-title');

    switch (vista) {
        case 'inicio':
            title.textContent = 'Inicio';
            container.innerHTML = `
                <div class="row">
                    <div class="col-md-12">
                        <div class="card p-5 text-center">
                            <h1>👋 Bienvenido al Panel</h1>
                            <p class="lead">Selecciona una opción del menú para comenzar.</p>
                        </div>
                    </div>
                </div>`;
            break;
        case 'clientes':
            title.textContent = 'Gestión de Clientes';
            cargarClientes();
            break;
        case 'productos':
            title.textContent = 'Gestión de Productos';
            container.innerHTML = '<div class="alert alert-info">🚧 Módulo en construcción</div>';
            break;
        case 'ventas':
            title.textContent = 'Gestión de Ventas';
            container.innerHTML = '<div class="alert alert-info">🚧 Módulo en construcción</div>';
            break;
        case 'empleados':
            title.textContent = 'Gestión de Empleados';
            container.innerHTML = '<div class="alert alert-info">🚧 Módulo en construcción</div>';
            break;
    }
}

// --- Módulo Clientes ---

async function cargarClientes() {
    const container = document.getElementById('app-container');
    container.innerHTML = `
        <div class="d-flex justify-content-between mb-3">
            <input type="text" class="form-control w-25" placeholder="🔍 Buscar cliente...">
            <button class="btn btn-primary" onclick="mostrarModalCliente()"><i class="bi bi-person-plus"></i> Nuevo Cliente</button>
        </div>
        <div class="card p-3">
            <div class="table-responsive">
                <table class="table table-hover align-middle">
                    <thead class="table-light">
                        <tr>
                            <th>ID</th>
                            <th>DNI</th>
                            <th>Nombre</th>
                            <th>Apellido</th>
                            <th>Tipo</th>
                            <th>Compras</th>
                            <th>Teléfono</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody id="tabla-clientes-body">
                        <tr><td colspan="8" class="text-center">Cargando...</td></tr>
                    </tbody>
                </table>
            </div>
        </div>
    `;

    try {
        const res = await fetch(`${API_URL}/clientes`);
        const clientes = await res.json();
        renderTablaClientes(clientes);
    } catch (error) {
        console.error('Error cargando clientes:', error);
        document.getElementById('tabla-clientes-body').innerHTML = '<tr><td colspan="8" class="text-center text-danger">Error al cargar datos.</td></tr>';
    }
}

function renderTablaClientes(clientes) {
    const tbody = document.getElementById('tabla-clientes-body');
    tbody.innerHTML = '';

    if (clientes.length === 0) {
        tbody.innerHTML = '<tr><td colspan="8" class="text-center">No hay clientes registrados.</td></tr>';
        return;
    }

    clientes.forEach(c => {
        const tipoDesc = c.tipCliente ? c.tipCliente.descripcion : '-';
        const telefono = c.telefono ? c.telefono.telefono : '-';
        const clienteStr = JSON.stringify(c).replace(/"/g, "&quot;");

        const tr = document.createElement('tr');
        tr.innerHTML = `
            <td>${c.id}</td>
            <td>${c.DNI}</td>
            <td>${c.Nombre}</td>
            <td>${c.Apellido}</td>
            <td><span class="badge bg-info text-dark">${tipoDesc}</span></td>
            <td>${c.cantCompras}</td>
            <td>${telefono}</td>
            <td>
                <button class="btn btn-sm btn-warning me-1" onclick="editarCliente(${clienteStr})"><i class="bi bi-pencil"></i></button>
                <button class="btn btn-sm btn-danger" onclick="eliminarCliente(${c.id})"><i class="bi bi-trash"></i></button>
            </td>
        `;
        tbody.appendChild(tr);
    });
}

function mostrarModalCliente(cliente = null) {
    const esEdicion = !!cliente;
    const titulo = esEdicion ? 'Editar Cliente' : 'Nuevo Cliente';
    const data = cliente || { DNI: '', Nombre: '', Apellido: '', cantCompras: 0, tipCliente: { tipo: 1 }, telefono: { telefono: '' } };
    const tipoId = data.tipCliente ? data.tipCliente.tipo : 1;
    const telNum = data.telefono ? data.telefono.telefono : '';

    Swal.fire({
        title: titulo,
        html: `
            <form id="form-cliente" class="text-start">
                <div class="mb-2">
                    <label>DNI</label>
                    <input type="number" id="dni" class="form-control" value="${data.DNI}" ${esEdicion ? 'readonly' : ''}>
                </div>
                <div class="row">
                    <div class="col-6 mb-2">
                        <label>Nombre</label>
                        <input type="text" id="nombre" class="form-control" value="${data.Nombre}">
                    </div>
                    <div class="col-6 mb-2">
                        <label>Apellido</label>
                        <input type="text" id="apellido" class="form-control" value="${data.Apellido}">
                    </div>
                </div>
                <div class="mb-2">
                    <label>Teléfono</label>
                    <input type="number" id="telefono" class="form-control" value="${telNum}">
                </div>
                <div class="row">
                    <div class="col-6 mb-2">
                         <label>Tipo (ID)</label>
                         <input type="number" id="tipo" class="form-control" value="${tipoId}">
                    </div>
                    <div class="col-6 mb-2">
                        <label>Compras</label>
                        <input type="number" id="cantCompras" class="form-control" value="${data.cantCompras}">
                    </div>
                </div>
            </form>
        `,
        showCancelButton: true,
        confirmButtonText: 'Guardar',
        preConfirm: () => {
            const dni = document.getElementById('dni').value;
            const nombre = document.getElementById('nombre').value;
            const apellido = document.getElementById('apellido').value;
            const telefono = document.getElementById('telefono').value;
            const tipo = document.getElementById('tipo').value;
            const cantCompras = document.getElementById('cantCompras').value;

            if (!dni || !nombre || !apellido) return false;

            return {
                DNI: parseInt(dni),
                Nombre: nombre,
                Apellido: apellido,
                cantCompras: parseInt(cantCompras),
                tipCliente: { tipo: parseInt(tipo), descripcion: "" },
                telefono: { telefono: parseInt(telefono), idPersona: parseInt(dni) }
            };
        }
    }).then((result) => {
        if (result.isConfirmed) guardarCliente(result.value, esEdicion);
    });
}

function editarCliente(cliente) {
    mostrarModalCliente(cliente);
}

async function guardarCliente(cliente, esEdicion) {
    try {
        const metodo = esEdicion ? 'PUT' : 'POST';
        const url = esEdicion ? `${API_URL}/clientes/0` : `${API_URL}/clientes`;
        const res = await fetch(url, {
            method: metodo,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(cliente)
        });
        if (res.ok) {
            Swal.fire('¡Éxito!', 'Cliente guardado.', 'success');
            cargarClientes();
        } else {
            Swal.fire('Error', 'No se pudo guardar.', 'error');
        }
    } catch (error) {
        Swal.fire('Error', 'Error de conexión.', 'error');
    }
}

async function eliminarCliente(id) {
    const result = await Swal.fire({
        title: '¿Estás seguro?',
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#d33',
        confirmButtonText: 'Sí, eliminar'
    });

    if (result.isConfirmed) {
        try {
            const res = await fetch(`${API_URL}/clientes/${id}`, { method: 'DELETE' });
            if (res.ok) {
                Swal.fire('Eliminado!', 'Cliente eliminado.', 'success');
                cargarClientes();
            } else {
                Swal.fire('Error', 'No se pudo eliminar.', 'error');
            }
        } catch (error) {
            Swal.fire('Error', 'Error de conexión.', 'error');
        }
    }
}

window.onload = () => cargarVista('inicio');