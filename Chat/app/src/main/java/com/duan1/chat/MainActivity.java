package com.duan1.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button btnLogin, btnChat;
    private EditText editText;
    private List<String> listMsg = new ArrayList<>();
    private ChatAdapter adapter;
    private final String URL_SERVER="http://192.168.5.116:3000";
    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        btnChat = findViewById(R.id.btnChat);
        btnLogin = findViewById(R.id.btnLogin);
        editText = findViewById(R.id.edt_name);
        listMsg.add("Bat dau chat nao");
        connectSocket();
        //mSocket.connect();
        mSocket.on("receiver_message",onNewMessage);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(this,listMsg);
        recyclerView.setAdapter(adapter);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("user_login",editText.getText().toString());

            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("send_message",editText.getText().toString());


            }
        });

    }
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject)args[0];
                    String message;
                    message = data.optString("data");
                    adapter.addMessage(message);

                }
            });
        }
    };
    private void connectSocket()
    {
        try {
            mSocket = IO.socket(URL_SERVER);
            mSocket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
