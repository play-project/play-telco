package com.orange.play.gcmAPI;

import java.io.IOException;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

public class Send {

    public static Result sendMessage(String registrationId, String messageStr) throws IOException {
        Sender sender = new Sender(Configuration.API_KEY);
        Message.Builder builder = new Message.Builder();
        builder.addData(Configuration.PAYLOAD_KEY, messageStr);
        Message message = builder.build();
        Result result = sender.send(message, registrationId, Configuration.MAX_ATTEMPS);
        return result;
    }
}
