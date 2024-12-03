class ContrasenaLogica {
  final String contrasenaAlmacenada = "password";


  bool verificarContrasena(String contrasenaIngresada) {
    return contrasenaIngresada.toLowerCase() == contrasenaAlmacenada.toLowerCase();
  }
}
