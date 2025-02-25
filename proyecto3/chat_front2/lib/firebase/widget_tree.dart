import 'package:chat_front2/firebase/home_page.dart';
import 'package:flutter/cupertino.dart';

import 'auth.dart';
import 'login_register_page.dart';

class WidgetTree extends StatefulWidget{
  WidgetTree({Key?key}): super(key: key);
  @override
  State<WidgetTree> createState()=>_WidgetTreeState();
}

class _WidgetTreeState extends State<WidgetTree> {
  @override
  Widget build(BuildContext context) {
    return StreamBuilder(
        stream: Auth().authStateChanges,
        builder: (context, snapshot){
          if(snapshot.hasData){
            return HomePage();
          }else{
            return LoginPage();
          }
        }
    );
  }
}