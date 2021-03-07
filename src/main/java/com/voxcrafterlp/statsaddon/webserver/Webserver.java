package com.voxcrafterlp.statsaddon.webserver;

import com.sun.net.httpserver.HttpServer;
import com.voxcrafterlp.statsaddon.webserver.api.APIHandler;
import lombok.Getter;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 09.02.2021
 * Time: 21:58
 * Project: LabyCookies
 */

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
