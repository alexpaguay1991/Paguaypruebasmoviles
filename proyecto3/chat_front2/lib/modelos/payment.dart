class Payment {
  final String usuarioId;  // Aquí corresponde al campo 'usuario_id' del backend
  final double monto;  // 'monto' es el campo que corresponde a 'amount' en el frontend
  final String tarjetaId;  // 'tarjeta_id' es el campo que corresponde a 'cardId' en el frontend
  final String fecha;
  Payment({required this.usuarioId, required this.monto, required this.tarjetaId,required this.fecha});

  // Convertir JSON a un objeto Payment
  factory Payment.fromJson(Map<String, dynamic> json) {
    return Payment(
      usuarioId: json['usuarioId'].toString() ?? '',  // Recibe 'usuarioId'
      monto: (json['monto'] is num) ? json['monto'].toDouble() : 0.0,  // 'monto'
      tarjetaId: json['tarjetaId'].toString() ?? '',  // 'tarjetaId'
      fecha: json['fecha'].toString() ?? '',
    );
  }

  // Convertir un objeto Payment a JSON
  Map<String, dynamic> toJson() {
    return {
      'usuarioId': usuarioId,  // Se envía como 'usuarioId'
      'monto': monto,  // Se envía como 'monto'
      'tarjetaId': tarjetaId,  // Se envía como 'tarjetaId'
      'fecha': fecha,
    };
  }
}
