import 'dart:convert';
import 'package:http/http.dart' as http;
import '../modelos/payment.dart';  // Asegúrate de importar la clase correcta

class PaymentController {
  final String baseUrl = 'http://localhost:8080/api/payments';  // URL base de la API

  Future<void> makePayment(double monto, String tarjetaId, String usuarioId) async {
    // Obtener la fecha y hora actual en formato ISO 8601
    String fechaActual = DateTime.now().toIso8601String();

    final response = await http.post(
      Uri.parse('$baseUrl/make'),
      headers: {'Content-Type': 'application/json'},
      body: json.encode({
        'usuarioId': usuarioId,  // Enviar 'usuarioId' como en el backend
        'monto': monto,  // Enviar 'monto'
        'tarjetaId': tarjetaId,  // Enviar 'tarjetaId'
        'fecha': fechaActual  // Enviar la fecha actual en formato ISO 8601
      }),
    );

    if (response.statusCode != 200) {
      throw Exception('Error al realizar el pago ${response.statusCode}');
    }
  }


  // Método para obtener los pagos de un usuario
  Future<List<Payment>> fetchPayments(String usuarioId) async {
    final response = await http.get(Uri.parse('$baseUrl/user/$usuarioId'));

    if (response.statusCode == 200) {
      print('Consulta exitosa');


      final List<dynamic> data = json.decode(response.body);

      return data.map((payment) => Payment.fromJson(payment)).toList();
    } else {
      throw Exception('Error al obtener los pagos');
    }
  }
}
