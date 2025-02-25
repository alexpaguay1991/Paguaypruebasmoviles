class CreditCard {
  final String id;  // El id es un String porque en Dart no usas Long
  final String cardNumber;  // En el backend es "numero"
    bool isFrozen;  // El campo "frozen" en Java es un Boolean

  // Constructor
  CreditCard({required this.id, required this.cardNumber, this.isFrozen = false});

  // Convertir JSON a un objeto CreditCard
  factory CreditCard.desdeJson(Map<String, dynamic> json) {
    return CreditCard(
      id: json['id'].toString(),  // El id en backend es Long, pero en Dart lo tratamos como String
      cardNumber: json['numero'] ?? '',  // El campo "numero" en backend
      isFrozen: json['frozen'] ?? false,  // El campo "frozen" en backend
    );
  }

  // Convertir un objeto CreditCard a JSON
  Map<String, dynamic> aJson() {
    return {
      'id': id,
      'numero': cardNumber,  // "numero" se mapea al campo "cardNumber" en el backend
      'frozen': isFrozen,
    };
  }
}
