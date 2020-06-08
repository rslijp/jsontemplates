'use strict';

function call(url, data, callback){
	var request = new XMLHttpRequest();
	request.open("GET", url, true);
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

module.exports = call;
