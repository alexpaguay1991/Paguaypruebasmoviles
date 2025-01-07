import 'dart:ui';

import 'package:flutter/material.dart';  // Asegúrate de importar esto para utilizar Image
import '../modelo/coche.dart';

class CocheControlador {
  List<Coche> coches = [
    Coche("Renault", "Twingo", Image.asset("images/car1.png")),
    Coche("Citroen", "Twingo2", Image.asset("images/car2.png")),
    Coche("Ford", "F150", Image.asset("images/car3.png")),
    Coche("Porsche", "Focus", Image.asset("images/car4.png")),
    Coche("Ferrari", "Twingo12", Image.asset("images/car5.png")),
    Coche("Citroen", "Twingo21", Image.asset("images/car1.png")),
    Coche("Renault", "Twingo22", Image.asset("images/car2.png")),
    Coche("Mazda", "Twingo223", Image.asset("images/car3.png")),
    Coche("Renault", "Twingo24", Image.asset("images/car4.png")),
    Coche("Citroen", "Twingo225", Image.asset("images/car5.png")),
  ];
}
