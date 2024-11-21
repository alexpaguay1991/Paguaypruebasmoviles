class Digitos {


  // Método para mostrar la suma de los dígitos
  int mostrarSumaDeDigitos(int numero) {
    if (numero < 0 || numero > 9999) {
      throw ArgumentError('El número debe ser positivo y tener como máximo 4 dígitos.');
    }

    // Convertir el número a una cadena y calcular la suma de sus dígitos
    String numeroStr = numero.toString().padLeft(4, '0'); // Asegura que siempre tenga 4 dígitos
    int suma = 0;

    // Calcular la suma de los dígitos
    for (int i = 0; i < numeroStr.length; i++) {
      suma += int.parse(numeroStr[i]);
    }

    // Devolver la suma en lugar de imprimirla
    return suma;
  }

}


