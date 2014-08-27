var lastPost = -9
function getPosts(initial) {
	$("#loading").show()
	$.getJSON('/post?initial=' + initial + '&_final=' + (initial + 10),
			function(json) {
				$("#posts").empty()
				for (var i = json.length - 1; i >= 0; i--) {
					list = $("#posts li ul")
					comment = "<li>" + json[i].msg + ", Comments: "
							+ viewComment(json[i]) + "</li>" + '<ul id="'
							+ json[i].id + 'ul"></ul>'
					if (list.length == 0) {
						$("#posts").prepend(comment);

					} else {
						list.last().after(comment)
					}
					$("#" + json[i].id).click({
						id : json[i].id
					}, callBackGetComments);
				}
				$("#loading").fadeOut();
			});
}

function callBackGetComments(event) {
	getComments(event.data.id)
}

function viewComment(json) {
	return "<a id=" + '"' + json.id + '"' + ">" + json.amountComments + " </a>"
}

function refreshComments(event){
	$("#" + event.data.id + "ul li").remove()
	$("#" + event.data.id + "button").remove()
	$("#" + event.data.id + "hide").remove()
	getComments(event.data.id)
	$("#" + event.data.id + "ul").after(
			'<button id="' + event.data.id
					+ 'button" type="button"> refresh </button>')
	
	$("#" + event.data.id + "button").click({
		id : event.data.id
	}, refreshComments);
}

function getComments(id) {
	commentsList = $("#" + id + "ul li")
	if (commentsList.length == 0) {

		$("#" + id + "ul").after(
				'<button id="' + id
						+ 'button" type="button"> refresh </button>')
		
		$("#" + id + "button").click({
			id : id
		}, refreshComments);

			$("#" + id + "button").after(
					'<button id="' + id + 'hide" type="button"> hide </button>')
	
		$("#" + id + "hide").click(function() {

			$("#" + id + "ul li").fadeOut()

			$("#" + id + "button").fadeOut()

			$("#" + id + "hide").fadeOut()
		});
		$.getJSON('/post/' + id + '/comment', function(json) {
			for (var i = 0; i < json.length; i++) {
				$("#" + id + "button").click({
					id : id
				}, function(event) {
					$("#" + id + "ul").empty()
					$("#" + id + "button").remove()
					getComments(event)
				})
				list = $("#" + id + "ul li")
				comment = "<li>" + json[i].comment + "</li>"
				if (list.length == 0) {
					$("#" + id + "ul").prepend(comment)
				} else {
					list.last().after(comment)
				}
			}
		});
	} else {
		commentsList.show()
		$("#" + id + "button").show()
		$("#" + id + "hide").show()
	}
}

function nextPosts(initial) {
	getPosts(initial)
	lastPost = initial
	if(lastPost > 1){
		$("#last").show()
	}
}

function lastPosts(initial) {
	if (initial >= 1) {
		getPosts(initial)
		lastPost = initial
	}
	if (lastPost == 1) {
		hidenLast()
	}
}

function hidenLast() {
	$("#last").hide()
}

function disableLast() {
	return lastPost > 1
}

function update() {
	$("#posts").empty()
	lastPost = 1
	getPosts(lastPost)
}
