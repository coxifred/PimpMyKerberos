    // loging
    
    include("javascript/jquery.js");
    include("javascript/jquery-ui.js");
    include("javascript/login.js");
	include("javascript/semantic.js");        
	include("javascript/calendar.min.js");        
	include("javascript/jquery-websocket.js"); 
	function s4() {
		    return Math.floor((1 + Math.random()) * 0x10000)
		      .toString(16)
		      .substring(1);
		  }

	function isInView(elem){
   		return elem.offset().top - $(window).scrollTop() < elem.height() ;
	}

	function guid() {
		  
		  return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
		    s4() + '-' + s4() + s4() + s4();
	}
    
	function msToTime(duration) {
	  	var milliseconds = parseInt((duration % 1000) / 100),
	    seconds = Math.floor((duration / 1000) % 60),
	    minutes = Math.floor((duration / (1000 * 60)) % 60),
	    hours = Math.floor((duration / (1000 * 60 * 60)) % 24);
	
		  hours = (hours < 10) ? "0" + hours : hours;
		  minutes = (minutes < 10) ? "0" + minutes : minutes;
		  seconds = (seconds < 10) ? "0" + seconds : seconds;
	
	  	return hours + ":" + minutes + ":" + seconds + "." + milliseconds;
	}

    
	function url(s) {
        var l = window.location;
        return ((l.protocol === "https:") ? "wss://" : "ws://") + l.hostname + (((l.port != 80) && (l.port != 443)) ? ":" + l.port : "") + "/" + s;
    }
    
    function async(your_function, callback) {
        setTimeout(function() {
            your_function();
            if (callback) {callback();};;
        }, 0);
    }
    
   
  
    function getDate()
    {
    	x = new Date();
		return (twoDigit(x.getHours())+ ":" + twoDigit(x.getMinutes())+ ":" + twoDigit(x.getSeconds()));
    }
    
    function twoDigit(number) {
    	  var twodigit = number >= 10 ? number : "0"+number.toString();
    	  return twodigit;
    	}
    
   
