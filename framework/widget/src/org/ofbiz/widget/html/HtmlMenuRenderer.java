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
package org.ofbiz.widget.html;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ofbiz.base.util.Assert;
import org.ofbiz.base.util.GeneralException;
import org.ofbiz.base.util.StringUtil;
import org.ofbiz.base.util.UtilMisc;
import org.ofbiz.base.util.UtilValidate;
import org.ofbiz.entity.GenericValue;
import org.ofbiz.webapp.control.RequestHandler;
import org.ofbiz.webapp.taglib.ContentUrlTag;
import org.ofbiz.widget.ModelWidget;
import org.ofbiz.widget.PortalPageWorker;
import org.ofbiz.widget.WidgetWorker;
import org.ofbiz.widget.menu.MenuWidgetVisitor;
import org.ofbiz.widget.menu.ModelMenu;
import org.ofbiz.widget.menu.ModelMenuAction;
import org.ofbiz.widget.menu.ModelMenuItem;
import org.ofbiz.widget.menu.ModelMenuItem.Image;
import org.ofbiz.widget.menu.ModelMenuItem.Link;

/**
 * HTML menu widget renderer.
 */
public final class HtmlMenuRenderer extends HtmlWidgetRenderer implements MenuWidgetVisitor {

    /**
     * Renders a <code>ModelMenu</code> as HTML.
     * 
     * @param modelMenu
     * @param writer
     * @param context
     * @throws IllegalArgumentException
     *             if any arguments are <code>null</code> or if <code>modelMenu</code> is
     *             an unsupported type
     * @throws IOException
     * @throws GeneralException
     */
    public static void render(ModelMenu modelMenu, Appendable writer, Map<String, Object> context) throws IOException, GeneralException {
        Assert.notNull("modelMenu", modelMenu);
        modelMenu.accept(new HtmlMenuRenderer(writer, context));
    }

    public static int renderedMenuItemCount(ModelMenu modelMenu, Map<String, Object> context) {
        int count = 0;
        for (ModelMenuItem item : modelMenu.getMenuItemList()) {
            if (item.shouldBeRendered(context))
                count++;
        }
        return count;
    }

    private final Appendable writer;
    private final Map<String, Object> context;

    public HtmlMenuRenderer(Appendable writer, Map<String, Object> context) {
        Assert.notNull("writer", writer, "context", context);
        this.writer = writer;
        this.context = context;
    }

    private boolean isDisableIfEmpty(ModelMenuItem menuItem) {
        boolean disabled = false;
        String disableIfEmpty = menuItem.getDisableIfEmpty();
        if (UtilValidate.isNotEmpty(disableIfEmpty)) {
            List<String> keys = StringUtil.split(disableIfEmpty, "|");
            for (String key : keys) {
                Object obj = context.get(key);
                if (obj == null) {
                    disabled = true;
                    break;
                }
            }
        }
        return disabled;
    }

    private boolean isHideIfSelected(ModelMenuItem menuItem) {
        ModelMenu menu = menuItem.getModelMenu();
        String currentMenuItemName = menu.getSelectedMenuItemContextFieldName(context);
        String currentItemName = menuItem.getName();
        Boolean hideIfSelected = menuItem.getHideIfSelected();
        return (hideIfSelected != null && hideIfSelected.booleanValue() && currentMenuItemName != null && currentMenuItemName.equals(currentItemName));
    }

    private void renderMenuClose(Appendable writer, Map<String, Object> context, ModelMenu modelMenu) throws IOException {
        // TODO: div can't be directly inside an UL
        String fillStyle = modelMenu.getFillStyle();
        if (UtilValidate.isNotEmpty(fillStyle)) {
            writer.append("<div class=\"").append(fillStyle).append("\">&nbsp;</div>");
        }
        if (renderedMenuItemCount(modelMenu, context) > 0) {
            writer.append(" </ul>");
            appendWhitespace(writer);
            writer.append("</li>");
            appendWhitespace(writer);
            writer.append("</ul>");
            appendWhitespace(writer);
        }
        writer.append(" <br class=\"clear\"/>");
        appendWhitespace(writer);
        writer.append("</div>");
        appendWhitespace(writer);
        if (ModelWidget.widgetBoundaryCommentsEnabled(context)) {
            writer.append(buildBoundaryComment("End", "Menu Widget", modelMenu.getBoundaryCommentName()));
        }
    }

