class AppNotification {
  final String id;
  final String message;
  final DateTime date;

  AppNotification({required this.id, required this.message, required this.date});

  // Convertir JSON a un objeto AppNotification
  factory AppNotification.desdeJson(Map<String, dynamic> json) {
    return AppNotification(
      id: json['id'] ?? '',
      message: json['message'] ?? '',
      date: DateTime.tryParse(json['date'] ?? '') ?? DateTime.now(),
    );
  }

  // Convertir un objeto AppNotification a JSON
  Map<String, dynamic> aJson() {
    return {
      'id': id,
      'message': message,
      'date': date.toIso8601String(),
    };
  }
}
