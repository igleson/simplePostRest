var app = angular.module("app", []);

app.controller("appCtrl", ForumController)

var http = null;
var scope = null;
function ForumController($scope, $http) {
    http = $http;
    scope = $scope;
    $scope.data = {};
    $scope.data.page = 0;
    $scope.data.posts = [];

    $scope.data.novoPost = ""

    $scope.data.newComments = ["", "", "", "", "", "", "", "", "", ""]

    $scope.nextPage = function () {
        $scope.data.posts = [];
        url = '/post?initial=' + ($scope.data.page * 10) + '&_final='
            + ($scope.data.page * 10 + 10)
        postPromisses = $http.get(url)
        postPromisses.success(function (data) {
            $scope.data.newComments = ["", "", "", "", "", "", "", "", "", ""]
            updatePosts(data);
            $scope.data.page = $scope.data.page + 1
        });
    }


    $scope.lastPage = function () {
        $scope.data.posts = [];
        url = '/post?initial=' + (($scope.data.page - 2) * 10) + '&_final='
            + (($scope.data.page - 2) * 10 + 10)
        postPromisses = $http.get(url)
        postPromisses.success(function (data) {
            $scope.data.newComments = ["", "", "", "", "", "", "", "", "", ""]
            updatePosts(data);
            $scope.data.page = $scope.data.page - 1
        });
    }

    $scope.update = function () {
        $scope.data.page = $scope.data.page - 1
        $scope.nextPage()
    }

    $scope.getComments = function (index) {
        if ($scope.data.posts[index].comments.length > 0) {
            $scope.data.posts[index].showingComments = true;
        } else {
            id = $scope.data.posts[index].id
            url = '/post/' + id + '/comment'
            commentsPromisses = $http.get(url)
            commentsPromisses.success(function (data) {
                for (var i = 0; i < data.length; i++) {
                    addComment(index, data[i])
                }
            });
        }
    }

    $scope.refreshComments = function (index) {
        $scope.data.posts[index].comments = []
        $scope.data.posts[index].showingComments = false
        $scope.getComments(index)
    }

    $scope.hideCommments = function (index) {
        $scope.data.posts[index].showingComments = false;
        $scope.data.newComments = ["", "", "", "", "", "", "", "", "", ""]
    }

    $scope.newPost = function () {
        if($scope.data.novoPost.length == 0){
            alert("You must type something to post")
            return;
        }

        url = "/post"
        newPostPromisse = $http.post(url, {msg: $scope.data.novoPost})
        newPostPromisse = newPostPromisse.success(function (data) {
            $scope.data.novoPost = ""
            if ($scope.data.page == 1) {
                $scope.data.posts.splice(0, 0, {
                    "id": data[0].id,
                    "text": data[0].msg,
                    "qtdeComments": data[0].amountComments,
                    "comments": [],
                    "showingComments": false
                })
            }
            $scope.data.posts.splice(10, 1)
        }).error(function (data) {
            alert(data)
        });
    }

    $scope.newComment = function (index) {
        if ($scope.data.newComments[index].length == 0) {
            alert("You must type something to comment")
            return;
        }
        url = "/post/" + $scope.data.posts[index].id + "/comment"
        newCommentPromisse = $http.post(url, {comment: $scope.data.newComments[index]})
        newCommentPromisse = newCommentPromisse.success(function (data) {
            $scope.data.newComments[index] = ""
            if ($scope.data.posts[index].comments.length > 0) {
                $scope.data.posts[index].comments.splice(0, 0, {"comment": data[0].comment})
            }
            $scope.data.posts[index].qtdeComments = $scope.data.posts[index].qtdeComments + 1
        }).error(function (data) {
            alert(data)
        });
        return newCommentPromisse
    }

    function updatePosts(data) {
        for (var i = 0; i < data.length; i++) {
            addPost(data[i])
        }
    }

    addPost = function (post) {
        $scope.data.posts.push({
            "id": post.id,
            "text": post.msg,
            "qtdeComments": post.amountComments,
            "comments": [],
            "showingComments": false
        });
    }

    addComment = function (index, comment) {
        $scope.data.posts[index].showingComments = true;
        $scope.data.posts[index].comments.push({
            "comment": comment.comment
        })
    }
}

