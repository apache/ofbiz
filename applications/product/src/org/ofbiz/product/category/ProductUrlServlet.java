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
package org.ofbiz.product.category;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.entity.Delegator;
import org.ofbiz.product.category.ftl.CatalogUrlSeoTransform;

/**
 * ControlServlet.java - Master servlet for the web application.
 */
@SuppressWarnings("serial")
public class ProductUrlServlet extends HttpServlet {

    public static final String module = ProductUrlServlet.class.getName();    
    public static final String PRODUCT_URL_MOUNT_POINT = "product";

    public ProductUrlServlet() {
        super();
    }

    /**
     * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Delegator delegator = (Delegator) getServletContext().getAttribute("delegator");
        CatalogUrlSeoTransform.forwardProductUri(request, response, delegator);
    }

    /**
     * @see javax.servlet.Servlet#destroy()
     */
    @Override
    public void destroy() {
        super.destroy();
    }

}
