package org.ofbiz.birt.container;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.framework.PlatformFileContext;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.ofbiz.base.container.Container;
import org.ofbiz.base.container.ContainerConfig;
import org.ofbiz.base.container.ContainerException;
import org.ofbiz.base.location.FlexibleLocation;
import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.UtilObject;
import org.ofbiz.base.util.UtilProperties;
import org.ofbiz.entity.Delegator;
import org.ofbiz.entity.DelegatorFactory;
import org.ofbiz.entity.GenericEntityException;
import org.ofbiz.service.GenericDispatcher;
import org.ofbiz.service.LocalDispatcher;
import org.ofbiz.widget.screen.ScreenFactory;

public class BirtContainer implements Container {
    
    public static final String module = BirtContainer.class.getName();
    
    public final static String CONFIG_FILE = "birt.properties";
    
    protected EngineConfig config;
    protected String configFile;
    
    private static IReportEngine engine;
    private static String delegatorGroupHelperName;
    private static String delegatorName;
    private static String dispatcherName;
    private static Delegator delegator;
    private static LocalDispatcher dispatcher;

    public void init(String[] args, String configFile)
            throws ContainerException {
        // TODO Auto-generated method stub
        this.configFile = configFile;
    }

    /**
     * start container
     */
    public boolean start() throws ContainerException {
        // TODO Auto-generated method stub
        
        Debug.logInfo("Start birt container", module);
        
        // make sure the subclass sets the config name
        if (this.getContainerConfigName() == null) {
            throw new ContainerException("Unknown container config name");
        }
        // get the container config
        ContainerConfig.Container cc = ContainerConfig.getContainer(this.getContainerConfigName(), configFile);
        if (cc == null) {
            throw new ContainerException("No " + this.getContainerConfigName() + " configuration found in container config!");
        }
        
        config = new EngineConfig();
        HashMap context = config.getAppContext();
        
        // set delegator, dispatcher and security objects to report
        
        // get the delegator
        delegatorName = ContainerConfig.getPropertyValue(cc, "delegator-name", "default");
        try {
              delegator = UtilObject.getObjectFromFactory(DelegatorFactory.class, delegatorName);
        } catch (ClassNotFoundException e) {
            Debug.logError(e, module);
        }
        
        // get the dispatcher
        dispatcherName = ContainerConfig.getPropertyValue(cc, "dispatcher-name", "birt-dispatcher");
        dispatcher = GenericDispatcher.getLocalDispatcher(dispatcherName, delegator);
        
        context.put("delegator", delegator);
        context.put("dispatcher", dispatcher);
        
        delegatorGroupHelperName = ContainerConfig.getPropertyValue(cc, "delegator-group-helper-name", "org.ofbiz");
        
        // set classloader for engine
        context.put(EngineConstants.APPCONTEXT_CLASSLOADER_KEY, BirtContainer.class.getClassLoader());
        context.put(EngineConstants.WEBAPP_CLASSPATH_KEY, BirtContainer.class.getClassLoader());
        
        // set log config to show all level in console
        config.setLogConfig(null, Level.ALL);
        
        // get report engine home
        URL reportEngineDirUrl = null;
        String reportEngineResourceName = "component://webtools/webapp/birt/WEB-INF/ReportEngine";
        synchronized (BirtContainer.class) {
            long startTime = System.currentTimeMillis();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = ScreenFactory.class.getClassLoader();
            }
            reportEngineDirUrl = null;
            try {
                reportEngineDirUrl = FlexibleLocation.resolveLocation(reportEngineResourceName, loader);
            } catch (MalformedURLException e) {
                throw new ContainerException(e);
            }
            if (reportEngineDirUrl == null) {
                throw new IllegalArgumentException("Could not resolve location to URL: " + reportEngineResourceName);
            }
        }
        
        // set engine home
        if (reportEngineDirUrl != null) {
            config.setEngineHome(reportEngineDirUrl.getPath());
            config.setBIRTHome(reportEngineDirUrl.getPath());
        }
        
        // set OSGi arguments specific in properties
        String argumentsString = UtilProperties.getPropertyValue(BirtContainer.CONFIG_FILE, "birt.osgi.arguments");
        config.setOSGiArguments(argumentsString.split(","));

        // set platform file context
        config.setPlatformContext(new PlatformFileContext(config));
        config.setAppContext(context);
        
        // startup platform
        try {
            Debug.logInfo("Startup birt platform", module);
            Platform.startup( config );
        } catch ( BirtException e ) {
            throw new ContainerException(e);
        }

        // create report engine
        Debug.logInfo("Create factory object", module);
        IReportEngineFactory factory = (IReportEngineFactory) Platform
              .createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );
        if (factory == null) {
            throw new ContainerException("can not create birt engine factory");
        }
        Debug.logInfo("Create report engine", module);
        engine = factory.createReportEngine( config );
        String[] supportedFormats = engine.getSupportedFormats();
        String formatList = null;
        for (String supportedFormat : supportedFormats) {
            if (formatList != null) {
                formatList += ", " + supportedFormat;
            } else {
                formatList = supportedFormat;
            }
        }
        Debug.logInfo("BIRT supported formats: " + formatList, module);
        return false;
    }
    
    public void stop() throws ContainerException {
        // TODO Auto-generated method stub
        
    }
    
    public String getContainerConfigName() {
        return "birt-container";
    }
    
    public static IReportEngine getReportEngine() throws GenericEntityException, SQLException {
        return engine;
    }
    
    public static String getDelegatorGroupHelperName() {
        return delegatorGroupHelperName;
    }
    
    public static String getDelegatorName() {
        return delegatorName;
    }
    
    public static String getDispatcherName() {
        return dispatcherName;
    }

    public static Delegator getDelegator() {
        return delegator;
    }
    
    public static LocalDispatcher getDispatcher() {
        return dispatcher;
    }
}
