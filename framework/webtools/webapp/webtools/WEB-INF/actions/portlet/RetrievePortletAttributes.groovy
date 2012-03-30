import org.ofbiz.widget.screen.ModelScreenWidget;
import org.ofbiz.base.util.UtilValidate;

if (UtilValidate.isNotEmpty(parameters.portalPortletId)) {
    context.portalPortletId = parameters.portalPortletId;
    if (UtilValidate.isNotEmpty(parameters.portalPageId)) {
        context.portalPageId = parameters.portalPageId;
    }
    if (UtilValidate.isNotEmpty(parameters.portletSeqId)) {
        context.portletSeqId = parameters.portletSeqId;
    }
    ModelScreenWidget.PortalPage.retrievePortletAttributes(context);
}
import org.ofbiz.widget.screen.ModelScreenWidget;
import org.ofbiz.base.util.UtilValidate;

if (UtilValidate.isNotEmpty(parameters.portalPortletId)) {
    context.portalPortletId = parameters.portalPortletId;
    if (UtilValidate.isNotEmpty(parameters.portalPageId)) {
        context.portalPageId = parameters.portalPageId;
    }
    if (UtilValidate.isNotEmpty(parameters.portletSeqId)) {
        context.portletSeqId = parameters.portletSeqId;
    }
    ModelScreenWidget.PortalPage.retrievePortletAttributes(context);
}
