class User {
  final String id;
  final String nombre;
  final String correo;
  final String contrasena;
  final double saldo;

  User({
    required this.id,
    required this.nombre,
    required this.correo,
    required this.contrasena,
    required this.saldo,

  });

  // Convertir JSON a un objeto User
  factory User.desdeJson(Map<String, dynamic> json) {
    return User(
      id: json['id']?.toString() ?? '', // Asegurándonos que 'id' se maneje como un String
      nombre: json['nombre'] ?? '', // 'nombre' del backend
      correo: json['correo'] ?? '', // 'correo' del backend
      contrasena: json['contraseña'] ?? '', // 'contraseña' del backend
      saldo: (json['saldo'] is num) ? json['saldo'].toDouble() : 0.0, // 'saldo' del backend
    );
  }

  // Convertir un objeto User a JSON
  Map<String, dynamic> aJson() {
    return {
      'id': id,
      'nombre': nombre,
      'correo': correo,
      'contraseña': contrasena,
      'saldo': saldo,
    };
  }
}
