/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package org.ofbiz.birt.report.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.IContext;
import org.eclipse.birt.report.presentation.aggregation.IFragment;
import org.eclipse.birt.report.presentation.aggregation.layout.FramesetFragment;
import org.eclipse.birt.report.presentation.aggregation.layout.RunFragment;
import org.eclipse.birt.report.service.BirtReportServiceFactory;
import org.eclipse.birt.report.servlet.BirtSoapMessageDispatcherServlet;
import org.eclipse.birt.report.utility.BirtUtility;
import org.ofbiz.birt.report.context.OFBizBirtContext;
import org.ofbiz.birt.report.service.OFBizBirtViewerReportService;

public class BirtViewerServlet extends BirtSoapMessageDispatcherServlet {
    
    public final static String module = BirtViewerServlet.class.getName();
    
    protected void __init(ServletConfig config) {
        // TODO Auto-generated method stub
        BirtReportServiceFactory.init( new OFBizBirtViewerReportService( config
                .getServletContext( ) ) );
        
        // handle 'frameset' pattern
        viewer = new FramesetFragment( );
        viewer.buildComposite( );
        viewer.setJSPRootPath( "/webcontent/birt" ); //$NON-NLS-1$

        // handle 'run' pattern
        run = new RunFragment( );
        run.buildComposite( );
        run.setJSPRootPath( "/webcontent/birt" ); //$NON-NLS-1$            
    }
    
    /**
     * Init context.
     * 
     * @param request
     *            incoming http request
     * @param response
     *            http response
     * @exception BirtException
     * @return IContext
     */
    protected IContext __getContext( HttpServletRequest request,
            HttpServletResponse response ) throws BirtException
    {
        BirtReportServiceFactory.getReportService( ).setContext(
                getServletContext( ), null );
        return new OFBizBirtContext( request, response );
    }
    
    /**
     * Local process http request with GET method.
     * 
     * @param request
     *            incoming http request
     * @param response
     *            http response
     * @exception ServletException
     * @exception IOException
     * @return
     */
    protected void __doGet( IContext context ) throws ServletException,
            IOException, BirtException
    {
        IFragment activeFragment = null;
        String servletPath = context.getRequest( ).getServletPath( );
        if ( IBirtConstants.SERVLET_PATH_FRAMESET
                .equalsIgnoreCase( servletPath ) )
        {
            activeFragment = viewer;
        }
        else if ( IBirtConstants.SERVLET_PATH_RUN
                .equalsIgnoreCase( servletPath ) )
        {
            activeFragment = run;
        }

        if ( activeFragment != null )
            activeFragment.service( context.getRequest( ), context
                    .getResponse( ) );
    }
    
    /**
     * Locale process http request with POST method. Four different servlet
     * paths are expected: "/frameset", "/navigation", "/toolbar", and "/run".
     * 
     * @param request
     *            incoming http request
     * @param response
     *            http response
     * @exception ServletException
     * @exception IOException
     * @return
     */
    protected void __doPost( IContext context ) throws ServletException,
            IOException, BirtException
    {
    }
    
    /**
     * Local authentication. Alwasy returns true.
     * 
     * @param request
     *            incoming http request
     * @param response
     *            http response
     * @return
     */
    protected boolean __authenticate( HttpServletRequest request,
            HttpServletResponse response )
    {
        return true;
    }
    
    /**
     * Process exception for non soap request.
     * 
     * @param request
     *            incoming http request
     * @param response
     *            http response
     * @param exception
     * @throws ServletException
     * @throws IOException
     */
    protected void __handleNonSoapException( HttpServletRequest request,
            HttpServletResponse response, Exception exception )
            throws ServletException, IOException
    {
        exception.printStackTrace( );
        BirtUtility.appendErrorMessage( response.getOutputStream( ), exception );
    }
}
