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

    void visit(Container container);

    void visit(Content content);

    void visit(DecoratorScreen decoratorScreen);

    void visit(DecoratorSection decoratorSection);

    void visit(DecoratorSectionInclude decoratorSectionInclude);

    void visit(Form form);

    void visit(HorizontalSeparator horizontalSeparator);

    void visit(HtmlTemplate htmlTemplate);

    void visit(HtmlTemplateDecorator htmlTemplateDecorator);

    void visit(HtmlTemplateDecoratorSection htmlTemplateDecoratorSection);

    void visit(HtmlWidget htmlWidget);

    void visit(Image image);

    void visit(IncludeScreen includeScreen);

    void visit(IterateSectionWidget iterateSectionWidget);

    void visit(Label label);

    void visit(Link link);

    void visit(Menu menu);

    void visit(ModelScreen modelScreen);

    void visit(PlatformSpecific platformSpecific);

    void visit(PortalPage portalPage);

    void visit(Screenlet screenlet);

    void visit(Section section);

    void visit(SubContent subContent);

    void visit(Tree tree);
}
