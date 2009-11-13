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
package org.ofbiz.birt.report.context;

import java.net.URL;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.ViewerAttributeBean;
import org.eclipse.birt.report.exception.ViewerException;
import org.eclipse.birt.report.resource.BirtResources;
import org.eclipse.birt.report.resource.ResourceConstants;
import org.eclipse.birt.report.utility.BirtUtility;
import org.eclipse.birt.report.utility.DataUtil;
import org.eclipse.birt.report.utility.ParameterAccessor;
import org.ofbiz.base.location.FlexibleLocation;

public class BirtViewerAttributeBean extends ViewerAttributeBean {
    
    public final static String module = BirtViewerAttributeBean.class.getName();
    
    /**
     * Module Options
     */
    private Map moduleOptions = null;
    
    /**
     * Request Type
     */
    private String requestType;
    
    private Boolean reportRtl;

    public BirtViewerAttributeBean(HttpServletRequest arg0) {
        super(arg0);
        // TODO Auto-generated constructor stub
    }
    
    /**
     * Init the bean.
     * 
     * @param request
     * @throws Exception
     */
    protected void __init( HttpServletRequest request ) throws Exception
    {
        // If GetImage operate, return directly.
        if ( ParameterAccessor.isGetImageOperator( request )
                && ( IBirtConstants.SERVLET_PATH_FRAMESET
                        .equalsIgnoreCase( request.getServletPath( ) )
                        || IBirtConstants.SERVLET_PATH_OUTPUT
                                .equalsIgnoreCase( request.getServletPath( ) )
                        || IBirtConstants.SERVLET_PATH_RUN
                                .equalsIgnoreCase( request.getServletPath( ) ) || IBirtConstants.SERVLET_PATH_PREVIEW
                        .equalsIgnoreCase( request.getServletPath( ) ) ) )
        {
            return;
        }

        this.category = "BIRT"; //$NON-NLS-1$
        this.masterPageContent = ParameterAccessor
                .isMasterPageContent( request );
        this.isDesigner = ParameterAccessor.isDesigner( );
        
        if ( !ParameterAccessor.isBookmarkReportlet( request ) )
        {
            this.bookmark = ParameterAccessor.getBookmark( request );
        }
        else
        {
            this.bookmark = null;
        }        
        
        this.isToc = ParameterAccessor.isToc( request );
        this.reportPage = ParameterAccessor.getPage( request );
        this.reportPageRange = ParameterAccessor.getPageRange( request );
        this.action = ParameterAccessor.getAction( request );

        // If use frameset/output/download/extract servlet pattern, generate
        // document
        // from design file
        if ( IBirtConstants.SERVLET_PATH_FRAMESET.equalsIgnoreCase( request
                .getServletPath( ) )
                || IBirtConstants.SERVLET_PATH_OUTPUT.equalsIgnoreCase( request
                        .getServletPath( ) )
                || IBirtConstants.SERVLET_PATH_DOWNLOAD
                        .equalsIgnoreCase( request.getServletPath( ) )
                || IBirtConstants.SERVLET_PATH_EXTRACT
                        .equalsIgnoreCase( request.getServletPath( ) ) )
        {
            this.reportDocumentName = ParameterAccessor.getReportDocument(
                    request, null, true );
        }
        else
        {
            this.reportDocumentName = ParameterAccessor.getReportDocument(
                    request, null, false );
        }

        String reportParam = DataUtil.trimString( ParameterAccessor.getParameter( request, ParameterAccessor.PARAM_REPORT ));
        if (reportParam.startsWith("component://")) {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = BirtViewerAttributeBean.class.getClassLoader();
            }
            URL reportFileUrl = null;
            reportFileUrl = FlexibleLocation.resolveLocation(reportParam, loader);
            if (reportFileUrl == null) {
                throw new IllegalArgumentException("Could not resolve location to URL: " + reportParam);
            }
            this.reportDesignName = reportFileUrl.getPath();
        } else {
            this.reportDesignName = ParameterAccessor.getReport( request, null );
        }

        this.emitterId = ParameterAccessor.getEmitterId( request );
        
        // If print action, force to use postscript format
        this.format = ParameterAccessor.getFormat( request );
        if ( IBirtConstants.ACTION_PRINT.equalsIgnoreCase( action ) )
        {
            // Check whether turn on this funtion
            if ( ParameterAccessor.isSupportedPrintOnServer )
            {
                this.format = IBirtConstants.POSTSCRIPT_RENDER_FORMAT;
                this.emitterId = null;
            }
            else
            {
                this.action = null;
            }
        }

        // Set locale information
        BirtResources.setLocale( ParameterAccessor.getLocale( request ) );

        // Set the request type
        this.requestType = request
                .getHeader( ParameterAccessor.HEADER_REQUEST_TYPE );

        // Determine the report design and doc 's timestamp
        processReport( request );

        // Report title.
        this.reportTitle = ParameterAccessor.getTitle( request );

        // Set whether show the report title
        this.isShowTitle = ParameterAccessor.isShowTitle( request );

        // Set whether show the toolbar
        this.isShowToolbar = ParameterAccessor.isShowToolbar( request );

        // Set whether show the navigation bar
        this.isShowNavigationbar = ParameterAccessor
                .isShowNavigationbar( request );

        // get some module options
        this.moduleOptions = BirtUtility.getModuleOptions( request );

        this.reportDesignHandle = getDesignHandle( request );
        if ( this.reportDesignHandle == null )
            throw new ViewerException(
                    ResourceConstants.GENERAL_EXCEPTION_NO_REPORT_DESIGN );

        this.reportRtl = null;
        
        // Initialize report parameters.
        __initParameters( request );
    }
}
