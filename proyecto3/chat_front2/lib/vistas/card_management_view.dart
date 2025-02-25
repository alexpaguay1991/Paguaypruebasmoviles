import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import '../controladores/card_controller.dart';
import '../modelos/card.dart';

class CardManagementView extends StatefulWidget {
  final String userId;

  CardManagementView({required this.userId});

  @override
  _CardManagementViewState createState() => _CardManagementViewState();
}

class _CardManagementViewState extends State<CardManagementView> {
  late Future<List<CreditCard>> futureCards;
  final CardController controller = CardController();

  @override
  void initState() {
    super.initState();
    futureCards = controller.fetchCards(widget.userId);
  }

  // Función para agregar una tarjeta
  void _addCard(String cardNumber) async {
    setState(() {
      // Crear la nueva tarjeta con los datos proporcionados
      CreditCard newCard = CreditCard(
        id: '',  // El id se manejará automáticamente por el backend
        cardNumber: cardNumber,
        isFrozen: false,  // Inicialmente no está congelada
      );

      // Llamamos al controlador para agregar la tarjeta (ahora pasando el userId)
      controller.addCard(widget.userId, newCard).then((_) {
        // Si la tarjeta se agrega correctamente, actualizamos la lista de tarjetas
        setState(() {
          futureCards = controller.fetchCards(widget.userId); // Actualiza la lista de tarjetas
        });
      }).catchError((error) {
        // Manejo de errores si no se pudo agregar la tarjeta
        ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Error al agregar tarjeta: $error')));
      });
    });
  }

  // Función para eliminar una tarjeta
  void _deleteCard(String cardId) async {
    await controller.deleteCard(cardId); // Eliminar la tarjeta
    setState(() {
      futureCards = controller.fetchCards(widget.userId); // Actualizar las tarjetas
    });
  }

  // Función para validar el número de la tarjeta usando el algoritmo de Luhn
  bool isValidCardNumber(String cardNumber) {
    // Eliminar los espacios en blanco si existen
    cardNumber = cardNumber.replaceAll(RegExp(r'\s+'), '');

    // Verificar si el número de tarjeta tiene al menos 13 dígitos
    if (cardNumber.length < 13 || cardNumber.length > 19) {
      return false;
    }

    int sum = 0;
    bool shouldDouble = false;

    // Recorrer los dígitos de derecha a izquierda
    for (int i = cardNumber.length - 1; i >= 0; i--) {
      int digit = int.parse(cardNumber[i]);

      if (shouldDouble) {
        digit *= 2;
        if (digit > 9) {
          digit -= 9;
        }
      }

      sum += digit;
      shouldDouble = !shouldDouble;
    }

    // Si la suma total es divisible por 10, la tarjeta es válida
    return sum % 10 == 0;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Manejo de Tarjetas')),
      body: FutureBuilder<List<CreditCard>>(
        future: futureCards,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(child: CircularProgressIndicator());
          } else if (snapshot.hasError) {
            return Center(child: Text('Error: ${snapshot.error}'));
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return Center(child: Text('No hay tarjetas disponibles.'));
          }

          final cards = snapshot.data!;

          return ListView.builder(
            itemCount: cards.length,
            itemBuilder: (context, index) {
              final card = cards[index];
              return Card(
                child: ListTile(
                  title: Text('Número de Tarjeta: ${card.cardNumber}'),
                  subtitle: Text(card.isFrozen ? 'Congelada' : 'Activa'),
                  trailing: Row(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      // Botón para congelar tarjeta
                      IconButton(
                        icon: Icon(card.isFrozen ? Icons.lock : Icons.lock_open),
                        onPressed: () async {
                          await controller.freezeCard(card.id);
                          setState(() {
                            card.isFrozen = !card.isFrozen;
                          });
                        },
                      ),
                      // Botón para eliminar tarjeta
                      IconButton(
                        icon: Icon(Icons.delete, color: Colors.red),
                        onPressed: () {
                          _deleteCard(card.id);
                        },
                      ),
                    ],
                  ),
                ),
              );
            },
          );
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () => _showAddCardDialog(context), // Abre el formulario para agregar tarjeta
        child: Icon(Icons.add),
        tooltip: 'Agregar Tarjeta',
      ),
    );
  }

  // Mostrar el formulario para agregar tarjeta
  void _showAddCardDialog(BuildContext context) {
    TextEditingController _cardNumberController = TextEditingController();

    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('Agregar Tarjeta'),
          content: TextField(
            controller: _cardNumberController,
            decoration: InputDecoration(labelText: 'Número de Tarjeta'),
          ),
          actions: <Widget>[
            TextButton(
              child: Text('Cancelar'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            TextButton(
              child: Text('Agregar'),
              onPressed: () {
                String cardNumber = _cardNumberController.text;
                if (cardNumber.isNotEmpty) {
                  // Validar el número de tarjeta antes de agregarlo
                  if (isValidCardNumber(cardNumber)) {
                    _addCard(cardNumber);  // Llamar a la función para agregar la tarjeta
                  } else {
                    ScaffoldMessenger.of(context).showSnackBar(
                      SnackBar(content: Text('Número de tarjeta inválido.')),
                    );
                  }
                }
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }
}
