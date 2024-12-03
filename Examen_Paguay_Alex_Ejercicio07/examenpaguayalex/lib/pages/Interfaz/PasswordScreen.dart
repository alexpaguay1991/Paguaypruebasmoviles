import 'package:flutter/material.dart';
import '../Logica/ContrasenaLogica.dart';
import 'ResultadoContrasenaPage.dart'; // Asegúrate de importar la página de resultados

class PasswordScreen extends StatefulWidget {
  @override
  _PasswordScreenState createState() => _PasswordScreenState();
}

class _PasswordScreenState extends State<PasswordScreen> {
  final TextEditingController _controller = TextEditingController();
  final TextEditingController _nombreController = TextEditingController(); // Controlador para el nombre

  // Instancia de la clase ContrasenaLogica
  final ContrasenaLogica _contrasenaLogica = ContrasenaLogica();

  void _checkPassword() {
    // Verificamos la contraseña
    bool esCorrecta = _contrasenaLogica.verificarContrasena(_controller.text);

    // Navegamos a la pantalla de resultados, pasando el nombre y si la contraseña es correcta
    Navigator.push(
      context,
      MaterialPageRoute(
        builder: (context) => ResultadoContrasenaPage(
          contrasenaIngresada: _controller.text,
          esCorrecta: esCorrecta,
          nombre: _nombreController.text, // Pasamos el nombre
        ),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text("Verificar Contraseña", style: TextStyle(color: Colors.white)),
        backgroundColor: Colors.blueGrey.shade700,  // Color del AppBar similar al de ResultadoContrasenaPage
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
                    Icons.lock_outline,
                    size: 100,
                    color: Colors.amber,  // Color amarillo para resaltar el icono
                  ),
                  const SizedBox(height: 20),
                  const Text(
                    "Introduce tu Nombre",
                    style: TextStyle(
                      fontSize: 28,
                      fontWeight: FontWeight.bold,
                      color: Colors.white,  // Texto en blanco
                    ),
                  ),
                  const SizedBox(height: 10),
                  // Campo de texto para el nombre
                  TextField(
                    controller: _nombreController,
                    decoration: InputDecoration(
                      labelText: "Nombre",
                      labelStyle: const TextStyle(color: Colors.white70),  // Etiqueta en blanco con opacidad
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(10.0),  // Bordes redondeados
                        borderSide: const BorderSide(color: Colors.white70),
                      ),
                      focusedBorder: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(10.0),
                        borderSide: const BorderSide(color: Colors.amber),
                      ),
                    ),
                    style: const TextStyle(color: Colors.white),  // Texto en blanco
                  ),
                  const SizedBox(height: 20),
                  const Text(
                    "Introduce tu Contraseña",
                    style: TextStyle(
                      fontSize: 28,
                      fontWeight: FontWeight.bold,
                      color: Colors.white,  // Texto en blanco
                    ),
                  ),
                  const SizedBox(height: 10),
                  // Campo de texto para la contraseña
                  TextField(
                    controller: _controller,
                    obscureText: true,
                    decoration: InputDecoration(
                      labelText: "Contraseña",
                      labelStyle: const TextStyle(color: Colors.white70),  // Etiqueta en blanco con opacidad
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(10.0),  // Bordes redondeados
                        borderSide: const BorderSide(color: Colors.white70),
                      ),
                      focusedBorder: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(10.0),
                        borderSide: const BorderSide(color: Colors.amber),
                      ),
                    ),
                    style: const TextStyle(color: Colors.white),  // Texto en blanco
                  ),
                  const SizedBox(height: 20),
                  ElevatedButton(
                    onPressed: _checkPassword,
                    child: const Text(
                      "Verificar Contraseña",
                      style: TextStyle(color: Colors.white), // Texto en blanco
                    ),
                    style: ElevatedButton.styleFrom(
                      padding: const EdgeInsets.symmetric(horizontal: 30.0, vertical: 15.0),
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
