import 'dart:async';
import 'dart:convert';

import 'package:flutter/services.dart';
import 'package:flutter_native_com_plugin/models/message.dart';
import 'package:flutter_native_com_plugin/models/messages.dart';
import 'package:flutter_native_com_plugin/models/messages_with_count.dart';

class FlutterNativeComPlugin {
  static const MethodChannel _channel =
      const MethodChannel('flutter_native_com_plugin');

  Function(Message message) _processUpdates;

  FlutterNativeComPlugin(this._processUpdates) {
    _channel.setMethodCallHandler(_randomMessageHandler);
  }

  Future<MessagesWithCount> getRandomMessage(Message _inputMessage) async {
    var inputMessages = jsonEncode(Messages([_inputMessage]));
    String response =
        await _channel.invokeMethod('getMessage', {"messages": inputMessages});
    MessagesWithCount messageWithCount =
        MessagesWithCount.fromJson(jsonDecode(response));
    messageWithCount = messageWithCount;
    return messageWithCount;
  }

  Future<void> startTestPolling() async {
    await _channel.invokeMethod('testPolling');
  }

  Future<dynamic> _randomMessageHandler(MethodCall methodCall) async {
    if (methodCall.method == 'getUpdates') {
      _processUpdates(Message.fromJson(jsonDecode(methodCall.arguments)));
    } else {
      throw MissingPluginException('notImplemented');
    }
  }
}
