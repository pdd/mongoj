$(function() {
	var pUploader = new qq.FileUploader({
	    element: document.getElementById('photos-uploader'),
	    action: usedcar.resourceURL,
	    onSubmit : function() {
	    	pUploader.setParams({
	    		vin : $("#VIN").val(),
	    		action : 'upload-photo'
	    	});
	    }
	});
	
	var tUploader = new qq.FileUploader({
	    element: document.getElementById('thumbnail-uploader'),
	    action: usedcar.resourceURL,
	    onSubmit : function() {
	    	tUploader.setParams({
	    		vin : $("#VIN").val(),
	    		action : 'upload-thumbnail'
	    	});
	    }
	});

	$("#year").datepicker({dateFormat: "yy", changeYear: true});
	
	var dateOptions = { changeYear: true };
	
	$("#regDate1").datepicker(dateOptions);
	$("#serviceDate11").datepicker(dateOptions);
	$("#serviceDate12").datepicker(dateOptions);
	$("#regDate2").datepicker(dateOptions);
	$("#serviceDate21").datepicker(dateOptions);
	$("#serviceDate22").datepicker(dateOptions);
	
	$("#tabs").tabs({
	   show: function(event, ui) {
			if (ui.index == 1) {
				refreshProfile();
			}
			else if (ui.index == 2) {
				$(':input','#adminForm')
				 .not(':button, :submit, :reset, :hidden')
				 .val('')
				 .removeAttr('checked')
				 .removeAttr('selected');
				
				$("#carAttrs").hide();
				
				$('#VIN').focus();
			}
			else {
				refreshStore();
			}
	   }
	});
	
	$('#VIN').keypress(function(e) {
		clearTimeout(usedcar.timer);
		usedcar.timer = setTimeout("getCarAttrs()", 400);
	});
});

function refreshProfile() {
	$.post(usedcar.resourceURL, {
		action : 'getProfile'
	}, function(response) {	
		if(response.status == 'success'){
			var user = response.data.user;
			
			$('#firstName').val(user.firstName);
			$('#lastName').val(user.lastName);
			
			displayLikedCars(response.data.likedCarImages);
		}
	}, "json");	
}

function displayLikedCars(likedCarImages) {
	var likedCarRows = '';
	
	for (key in likedCarImages) {
		var imageBinData = likedCarImages[key];
		
		imageBinData = imageBinData == null ? '' : 
			String.fromCharCode.apply(String, imageBinData);
		
		var row = $('#likesRowTemplate').html();
		
		row = row.replace("%vin%", key)
			.replace("%imageBinData%", imageBinData);
		
		likedCarRows += row + '<hr />';
	}
	
	$('#likesContent').html(likedCarRows);
}

function refreshStore() {
	$.post(usedcar.resourceURL, {
		action 		: 		'store'
	}, function(response) {	
		if (response.status == 'success') {
			renderRows(response.data.cars);
		}
	}, "json");	
}

usedcar.dummyImage = 'iVBORw0KGgoAAAANSUhEUgAAAAUAAAAFCAYAAACNbyblAAAAHElEQVQI12P4//8/w38GIAXDIBKE0DHxgljNBAAO9TXL0Y4OHwAAAABJRU5ErkJggg==';

function renderRows(cars) {
	var rows = '';
	
	for(var i = 0; i < cars.length; i++) {
		var row = $('#rowTemplate').html();
		
		var binImageData = cars[i].thumbnail;
		binImageData = binImageData == null ? usedcar.dummyImage : 
			String.fromCharCode.apply(String, binImageData);

		for(var j = 0; j < 6; j++) {
			cars[i].photos[j] = cars[i].photos[j] == undefined ?  binImageData :
				String.fromCharCode.apply(String, cars[i].photos[j]);
		}
		
		row = row.replace(/%vin%/g, cars[i].VIN)
			.replace("%make%", cars[i].make)
			.replace("%model%", cars[i].model)
			.replace("%style%", cars[i].style)
			.replace("%year%", cars[i].year)
			.replace("%mileage%", cars[i].mileage)
			.replace("%color%", cars[i].color)
			.replace("%price%", cars[i].price)
			.replace("%automatic%", cars[i].automatic)
			.replace("%imageBinData%", binImageData)
			.replace("%p0%", cars[i].photos[0])
			.replace("%p1%", cars[i].photos[1])
			.replace("%p2%", cars[i].photos[2])
			.replace("%p3%", cars[i].photos[3])
			.replace("%p4%", cars[i].photos[4])
			.replace("%p5%", cars[i].photos[5]);

		
		rows += row + '<hr />';
	}
	
	$('#rows').html(rows);
}

