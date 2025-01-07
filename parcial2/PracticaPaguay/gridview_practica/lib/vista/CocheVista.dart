import 'package:flutter/material.dart';
import 'package:gridview_practica/controlador/CocheControlador.dart';
import 'package:gridview_practica/vista/DetallesCoche.dart';

class CocheVista extends StatefulWidget{
  @override
  State<StatefulWidget> createState() =>VistaCocheState();

}

class VistaCocheState extends State<CocheVista> {
  final CocheControlador _cocheControlador=CocheControlador();
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    return Scaffold(
      appBar: AppBar(
        title: Text("Ejemplo Grid"),
        backgroundColor: Colors.cyan,
      ),
      body: GridView.builder(
        //definir como estan los elementos
          gridDelegate:SliverGridDelegateWithFixedCrossAxisCount(
            crossAxisCount: 2, // Cambia este valor según cuántas columnas deseas
          ),
          itemCount: _cocheControlador.coches.length,
          itemBuilder: (context, index){
            final item=_cocheControlador.coches[index];
            return GestureDetector(
              onTap:(){
                Navigator.push(
                    context,
                    MaterialPageRoute(
                        builder: (context)=>DetallesCoche(coche:item)
                    )

                );

              },
              child: Card(
                color: Colors.cyan,
                elevation: 8.0,
                child: Container(
                  padding: EdgeInsets.fromLTRB(15, 0, 15, 0),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: [
                      item.image,
                      //espaciado con color
                      Divider(
                        color: Colors.white,
                      ),
                      Text(
                        '${item.modelo}',
                        style: TextStyle(color: Colors.white,
                            fontWeight: FontWeight.bold,
                            fontSize: 20
                        ),

                      ),
                      Text(
                        '${item.marca}',
                        style: TextStyle(color: Colors.white
                        ),

                      ),
                    ],
                  ),
                ),
              )

            );
            return Card(
              color: Colors.cyan,
              elevation: 8.0,
              child: Container(
                padding: EdgeInsets.fromLTRB(15, 0, 15, 0),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: [
                    item.image,
                    //espaciado con color
                    Divider(
                      color: Colors.white,
                    ),
                    Text(
                        '${item.modelo}',
                      style: TextStyle(color: Colors.white,
                      fontWeight: FontWeight.bold,
                      fontSize: 20
                      ),

                    ),
                    Text(
                      '${item.marca}',
                      style: TextStyle(color: Colors.white
                      ),

                    ),
                  ],
                ),
              ),
            );
          }
      ),
    );

  }
}