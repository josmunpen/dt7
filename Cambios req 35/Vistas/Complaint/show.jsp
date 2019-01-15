<%--
 * show.jsp
 *
 * Copyright (C) 2018 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- Action links -->

<security:authorize access="hasRole('CUSTOMER')||hasRole('REFEREE')">
	<div>
		<spring:message code="complaint.list" />:
		<a href="complaint/customer/list.do"> <spring:message
				code="complaint.list" />
		</a>
	</div>
</security:authorize>

<!------------------->
	
 		<p><span><spring:message code="complaint.ticker"/>:
		<jstl:out value="${complaint.ticker}"></jstl:out></span></p>

		<p><span>
		<spring:message code="complaint.moment"/>:
		<fmt:formatDate value="${complaint.moment}" />
		</span></p>
		
		<p><span>
		<spring:message code="complaint.description" />:
		<jstl:out value="${complaint.description}"></jstl:out></span></p>
		
		
		<p><span>
		<spring:message code="complaint.attachmentURL" />:
		<jstl:out value="${complaint.attachments}" />
		</span>
	
	<security:authorize access="hasRole('REFEREE')">
	<a href="report/referee/create.do?complaintId=${complaint.id}"><spring:message code="complaint.createReport"/></a>
	</security:authorize>	
	