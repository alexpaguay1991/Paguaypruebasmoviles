import 'dart:convert';
import 'package:http/http.dart' as http;

import '../modelos/notification.dart';


class NotificationController {
  final String baseUrl = 'http://localhost:8080/api/notifications';

  Future<List<AppNotification>> fetchNotifications(String userId) async {
    final response = await http.get(Uri.parse('$baseUrl/user/$userId'));

    if (response.statusCode == 200) {
      final List<dynamic> data = json.decode(response.body);
      return data.map((notification) => AppNotification.desdeJson(notification)).toList();
    } else {
      throw Exception('Error al obtener notificaciones');
    }
  }
}
