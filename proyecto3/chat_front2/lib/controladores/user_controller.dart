import 'dart:convert';
import 'package:http/http.dart' as http;
import '../modelos/user.dart'; // Asegúrate de que esté importado correctamente

class UserController {
  final String baseUrl = 'http://localhost:8080/api/users';

  // Método de login
  // Método de login
  Future<User?> login(String correo, String contrasena) async {
    final response = await http.post(
      Uri.parse('$baseUrl/login?correo=$correo&contrasena=$contrasena'), // Aquí envío los parámetros en la URL
      headers: {'Content-Type': 'application/json'},
    );

    if (response.statusCode == 200) {
      final data = json.decode(response.body);
      return User.desdeJson(data);  // Usa el factory desdeJson de la clase User
    } else {
      // Manejar error de inicio de sesión
      return null;
    }
  }

  // Método de registro
  Future<void> register(String nombre, String correo, String contrasena) async {
  final response = await http.post(
  Uri.parse('$baseUrl/register'), // Añadí '/register' al final de la URL para coincidir con el endpoint
  headers: {'Content-Type': 'application/json'},
  body: json.encode({
  'nombre': nombre, // Cambié 'name' a 'nombre'
  'correo': correo, // Cambié 'email' a 'correo'
  'contraseña': contrasena, // Cambié 'password' a 'contraseña'
  }),
  );

  if (response.statusCode != 201) {
  throw Exception('Error al registrar el usuario');
  }
  }
}
