package ro.pub.cs.systems.pdsd.practicaltest02;

/**
 * Created by student on 21.05.2018.
 */

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

import ro.pub.cs.systems.pdsd.practicaltest02.General.Constants;


public class ServerThread extends Thread {

    private boolean isRunning;
    public static HashMap<String,String> map = new HashMap<>();
    private ServerSocket serverSocket;

    private TextView serverTextEditText;
    private String port;
    public ServerThread(TextView serverTextEditText, String port) {
        this.serverTextEditText = serverTextEditText;
        this.port = port;
    }

    public void startServer() {
        isRunning = true;
        start();
        Log.v(Constants.TAG, "startServer() method was invoked");
    }

    public void stopServer() {
        isRunning = false;
        try {
            serverSocket.close();
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
        Log.v(Constants.TAG, "stopServer() method was invoked");
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(Integer.parseInt(this.port));
            while (isRunning) {
                Socket socket = serverSocket.accept();
                if (socket != null) {
                    CommunicationThread communicationThread = new CommunicationThread(socket, serverTextEditText);
                    communicationThread.start();
                }
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        }
    }
}