    private void renderMenuOpen(Appendable writer, Map<String, Object> context, ModelMenu modelMenu) throws IOException {
        if (ModelWidget.widgetBoundaryCommentsEnabled(context)) {
            writer.append(buildBoundaryComment("Begin", "Menu Widget", modelMenu.getBoundaryCommentName()));
        }
        writer.append("<div");
        String menuId = modelMenu.getId();
        if (UtilValidate.isNotEmpty(menuId)) {
            writer.append(" id=\"").append(menuId).append("\"");
        } else {
            // TODO: Remove else after UI refactor - allow both id and style
            String menuContainerStyle = modelMenu.getMenuContainerStyle(context);
            if (UtilValidate.isNotEmpty(menuContainerStyle)) {
                writer.append(" class=\"").append(menuContainerStyle).append("\"");
            }
        }
        String menuWidth = modelMenu.getMenuWidth();
        // TODO: Eliminate embedded styling after refactor
        if (UtilValidate.isNotEmpty(menuWidth)) {
            writer.append(" style=\"width:").append(menuWidth).append(";\"");
        }
        writer.append(">");
        appendWhitespace(writer);
        String menuTitle = modelMenu.getTitle(context);
        if (UtilValidate.isNotEmpty(menuTitle)) {
            writer.append("<h2>").append(menuTitle).append("</h2>");
            appendWhitespace(writer);
        }
        if (renderedMenuItemCount(modelMenu, context) > 0) {
            writer.append("<ul>");
            appendWhitespace(writer);
            writer.append("<li>");
            appendWhitespace(writer);
            writer.append(" <ul>");
            appendWhitespace(writer);
        }
    }

    public void visit(Image image) throws IOException, GeneralException {
        writer.append("<img ");
        String id = image.getId(context);
        if (UtilValidate.isNotEmpty(id)) {
            writer.append(" id=\"");
            writer.append(id);
            writer.append("\"");
        }
        String style = image.getStyle(context);
        if (UtilValidate.isNotEmpty(style)) {
            writer.append(" class=\"");
            writer.append(style);
            writer.append("\"");
        }
        String wid = image.getWidth(context);
        if (UtilValidate.isNotEmpty(wid)) {
            writer.append(" width=\"");
            writer.append(wid);
            writer.append("\"");
        }
        String hgt = image.getHeight(context);
        if (UtilValidate.isNotEmpty(hgt)) {
            writer.append(" height=\"");
            writer.append(hgt);
            writer.append("\"");
        }
        String border = image.getBorder(context);
        if (UtilValidate.isNotEmpty(border)) {
            writer.append(" border=\"");
            writer.append(border);
            writer.append("\"");
        }
        String src = image.getSrc(context);
        if (UtilValidate.isNotEmpty(src)) {
            writer.append(" src=\"");
            String urlMode = image.getUrlMode();
            boolean fullPath = false;
            boolean secure = false;
            boolean encode = false;
            HttpServletResponse response = (HttpServletResponse) context.get("response");
            HttpServletRequest request = (HttpServletRequest) context.get("request");
            if (urlMode != null && urlMode.equalsIgnoreCase("ofbiz")) {
                if (request != null && response != null) {
                    ServletContext ctx = (ServletContext) request.getAttribute("servletContext");
                    RequestHandler rh = (RequestHandler) ctx.getAttribute("_REQUEST_HANDLER_");
                    String urlString = rh.makeLink(request, response, src, fullPath, secure, encode);
                    writer.append(urlString);
                } else {
                    writer.append(src);
                }
            } else if (urlMode != null && urlMode.equalsIgnoreCase("content")) {
                if (request != null && response != null) {
                    StringBuilder newURL = new StringBuilder();
                    ContentUrlTag.appendContentPrefix(request, newURL);
                    newURL.append(src);
                    writer.append(newURL.toString());
                }
            } else {
                writer.append(src);
            }
            writer.append("\"");
        }
        writer.append("/>");
    }

