package com.example.birdsofafeather.model.db;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

public class Wave {
    private static final String TAG = "BoaF_Wave";
    public String uuid;
    public boolean waveAt;

    public Wave(String uuid, boolean waveAt) {
        this.uuid = uuid;
        this.waveAt = waveAt;
    }

    public Wave(byte[] bytes) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        int uuidLength = inputStream.read();
        byte[] uuidBytes = new byte[uuidLength];
        if (inputStream.read(uuidBytes, 0, uuidLength) < uuidLength) {
            Log.e(TAG, "Reading uuid, expected " + uuidLength + "bytes and got less.");
            throw new IllegalArgumentException("Invalid bytearray!");
        }
        this.uuid = new String(uuidBytes, StandardCharsets.US_ASCII);

        int fromWave = inputStream.read();
        if(fromWave == (byte) 1) {
            this.waveAt = true;
        } else {
            this.waveAt = false;
        }
    }

    public byte[] toByteArray() {
        byte[] uuidBytes = uuid.getBytes(StandardCharsets.UTF_8);
        byte[] waveAtByte = new byte[] {waveAt ? (byte) 1 : 0};

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(uuidBytes.length);
        outputStream.write(uuidBytes, 0, uuidBytes.length);

        outputStream.write(waveAtByte, 0, 1);

        return outputStream.toByteArray();
    }
}