//	function log(msg,from,type)
//	{
//		console.log(msg);
//	}
    
    function getValueFromActionGet(action,type) {
    	//var host="http://yvasp470:9999/X4450";
    	
		var retour;
		var request = $
				.ajax({
					url :  action,
					type : "GET",
					data : {},
					crossDomain: true,
					async : type,
					dataType : "html",
					error:function(xhr, status, errorThrown) {
					//alert("ERROR ! " + errorThrown+'\n'+status+'\n'+xhr.statusText);
					}
				});
//		log("Transmission requête Ajax : " + 
//				 action);
		request.done(function(msg) {
//			log("Retour requête Ajax : " + msg);
			retour = trim(msg);
		});
		return retour;
	}
    
	/**
	 * Function Ajax
	 * @param theUrl
	 * @param asyncMode
	 * @returns
	 */
	function getValueFromUrl(theUrl,asyncMode,myfunction) {
		var retour; 
		try 
		{
		var request = $
				.ajax({
					url : theUrl,
					type : "POST",
					data : {},
					async : asyncMode,
					dataType : "html",
						error:function(xhr, status, errorThrown) {
						if ( typeof myfunction != "undefined" )
						{
						myfunction("ERROR");
						}else
							{
						retour = trim("ERROR");
							}
//						alert("ERROR ! " + errorThrown+'\n'+status+'\n'+xhr.statusText);
							retour="ERROR";
				        } 
				});
				
		request
				.done(function(msg) {
					if ( typeof myfunction != "undefined" )
						{
						myfunction(msg);
						}else
							{
					retour = trim(msg);
							}
					
				});
				return retour;
				
	}	catch (err)
		{
			log("ERROR getValueFromUrl");
			if ( typeof myfunction != "undefined" )
						{
						myfunction("ERROR");
						}else
						{
						retour = "ERROR";
						return retour;
			}
		}
	}
	
	function setUrl(url)
	{
		document.location=url;
	}
	
     // Quelques fonctions 
	function getValueFromAction(action,type,data) {

    	
		var retour;
		var request = $
				.ajax({
					url :  action,
					type : "POST",
					data : data,
					crossDomain: true,
					async : type,
					dataType : "html",
					error:function(xhr, status, errorThrown) {
					//alert("ERROR ! " + errorThrown+'\n'+status+'\n'+xhr.statusText);
					}
				});
//		log("Transmission requête Ajax : " + 
//				 action);
		request.done(function(msg) {
//			log("Retour requête Ajax : " + msg);
			retour = trim(msg);
		});
		return retour;
	}
	
	 // Quelques fonctions 
	function getValueFromActionPost(action,type,session,actionName) {
		var retour;
		var request = $
				.ajax({
					url :  action,
					type : "POST",
					method : "POST",
					data : { payload: session, action: actionName },
					crossDomain: true,
					async : type,
					dataType : "html",
					error:function(xhr, status, errorThrown) {
						log("ERROR ! " + errorThrown+'\n'+status+'\n'+xhr.statusText);
					}
				});
//		log("Transmission requête Ajax : " + 
//				 action);
		request.done(function(msg) {
//			log("Retour requête Ajax : " + msg);
			retour = trim(msg);
		});
		return retour;

	}
	
	function include(fileName){
		  document.write("<script type='text/javascript' src='"+fileName+"'></script>" );
		}
	
	
	function openFullscreen(elem) {
		  if (elem.requestFullscreen) {
		    elem.requestFullscreen();
		  } else if (elem.mozRequestFullScreen) { /* Firefox */
		    elem.mozRequestFullScreen();
		  } else if (elem.webkitRequestFullscreen) { /* Chrome, Safari and Opera */
		    elem.webkitRequestFullscreen();
		  } else if (elem.msRequestFullscreen) { /* IE/Edge */
		    elem.msRequestFullscreen();
		  }
		}
	
	
	function applyTheme(themeName)
	{
	 console.log("Apply " + themeName + " theme");	
	 $("#theme").attr('href','css/themes/' + themeName + '.css');
	 getValueFromAction("AdminAction?function=setTheme&command="+themeName,false);
	 
	 
	 var menuElement = document.createElement('script');
	 menuElement.src = "javascript/themes/" + themeName + "Menu.js";
	 document.body.appendChild(menuElement);
	 
	 var scriptElement = document.createElement('script');
     scriptElement.src = "javascript/themes/" + themeName + ".js";
     document.body.appendChild(scriptElement);
	
	}
	
	function selectCss() {
        var link = $("link[rel=stylesheet]")[0].href;
        var css = link.substring(link.lastIndexOf('/') + 1, link.length)
        $('link[href="' + css + '"]').attr('href', $('#changeCss').val() + '.css');
    }
	
	function log(message)
    {
		var date = new Date();
    	console.log(date + " " + message);
    	//console.log("LogMessage " + $("#LogMessage"));
    	//alert(date + " " + message +  "<br>" + $("#LogMessage").html());
    	
    }
	function replaceAll(str,replace,with_this)
	{
	    var str_hasil ="";
	    var temp;

	    for(var i=0;i<str.length;i++) // not need to be equal. it causes the last change: undefined..
	    {
	        if (str[i] == replace)
	        {
	            temp = with_this;
	        }
	        else
	        {
	                temp = str[i];
	        }

	        str_hasil += temp;
	    }

	    return str_hasil;
	}
	function trim (myString)
    {
    return myString.replace(/^\s+/g,'').replace(/\s+$/g,'');
    } 
	
	
	function popupAddSimpleText(text,height,width)
    {
		$.modal(text);
    }
	
	function popupAdd(src,height,width)
    {
		popup=$.modal('<iframe src="' + src + '" height="' + (height-17) + '" width="' + (width -17) + '" style="position:relative;border:0">', {
			closeHTML:"",
			
			containerCss:{
				border:0,
				height:height, 
				padding:0, 
				width:width,
			},
			overlayClose:true	
		});
		$("div.simplemodal-container").css("top","10px");
		
    }
	
	function ssPopupAdd(src,height,width)
    {
		ssPopup=$.modal('<iframe src="' + src + '" height="' + (height-17) + '" width="' + (width -17) + '" style="position:fixed;border:0">', {
			closeHTML:"",
			
			containerCss:{
				border:0,
				height:height, 
				padding:0, 
				width:width
			},
			overlayClose:true
		});

    }
	
	function getUrlVars(cle)
	{
	    var vars = [], hash;
	    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
	    for(var i = 0; i < hashes.length; i++)
	    {
	        hash = hashes[i].split('=');
	        vars.push(hash[0]);
	        vars[hash[0]] = hash[1];
	    }
	    var valeur = vars[cle];
	    return valeur;
	}
	
	function deletePopup() {
	     //window.parent.document.getElementById('popup').style.display = 'none';
		 //alert("Suppression popup");
	     popup.close();
	     //$.modal.close();
	}
	
	function deleteSsPopup() {
	     //window.parent.document.getElementById('popup').style.display = 'none';
		 //alert("Suppression ssPopup");
	     ssPopup.close();
	     //$.modal.close();
	}
	
	
	function twoDigit(number) {
		  var twodigit = number >= 10 ? number : "0"+number.toString();
		  return twodigit;
		}
	
	