
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

class ClimaVista extends StatelessWidget{
  final String ciudad;
  final double temperatura;
  final String descripcion;

  ClimaVista({
    required this.ciudad,
    required this.temperatura,
    required this.descripcion
});
  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: 10,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(20)
      ),
      child: Container(
        padding: EdgeInsets.all(16),
        width: 300,
        decoration: BoxDecoration(
          gradient: LinearGradient(
            colors: [Colors.blue,Colors.grey],
            begin: Alignment.topLeft,
            end: Alignment.bottomLeft
          )
        ),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Text(ciudad,
              style:TextStyle(
                fontWeight: FontWeight.bold,
                fontSize: 24,
                color: Colors.white
              )
            ),
            SizedBox(height: 20,),
            Icon(Icons.sunny, size: 50,color: Colors.yellowAccent,),
            SizedBox(height: 20,),
            Text("${temperatura.toStringAsFixed(1)} °C",
                style:TextStyle(
                    fontWeight: FontWeight.normal,
                    fontSize: 24,
                    color: Colors.white
                )
            ),
            SizedBox(height: 20,),
            Text(descripcion,
                style:TextStyle(
                    fontWeight: FontWeight.normal,
                    fontSize: 24,
                    color: Colors.white
                )
            ),
          ],
        ),
      ),

    );
  }

}