package net.eulerframework.web.module.datastore.bootstrap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.annotation.Order;
import org.springframework.web.WebApplicationInitializer;

import net.eulerframework.web.module.datastore.listener.FileArchiveListener;

@Order(1)
public class FileArchiveBootstrap implements WebApplicationInitializer {
    private final Logger log = LogManager.getLogger();
    
    @Override
    public void onStartup(ServletContext container) throws ServletException {
        log.info("Executing Data Store bootstrap.");
        container.addListener(new FileArchiveListener());
    }
}
