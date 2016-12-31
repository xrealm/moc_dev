package com.mao.dev.ui.port;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mao.dev.AppKit;
import com.mao.dev.R;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.DatagramChannel;

/**
 * Created by mpg on 2016/12/20 .
 */

public class PortTestActivity extends AppCompatActivity {

    Button btnStart;
    TextView tvPort;
    EditText etPort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_port_test);
        etPort = (EditText) findViewById(R.id.et_port);
        tvPort = (TextView) findViewById(R.id.tv_port);
        btnStart = (Button) findViewById(R.id.btn_start);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                findPort();
//                ping("192.167.1.100");
                parseHost();
            }
        });
    }

    private void parseHost() {
        String hostIp = AppKit.getHostIp();
        String text = hostIp + ":8888";
        InetSocketAddress address = new InetSocketAddress(hostIp, 8888);
        try {
            URL url = new URL(text);
            Log.d("mao", "host=" + url.getHost());
            Log.d("mao", "port=" + url.getPort());

        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * ping ip
     */
    private void ping(final String hostIp) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Process process = Runtime.getRuntime().exec("ping -c 1 -w 5 " + hostIp);
                    int status = process.waitFor();
                    if (status == 0) {
                        Log.d("mao", "ping ip success : " + hostIp);
                        tvPort.post(new Runnable() {
                            @Override
                            public void run() {
                                tvPort.setText(hostIp);
                            }
                        });
                    } else {
                        Log.d("mao", "连接失败");
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.d("mao", "连接异常");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d("mao", "连接中断");
                }
            }
        }).start();

    }

    private void findPort() {
        String content = etPort.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return;
        }
        final int port = Integer.parseInt(content);

        new Thread(new Runnable() {
            @Override
            public void run() {
                startFindPort(port);
            }
        }).start();
    }

    private void startFindPort(int port) {
        String hostIp = AppKit.getHostIp();
        boolean find = false;
        do {
            try {
                Socket socket = new Socket();
                socket.bind(new InetSocketAddress(port));
                find = true;
                foundPort("端口 " + port + " 可用");
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("mao", "connect error");
                foundPort("出错出错");
            }
            port++;
        } while (port<3000);

//        for (int i = 0; i < 500; i++) {
//            try {
//                Socket socket = new Socket();
//                SocketAddress socketAddress = new InetSocketAddress(i);
//                socket.connect(socketAddress, 50);
//                Log.d("mao", "端口 " + i + " 可用");
//                socket.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.d("mao", "connect error");
//            }
//        }
    }

    private void foundPort(final String text) {
        tvPort.post(new Runnable() {
            @Override
            public void run() {
                tvPort.setText(text);
            }
        });
    }

    private void udpScan() {
        String host = AppKit.getHostIp();
        try {
            DatagramSocket socket = new DatagramSocket();
            DatagramChannel channel = DatagramChannel.open();
            channel.connect(new InetSocketAddress(host, 9999));
            channel.configureBlocking(true);

            socket.setSoTimeout(5000);
//            socket.send(send);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
