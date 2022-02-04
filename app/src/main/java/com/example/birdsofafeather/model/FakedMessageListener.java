package com.example.birdsofafeather.model;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.PublishOptions;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FakedMessageListener extends MessageListener{
    private final MessageListener messageListener;
    private final ScheduledExecutorService executor;
    private static final String TAG = "NearbyDevices";
    private MessageListener listener;

    public FakedMessageListener(MessageListener realMessageListener, int frequency, String messageStr) {
        this.messageListener = realMessageListener;
        this.executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(() -> {
            Message message = new Message (messageStr.getBytes(StandardCharsets.UTF_8));
            this.messageListener.onFound(message);
            this.messageListener.onLost(message);
        },  0, frequency, TimeUnit.SECONDS);

    }

}
