// Copyright 2014 The Flutter Authors. All rights reserved.
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter_native_com_plugin/flutter_native_com_plugin.dart';
import 'package:flutter_native_com_plugin/models/message.dart';
import 'package:flutter_native_com_plugin/models/messages_with_count.dart';


class FlutterJavaTwoWayMessaging extends StatefulWidget {
  const FlutterJavaTwoWayMessaging({Key? key}) : super(key: key);

  @override
  State<FlutterJavaTwoWayMessaging> createState() =>
      _FlutterJavaTwoWayMessagingState();
}

class _FlutterJavaTwoWayMessagingState
    extends State<FlutterJavaTwoWayMessaging> {
  Message _inputMessage = new Message('', 0);
  String _resultMessage = 'No messages received yet!';
  int _count = 0;
  List<String> _messages = [];
  late FlutterNativeComPlugin _flutterNativeComPluginService;
  String _pollingResult = 'No results received yet';

  Future<void> _getMessage() async {
    try {
      _inputMessage = getInputMessage(_count++);
      MessagesWithCount messageWithCount =
          await _flutterNativeComPluginService.getRandomMessage(_inputMessage);
      setState(() {
        _resultMessage =
            '${messageWithCount.messages[0].text} => ${messageWithCount.count}';
        _messages.add(_resultMessage);
      });
      print(_resultMessage);
    } on PlatformException catch (e) {
      print(e);
    }
  }

  Message getInputMessage(int _count) =>
      Message("message " + _count.toString(), _count);

  void processUpdates(Message message) {
    setState(() {
      _pollingResult = message.text + message.value.toString();
    });
  }

  @override
  void initState() {
    super.initState();
    _flutterNativeComPluginService = new FlutterNativeComPlugin(processUpdates);
    _flutterNativeComPluginService.startTestPolling();
  }

  @override
  Widget build(BuildContext context) {
    return Material(
      child: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            Text(
                'Input message : ${_inputMessage.text} => ${_inputMessage.value}'),
            ElevatedButton(
              child: Text('Get updated message'),
              onPressed: _getMessage,
            ),
            Text('Result message : $_resultMessage'),
            Text(_pollingResult),
          ],
        ),
      ),
    );
  }
}

void main() {
  runApp(const MaterialApp(home: FlutterJavaTwoWayMessaging()));
}
