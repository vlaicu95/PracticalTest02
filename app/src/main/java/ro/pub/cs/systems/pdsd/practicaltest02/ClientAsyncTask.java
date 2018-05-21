package ro.pub.cs.systems.pdsd.practicaltest02;

/**
 * Created by student on 21.05.2018.
 */

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import ro.pub.cs.systems.pdsd.practicaltest02.General.Constants;
import ro.pub.cs.systems.pdsd.practicaltest02.General.Utilities;


public class ClientAsyncTask extends AsyncTask<String, String, Void> {

    private TextView serverMessageTextView;

    public ClientAsyncTask(TextView serverMessageTextView) {
        this.serverMessageTextView = serverMessageTextView;
    }

    @Override
    protected Void doInBackground(String... params) {
        Socket socket = null;
        try {
            String serverAddress = "localhost";
            int serverPort = Integer.parseInt(params[0]);
            socket = new Socket(serverAddress, serverPort);
            if (socket == null) {
                return null;
            }
            Log.v(Constants.TAG, "Connection opened with " + socket.getInetAddress() + ":" + socket.getLocalPort());
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (params[1].equals("put")) {
                printWriter.println(params[1]);
                printWriter.flush();
                printWriter.println(params[2]);
                printWriter.flush();
                printWriter.println(params[3]);
                printWriter.flush();
            } else if (params[1].equals("get")) {
                printWriter.println(params[1]);
                printWriter.flush();
                printWriter.println(params[2]);
                printWriter.flush();
                String currentLine;
                while ((currentLine = bufferedReader.readLine()) != null) {
                    Log.v("CLIENT", currentLine);
                    publishProgress(currentLine);
                }
            }

        } catch (IOException ioException) {
            Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                Log.v(Constants.TAG, "Connection closed");
            } catch (IOException ioException) {
                Log.e(Constants.TAG, "An exception has occurred: " + ioException.getMessage());
                if (Constants.DEBUG) {
                    ioException.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        serverMessageTextView.setText("");
    }

    @Override
    protected void onProgressUpdate(String... progress) {
        serverMessageTextView.append(progress[0] + "\n");
    }

}

