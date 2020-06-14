'use strict';

function get(token, data, callback){
	const request = new XMLHttpRequest();
	request.open("GET", "/workbench-api/"+token, true);
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

function post(token, data, callback){
	const request = new XMLHttpRequest();
	request.open("POST", "/workbench-api/"+token, true);
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
export {get,post};