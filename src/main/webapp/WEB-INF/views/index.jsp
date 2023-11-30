<%@ include file="components/header.jspf"%>

<%@ include file="components/nav.jspf"%>

<div class="container">
	<div class="row mt-3">

		<div class="col-md-12">
			<div class="row">
				<div class="col-md-12">
					<c:forEach
						items="${requestScope.articleService.getCategoryArticles(6)}"
						var="categoryArticle">
						<div class="card mb-3">
							<div class="card-header">
								<h4><a
									href="<c:url value="/category"><c:param name="id" value="${categoryArticle.key.id}"/></c:url>"
									class="card-link text-dark">${categoryArticle.key.name}</a></h4>
							</div>
							<div class="card-body">
								<div class="card-group mb-2">
								<c:if test="${ categoryArticle.value.size() < 6 }">
									No articles found
								</c:if>
									<c:if test="${ categoryArticle.value.size() == 6 }">
									<c:forEach begin="0" end="2" var="i">
										<div class="card bg-inverse text-white ${ i == 1 ? 'ml-2 mr-2' : '' }">

												<img class="img-fluid w-100 h-100" id="articleImage" src="<c:url value="/images/${categoryArticle.value.get(i).imagePath}"/>">

											
											<div style="background-color: rgba(0, 0, 0, 0.4);"
												class="card-img-overlay h-100 w-100 d-flex flex-column justify-content-end">
												<h4  class="card-title">
												<a href="<c:url value="/article"><c:param name="articleId" value="${ categoryArticle.value.get(i).id }" /></c:url>" class="text-white">${ categoryArticle.value.get(i).title }</a>
												</h4>
												<p class="card-text">
													<small class="text-reset">${categoryArticle.value.get(i).publishedTime} ${categoryArticle.value.get(i).publishedTime != categoryArticle.value.get(i).editedTime ? '> ' : ''}
														${categoryArticle.value.get(i).publishedTime != categoryArticle.value.get(i).editedTime ? categoryArticle.value.get(i).editedTime : ''}
													</small>
												</p>
											</div>
										</div>

									</c:forEach>
									</c:if>
								</div>
								<div class="card-group mb-2">
									<c:if test="${ categoryArticle.value.size() == 6 }">
									<c:forEach begin="3" end="5" var="i">
										<div class="card bg-inverse text-white ${ i == 4 ? 'ml-2 mr-2' : '' }">
											<a href="#">
											<img class="img-fluid w-100 h-100" id="articleImage" src="<c:url value="/images/${categoryArticle.value.get(i).imagePath}"/>">
											</a>
											<div style="background-color: rgba(0, 0, 0, 0.4);"
												class="card-img-overlay h-100 d-flex flex-column justify-content-end">
												<h4 class="card-title">
													<a href="<c:url value="/article"><c:param name="articleId" value="${ categoryArticle.value.get(i).id }" /></c:url>" class="text-white">${ categoryArticle.value.get(i).title }</a>
												</h4>
												<p class="card-text">
													<small class="text-reset">${categoryArticle.value.get(i).publishedTime} ${categoryArticle.value.get(i).publishedTime != categoryArticle.value.get(i).editedTime ? '> ' : ''}
														${categoryArticle.value.get(i).publishedTime != categoryArticle.value.get(i).editedTime ? categoryArticle.value.get(i).editedTime : ''}
													</small>
												</p>
											</div>
										</div>
									</c:forEach>
									</c:if>
								</div>

							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
</div>
<%@ include file="components/footer.jspf"%>
