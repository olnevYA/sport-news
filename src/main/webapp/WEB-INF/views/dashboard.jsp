<%@ include file="components/dashboard/header.jspf"%>

<%@ include file="components/dashboard/nav.jspf"%>

<div class="container-fluid">
	<div class="row">
		<%@ include file="components/dashboard/sidebar.jspf"%>
		
		<main role="main" class="col-md-9 ml-sm-auto col-lg-10 px-4"> 
		
		<c:if test="${ requestScope.dashboardView == 'users' }">
			<%@ include file="components/dashboard/users.jspf"%>
		</c:if> 
		
		<c:if test="${ requestScope.dashboardView == 'articles' }">
			<%@ include file="components/dashboard/articles.jspf"%>
		</c:if> 
		
		<c:if test="${ requestScope.dashboardView == 'categories' }">
			<%@ include file="components/dashboard/categories.jspf"%>
		</c:if> 
		
		<c:if test="${ requestScope.dashboardView == 'comments' }">
			<%@ include file="components/dashboard/comments.jspf"%>
		</c:if> 
		
		</main>
	</div>
</div>

<%@ include file="components/dashboard/footer.jspf"%>
