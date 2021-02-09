package com.voxcrafterlp.statsaddon.webserver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This file was created by VoxCrafter_LP!
 * Date: 09.02.2021
 * Time: 21:58
 * Project: LabyCookies
 */

@Getter
public class Webserver {

    private final Tomcat webserver;

    public Webserver() throws LifecycleException {
        this.webserver = new Tomcat();
        this.webserver.setPort(3412);
        this.webserver.setBaseDir("/");

        String contextPath = "/Stats";
        String docBase = "/";

        Context context = this.webserver.addContext(contextPath, docBase);

        HttpServlet servlet = new HttpServlet() {
            @Override
            protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
                PrintWriter writer = resp.getWriter();

                writer.println("<html><title>Indian scammer</title><body>");
                writer.println("<h1>Sir, listen! You have to buy Google Play cards</h1>");
                writer.println("</body></html>");
            }
        };
        String servletName = "Servlet1";
        String urlPattern = "/go";

        this.webserver.addServlet(contextPath, servletName, servlet);
        context.addServletMappingDecoded(urlPattern, servletName);

        this.webserver.start();
    }
}
