/*
 * Copyright (c) 1997-2006 by wemove GmbH
 */
package de.ingrid.portal.portlets.admin;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.portals.bridges.common.GenericServletPortlet;
import org.apache.portals.bridges.velocity.GenericVelocityPortlet;
import org.apache.velocity.context.Context;

import de.ingrid.portal.global.IngridResourceBundle;
import de.ingrid.portal.global.Settings;
import de.ingrid.portal.scheduler.IngridMonitorFacade;

/**
 * Portlet handling content management of Partners
 * 
 * @author martin@wemove.com
 */
public class AdminComponentMonitorPortlet extends GenericVelocityPortlet {

	private final static Log log = LogFactory.getLog(AdminComponentMonitorPortlet.class);

	private static final String PSML_PAGE = "/ingrid-portal/portal/administration/admin-component-monitor.psml";

	private static final String VIEW_DEFAULT = "/WEB-INF/templates/administration/component-monitor.vm";

	private static final String VIEW_EDIT = "/WEB-INF/templates/administration/edit_component-monitor.vm";

	private static final String VIEW_NEW = "/WEB-INF/templates/administration/edit_component-monitor.vm";

	/**
	 * @see javax.portlet.Portlet#init(javax.portlet.PortletConfig)
	 */
	public void init(PortletConfig config) throws PortletException {
		super.init(config);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.portals.bridges.velocity.GenericVelocityPortlet#doView(javax.portlet.RenderRequest,
	 *      javax.portlet.RenderResponse)
	 */
	public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {

		IngridResourceBundle messages = new IngridResourceBundle(getPortletConfig().getResourceBundle(
				request.getLocale()));
		Context context = getContext(request);
		context.put("MESSAGES", messages);

		String action = request.getParameter(Settings.PARAM_ACTION);
		if (action == null) {
			request.setAttribute(GenericServletPortlet.PARAM_VIEW_PAGE, VIEW_DEFAULT);
			String sortBy = request.getParameter("sortby");
			boolean ascending = request.getParameter("desc") == null || !request.getParameter("desc").equals("true");
			context.put("model", IngridMonitorFacade.instance().getJobs(sortBy, ascending));
		}
		super.doView(request, response);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.portals.bridges.velocity.GenericVelocityPortlet#processAction(javax.portlet.ActionRequest,
	 *      javax.portlet.ActionResponse)
	 */
	public void processAction(ActionRequest request, ActionResponse response) throws PortletException, IOException {
		// TODO Auto-generated method stub
		super.processAction(request, response);
	}

}
