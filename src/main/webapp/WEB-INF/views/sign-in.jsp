<%@page import="rs.rnk.example.sportnews.service.MessageService"%>
<%@ include file="components/header.jspf"%>

<%@ include file="components/nav.jspf"%>

<div class="text-center">
	<form class="form-signin" method="POST" action="<c:url value="/do/login" />">
		<h1 class="h3 mb-3 font-weight-normal">Please sign in</h1>

		<c:forEach items="${requestScope.messageService.messages }"
			var="message">
			<div
				class="alert alert-${ message.type.name } form-group alert-dismissible fade show"
				role="alert">
				${ message.content }
				<button type="button" class="close" data-dismiss="alert"
					aria-label="Close">
					<span aria-hidden="true">&times;</span>
				</button>
			</div>
		</c:forEach>


		<div class="form-group">
			<label for="inputUsername" class="sr-only">Email address</label> <input
				type="text" id="inputUsername" name="username" class="form-control"
				placeholder="Username" autofocus
				value="${requestScope.loginUsername}">
		</div>
		<div class="form-group">
			<label for="inputPassword" class="sr-only">Password</label> <input
				type="password" id="inputPassword" class="form-control"
				name="password" placeholder="Password">
		</div>
		<button class="btn btn-lg btn-primary btn-block" type="submit">Sign
			in</button>

	</form>


</div>
<%@ include file="components/footer.jspf"%>
