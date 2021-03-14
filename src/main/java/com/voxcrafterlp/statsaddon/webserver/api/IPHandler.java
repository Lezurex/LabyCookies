package com.voxcrafterlp.statsaddon.webserver.api;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.*;
import java.util.Enumeration;
import java.util.List;

public class IPHandler implements ActionHandler {

    @Override
    public String handle(List<String> pathParts, JsonObject body) {
        String address = "error";

        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        List<InetAddress> foundAddresses = Lists.newCopyOnWriteArrayList();

        try {
            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = interfaces.nextElement();
                    try {
                        if (networkInterface.isUp()) {
                            if (!networkInterface.isVirtual()) {
                                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                                while (inetAddresses.hasMoreElements()) {
                                    InetAddress inetAddress = inetAddresses.nextElement();
                                    if (inetAddress instanceof Inet4Address) {
                                        if (!inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress()) {
                                            foundAddresses.add(inetAddress);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (SocketException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (foundAddresses.size() != 0) {
            address = foundAddresses.get(0).getHostAddress();
        }

        final JsonObject response = new JsonObject();
        final JsonArray array = new JsonArray();
        final JsonObject data = new JsonObject();

        data.addProperty("ip", address);
        array.add(data);
        response.add("data", array);

        return response.toString();
    }
}
