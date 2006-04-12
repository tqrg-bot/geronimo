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

import org.apache.commons.fileupload.FileItem;
import org.apache.geronimo.console.MultiPageModel;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.File;
import java.io.IOException;

/**
 * Handler for entering a password to unlock a keystore
 *
 * @version $Rev: 46019 $ $Date: 2004-09-14 05:56:06 -0400 (Tue, 14 Sep 2004) $
 */
public class UploadCertificateHandler extends BaseKeystoreHandler {
    public UploadCertificateHandler() {
        super(UPLOAD_CERTIFICATE, "/WEB-INF/view/keystore/uploadCertificate.jsp");
    }

    public String actionBeforeView(ActionRequest request, ActionResponse response, MultiPageModel model) throws PortletException, IOException {
        String id = request.getParameter("id");
        if(id != null) {
            response.setRenderParameter("id", id);
        } // else we hope this is after a failure and the actionAfterView took care of it below!
        return getMode();
    }

    public void renderView(RenderRequest request, RenderResponse response, MultiPageModel model) throws PortletException, IOException {
        request.setAttribute("id", request.getParameter("id"));
    }

    public String actionAfterView(ActionRequest request, ActionResponse response, MultiPageModel model) throws PortletException, IOException {
        String id = getUploadFields().getProperty("id");
        if(id == null) {
            return LIST_MODE+BEFORE_ACTION;
        }
        String alias = getUploadFields().getProperty("alias");
        if(alias == null) {
            return getMode()+BEFORE_ACTION; //todo: some kind of error message
        }
        File certFile;
        FileItem item = (FileItem) getUploadFiles().get("certificate");
        if(item != null) {
            certFile = File.createTempFile("geronimo", ".cert");
            certFile.deleteOnExit();
            try {
                item.write(certFile);
            } catch (Exception e) {
                throw new PortletException("Unable to save uploaded file", e);
            }
        } else {
            response.setRenderParameter("id", id);
            return getMode()+BEFORE_ACTION;
        }
        response.setRenderParameter("id", id); // the Keystore
        response.setRenderParameter("certificate", certFile.getAbsolutePath());
        response.setRenderParameter("alias", alias);

        return CONFIRM_CERTIFICATE+BEFORE_ACTION;
    }
}
