
var lasthr = -5;
var isVibrateEnabled = false;
var SAAgent,
    SASocket,
    connectionListener;

/* Make Provider application running in background */
tizen.application.getCurrentApplication().hide();

window.onload = function () {
	$("#bodyDiv").load("home.html");
	window.webapis.motion.start("HRM", onchangedCB);
};

function receiveData(channelId, data) {
	//data
    if (!SAAgent.channelIds[0]) {
        console.log("Something goes wrong...NO CHANNEL ID!");
        return;
    }
    console.log("RECEIVED: " + data);
    if (data == "EXIT") exit();
    else if (data == "ALERT") {
    	$("#bodyDiv").load("alert.html");
    	isVibrateEnabled = true;
    } else if (data == "ALERT_CANCEL") {
    	$("#bodyDiv").load("home.html");
    	isVibrateEnabled = false;
    } else {
    	lasthr = Math.floor((Math.random() * 20) + 100);
    	SASocket.sendData(SAAgent.channelIds[0], lasthr);
    }
}


function onchangedCB(hrmInfo) 
{
	if (isVibrateEnabled == true) navigator.vibrate(50);
	lasthr = hrmInfo.heartRate;
}

function exit() {
	webapis.motion.stop("HRM");
	try {
		if (SASocket != null) {
			SASocket.close();
			SASocket = null;
			log("closeConnection");
		}
	} catch(err) {
		log("exception [" + err.name + "] msg[" + err.message + "]");
	}

	tizen.application.getCurrentApplication().exit();
}

function onAlertButtonClick() {
	SASocket.sendData(SAAgent.channelIds[0], "ALERT_CANCEL");
	$("#bodyDiv").load("home.html");
}

connectionListener = {
	    /* Remote peer agent (Consumer) requests a service (Provider) connection */
	    onrequest: function (peerAgent) {

	        console.log("peerAgent: peerAgent.appName<br />" +
	                    "is requsting Service conncetion...");

	        /* Check connecting peer by appName*/
	        if (peerAgent.appName === "Liveo") {
	            SAAgent.acceptServiceConnectionRequest(peerAgent);
	            console.log("Service connection request accepted.");

	        } else {
	            SAAgent.rejectServiceConnectionRequest(peerAgent);
	            console.log("Service connection request rejected.");

	        }
	    },

	    /* Connection between Provider and Consumer is established */
	    onconnect: function (socket) {
	        var onConnectionLost,
	            dataOnReceive;

	        console.log("Service connection established");

	        /* Obtaining socket */
	        SASocket = socket;

	        onConnectionLost = function onConnectionLost (reason) {
	            console.log("Service Connection disconnected due to following reason:<br />" + reason);
	        };

	        /* Inform when connection would get lost */
	        SASocket.setSocketStatusListener(onConnectionLost);

	        dataOnReceive =  function dataOnReceive (channelId, data) {
	        	receiveData(channelId, data);
	        };

	        /* Set listener for incoming data from Consumer */
	        SASocket.setDataReceiveListener(dataOnReceive);
	    	window.webapis.motion.start("HRM", onchangedCB);
	    },
	    onerror: function (errorCode) {
	        console.log("Service connection error<br />errorCode: " + errorCode);
	    }
	};

function requestOnSuccess (agents) {
    var i = 0;

    for (i; i < agents.length; i += 1) {
        if (agents[i].role === "PROVIDER") {
            console.log("Service Provider found!<br />" +
                        "Name: " +  agents[i].name);
            SAAgent = agents[i];
            break;
        }
    }

    /* Set listener for upcoming connection from Consumer */
    SAAgent.setServiceConnectionListener(connectionListener);
};

function requestOnError (e) {
    console.log("requestSAAgent Error" +
                "Error name : " + e.name + "<br />" +
                "Error message : " + e.message);
};

/* Requests the SAAgent specified in the Accessory Service Profile */
webapis.sa.requestSAAgent(requestOnSuccess, requestOnError);


(function () {
    /* Basic Gear gesture & buttons handler */
    window.addEventListener('tizenhwkey', function(ev) {
        var page,
            pageid;

        if (ev.keyName === "back") {
            page = document.getElementsByClassName('ui-page-active')[0];
            pageid = page ? page.id : "";
            if (pageid === "main") {
                try {
                	tizen.application.getCurrentApplication().exit();
                } catch (ignore) {
                }
            } else {
                window.history.back();
            }
        }
    });
}());
