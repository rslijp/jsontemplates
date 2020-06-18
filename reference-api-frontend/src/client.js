'use strict';

function call(method, token, data, callback){
	const request = new XMLHttpRequest();
	request.open(method, "/workbench-api/"+token, true);
	request.setRequestHeader('Content-Type', 'application/json;charset=UTF-8');
	request.setRequestHeader('Accept', 'application/json, text/javascript');
	request.onreadystatechange = (() => {
		return () => {
			if (request.readyState === 4) {
				if (request.status === 200) {
					const response = JSON.parse(request.response);

					callback(response);
				} else {
					alert("Error");
				}
			}
		};
	})(this);
	request.send(data?JSON.stringify(data):null);
}

function get(token, callback){
	call("GET", token, null, callback);
}

function post(token, data, callback){
	call("POST", token, data, callback);
}

function revert(token, callback){
	call("DELETE", token, null, callback);
}

export {get, post, revert};