<%--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet"%>
<portlet:defineObjects/>
<p>This tool walks you through the process of configuring access to Geronimo
   through the Apache 2 HTTP server using the <tt>mod_jk</tt> Apache module.
   There are several steps needed to do this:</p>
<ol>
    <li>Configure the Geronimo web container to support the AJP protocol</li>
    <li>Install the <tt>mod_jk.so</tt> Apache module (compiling it if necessary)</li>
    <li>Create a <tt>workers.properties</tt> configuration file to point Apache to the
       Geronimo AJP connector</li>
    <li>Add Apache configuration information indicating which URLs should be sent to
       Geronimo and whether Apache should serve static content (leaving only
       dynamic content within the web application to Geronimo)</li>
</ol>
<p>By answering a few questions here, we can give you specific guidance and generate
  configuration information you can use.</p>
<p>Notes:</p>
<ul>
    <li>Geronimo does not remember previous settings generated using this tool;
        you'll need to reenter all the data each time, or just paste the new
        information into your existing configuration files.</li>
    <li>Any web applications to be exposed via Apache must be running when you
        run this tool, and of course must be running to be accessed from Apache.</li>
</ul>

<a href="<portlet:actionURL portletMode="view"><portlet:param name="mode" value="index-after" /></portlet:actionURL>">Get Started</a>
