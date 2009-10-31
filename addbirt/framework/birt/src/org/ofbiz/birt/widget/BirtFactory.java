package org.ofbiz.widget.birt;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.ofbiz.base.location.FlexibleLocation;
import org.ofbiz.base.util.Debug;
import org.ofbiz.widget.screen.ScreenFactory;
import org.xml.sax.SAXException;

public class BirtFactory {
    
    public static final String module = BirtFactory.class.getName();
    
    /**
     * get report inport stream from location
     * @param resourceName
     * @return
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    public static InputStream getReportInputStreamFromLocation(String resourceName)
        throws IOException, SAXException, ParserConfigurationException{
        
        InputStream reportInputStream = null;
        synchronized (BirtFactory.class) {
            long startTime = System.currentTimeMillis();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = ScreenFactory.class.getClassLoader();
            }
            URL reportFileUrl = null;
            reportFileUrl = FlexibleLocation.resolveLocation(resourceName, loader);
            if (reportFileUrl == null) {
                throw new IllegalArgumentException("Could not resolve location to URL: " + resourceName);
            }
            reportInputStream = reportFileUrl.openStream();
            double totalSeconds = (System.currentTimeMillis() - startTime)/1000.0;
            Debug.logInfo("Got report in " + totalSeconds + "s from: " + reportFileUrl.toExternalForm(), module);
        }
        
        if (reportInputStream == null) {
            throw new IllegalArgumentException("Could not find report file with location [" + resourceName + "]");
        }
        return reportInputStream;
    }
}