    public void visit(Link link) throws IOException, GeneralException {
        String target = link.getTarget(context);
        ModelMenuItem menuItem = link.getLinkMenuItem();
        if (menuItem.getDisabled() || isDisableIfEmpty(menuItem)) {
            target = null;
        }
        if (UtilValidate.isNotEmpty(target)) {
            HttpServletResponse response = (HttpServletResponse) context.get("response");
            HttpServletRequest request = (HttpServletRequest) context.get("request");
            String targetWindow = link.getTargetWindow(context);
            String uniqueItemName = menuItem.getModelMenu().getName() + "_" + menuItem.getName() + "_LF_"
                    + UtilMisc.<String> addToBigDecimalInMap(context, "menuUniqueItemIndex", BigDecimal.ONE);
            String linkType = WidgetWorker.determineAutoLinkType(link.getLinkType(), target, link.getUrlMode(), request);
            if ("hidden-form".equals(linkType)) {
                writer.append("<form method=\"post\"");
                writer.append(" action=\"");
                // note that this passes null for the parameterList on purpose so they
                // won't be put into the URL
                WidgetWorker.buildHyperlinkUrl(writer, target, link.getUrlMode(), null, link.getPrefix(context), link.getFullPath(), link.getSecure(), link
                        .getEncode(), request, response, context);
                writer.append("\"");
                if (UtilValidate.isNotEmpty(targetWindow)) {
                    writer.append(" target=\"");
                    writer.append(targetWindow);
                    writer.append("\"");
                }
                writer.append(" name=\"");
                writer.append(uniqueItemName);
                writer.append("\">");
                StringUtil.SimpleEncoder simpleEncoder = (StringUtil.SimpleEncoder) context.get("simpleEncoder");
                for (Map.Entry<String, String> parameter : link.getParameterMap(context).entrySet()) {
                    writer.append("<input name=\"");
                    writer.append(parameter.getKey());
                    writer.append("\" value=\"");
                    if (simpleEncoder != null) {
                        writer.append(simpleEncoder.encode(parameter.getValue()));
                    } else {
                        writer.append(parameter.getValue());
                    }
                    writer.append("\" type=\"hidden\"/>");
                }

                writer.append("</form>");
            }
            writer.append("<a");
            String id = link.getId(context);
            if (UtilValidate.isNotEmpty(id)) {
                writer.append(" id=\"");
                writer.append(id);
                writer.append("\"");
            }
            String style = link.getStyle(context);
            if (UtilValidate.isNotEmpty(style)) {
                writer.append(" class=\"");
                writer.append(style);
                writer.append("\"");
            }
            String name = link.getName(context);
            if (UtilValidate.isNotEmpty(name)) {
                writer.append(" name=\"");
                writer.append(name);
                writer.append("\"");
            }
            if (UtilValidate.isNotEmpty(targetWindow)) {
                writer.append(" target=\"");
                writer.append(targetWindow);
                writer.append("\"");
            }
            writer.append(" href=\"");
            String confirmationMsg = link.getConfirmation(context);
            if ("hidden-form".equals(linkType)) {
                if (UtilValidate.isNotEmpty(confirmationMsg)) {
                    writer.append("javascript:confirmActionFormLink('");
                    writer.append(confirmationMsg);
                    writer.append("', '");
                    writer.append(uniqueItemName);
                    writer.append("')");
                } else {
                    writer.append("javascript:document.");
                    writer.append(uniqueItemName);
                    writer.append(".submit()");
                }
            } else {
                if (UtilValidate.isNotEmpty(confirmationMsg)) {
                    writer.append("javascript:confirmActionLink('");
                    writer.append(confirmationMsg);
                    writer.append("', '");
                    WidgetWorker.buildHyperlinkUrl(writer, target, link.getUrlMode(), link.getParameterMap(context), link.getPrefix(context), link
                            .getFullPath(), link.getSecure(), link.getEncode(), request, response, context);
                    writer.append("')");
                } else {
                    WidgetWorker.buildHyperlinkUrl(writer, target, link.getUrlMode(), link.getParameterMap(context), link.getPrefix(context), link
                            .getFullPath(), link.getSecure(), link.getEncode(), request, response, context);
                }
            }
            writer.append("\">");
        }
        Image img = link.getImage();
        if (img != null) {
            img.accept(this);
            writer.append("&nbsp;" + link.getText(context));
        } else {
            writer.append(link.getText(context));
        }
        if (UtilValidate.isNotEmpty(target)) {
            writer.append("</a>");
        }
    }

