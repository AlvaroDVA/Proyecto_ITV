
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
    if (validarNumero() == false) {
        return
    }
    if (validarMatricula() == false) {
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

function validarNumero() {
    let numero = document.getElementById("telefono")
    if (isNaN(parseInt(numero.value)) && numero.value.length != 9) {
        alert("No has introducido un numero valido")
        return false
    }
    return true
}

function validarMatricula() {
    let matricula = document.getElementById("matricula")
    
    let regex = /^[0-9]{4}[A-Z]{3}$/;
    if (regex.test(matricula.value)) {
        return true
    } else {
        alert("No has introducido una matricula valida")
        return false
    }
}

function enviarCorreo() {
    const email = document.getElementById("email").value;
    const nombre = document.getElementById("nombre").value;
    const apellido = document.getElementById("apellido").value;
    const matricula = document.getElementById("matricula").value;
    const numero = document.getElementById("telefono").value;
    const vehiculo = document.getElementById("vehiculo").value;

    const subject = "Datos del formulario";
    const body = `Nombre: ${nombre}\nApellido: ${apellido}\nEmail: ${email}\nMatrícula: ${matricula}\nNúmero: ${numero}\nVehículo: ${vehiculo}`;

    const mailtoLink = `mailto:mohamad.elsayed@alumno.iesluisvives.org?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`;

    window.location.href = mailtoLink;
}

function toggleServices() {
    servicesBar.classList.toggle("visible");
}