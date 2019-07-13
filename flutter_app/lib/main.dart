import 'package:flutter/material.dart';
import 'package:english_words/english_words.dart';
import 'package:flutter_app/RandomWordsState.dart';

import 'LearnLayout.dart';
//void main()=> runApp(new MyApp());
void main() {
//  runApp(new MyApp());
  runApp(new FlutterLayout());
}
class MyApp extends StatelessWidget {
  @override

    Widget build(BuildContext context) {
      return new MaterialApp(
        title: "MaterialApp title：Startup Name Generator",
        theme: new ThemeData(
          primaryColor: Colors.amberAccent,
        ),
        home:new RandomWords(),
      );
  }
}