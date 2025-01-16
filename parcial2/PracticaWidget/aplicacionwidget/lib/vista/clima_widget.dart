import 'package:flutter/material.dart';
import '../controlador/clima_controlador.dart';
import '../modelo/clima_modelo.dart';
import 'clima_vista.dart';

class ClimaWidget extends StatefulWidget {
  @override
  ClimaWidgetState createState() => ClimaWidgetState();
}

class ClimaWidgetState extends State<ClimaWidget> {
  final TextEditingController ciudadController = TextEditingController();
  final ClimaControlador climaControlador = ClimaControlador();
  ClimaModelo? climaModelo;
  bool isLoading = false;
  String? errorMessage;

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text("Clima")),
      body: Center(
        child: Padding(
          padding: const EdgeInsets.all(16.0),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            children: [
              TextField(
                controller: ciudadController,
                decoration: InputDecoration(
                  hintText: "Ingrese ciudad",
                  border: OutlineInputBorder(),
                  filled: true,
                  fillColor: Colors.white,
                ),
              ),
              SizedBox(height: 10),
              ElevatedButton(
                onPressed: () async {
                  setState(() {
                    isLoading = true;
                    errorMessage = null; // Reset error message
                  });
                  final clima = await climaControlador.obtenerClima(ciudadController.text);
                  setState(() {
                    climaModelo = clima;
                    isLoading = false;
                    if (climaModelo == null) {
                      errorMessage = "Ciudad no encontrada. Intente de nuevo.";
                    }
                  });
                },
                child: Text("Obtener Clima"),
              ),
              SizedBox(height: 20),
              if (isLoading) CircularProgressIndicator(),
              if (errorMessage != null)
                Text(
                  errorMessage!,
                  style: TextStyle(color: Colors.red),
                ),
              if (climaModelo != null) ...[
                SizedBox(height: 20),
                ClimaVista(
                  ciudad: climaModelo!.ciudad,
                  temperatura: climaModelo!.temperatura,
                  descripcion: climaModelo!.descripcion,
                ),
              ],
            ],
          ),
        ),
      ),
    );
  }
}
