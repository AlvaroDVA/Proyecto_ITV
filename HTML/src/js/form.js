
var servicesBar = document.getElementById("servicesBar");

function validaciones() {
    if (validarNombre("nombre") == false) {
        return
    }
    if (validarNombre("apellido") == false) {
        return
    }
    if (validarEmail() == false) {
        return
    }
    if (validarTelefono() == false) {
        return
    }
    if (validarMatricula() == false) {
        return
    }
    if (validarDNI() == false) {
        return
    }
    if(validarMarca() == false) {
        return
    }
    enviarCorreo()
}

function validarNombre() {
    let nombre = document.getElementById("nombre")

    if (nombre.value.length <= 2) {
        alert("Un nombre no puede tener 2 o menos caracteres")
        return false
    }

    let regex = /^[^\d\s]+$/;
   
    if (regex.test(nombre.value)) {
            return true
    } else {
            alert("El nombre no puede contener digitos")
            return false
    }
   
}


function validarApellido() {
    let nombre = document.getElementById("apellido")

    if (nombre.value.length <= 2) {
        alert("Un Apellido no puede tener 2 o menos caracteres")
        return false
    }
    let regex = /^[^\d\s]+$/;
   
    if (regex.test(nombre.value)) {
        return true
    } else {
        alert("El nombre no puede contener digitos")
        return false
    }
}

function validarEmail() {
    let email = document.getElementById("email") 
    let regex = /^\w+([.-_+]?\w+)*@\w+([.-]?\w+)*(\.\w{2,10})+$/;
    if (regex.test(email.value)) {
        return true
    }else {
        alert("No has introducido un email valido")
        return false
    }
}

function validarTelefono() {
    let numero = document.getElementById("telefono")
    if (isNaN(parseInt(numero.value)) && numero.value.length != 9) {
        alert("No has introducido un numero valido")
        return false
    }
    return true
}

function validarDNI() {
    let DNI = document.getElementById("dni")
    let regex = /^[0-9]{8}[A-Z]{1}$/;

    if (regex.test(DNI.value)) {
        return true
    } else {
        alert("No has introducido un DNI válido")
        return false
    }
}

function validarMarca() {
    let marca = document.getElementById("marca")
    let regex = /^[^\d\s]+$/;
    if (regex.test(marca.value)) {
        return true
    } else {
        alert("La marca del vehículo no puede contener digitos")
        return false
    }
}

function validarMatricula() {
    let matricula = document.getElementById("matricula")
    
    if (matricula.length >= 11) {
        alert("No has introducido una matricula valida")
        return false
    } else {
        return true
    }
}

function enviarCorreo() {
    
    const nombre = document.getElementById("nombre").value;
    const apellido = document.getElementById("apellido").value;
    const email = document.getElementById("email").value;
    const telefono = document.getElementById("telefono").value;
    const DNI = document.getElementById("dni").value;
    const vehiculo = document.getElementById("vehiculo").value;
    const marca = document.getElementById("marca").value;
    const modelo = document.getElementById("modelo").value;
    const motor = document.getElementById("motor").value;
    const matricula = document.getElementById("matricula").value;
    const matriculacion = document.getElementById("año-matriculacion").value;
    const revision = document.getElementById("ultima-revision").value;

    const subject = "Datos del formulario";
    const body = `Nombre: ${nombre}\nApellido: ${apellido}\nEmail: ${email}\nNúmero de Teléfono: ${telefono}\nDNI: ${DNI}\nTipo de Vehículo: ${vehiculo}\nMarca: ${marca}\nModelo: ${modelo}\nMotor: ${motor}\nMatrícula: ${matricula}\nAño de matriculación: ${matriculacion}\nÚltima revisión: ${revision}`;

    const mailtoLink = `mailto:mohamad.elsayed@alumno.iesluisvives.org?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;

    window.location.href = mailtoLink;
}

function toggleServices() {
    servicesBar.classList.toggle("visible");
}



const formulario = document.getElementById("miFormulario");
const botonEnviar = document.getElementById("boton");

formulario.addEventListener("input", validarFormulario);

function validarFormulario() {
  const camposCompletos = Array.from(formulario.elements).every((elemento) => {
    return elemento.value !== "";
  });

  if (camposCompletos) {
    botonEnviar.removeAttribute("disabled");
  } else {
    botonEnviar.setAttribute("disabled", "disabled");
  }
}
