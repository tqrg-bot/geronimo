/**
 *
 * Copyright 2005 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.geronimo.console.keystores;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.geronimo.console.MultiPageModel;

/**
 * Handler for importing a certficate issued by a CA
 *
 * @version $Rev: 409817 $ $Date: 2006-05-27 13:26:38 +0530 (Sat, 27 May 2006) $
 */
public class ImportCAReplyHandler extends BaseKeystoreHandler {
    public ImportCAReplyHandler() {
        super(IMPORT_CA_REPLY, "/WEB-INF/view/keystore/importCAReply.jsp");
    }

    public String actionBeforeView(ActionRequest request, ActionResponse response, MultiPageModel model) throws PortletException, IOException {
    	String id = request.getParameter("id");
    	String alias = request.getParameter("alias");
        response.setRenderParameter("id", id);
        response.setRenderParameter("alias", alias);
        return getMode();
    }
	public void renderView(RenderRequest request, RenderResponse response, MultiPageModel model) throws PortletException, IOException {
		String id = request.getParameter("id");
    	String alias = request.getParameter("alias");
        request.setAttribute("id", id);
        request.setAttribute("alias", alias);
    }

    public String actionAfterView(ActionRequest request, ActionResponse response, MultiPageModel model) throws PortletException, IOException {
        String id = request.getParameter("id");
        String alias = request.getParameter("alias");
        response.setRenderParameter("id", id);
        response.setRenderParameter("alias", alias);
    	if("Cancel".equals(request.getParameter("submit")))
    		return CERTIFICATE_DETAILS+BEFORE_ACTION;
        String pkcs7cert = request.getParameter("pkcs7cert");
        KeystoreData data = ((KeystoreData) request.getPortletSession(true).getAttribute(KEYSTORE_DATA_PREFIX + id));
        data.importPKCS7Certificate(alias, pkcs7cert);
        return CERTIFICATE_DETAILS+BEFORE_ACTION;
    }
}
