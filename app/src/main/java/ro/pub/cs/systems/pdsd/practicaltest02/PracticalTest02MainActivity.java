package ro.pub.cs.systems.pdsd.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ro.pub.cs.systems.pdsd.practicaltest02.General.Constants;

public class PracticalTest02MainActivity extends AppCompatActivity {

    private ServerThread serverThread = null;
    private Button connect;
    private EditText port;
    private EditText key1;
    private EditText val;
    private EditText key2;
    private Button put;
    private Button get;
    private TextView output;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        connect = (Button)this.findViewById(R.id.button);


        put = (Button)this.findViewById(R.id.button2);
        get = (Button)this.findViewById(R.id.button3);
        port = (EditText)this.findViewById(R.id.port_text);
        key1 = (EditText)this.findViewById(R.id.key1);
        val = (EditText)this.findViewById(R.id.key2);
        key2 = (EditText)this.findViewById(R.id.key3);
        output = (TextView)this.findViewById(R.id.textView3);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (port.getText().toString() != null && !port.getText().toString().equals("") &&
                        serverThread == null) {
                    serverThread = new ServerThread(output, port.getText().toString());
                    serverThread.startServer();
                    Log.v(Constants.TAG, "Starting server...");
                }
            }
        });

        put.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientAsyncTask clientAsyncTask = new ClientAsyncTask(output);
                clientAsyncTask.execute(port.getText().toString(), "put", key1.getText().toString(), val.getText().toString());
            }
        });

        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientAsyncTask clientAsyncTask = new ClientAsyncTask(output);
                clientAsyncTask.execute(port.getText().toString(), "get", key2.getText().toString());
            }
        });
    }

    @Override
    public void onDestroy() {
        if (serverThread != null) {
            serverThread.stopServer();
        }
        super.onDestroy();
    }
}
