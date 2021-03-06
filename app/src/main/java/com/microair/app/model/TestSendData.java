package com.microair.app.model;

import com.xuhao.didi.core.iocore.interfaces.ISendable;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public class TestSendData implements ISendable {

    private String str = "";
    private int cmd;

    public TestSendData(int cmd,String data) {
        this.cmd = cmd;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("cmd", cmd);
            jsonObject.put("data", data);
            str = jsonObject.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public byte[] parse() {
        //Build the byte array according to the server's parsing rules
        byte[] payload = str.getBytes(Charset.defaultCharset());
        //4 is package header fixed length and payload length
        ByteBuffer bb = ByteBuffer.allocate(4 + payload.length);
        bb.order(ByteOrder.BIG_ENDIAN);
        bb.putInt(payload.length);
        bb.put(payload);
        return bb.array();
    }
}
