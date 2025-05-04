document.addEventListener('DOMContentLoaded', () => {
  const agregarPropiedadForm = document.getElementById('agregarPropiedadForm');
  const editarPropiedadForm = document.getElementById('editarPropiedadForm');
  const propiedadList = document.getElementById('propiedadList');
  const cancelarEdicionBtn = document.getElementById('cancelarEdicion');

  // Cargar propiedades al iniciar
  fetchPropiedades();

  // Manejar el envío del formulario de agregar propiedad
  agregarPropiedadForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    
    // Obtener valores del formulario
    const nombre = document.getElementById('nombre').value.trim();
    const ubicacion = document.getElementById('ubicacion').value.trim();
    const precio = document.getElementById('precio').value.trim();
    
    // Validar campos
    if (!nombre || !ubicacion || !precio) {
      alert('Por favor complete todos los campos');
      return;
    }
    
    if (isNaN(precio) || parseFloat(precio) <= 0) {
      alert('El precio debe ser un número válido mayor a 0');
      return;
    }
    
    // Crear objeto propiedad
    const propiedad = { 
      nombre, 
      ubicacion, 
      precio: parseFloat(precio) 
    };
    
    try {
      // Enviar datos al backend
      const response = await fetch('http://localhost:8081/propiedad', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(propiedad)
      });
      
      // Verificar respuesta
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error al agregar propiedad');
      }
      
      // Procesar respuesta exitosa
      const nuevaPropiedad = await response.json();
      alert(`Propiedad "${nuevaPropiedad.nombre}" agregada con éxito`);
      
      // Actualizar lista y limpiar formulario
      fetchPropiedades();
      agregarPropiedadForm.reset();
      
    } catch (error) {
      console.error('Error:', error);
      alert(error.message);
    }
  });

  // Función para cargar propiedades
  async function fetchPropiedades() {
    try {
      propiedadList.innerHTML = '<li>Cargando propiedades...</li>';
      
      const response = await fetch('http://localhost:8081/propiedad');
      
      if (!response.ok) {
        throw new Error('Error al obtener propiedades');
      }
      
      const propiedades = await response.json();
      
      if (propiedades.length === 0) {
        propiedadList.innerHTML = '<li>No hay propiedades registradas</li>';
        return;
      }
      
      // Mostrar propiedades en la lista
      propiedadList.innerHTML = '';
      propiedades.forEach(propiedad => {
        const li = document.createElement('li');
        li.innerHTML = `
          <div class="propiedad-info">
            <strong>${propiedad.nombre}</strong> - 
            ${propiedad.ubicacion} - 
            $${propiedad.precio.toLocaleString()}
          </div>
          <div class="propiedad-actions">
            <button class="editar-btn" data-id="${propiedad.id}">Editar</button>
            <button class="eliminar-btn" data-id="${propiedad.id}">Eliminar</button>
          </div>
        `;
        
        // Agregar eventos a los botones
        li.querySelector('.editar-btn').addEventListener('click', () => {
          editarPropiedad(propiedad);
        });
        
        li.querySelector('.eliminar-btn').addEventListener('click', () => {
          eliminarPropiedad(propiedad.id);
        });
        
        propiedadList.appendChild(li);
      });
      
    } catch (error) {
      console.error('Error:', error);
      propiedadList.innerHTML = `<li>Error al cargar propiedades: ${error.message}</li>`;
    }
  }

  // Función para editar propiedad
  function editarPropiedad(propiedad) {
    // Llenar formulario de edición
    document.getElementById('editarId').value = propiedad.id;
    document.getElementById('editarNombre').value = propiedad.nombre;
    document.getElementById('editarUbicacion').value = propiedad.ubicacion;
    document.getElementById('editarPrecio').value = propiedad.precio;
    
    // Mostrar formulario de edición y ocultar el de agregar
    editarPropiedadForm.style.display = 'block';
    agregarPropiedadForm.style.display = 'none';
  }

  // Manejar envío del formulario de edición
  editarPropiedadForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    
    const id = document.getElementById('editarId').value;
    const nombre = document.getElementById('editarNombre').value.trim();
    const ubicacion = document.getElementById('editarUbicacion').value.trim();
    const precio = document.getElementById('editarPrecio').value.trim();
    
    // Validaciones
    if (!nombre || !ubicacion || !precio) {
      alert('Por favor complete todos los campos');
      return;
    }
    
    if (isNaN(precio) || parseFloat(precio) <= 0) {
      alert('El precio debe ser un número válido mayor a 0');
      return;
    }
    
    try {
      const response = await fetch(`http://localhost:8081/propiedad/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          nombre,
          ubicacion,
          precio: parseFloat(precio)
        })
      });
      
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error al actualizar propiedad');
      }
      
      alert('Propiedad actualizada con éxito');
      fetchPropiedades();
      cancelarEdicion();
      
    } catch (error) {
      console.error('Error:', error);
      alert(error.message);
    }
  });

  // Función para eliminar propiedad
  async function eliminarPropiedad(id) {
    if (!confirm('¿Está seguro de eliminar esta propiedad?')) {
      return;
    }
    
    try {
      const response = await fetch(`http://localhost:8081/propiedad/${id}`, {
        method: 'DELETE'
      });
      
      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || 'Error al eliminar propiedad');
      }
      
      alert('Propiedad eliminada con éxito');
      fetchPropiedades();
      
    } catch (error) {
      console.error('Error:', error);
      alert(error.message);
    }
  }

  // Función para cancelar edición
  function cancelarEdicion() {
    editarPropiedadForm.reset();
    editarPropiedadForm.style.display = 'none';
    agregarPropiedadForm.style.display = 'block';
  }

  // Manejar botón de cancelar edición
  cancelarEdicionBtn.addEventListener('click', cancelarEdicion);
});