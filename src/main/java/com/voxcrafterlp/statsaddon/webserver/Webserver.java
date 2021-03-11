package com.voxcrafterlp.statsaddon.webserver;

import com.sun.net.httpserver.HttpServer;
import com.voxcrafterlp.statsaddon.webserver.api.APIHandler;
import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;

@Getter
public class Webserver {

    private HttpServer webserver;

    public Webserver() {
        try {
            webserver = HttpServer.create(new InetSocketAddress(3412), 0);

            webserver.createContext("/api", new APIHandler());
            webserver.createContext("/", new MainHandler());
            webserver.setExecutor(null);
            webserver.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
