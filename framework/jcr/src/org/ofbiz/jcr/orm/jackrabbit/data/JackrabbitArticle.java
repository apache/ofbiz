/*
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
 */
package org.ofbiz.jcr.orm.jackrabbit.data;

import java.util.Calendar;

import org.apache.jackrabbit.ocm.mapper.impl.annotation.Field;
import org.apache.jackrabbit.ocm.mapper.impl.annotation.Node;

@Node(extend = JackrabbitLocalizedContent.class)
public class JackrabbitArticle extends JackrabbitLocalizedContent {

    @Field(id = true)
    private String title = null;
    @Field
    private String content = null;
    @Field
    private Calendar pubDate = null;

    /**
     *
     * @param nodePath
     * @param language
     * @param title
     * @param content
     * @param pubDate
     */
    public JackrabbitArticle(String nodePath, String language, String title, String content, Calendar pubDate) {
        super(nodePath, language);

        this.title = title;
        this.content = content;
        this.pubDate = pubDate;
    }

    /**
     *
     */
    public JackrabbitArticle() {
        super();
        // create empty instance
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Calendar getPubDate() {
        return pubDate;
    }

    public void setPubDate(Calendar pubDate) {
        this.pubDate = pubDate;
    }
}
