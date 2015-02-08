function err_message_quietly(msg, f) {
	$.layer({
		title : false,
		closeBtn : false,
		time : 2,
		dialog : {
			msg : msg
		},
		end : f
	});
}

function ok_message_quietly(msg, f) {
	$.layer({
		title : false,
		closeBtn : false,
		time : 1,
		dialog : {
			msg : msg,
			type : 1
		},
		end : f
	});
}

function my_confirm(msg, btns, yes_func, no_func) {
	$.layer({
		shade : [ 0 ],
		area : [ 'auto', 'auto' ],
		dialog : {
			msg : msg,
			btns : 2,
			type : 4,
			btn : btns,
			yes : yes_func,
			no : no_func
		}
	});
}

function handle_quietly(json, f) {
	if (json.msg.length > 0) {
		err_message_quietly(json.msg);
	} else {
		ok_message_quietly("successfully:-)", f);
	}
}

// - business function -
function create_app() {
	$.post("/app/create", {"name": $("#name").val(), "team": $("#team").val(), "health": $("#health").val()}, function(json) {
		handle_quietly(json, function() {
			location.href="/";
		})
	});
}

function del_app(app_id) {
    my_confirm('确定要删除？？？', ['确定', '取消'], function () {
        $.getJSON('/app/delete/' + app_id, {}, function (json) {
            handle_quietly(json, function() {
            	location.reload();
            });
        });
    }, function () {
        return false;
    });
} 

function add_env(app_id) {
	$.post("/env/add", {"k": $("#k").val(), "v": $("#v").val(), "app_id": $("#app_id").val()}, function(json) {
		handle_quietly(json, function() {
			location.reload();
		})
	});
}

function del_env(env_id) {
	$.post("/env/delete", {"id": env_id}, function(json) {
		handle_quietly(json, function() {
			location.reload();
		})
	});
}

function create_domain() {
	$.post("/domain/create", {"domain": $("#domain").val(), "team": $("#team").val()}, function(json) {
		handle_quietly(json, function() {
			location.href="/domain";
		})
	});
}

function delete_domain(id) {
	$.post("/domain/delete/" + id, {}, function(json) {
		handle_quietly(json, function() {
			location.reload();
		})
	});
}

function scale() {
	$.post("/app/scale/" + $("#app_id").val(), {"instance": $("#instance").val()}, function(json) {
		handle_quietly(json, function() {
			location.href = "/";
		})
	});
}

function edit_app() {
	$.post("/app/edit/" + $("#app_id").val(), {"team": $("#team").val(), "health": $("#health").val()}, function(json) {
		handle_quietly(json, function() {
			location.href = "/";
		})
	});
}

function edit_domain() {
	$.post("/domain/edit/" + $("#domain_id").val(), {"team": $("#team").val()}, function(json) {
		handle_quietly(json, function() {
			location.href = "/domain";
		})
	});
}

function bind_app() {
	$.post("/domain/bind/" + $("#domain_id").val(), {"app_id": $("#app_id").val()}, function(json) {
		handle_quietly(json, function() {
			location.href = "/domain";
		})
	});
}

function del_history(id) {
	$.post("/history/delete/" + id, {}, function(json) {
		handle_quietly(json, function() {
			location.reload();
		});
	});
}

function deploy_app() {
	$.post("/app/deploy/" + $("#app_id").val(), {
		"memory": $("#memory").val(),
		"instance": $("#instance").val(),
		"resume": $("#resume").val(),
		"image": $("#image").val()
	}, function(json) {
		if (json.msg.length > 0) {
			err_message_quietly(json.msg);
		} else {
			ok_message_quietly("successfully:-) 后台正在努力部署...", function() {
				location.href="/app/instance/" + $("#app_id").val();
			});
		}
	});
}

