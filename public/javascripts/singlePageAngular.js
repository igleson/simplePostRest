var app = angular.module("app", []);

app.controller("appCtrl", ForumController)

var http = null;
function ForumController($scope, $http) {
	http = $http;
	$scope.data = {};
	$scope.data.page = 0;
	$scope.data.posts = [];

	$scope.nextPage = function() {
		$scope.data.posts = [];
		url = '/post?initial=' + ($scope.data.page * 10) + '&_final='
				+ ($scope.data.page * 10 + 10)
		postPromisses = $http.get(url)
		postPromisses.success(function(data){
			updatePosts(data);
			$scope.data.page = $scope.data.page + 1
		});
	}


	$scope.lastPage = function() {
		$scope.data.posts = [];
		url = '/post?initial=' + (($scope.data.page - 2) * 10) + '&_final='
				+ (($scope.data.page - 2) * 10 + 10)
		postPromisses = $http.get(url)
		postPromisses.success(function(data){
			updatePosts(data);
			$scope.data.page = $scope.data.page - 1
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
			url = '/post/' + id + '/comment'
			commentsPromisses = $http.get(url)
			commentsPromisses.success(function(data){
				for (var i = 0; i < data.length; i++) {
					addComment(index, data[i])
				}
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
	
	$scope.newPost = function(){
		url = "/post"
		newPostPromisse = $http.post(url, "msg" + $scope.data.novoPost)
		newPostPromisse = newPostPromisse.success(function(data){
			alert(data)
			$scope.data.novoPost = ""
		}).error(function(data){
			alert(data)
		});
	}

	function updatePosts(data){
		for(var i = 0; i < data.length; i++){
			addPost(data[i])
		}
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

