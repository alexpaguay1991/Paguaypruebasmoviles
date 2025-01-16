class ClimaModelo{
  final String ciudad;
  final double temperatura;
  final String descripcion;

  ClimaModelo({required this.ciudad,required this.temperatura,required this.descripcion});
  //convertir de json a objeto
  factory ClimaModelo.fromJson(Map<String, dynamic> json){
    return ClimaModelo(
        ciudad:json['name'],
        temperatura:json['main']['temp'],
        descripcion: json['weather'][0]['description']
    );

  }


}