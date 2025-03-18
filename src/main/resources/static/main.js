document.addEventListener('DOMContentLoaded', () => {
  const agregarPropiedadForm = document.getElementById('agregarPropiedadForm');
  const editarPropiedadForm = document.getElementById('editarPropiedadForm');
  const propiedadList = document.getElementById('propiedadList');
  const cancelarEdicionBtn = document.getElementById('cancelarEdicion');

  let propiedadEditando = null;

  // Cargar propiedades al iniciar
  fetchPropiedades();

  // Manejar el envío del formulario de agregar propiedad
  agregarPropiedadForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const nombre = document.getElementById('nombre').value;
    const ubicacion = document.getElementById('ubicacion').value;
    const precio = parseFloat(document.getElementById('precio').value);

    const propiedad = { nombre, ubicacion, precio };

    try {
      const response = await fetch('http://localhost:8081/propiedades', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(propiedad),
      });

      if (response.ok) {
        alert('Propiedad agregada con éxito');
        fetchPropiedades(); // Recargar la lista
        agregarPropiedadForm.reset(); // Limpiar el formulario
      } else {
        alert('Error al agregar la propiedad');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  });

  // Manejar el envío del formulario de editar propiedad
  editarPropiedadForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const id = document.getElementById('editarId').value;
    const nombre = document.getElementById('editarNombre').value;
    const ubicacion = document.getElementById('editarUbicacion').value;
    const precio = parseFloat(document.getElementById('editarPrecio').value);

    const propiedad = { nombre, ubicacion, precio };

    try {
      const response = await fetch(`http://localhost:8081/propiedades/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(propiedad),
      });

      if (response.ok) {
        alert('Propiedad actualizada con éxito');
        fetchPropiedades(); // Recargar la lista
        cancelarEdicion(); // Ocultar el formulario de edición
      } else {
        alert('Error al actualizar la propiedad');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  });

  // Manejar el botón de cancelar edición
  cancelarEdicionBtn.addEventListener('click', cancelarEdicion);

  // Función para cargar propiedades
  async function fetchPropiedades() {
    try {
      const response = await fetch('http://localhost:8081/propiedades');
      const propiedades = await response.json();

      propiedadList.innerHTML = ''; // Limpiar la lista
      propiedades.forEach(propiedad => {
        const li = document.createElement('li');
        li.textContent = `${propiedad.nombre} - ${propiedad.ubicacion} - $${propiedad.precio}`;

        // Botón para editar
        const editarBtn = document.createElement('button');
        editarBtn.textContent = 'Editar';
        editarBtn.addEventListener('click', () => editarPropiedad(propiedad));
        li.appendChild(editarBtn);

        // Botón para eliminar
        const eliminarBtn = document.createElement('button');
        eliminarBtn.textContent = 'Eliminar';
        eliminarBtn.addEventListener('click', () => eliminarPropiedad(propiedad.id));
        li.appendChild(eliminarBtn);

        propiedadList.appendChild(li);
      });
    } catch (error) {
      console.error('Error al cargar propiedades:', error);
    }
  }

  // Función para editar una propiedad
  function editarPropiedad(propiedad) {
    document.getElementById('editarId').value = propiedad.id;
    document.getElementById('editarNombre').value = propiedad.nombre;
    document.getElementById('editarUbicacion').value = propiedad.ubicacion;
    document.getElementById('editarPrecio').value = propiedad.precio;

    // Mostrar el formulario de edición y ocultar el de agregar
    editarPropiedadForm.style.display = 'block';
    agregarPropiedadForm.style.display = 'none';
  }

  // Función para cancelar la edición
  function cancelarEdicion() {
    editarPropiedadForm.reset();
    editarPropiedadForm.style.display = 'none';
    agregarPropiedadForm.style.display = 'block';
  }

  // Función para eliminar una propiedad
  async function eliminarPropiedad(id) {
    try {
      const response = await fetch(`http://localhost:8081/propiedades/${id}`, {
        method: 'DELETE',
      });

      if (response.ok) {
        alert('Propiedad eliminada con éxito');
        fetchPropiedades(); // Recargar la lista
      } else {
        alert('Error al eliminar la propiedad');
      }
    } catch (error) {
      console.error('Error:', error);
    }
  }
});