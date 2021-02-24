package com.voxcrafterlp.statsaddon.webserver;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

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
        String webappDirLocation = "src/main/webapp/";
        webserver = new Tomcat();

        webserver.setPort(3412);

        StandardContext ctx = (StandardContext) webserver.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        System.out.println("configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());

        // Declare an alternative location for your "WEB-INF/classes" dir
        // Servlet 3.0 annotation will work
        File additionWebInfClasses = new File("target/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        webserver.start();
        webserver.getServer().await();
    }
}
