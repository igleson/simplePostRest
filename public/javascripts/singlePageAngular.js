function ForumController($scope) {
	$scope.data = {};
	$scope.data.page = 0;
	$scope.data.posts = [];

	$scope.nextPage = function() {
		$scope.data.posts = [];
		$.getJSON('/post?initial=' + ($scope.data.page * 10) + '&_final='
				+ ($scope.data.page * 10 + 10), function(json) {
			for (var i = 0; i < json.length; i++) {
				addPost(json[i])
			}
			$scope.data.page = $scope.data.page + 1
			$scope.$apply()
		});
	}

	$scope.lastPage = function() {
		$scope.data.posts = [];
		$.getJSON('/post?initial=' + (($scope.data.page - 2) * 10) + '&_final='
				+ (($scope.data.page - 2) * 10 + 10), function(json) {
			for (var i = 0; i < json.length; i++) {
				addPost(json[i])
			}
			$scope.data.page = $scope.data.page - 1
			$scope.$apply()
		});
	}
	
	$scope.update = function() {
		$scope.data.page = $scope.data.page - 1
		$scope.nextPage()
	}

	$scope.getComments = function(index) {
		if($scope.data.posts[index].comments.length > 0){
			$scope.data.posts[index].showingComments = true;
		}else{
			id = $scope.data.posts[index].id
			$.getJSON('/post/' + id + '/comment', function(json) {
				for (var i = 0; i < json.length; i++) {
					addComment(index, json[i])
				}
				$scope.$apply()
			});
		}
	}
	
	$scope.refreshComments = function(index) {
		$scope.data.posts[index].comments = []
		$scope.data.posts[index].showingComments = false
		$scope.getComments(index)
	}
	
	$scope.hideCommments = function(index){
		$scope.data.posts[index].showingComments = false;
	}

	addPost = function(post) {
		$scope.data.posts.push({
			"id" : post.id,
			"text" : post.msg,
			"qtdeComments" : post.amountComments,
			"comments" : [],
			"showingComments" : false
		});
	}

	addComment = function(index, comment) {
		$scope.data.posts[index].showingComments = true;
		$scope.data.posts[index].comments.push({
			"comment" : comment.comment
		})
	}
}