function getCarAttrs() {
	var vin = $("#VIN").val();
	if (vin == '') {
		$("#carAttrs").hide();
		
		return;
	}

	$.post(usedcar.resourceURL, {
		vin : vin,
		action : 'getCar'
	}, function(response) {	
		if(response.status == 'success'){
			var car = response.data.car;

			$('#make').val(car.make);
			$('#model').val(car.model);
			$('#style').val(car.style);
			$('#year').val(car.year);
			$('#mileage').val(car.mileage);
			$('#color').val(car.color);
			
			if (car.automatic == true) {
				$('#rd1').attr('checked', true);
			}
			else {
				$('#rd2').attr('checked', true);
			}

			$('#price').val(car.price);
			
			if (car.history != null) {
				$('#regDate1').val(formatDate(car.history[0].regDate));
				$('#regMileage1').val(car.history[0].mileage);
				$('#regState1').val(car.history[0].state);
				$('#serviceDate11').val(formatDate(car.history[0].serviceRecord[0].serviceDate));
				$('#serviceDate12').val(formatDate(car.history[0].serviceRecord[1].serviceDate));
				$('#notes11').val(car.history[0].serviceRecord[0].notes);
				$('#notes12').val(car.history[0].serviceRecord[1].notes);
				
				$('#regDate2').val(formatDate(car.history[1].regDate));
				$('#regMileage2').val(car.history[1].mileage);
				$('#regState2').val(car.history[1].state);
				$('#serviceDate21').val(formatDate(car.history[1].serviceRecord[0].serviceDate));
				$('#serviceDate22').val(formatDate(car.history[1].serviceRecord[1].serviceDate));
				$('#notes21').val(car.history[1].serviceRecord[0].notes);
				$('#notes22').val(car.history[1].serviceRecord[1].notes);
			}
		}
	}, "json");
	
	$("#carAttrs").show("fast");
}

function formatDate(d) {
	var date = new Date(d);
	
	return date.getMonth() + '/' + date.getDay() + '/' + date.getFullYear();
}

function deleteCar() {
	$.post(usedcar.resourceURL, {
		vin			:		$('#VIN').val(),
		action 		: 		'deleteCar'
	}, function(response) {	
		if (response.status == 'success') {
			$(':input','#adminForm')
			 .not(':button, :submit, :reset, :hidden')
			 .val('')
			 .removeAttr('checked')
			 .removeAttr('selected');
			
			$("#carAttrs").hide();
			
			$('#VIN').focus();
		}
	}, "json");
}

function saveCar() {
	var history	=	
		[
			{
				regDate	:	$('#regDate1').val(),
				mileage	:	parseInt($('#regMileage1').val()),
				state	:	$('#regState1').val(),
				serviceRecord	: [
					{serviceDate: $('#serviceDate11').val(), notes : $('#notes11').val()},
					{serviceDate: $('#serviceDate12').val(), notes : $('#notes12').val()}
				]
			},
			{
				regDate	:	$('#regDate2').val(),
				mileage	:	parseInt($('#regMileage2').val()),
				state	:	$('#regState2').val(),
				serviceRecord	: [
					{serviceDate: $('#serviceDate21').val(), notes : $('#notes21').val()},
					{serviceDate: $('#serviceDate22').val(), notes : $('#notes22').val()}
				]
			}
		];

	$.post(usedcar.resourceURL, {
		vin			:		$('#VIN').val(),
		make		:		$('#make').val(),
		model		:		$('#model').val(),
		style		:		$('#style').val(),
		year		:		$('#year').val(),
		mileage		:		$('#mileage').val(),
		color		:		$('#color').val(),
		automatic	:		$("input[name='automatic']:checked").val(),
		price		:		$('#price').val(),
		history		:		JSON.stringify(history),
		action 		: 		'saveCar'
	}, function(response) {	
		if (response.status == 'success') {
			//TODO
			
		}
	}, "json");	
	
}

function likeCar(vin) {
	$.post(usedcar.resourceURL, {
		vin : vin,
		action : 'likeCar'
	}, function(response) {	
		if (response.status == 'success') {
			//TODO
			
		}
	}, "json");
}

function unlikeCar(vin) {
	$.post(usedcar.resourceURL, {
		vin : vin,
		action : 'unlikeCar'
	}, function(response) {	
		if (response.status == 'success') {
			refreshProfile();
		}
	}, "json");
}

function saveUser() {
	$.post(usedcar.resourceURL, {
		firstName : $('#firstName').val(),
		lastName : $('#lastName').val(), 
		action : 'saveUser'
	}, function(response) {	
		if (response.status == 'success') {
			//TODO
			
		}
	}, "json");	
}