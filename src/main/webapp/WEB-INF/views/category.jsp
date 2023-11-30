<%@ include file="components/header.jspf"%>

<%@ include file="components/nav.jspf"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container">
	<div class="row mt-3">

		<div class="col-md-12">
			<div class="row mt-3 mb-3">
				<div class="col-md-12">
					<h3>Популярные</h3>
					<div class="row mt-3 mb-3">
						<c:if test="${ requestScope.mostPopular.size() >= 2 }">

						<c:forEach begin="0" end="1" var="i">
							<div class="col-md-6 row">
								<div class="col-md-4">

									<a href="<c:url value="/article"><c:param name="articleId" value="${ requestScope.mostPopular.get(i).id }" /></c:url>"><img class="img-fluid" id="articleImage" src="<c:url value="/images/${requestScope.mostPopular.get(i).imagePath}"/>"></a>
								</div>
								<div class="col-md-8">
									<a href="<c:url value="/article"><c:param name="articleId" value="${ requestScope.mostPopular.get(i).id }" /></c:url>"><h5>${ requestScope.mostPopular.get(i).title }</h5></a>
									<p>${ mostPopular.get(i).getExcerpt(100) }</p>
								</div>
							</div>
						</c:forEach>
						</c:if>
					</div>
					<div class="row mt-3 mb-3">
						<c:if test="${ requestScope.mostPopular.size() >= 4 }">
						<c:forEach begin="2" end="3" var="i">
							<div class="col-md-6 row">
								<div class="col-md-4">
									<a href="<c:url value="/article"><c:param name="articleId" value="${ requestScope.mostPopular.get(i).id }" /></c:url>"><img class="img-fluid" id="articleImage" src="<c:url value="/images/${requestScope.mostPopular.get(i).imagePath}"/>"></a>
								</div>
								<div class="col-md-8">
									<a href="<c:url value="/article"><c:param name="articleId" value="${ requestScope.mostPopular.get(i).id }" /></c:url>"><h5>${ requestScope.mostPopular.get(i).title }</h5></a>
									<p>${ mostPopular.get(i).getExcerpt(100) }</p>
								</div>
							</div>
						</c:forEach>
						</c:if>
					</div>
				</div>
			</div>
			<div class="row mt-3 mb-3">
				<div class="col-md-12">
					<h3>Последние</h3>

					<div class="row mt-3 overflow-hidden">

						<c:forEach items="${ requestScope.latest }" var="article">
						<div class="col-md-12 row mb-4">
							<div class="col-md-4">
								<a href="<c:url value="/article"><c:param name="articleId" value="${ article.id }" /></c:url>"><img class="img-fluid" id="articleImage" src="<c:url value="/images/${article.imagePath}"/>"></a>
							</div>
							<div class="col-md-8">
								<a href="<c:url value="/article"><c:param name="articleId" value="${ article.id }" /></c:url>"><h5>${ article.title }</h5></a>
								<p>${article.excerpt }</p>
							</div>
						</div>
						</c:forEach>

					</div>

				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="components/footer.jspf"%>
