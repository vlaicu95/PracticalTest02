package ro.pub.cs.systems.pdsd.practicaltest02;

/**
 * Created by student on 21.05.2018.
 */

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import ro.pub.cs.systems.pdsd.practicaltest02.General.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02.General.Utilities;


public class CommunicationThread extends Thread {

    private Socket socket;
    private TextView serverTextEditText;

    public CommunicationThread(Socket socket, TextView serverTextEditText) {
        this.socket = socket;
        this.serverTextEditText = serverTextEditText;
    }

    @Override
    public void run() {
        try {
            Log.v(Constants.TAG, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());
            PrintWriter printWriter = Utilities.getWriter(socket);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            String currentLine;
            String method = "";
            String key = "";
            String value = "";
            int i = 0;
            while ((currentLine = bufferedReader.readLine()) != null) {
                Log.v(Constants.TAG, currentLine);
                if (i == 0) {
                    method = currentLine;
                } else if (i == 1) {
                    key = currentLine;
                } else if (i == 0) {
                    value = currentLine;
                }
                i++;
            }

            if (method.equals("put")) {
                ServerThread.map.put(key,value);
            } else if (method.equals("get")) {
                printWriter.println(ServerThread.map.get(key));
                printWriter.flush();
            }

            socket.close();
            Log.v(Constants.TAG, "Conenction closed");
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }

}
