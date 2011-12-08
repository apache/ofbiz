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
package org.ofbiz.widget.screen;

import java.io.IOException;

import org.ofbiz.base.util.GeneralException;
import org.ofbiz.widget.screen.HtmlWidget.HtmlTemplate;
import org.ofbiz.widget.screen.HtmlWidget.HtmlTemplateDecorator;
import org.ofbiz.widget.screen.HtmlWidget.HtmlTemplateDecoratorSection;
import org.ofbiz.widget.screen.ModelScreenWidget.Container;
import org.ofbiz.widget.screen.ModelScreenWidget.Content;
import org.ofbiz.widget.screen.ModelScreenWidget.DecoratorScreen;
import org.ofbiz.widget.screen.ModelScreenWidget.DecoratorSection;
import org.ofbiz.widget.screen.ModelScreenWidget.DecoratorSectionInclude;
import org.ofbiz.widget.screen.ModelScreenWidget.Form;
import org.ofbiz.widget.screen.ModelScreenWidget.HorizontalSeparator;
import org.ofbiz.widget.screen.ModelScreenWidget.Image;
import org.ofbiz.widget.screen.ModelScreenWidget.IncludeScreen;
import org.ofbiz.widget.screen.ModelScreenWidget.Label;
import org.ofbiz.widget.screen.ModelScreenWidget.Link;
import org.ofbiz.widget.screen.ModelScreenWidget.Menu;
import org.ofbiz.widget.screen.ModelScreenWidget.PlatformSpecific;
import org.ofbiz.widget.screen.ModelScreenWidget.PortalPage;
import org.ofbiz.widget.screen.ModelScreenWidget.Screenlet;
import org.ofbiz.widget.screen.ModelScreenWidget.Section;
import org.ofbiz.widget.screen.ModelScreenWidget.SubContent;
import org.ofbiz.widget.screen.ModelScreenWidget.Tree;

/**
 * Screen widget visitor.
 */
public interface ScreenWidgetVisitor {

    void visit(Container container) throws IOException, GeneralException;

    void visit(Content content) throws IOException, GeneralException;

    void visit(DecoratorScreen decoratorScreen) throws IOException, GeneralException;

    void visit(DecoratorSection decoratorSection) throws IOException, GeneralException;

    void visit(DecoratorSectionInclude decoratorSectionInclude) throws IOException, GeneralException;

    void visit(Form form) throws IOException, GeneralException;

    void visit(HorizontalSeparator horizontalSeparator) throws IOException, GeneralException;

    void visit(HtmlTemplate htmlTemplate) throws IOException, GeneralException;

    void visit(HtmlTemplateDecorator htmlTemplateDecorator) throws IOException, GeneralException;

    void visit(HtmlTemplateDecoratorSection htmlTemplateDecoratorSection) throws IOException, GeneralException;

    void visit(HtmlWidget htmlWidget) throws IOException, GeneralException;

    void visit(Image image) throws IOException, GeneralException;

    void visit(IncludeScreen includeScreen) throws IOException, GeneralException;

    void visit(IterateSectionWidget iterateSectionWidget) throws IOException, GeneralException;

    void visit(Label label) throws IOException, GeneralException;

    void visit(Link link) throws IOException, GeneralException;

    void visit(Menu menu) throws IOException, GeneralException;

    void visit(ModelScreen modelScreen) throws IOException, GeneralException;

    void visit(PlatformSpecific platformSpecific) throws IOException, GeneralException;

    void visit(PortalPage portalPage) throws IOException, GeneralException;

    void visit(Screenlet screenlet) throws IOException, GeneralException;

    void visit(Section section) throws IOException, GeneralException;

    void visit(SubContent subContent) throws IOException, GeneralException;

    void visit(Tree tree) throws IOException, GeneralException;

}
