new function() {
	var ws = null;
	var connected = false;

	var serverUrl;
	var connectionStatus;
	var sendMessage;
	
	
	
	var open = function(url) {
		ws = new WebSocket(url);
		ws.onopen = onOpen;
		ws.onclose = onClose;
		ws.onmessage = onMessage;
		ws.onerror = onError;
	}
	
	
	var close = function() {
		if (ws) {
			console.log('CLOSING ...');
			ws.close();
		}
		
	}
	
	
	var onOpen = function() {
		console.log('OPENED: ' + serverUrl);
		connected = true;
	};
	
	var onClose = function() {
		console.log('CLOSED: ' + serverUrl);
		ws = null;
	};
	
	
	
	
	
	
	var onMessage = function(event) {
		console.log("Mute = " + mute );
		if ( mute == 0 ) 
		{
			
		
		var data = event.data;
		try
		{
			var jsonMessage=JSON.parse(data);
			console.log(jsonMessage);
			if ( jsonMessage.action == "RELOAD" )
			{
			
				document.getElementById("reloadMessage").style.display = "block";
				//document.getElementById("messageImage").src = "/display?fileName=" + jsonMessage.imagePath;
				document.getElementById("messageDetail").innerHTML = jsonMessage.message;
				
				
			}else
			{
 			 console.log("MESSAGE not implemented  " + jsonMessage.action);
			}
		}  catch (e)
		{
			console.log("Something wrong with parsing jsonMessage " + data);
		}
		}
	};
	
	var onError = function(event) {
		alert("Error : " + event.data);
	}
	
	

	WebSocketClient = {
			open: function (url,user)
			{
				serverUrl=url;
				open(url,user);
				
			},
			send: function (message)
			{
				ws.send(message);
			}
			
		
	};
}