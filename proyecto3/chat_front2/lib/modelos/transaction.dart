class Transaction {
  final String id;
  final double amount;
  final DateTime date;

  Transaction({required this.id, required this.amount, required this.date});

  // Convertir JSON a un objeto Transaction
  factory Transaction.desdeJson(Map<String, dynamic> json) {
    return Transaction(
      id: json['id'] ?? '',
      amount: (json['amount'] is num) ? json['amount'].toDouble() : 0.0,
      date: DateTime.tryParse(json['date'] ?? '') ?? DateTime.now(),
    );
  }

  // Convertir un objeto Transaction a JSON
  Map<String, dynamic> aJson() {
    return {
      'id': id,
      'amount': amount,
      'date': date.toIso8601String(),
    };
  }
}