    public void visit(ModelMenu modelMenu) throws IOException, GeneralException {
        if (!"simple".equals(modelMenu.getType())) {
            throw new IllegalArgumentException("The type " + modelMenu.getType() + " is not supported for menu with name " + modelMenu.getName());
        }
        ModelMenuAction.runSubActions(modelMenu.getActions(), this.context);
        renderMenuOpen(writer, context, modelMenu);
        for (ModelMenuItem item : modelMenu.getMenuItemList()) {
            item.accept(this);
        }
        renderMenuClose(writer, context, modelMenu);
    }

    public void visit(ModelMenuItem modelMenuItem) throws IOException, GeneralException {
        if (isHideIfSelected(modelMenuItem) || !modelMenuItem.evaluateConditions(context)) {
            return;
        }
        Locale locale = (Locale) context.get("locale");
        ModelMenuAction.runSubActions(modelMenuItem.getActions(), context);
        String parentPortalPageId = modelMenuItem.getParentPortalPageId(context);
        if (UtilValidate.isNotEmpty(parentPortalPageId)) {
            List<GenericValue> portalPages = PortalPageWorker.getPortalPages(parentPortalPageId, context);
            if (UtilValidate.isNotEmpty(portalPages)) {
                for (GenericValue portalPage : portalPages) {
                    if (UtilValidate.isNotEmpty(portalPage.getString("portalPageName"))) {
                        ModelMenuItem localItem = new ModelMenuItem(modelMenuItem.getModelMenu());
                        localItem.setName(portalPage.getString("portalPageId"));
                        localItem.setTitle((String) portalPage.get("portalPageName", locale));
                        localItem.setLink(new Link(modelMenuItem));
                        List<WidgetWorker.Parameter> linkParams = localItem.getLink().getParameterList();
                        linkParams.add(new WidgetWorker.Parameter("portalPageId", portalPage.getString("portalPageId"), false));
                        linkParams.add(new WidgetWorker.Parameter("parentPortalPageId", parentPortalPageId, false));
                        if (modelMenuItem.getLink() != null) {
                            localItem.getLink().setTarget(modelMenuItem.getLink().getTarget());
                            linkParams.addAll(modelMenuItem.getLink().getParameterList());
                        } else {
                            localItem.getLink().setTarget("showPortalPage");
                        }
                        localItem.getLink().setText((String)portalPage.get("portalPageName", locale));
                        localItem.accept(this);
                    }
                }
                return;
            }
        }
        String style = modelMenuItem.getWidgetStyle();
        if (modelMenuItem.isSelected(context)) {
            style = modelMenuItem.getSelectedStyle();
            if (UtilValidate.isEmpty(style)) {
                style = "selected";
            }
        }
        if (modelMenuItem.getDisabled() || isDisableIfEmpty(modelMenuItem)) {
            style = modelMenuItem.getDisabledTitleStyle();
        }
        writer.append("  <li");
        String alignStyle = modelMenuItem.getAlignStyle();
        if (UtilValidate.isNotEmpty(style) || UtilValidate.isNotEmpty(alignStyle)) {
            writer.append(" class=\"");
            if (UtilValidate.isNotEmpty(style)) {
                writer.append(style).append(" ");
            }
            if (UtilValidate.isNotEmpty(alignStyle)) {
                writer.append(alignStyle);
            }
            writer.append("\"");
        }
        String toolTip = modelMenuItem.getTooltip(context);
        if (UtilValidate.isNotEmpty(toolTip)) {
            writer.append(" title=\"").append(toolTip).append("\"");
        }
        writer.append(">");
        Link link = modelMenuItem.getLink();
        if (link != null) {
            link.accept(this);
        } else {
            String txt = modelMenuItem.getTitle(context);
            StringUtil.SimpleEncoder simpleEncoder = (StringUtil.SimpleEncoder) context.get("simpleEncoder");
            if (simpleEncoder != null) {
                txt = simpleEncoder.encode(txt);
            }
            writer.append(txt);
        }
        if (!modelMenuItem.getMenuItemList().isEmpty()) {
            appendWhitespace(writer);
            writer.append("    <ul>");
            appendWhitespace(writer);
            for (ModelMenuItem childMenuItem : modelMenuItem.getMenuItemList()) {
                childMenuItem.accept(this);
            }
            writer.append("    </ul>");
            appendWhitespace(writer);
        }
        writer.append("</li>");
        appendWhitespace(writer);
    }

}
