import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import '../logica/Digitos.dart'; // Asegúrate de que esta ruta sea correcta.

class DigitosView extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => DigitosViewState();
}

class DigitosViewState extends State<DigitosView> {
  final TextEditingController _numeroController = TextEditingController();
  String _resultadoDigitos = "";
  String _resultadoSuma = "";
  // Instanciar clase
  final Digitos _digitos = Digitos();

  // Método para mostrar la suma de los dígitos
  void _mostrarSumaDigitos() {
    final numero = int.tryParse(_numeroController.text);

    if (numero == null || numero < 0 || numero > 9999) {
      setState(() {
        _resultadoSuma = "Ingrese un número válido (máximo 4 dígitos)";
      });
    } else {
      try {
        // Llamar a la función mostrarSumaDeDigitos y obtener la suma
        final suma = _digitos.mostrarSumaDeDigitos(numero);
        setState(() {
          _resultadoSuma = "La suma de los dígitos es: $suma";
        });
      } catch (e) {
        // En caso de que haya un error (por ejemplo, número fuera de rango)
        setState(() {
          _resultadoSuma = e.toString();
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Mostrar Suma de Dígitos de un número"),
        backgroundColor: Colors.blueAccent,
      ),
      body: Padding(
        padding: EdgeInsets.all(20.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            TextField(
              controller: _numeroController,
              decoration: InputDecoration(
                labelText: "Número (máximo 4 dígitos)",
                labelStyle: TextStyle(color: Colors.blueAccent),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                  borderSide: BorderSide(color: Colors.blueAccent),
                ),
                focusedBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(12),
                  borderSide: BorderSide(color: Colors.blueAccent, width: 2),
                ),
              ),
              keyboardType: TextInputType.number,
            ),
            SizedBox(height: 20),
            ElevatedButton.icon(
              onPressed: _mostrarSumaDigitos,
              icon: Icon(Icons.calculate, size: 24, color: Colors.white), // Ícono de cálculo
              label: Text("Mostrar Suma de Dígitos", style: TextStyle(fontSize: 16, color: Colors.white)),
              style: ElevatedButton.styleFrom(
                backgroundColor: Colors.blueAccent,
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(12),
                ),
                elevation: 5,
                padding: EdgeInsets.symmetric(vertical: 14),
              ),
            ),
            SizedBox(height: 20),
            Text(
              _resultadoDigitos,
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
                color: Colors.black87,
                shadows: [
                  Shadow(
                    blurRadius: 4.0,
                    color: Colors.black54,
                    offset: Offset(2.0, 2.0),
                  ),
                ],
              ),
              textAlign: TextAlign.center,
            ),
            SizedBox(height: 10),
            Text(
              _resultadoSuma,
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
                color: Colors.green,
                shadows: [
                  Shadow(
                    blurRadius: 4.0,
                    color: Colors.black54,
                    offset: Offset(2.0, 2.0),
                  ),
                ],
              ),
              textAlign: TextAlign.center,
            ),
          ],
        ),
      ),
    );
  }
}
