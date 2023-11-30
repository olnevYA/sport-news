<%@ include file="components/header.jspf"%>

<%@ include file="components/nav.jspf"%>

<div class="text-center">

	<form class="form-signin" action="<c:url value="/sign-up" />" method="POST">
		<h1 class="h3 mb-3 font-weight-normal">Please sign up</h1>

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
			<label for="inputUsername" class="sr-only">Username</label> <input
				type="text" id="inputUsername" class="form-control"
				placeholder="Username" required autofocus name="username"
				value="${requestScope.regUsername}">
		</div>

		<div class="form-group">
			<label for="inputEmail" class="sr-only">Email address</label> <input
				type="email" id="inputEmail" class="form-control"
				placeholder="Email address" required name="email"
				value="${requestScope.regEmail}">
		</div>

		<div class="form-group">
			<label for="inputName" class="sr-only">Name</label> <input
				type="text" id="inputName" class="form-control" placeholder="Name"
				required name="name" value="${requestScope.regName}">
		</div>

		<div class="form-group">
			<label for="inputPassword" class="sr-only">Password</label> <input
				type="password" id="inputPassword" class="form-control"
				placeholder="Password" required name="password">
		</div>

		<div class="form-group">
			<label for="inputUserRole" class="sr-only">User role</label> <select
				id="inputUserRole" name="userRoleId" class="form-control">
				<c:forEach items="${userService.allUserRoles}"
					var="userRole">
					<option
						${requestScope.regUserRoleId == userRole.id ? 'selected' : ''}
						value="${userRole.id}">${userRole.name}</option>
				</c:forEach>
			</select>
		</div>

		<button class="btn btn-lg btn-primary btn-block" type="submit">Sign
			up</button>

	</form>


</div>
<%@ include file="components/footer.jspf"%>
