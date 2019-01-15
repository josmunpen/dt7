<%--
 * create.jsp
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

<security:authorize access="hasRole('CUSTOMER')||hasRole('REFEREE')">
<form:form action="complaint/customer/edit.do" modelAttribute="complaint">

	<form:hidden path="id" />

	<form:hidden path="version" />
	
	<form:hidden path="ticker"/>
	
	<form:hidden path="moment"/>
	
	<jstl:if test="${complaint.id != 0}">
		<form:hidden path="fixUpTask"/>
	</jstl:if>
	<jstl:if test="${complaint.id == 0}">
		<form:label path="fixUpTask">
			<spring:message code="complaint.fixUpTask" />:
		</form:label>
		<form:select id="fixUpTasks" path="fixUpTask">
			<form:options items="${fixUpTasks}" itemLabel="address" itemValue="id"/>
			<form:option value="0" label="----"/>
		</form:select>
		<form:errors cssClass="error" path="fixUpTask" />
	</jstl:if>
	<br/>
	
	<form:label path="description">
			<spring:message code="complaint.description" />:
		</form:label>
		<form:textarea path="description"/>
		<form:errors cssClass="error" path="description" />
		<br/>
		
	<form:label path="attachments">
		<spring:message code="complaint.attachmentURL"/>
		</form:label>
		<form:textarea path="attachments" placeholder="http://www.ejemplo.com" />
		<form:errors cssClass="error" path="attachments" />
		
	<br />
	<br />


	<input type="submit" name="save" 
	value="<spring:message code="complaint.save" />" />&nbsp; 
	
	<input type="button" name="cancel" value="<spring:message code="phase.cancel" />"
		onclick="javascript: relativeRedir('complaint/customer/list.do');" />
	<br />

</form:form>
</security:authorize>