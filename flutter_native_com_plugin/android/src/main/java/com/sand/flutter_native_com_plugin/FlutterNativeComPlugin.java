package com.sand.flutter_native_com_plugin;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.sand.pollingsdk.PollingService;
import com.sand.pollingsdk.models.Message;
import com.sand.pollingsdk.models.Messages;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * FlutterNativeComPlugin
 */
public class FlutterNativeComPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private static final String CHANNEL = "flutter_native_com_plugin";
    private MethodChannel channel;
    PollingService pollingService;
    Gson gson;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        pollingService = new PollingService(this::sendUpdates);
        gson = new Gson();
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), CHANNEL);
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        if (call.method.equals("getMessage")) {
            Messages messages = gson.fromJson((String) call.argument("messages"), Messages.class);
            String message = getMessage(messages);
            if (message != null) {
                result.success(message);
            } else {
                result.error("UNAVAILABLE", "Error getting messages.", null);
            }
        } else if (call.method.equals("testPolling")) {
            pollingService.testPolling();
        } else {
            result.notImplemented();
        }

    }

    private String getMessage(Messages messages) {
        return new Gson().toJson(pollingService.getMessage(messages));
    }

    private void sendUpdates(Message message) {
        channel.invokeMethod("getUpdates", gson.toJson(message), new MethodChannel.Result() {
            @Override
            public void success(Object o) {
                System.out.println("dart method called successfully");
            }

            @Override
            public void error(String s, String s1, Object o) {
                System.err.println(s + " => " + s1);
            }

            @Override
            public void notImplemented() {
                System.err.println("not implemented");
            }
        });
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }
}
