import 'package:chat_front2/vistas/payment_view.dart';
import 'package:chat_front2/vistas/transaction_history_view.dart';
import 'package:flutter/material.dart';
import '../modelos/user.dart';
import 'card_management_view.dart';  // Importa la vista de manejo de tarjetas

class HomeScreen extends StatefulWidget {
  final User user;

  HomeScreen({required this.user});

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Inicio')),
      body: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text('Bienvenido, ${widget.user.nombre}', style: TextStyle(fontSize: 22, fontWeight: FontWeight.bold)),
                SizedBox(height: 8),
                Text('Correo: ${widget.user.correo}', style: TextStyle(fontSize: 16)),
                Text('Saldo Disponible: \$${widget.user.saldo.toStringAsFixed(2)}', style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold, color: Colors.green)),
                SizedBox(height: 20),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    _buildShortcutButton(Icons.payment, 'Pagos', () {
                      // Navegar a la pantalla de pagos
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => PaymentScreen(usuarioId: widget.user.id), // Pasamos el userId
                        ),
                      );
                    }),
                    _buildShortcutButton(Icons.history, 'Historial', () {
                      // Navegar a la pantalla de historial de transacciones
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => TransactionHistoryView(userId: widget.user.id), // Pasamos el userId
                        ),
                      );
                    }),
                    _buildShortcutButton(Icons.credit_card, 'Tarjetas', () {
                      // Navegar a la pantalla de Manejo de Tarjetas
                      Navigator.push(
                        context,
                        MaterialPageRoute(
                          builder: (context) => CardManagementView(userId: widget.user.id), // Pasamos el userId
                        ),
                      );
                    }),
                  ],
                ),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildShortcutButton(IconData icon, String label, VoidCallback onPressed) {
    return Column(
      children: [
        IconButton(
          icon: Icon(icon, size: 30, color: Colors.blue),
          onPressed: onPressed,
        ),
        Text(label, style: TextStyle(fontSize: 14)),
      ],
    );
  }
}
