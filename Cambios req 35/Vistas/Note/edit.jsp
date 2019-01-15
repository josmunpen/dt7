<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<security:authorize access="hasRole('CUSTOMER')">
<form:form action="note/customer/edit.do" modelAttribute="note">
	
	<form:hidden path="id" />

	<form:hidden path="version" />
	
	<form:hidden path="moment" />
	
	<form:hidden path="handyWorker" />
	
	<form:hidden path="referee" />
	
	<form:hidden path="customer" />
	
	<form:hidden path="mandatoryComment" />
	
	<form:hidden path="refereeComment" />
	
	<form:hidden path="handyWorkerComment" />
	
	<form:label path="customerComment">
			<spring:message code="note.comments" />:
		</form:label>
		<form:textarea path="customerComment"/>
		<form:errors cssClass="error" path="customerComment" />
		<br/>	
		<br/>
	
	<input type="submit" name="save"
	 value="<spring:message code="note.save" />" />&nbsp;
	<input type="button" name="cancel" onclick="javascript: window.location.replace('note/customer/list.do')"
			value="<spring:message code="note.cancel" />" />
	</form:form>
	
	<br/><br/>
	
</security:authorize>

<security:authorize access="hasRole('HANDYWORKER')">
<form:form action="note/handyworker/edit.do" modelAttribute="note">
	
	<form:hidden path="id" />

	<form:hidden path="version" />
	
	<form:hidden path="moment" />
	
	<form:hidden path="handyWorker" />
	
	<form:hidden path="referee" />
	
	<form:hidden path="customer" />
	
	<form:hidden path="mandatoryComment" />
	
	<form:hidden path="refereeComment" />
	
	<form:hidden path="customerComment" />
	
	<form:label path="handyWorkerComment">
			<spring:message code="note.comments" />:
		</form:label>
		<form:textarea path="handyWorkerComment"/>
		<form:errors cssClass="error" path="handyWorkerComment" />
		<br/>
		<br/>
	
	<input type="submit" name="save"
	 value="<spring:message code="note.save" />" />&nbsp;
	<input type="button" name="cancel" onclick="javascript: window.location.replace('note/handyWorker/list.do')"
			value="<spring:message code="note.cancel" />" />
	</form:form>
	
</security:authorize>

<security:authorize access="hasRole('REFEREE')">
<form:form action="note/referee/edit.do" modelAttribute="note">
	
	<form:hidden path="id" />

	<form:hidden path="version" />
	
	<form:hidden path="moment" />
	
	<form:hidden path="handyWorker" />
	
	<form:hidden path="referee" />
	
	<form:hidden path="customer" />
	
	<form:hidden path="mandatoryComment" />
	
	<form:hidden path="handyWorkerComment" />
	
	<form:hidden path="customerComment" />
	
	<form:label path="refereeComment">
			<spring:message code="note.comments" />:
		</form:label>
		<form:textarea path="refereeComment"/>
		<form:errors cssClass="error" path="refereeComment" />
		<br/>
	<br/>
	<input type="submit" name="save"
	 value="<spring:message code="note.save" />" />&nbsp;
	<input type="button" name="cancel" onclick="javascript: window.location.replace('note/referee/list.do')"
			value="<spring:message code="note.cancel" />" />
	</form:form>
	
</security:authorize>

