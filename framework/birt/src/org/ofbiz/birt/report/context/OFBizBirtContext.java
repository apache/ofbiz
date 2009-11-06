package org.ofbiz.birt.report.context;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.report.IBirtConstants;
import org.eclipse.birt.report.context.BirtContext;
import org.eclipse.birt.report.context.ViewerAttributeBean;

public class OFBizBirtContext extends BirtContext {

    public OFBizBirtContext(HttpServletRequest request,
            HttpServletResponse response) {
        super(request, response);
        // TODO Auto-generated constructor stub
    }

    protected void __init() {
        // TODO Auto-generated method stub
        this.bean = (ViewerAttributeBean) request
                .getAttribute( IBirtConstants.ATTRIBUTE_BEAN );
        if ( bean == null )
        {
            bean = new BirtViewerAttributeBean( request );
        }
        request.setAttribute( IBirtConstants.ATTRIBUTE_BEAN, bean );
    }
}
