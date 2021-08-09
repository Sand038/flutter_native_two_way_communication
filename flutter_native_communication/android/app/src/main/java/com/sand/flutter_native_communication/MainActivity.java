package com.sand.flutter_native_communication;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.sand.pollingsdk.PollingService;
import com.sand.pollingsdk.models.Message;
import com.sand.pollingsdk.models.Messages;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;

public class MainActivity extends FlutterActivity {
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        super.configureFlutterEngine(flutterEngine);
    }
}
