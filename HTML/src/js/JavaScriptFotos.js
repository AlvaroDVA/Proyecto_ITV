var imagenes = [
    "src/Imagenes/Imagen-Uno.jpg",
    "src/Imagenes/Imagen-Dos.jpg",
    "src/Imagenes/Imagen-Tres.jpg"
  ];
  
  document.getElementById("Imagen").src = imagenes[0];
  
  var SliderDerecha = document.querySelector(".slider-derecho");
  var SliderIzquierda = document.querySelector(".slider-izquierdo");
  var Contador = 0;
  var Intervalo = setInterval(MoverDerecha, 3500);
  
  var servicesBar = document.getElementById("servicesBar");
  
  function MoverDerecha() {
    Contador++;
    if (Contador > imagenes.length - 1) {
      Contador = 0;
    }
    document.getElementById("Imagen").src = imagenes[Contador];
  }
  
  SliderDerecha.addEventListener("click", function() {
    clearInterval(Intervalo);
    MoverDerecha();
    Intervalo = setInterval(MoverDerecha, 3500);
  });
  
  function MoverIzquierda() {
    Contador--;
    if (Contador < 0) {
      Contador = imagenes.length - 1;
    }
    document.getElementById("Imagen").src = imagenes[Contador];
  }
  
  SliderIzquierda.addEventListener("click", function() {
    clearInterval(Intervalo);
    MoverIzquierda();
    Intervalo = setInterval(MoverDerecha, 3500);
  });
  
  function toggleServices() {
    servicesBar.classList.toggle("visible");
  }
  
  