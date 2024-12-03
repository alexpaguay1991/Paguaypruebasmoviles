import 'package:flutter/material.dart';

class ResultadoContrasenaPage extends StatelessWidget {
  final String contrasenaIngresada;
  final bool esCorrecta;
  final String nombre;

  // Constructor para recibir los parámetros
  ResultadoContrasenaPage({
    required this.contrasenaIngresada,
    required this.esCorrecta,
    required this.nombre,
  });

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Resultado Contraseña", style: TextStyle(color: Colors.white)),
        backgroundColor: Colors.blueGrey.shade700,  // Color del AppBar
      ),
      body: Stack(
        children: [
          // Fondo con degradado
          Container(
            decoration: BoxDecoration(
              gradient: LinearGradient(
                colors: [Colors.blueGrey.shade700, Colors.black],  // Degradado similar
                begin: Alignment.topCenter,
                end: Alignment.bottomCenter,
              ),
            ),
          ),
          // Contenido principal
          Center(
            child: Padding(
              padding: const EdgeInsets.all(20.0),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    esCorrecta ? Icons.check_circle_outline : Icons.error_outline,
                    size: 100,
                    color: esCorrecta ? Colors.green : Colors.red,
                  ),
                  const SizedBox(height: 20),
                  Text(
                    "Hola, $nombre",  // Saludo con el nombre ingresado
                    style: const TextStyle(
                      fontSize: 28,
                      fontWeight: FontWeight.bold,
                      color: Colors.white,  // Texto en blanco
                    ),
                  ),
                  const SizedBox(height: 20),
                  Text(
                    esCorrecta ? "¡Contraseña correcta!" : "Contraseña incorrecta.",
                    style: TextStyle(
                      fontSize: 22,
                      color: esCorrecta ? Colors.green : Colors.red,  // Aqui se maneja el boton
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 30),
                  ElevatedButton.icon(
                    onPressed: () {
                      Navigator.pop(context);  // Regresar a la pantalla anterior
                    },
                    icon: const Icon(Icons.arrow_back, color: Colors.white),
                    label: const Text(
                      "Regresar",
                      style: TextStyle(color: Colors.white),  // Texto en blanco
                    ),
                    style: ElevatedButton.styleFrom(
                      padding: const EdgeInsets.symmetric(horizontal: 20.0, vertical: 15.0),
                      backgroundColor: Colors.green,  // Fondo del botón verde
                      shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(10.0),  // Bordes redondeados
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  }
}
