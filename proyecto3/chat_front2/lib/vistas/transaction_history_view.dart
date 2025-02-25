import 'package:flutter/material.dart';
import 'package:pdf/pdf.dart';
import 'package:pdf/widgets.dart' as pw;
import 'package:printing/printing.dart';
import '../controladores/card_controller.dart';
import '../controladores/payment_controller.dart';
import '../modelos/payment.dart';
import '../modelos/card.dart';

class TransactionHistoryView extends StatefulWidget {
  final String userId;

  TransactionHistoryView({required this.userId});

  @override
  _TransactionHistoryViewState createState() => _TransactionHistoryViewState();
}

class _TransactionHistoryViewState extends State<TransactionHistoryView> {
  DateTime? startDate;
  DateTime? endDate;
  String selectedCardNumber = 'Todas';
  final PaymentController _paymentController = PaymentController();
  final CardController _cardController = CardController();
  Map<String, String> cardNumbers = {}; // Mapa para almacenar los números de tarjeta

  @override
  void initState() {
    super.initState();
    _loadUserCards();
  }

  Future<void> _loadUserCards() async {
    final cards = await _cardController.fetchCards(widget.userId);
    setState(() {
      cardNumbers = {for (var card in cards) card.id: card.cardNumber};
    });
  }

  Future<void> generatePdf(List<Payment> payments) async {
    final pdf = pw.Document();

    pdf.addPage(
      pw.Page(
        build: (pw.Context context) {
          return pw.Column(
            children: [
              pw.Text('Resumen de Transacciones', style: pw.TextStyle(fontSize: 24)),
              pw.SizedBox(height: 20),
              pw.Table(
                border: pw.TableBorder.all(),
                children: [
                  pw.TableRow(
                    children: [
                      pw.Padding(padding: const pw.EdgeInsets.all(8.0), child: pw.Text('ID')),
                      pw.Padding(padding: const pw.EdgeInsets.all(8.0), child: pw.Text('Monto')),
                      pw.Padding(padding: const pw.EdgeInsets.all(8.0), child: pw.Text('Número de Tarjeta')),
                      pw.Padding(padding: const pw.EdgeInsets.all(8.0), child: pw.Text('Fecha')),
                    ],
                  ),
                  ...payments.map((payment) {
                    return pw.TableRow(
                      children: [
                        pw.Padding(padding: const pw.EdgeInsets.all(8.0), child: pw.Text('${payment.usuarioId}')),
                        pw.Padding(padding: const pw.EdgeInsets.all(8.0), child: pw.Text('\$${payment.monto}')),
                        pw.Padding(padding: const pw.EdgeInsets.all(8.0), child: pw.Text(cardNumbers[payment.tarjetaId] ?? 'Desconocido')),
                        pw.Padding(padding: const pw.EdgeInsets.all(8.0), child: pw.Text('${payment.fecha}')),
                      ],
                    );
                  }).toList(),
                ],
              ),
            ],
          );
        },
      ),
    );

    await Printing.layoutPdf(onLayout: (PdfPageFormat format) async => pdf.save());
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Historial de Transacciones')),
      body: Column(
        children: [
          Row(
            children: [
              ElevatedButton(
                onPressed: () async {
                  final date = await showDatePicker(
                    context: context,
                    initialDate: DateTime.now(),
                    firstDate: DateTime(2000),
                    lastDate: DateTime.now(),
                  );
                  if (date != null) {
                    setState(() {
                      startDate = date;
                    });
                  }
                },
                child: Text(startDate == null ? 'Fecha Inicio' : '${startDate!.toLocal()}'.split(' ')[0]),
              ),
              ElevatedButton(
                onPressed: () async {
                  final date = await showDatePicker(
                    context: context,
                    initialDate: DateTime.now(),
                    firstDate: DateTime(2000),
                    lastDate: DateTime.now(),
                  );
                  if (date != null) {
                    setState(() {
                      endDate = date;
                    });
                  }
                },
                child: Text(endDate == null ? 'Fecha Fin' : '${endDate!.toLocal()}'.split(' ')[0]),
              ),
              DropdownButton<String>(
                value: selectedCardNumber,
                items: ['Todas', ...cardNumbers.values].map((String value) {
                  return DropdownMenuItem<String>(
                    value: value,
                    child: Text(value),
                  );
                }).toList(),
                onChanged: (String? newValue) {
                  setState(() {
                    selectedCardNumber = newValue!;
                  });
                },
              ),
              ElevatedButton(
                onPressed: () {
                  setState(() {}); // Refrescar para aplicar filtros
                },
                child: Text('Filtrar'),
              ),
              ElevatedButton(
                onPressed: () async {
                  List<Payment> payments = await _paymentController.fetchPayments(widget.userId);
                  await generatePdf(payments);
                },
                child: Text('Descargar PDF'),
              ),
            ],
          ),
          Expanded(
            child: FutureBuilder<List<Payment>>(
              future: _paymentController.fetchPayments(widget.userId),
              builder: (context, snapshot) {
                if (snapshot.connectionState == ConnectionState.waiting) {
                  return Center(child: CircularProgressIndicator());
                }

                if (snapshot.hasError) {
                  return Center(child: Text('Error al cargar las transacciones: ${snapshot.error}'));
                }

                if (!snapshot.hasData || snapshot.data!.isEmpty) {
                  return Center(child: Text('No tienes transacciones.'));
                }

                final List<Payment> payments = snapshot.data!;

                // Aplicar filtros
                List<Payment> filteredPayments = payments.where((payment) {
                  final DateTime paymentDate = DateTime.parse(payment.fecha); // Convertir fecha
                  bool dateInRange = (startDate == null || paymentDate.isAfter(startDate!)) &&
                      (endDate == null || paymentDate.isBefore(endDate!));

                  bool cardMatches = selectedCardNumber == 'Todas' || cardNumbers[payment.tarjetaId] == selectedCardNumber;

                  return dateInRange && cardMatches;
                }).toList();

                return ListView.builder(
                  itemCount: filteredPayments.length,
                  itemBuilder: (context, index) {
                    final payment = filteredPayments[index];
                    return ListTile(
                      title: Text('ID: ${payment.usuarioId}'),
                      subtitle: Text('Monto: \$${payment.monto}'),
                      trailing: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: [
                          Text('Tarjeta: ${cardNumbers[payment.tarjetaId] ?? 'Desconocido'}'),
                          Text('Fecha: ${payment.fecha}'),
                        ],
                      ),
                      onTap: () {},
                    );
                  },
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
