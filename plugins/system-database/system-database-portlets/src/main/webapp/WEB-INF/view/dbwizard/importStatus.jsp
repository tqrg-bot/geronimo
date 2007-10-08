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

<p><b>Import Database Pools</b> -- Step 2: Review Imported Data</p>

<p>The list of recognized database pools appears below.  You can deploy any pools to
  Geronimo that were configured as plain JDBC pools, or XA pools where Geronimo has
  a supported XA adapter.  Below the pool list is the list of status messages from
  the import process.</p>

<c:choose>
  <c:when test="${empty(status.pools)}"><p><i>The import failed or did not discover any usable database pools!</i></p></c:when>
  <c:otherwise>
<table width="100%">
  <tr>
    <td class="DarkBackground">Original Name</td>
    <td class="DarkBackground" align="center">Original JNDI</td>
    <td class="DarkBackground" align="center">Import Status</td>
    <td class="DarkBackground" align="center">Actions</td>
  </tr>
<c:forEach var="pool" items="${status.pools}" varStatus="loop" >
  <tr>
    <td>${pool.pool.name}</td>
    <td>${pool.pool.jndiName}</td>
    <td>${pool.status}</td>
    <td>
  <c:choose>
    <c:when test="${pool.skipped || pool.finished}">
    </c:when>
    <c:otherwise>
      <a href="<portlet:actionURL portletMode="view">
        <portlet:param name="mode" value="importEdit" />
        <portlet:param name="importIndex" value="${loop.index}" />
      </portlet:actionURL>">Confirm and Deploy</a>
    </c:otherwise>
  </c:choose>
    </td>
  </tr>
</c:forEach>
  <tr>
    <td colspan="4" align="center">
      <a href="<portlet:actionURL portletMode="view">
        <portlet:param name="mode" value="importComplete" />
      </portlet:actionURL>"><c:choose><c:when test="${status.finished}">Finish</c:when><c:otherwise>Skip Remaining Pools</c:otherwise></c:choose></a>
    </td>
  </tr>
</table>
  </c:otherwise>
</c:choose>

<hr />

<p>Current Pools in Server:</p>
<ul>
<c:forEach var="pool" items="${pools}">
  <li>${pool.name}</li>
</c:forEach>
</ul>

<hr />

<p>Import Messages:</p>
<ul>
<c:forEach var="message" items="${status.original.messages}">
  <li>${message}</li>
</c:forEach>
</ul>
