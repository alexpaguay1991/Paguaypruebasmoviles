import 'package:firebase_auth/firebase_auth.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';

import 'auth.dart';

class HomePage extends StatelessWidget{
  HomePage({Key?key}): super(key: key);
  final User? user=Auth().currentUser;
  Future<void> signOut() async{
    await Auth().signOut();
  }
  Widget _title(){
    return const Text("Firebase auth");
  }
  Widget _userId(){
    return Text(user?.email ?? "User email");
  }

  Widget _signOutButton(){
    return ElevatedButton(
        onPressed: signOut,
        child: const Text("Sign out")
    );
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: _title(),
      ),
      body: Container(
        height: double.infinity,
        width: double.infinity,
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.center,
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            _userId(),
            _signOutButton()
          ],
        ),
      ),
    );
  }
  
}