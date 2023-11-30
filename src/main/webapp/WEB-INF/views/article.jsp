<%@ include file="components/header.jspf"%>

<%@ include file="components/nav.jspf"%>

<div class="container">
	<div class="row mt-3">

		<div class="col-md-12">

			<h2>${ article.title }</h2>
			${ article.publishedTime } &gt; ${ article.editedTime }
			<hr />
			
			
			<div class="text-center py-3">
				
				<figure class="figure">
  <img src="<c:url value="/images/${article.imagePath}"/>" class="figure-img img-fluid my-2" alt="...">
  <figcaption class="figure-caption">${ article.imageCredits }</figcaption>
</figure>
			</div>
				
			<p>${ article.content }</p>
			
	<h4>Comments:</h4>
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
	<div class="row mt-3">
		
        <div class="comment-wrapper">
            <div class="card">
                <div class="card-body">
                
                	<form action='<c:url value="/comments/do/add"/>' method="POST" >
                	<input type="hidden" name="articleId" value="${ article.id }"/>
                	<input class="form-control form-group" type="text" name="commentAuthor" placeholder="Name...">
                	<textarea name="commentContent" class="form-control" placeholder="Write a comment..." rows="3"></textarea>
                    <br>
                    <button name="addCommentSubmit" type="submit" class="btn btn-info pull-right">Post</button>
                    </form>
                    <div class="clearfix"></div>
                    <hr>
                    <ul class="media-list">
                        <c:forEach items="${ comments }" var="comment">
                        	<li class="media">
                            <a href="#" class="pull-left px-2">
                                <i class="fas fa-user-tie fa-3x"></i>
                            </a>
                            <div class="media-body">
                                <span class="text-muted pull-right">
                                    <small class="text-muted">${ comment.formattedDateTime }</small>
                                </span>
                                <strong class="text-success">@${ comment.author }</strong>
                                <p>
                                    ${ comment.content }
                                </p>
                            </div>
                        </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
	</div>
		</div>
	</div>

</div>

<%@ include file="components/footer.jspf"%>
