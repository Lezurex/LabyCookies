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
                    System.out.println("Network Interface " + networkInterface.getName());
                    try {
                        if (networkInterface.isUp()) {
                            System.out.println("Is up");
                            if (!networkInterface.isVirtual()) {
                                System.out.println("Is not virtual");
                                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                                while (inetAddresses.hasMoreElements()) {
                                    InetAddress inetAddress = inetAddresses.nextElement();
                                    System.out.println("Looping address " + inetAddress.getHostAddress());
                                    if (inetAddress instanceof Inet4Address) {
                                        System.out.println("Is IPv4");
                                        if (!inetAddress.isLinkLocalAddress() && !inetAddress.isLoopbackAddress()) {
                                            System.out.println("Is link local");
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
