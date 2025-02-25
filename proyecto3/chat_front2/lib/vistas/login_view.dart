import 'dart:html';

import 'package:flutter/material.dart';
import '../controladores/user_controller.dart';
import '../modelos/user.dart';
import 'home_view.dart';

class LoginScreen extends StatefulWidget {
  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final UserController _userController = UserController();
  final TextEditingController _correoController = TextEditingController();  // Cambié _emailController a _correoController
  final TextEditingController _contrasenaController = TextEditingController();  // Cambié _passwordController a _contraseñaController
  final TextEditingController _nombreController = TextEditingController(); // Para el registro

  // Método de inicio de sesión
  void _login() async {
    try {
      User? user = await _userController.login(
        _correoController.text,
        _contrasenaController.text,
      );
      if (user != null) {
        // Navegar a la pantalla principal si el inicio es exitoso
        Navigator.pushReplacement(
          context,
          MaterialPageRoute(builder: (context) => HomeScreen(user: user)), // Aquí se reemplaza con la pantalla que deseas
        );
      } else {
        // Mostrar mensaje de error si el inicio de sesión falla
        _showError('Error de inicio de sesión');
      }
    } catch (e) {
      // Si ocurre algún error (ej. error de conexión)
      _showError('Hubo un problema. Intenta nuevamente $e');
      print('Error: $e');

    }
  }

  // Método de registro de usuario
  void _register() async {
    try {
      await _userController.register(
        _nombreController.text,
        _correoController.text,
        _contrasenaController.text,
      );
      // Mostrar mensaje de éxito y navegar
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Registro exitoso. Ahora puedes iniciar sesión.')),
      );
      // Navegar al inicio de sesión
      Navigator.pop(context);
    } catch (e) {
      // Si ocurre un error durante el registro
      _showError('Error al registrar. Intenta nuevamente');
    }
  }

  // Método para mostrar los errores
  void _showError(String message) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(content: Text(message)),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Iniciar Sesión')),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            // Campo de correo electrónico
            TextField(
              controller: _correoController,  // Cambié de _emailController a _correoController
              decoration: InputDecoration(labelText: 'Correo electrónico'),  // Cambié 'Email' a 'Correo electrónico'
            ),
            // Campo de contraseña
            TextField(
              controller: _contrasenaController,  // Cambié de _passwordController a _contraseñaController
              decoration: InputDecoration(labelText: 'Contraseña'),
              obscureText: true,
            ),
            SizedBox(height: 20),
            // Botón de inicio de sesión
            ElevatedButton(
              onPressed: _login,
              child: Text('Iniciar Sesión'),
            ),
            SizedBox(height: 20),
            // Alternativa para ir al registro
            TextButton(
              onPressed: () {
                // Mostrar diálogo de registro
                _showRegisterDialog();
              },
              child: Text('¿No tienes cuenta? Regístrate aquí'),
            ),
          ],
        ),
      ),
    );
  }

  // Diálogo para el registro
  void _showRegisterDialog() {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: Text('Registro'),
        content: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            // Campo de nombre
            TextField(
              controller: _nombreController,
              decoration: InputDecoration(labelText: 'Nombre'),
            ),
            // Campo de correo electrónico
            TextField(
              controller: _correoController,  // Cambié _emailController a _correoController
              decoration: InputDecoration(labelText: 'Correo electrónico'),  // Cambié 'Email' a 'Correo electrónico'
            ),
            // Campo de contraseña
            TextField(
              controller: _contrasenaController,  // Cambié _passwordController a _contraseñaController
              decoration: InputDecoration(labelText: 'Contraseña'),
              obscureText: true,
            ),
          ],
        ),
        actions: [
          // Botón para registrar
          TextButton(
            onPressed: () {
              _register();
              Navigator.pop(context); // Cerrar el diálogo
            },
            child: Text('Registrar'),
          ),
          // Botón para cancelar el registro
          TextButton(
            onPressed: () {
              Navigator.pop(context); // Cerrar el diálogo sin registrar
            },
            child: Text('Cancelar'),
          ),
        ],
      ),
    );
  }
}
