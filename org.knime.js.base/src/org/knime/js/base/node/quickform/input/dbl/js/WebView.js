org_knime_js_base_node_quickform_input_dbl = function() {
	var doubleInput = {
			version: "1.0.0"
	};
	doubleInput.name = "Double input";
	var viewValue;
	var viewRepresentation;
	var input;
	var errorMessage;

	doubleInput.init = function(representation, value) {
		viewValue = value;
		viewRepresentation = representation;
		var body = $('body');
		input = $('<input>');
		body.append(input);
		input.spinner({
			step: 0.01
		});
		if (viewRepresentation.usemin) {
			input.spinner('option', 'min', viewRepresentation.min);
		}
		if (viewRepresentation.usemax) {
			input.spinner('option', 'max', viewRepresentation.max);
		}
		input.val(viewValue.double);
		body.append($('<br>'));
		errorMessage = $('<span>');
		errorMessage.css('display', 'none');
		errorMessage.css('color', 'red');
		errorMessage.css('font-style', 'italic');
		errorMessage.css('font-size', '75%');
		body.append(errorMessage);
	};
	
	doubleInput.validate = function() {
		var valid;
		var value = input.val();
		var min = viewRepresentation.min;
		var max = viewRepresentation.max;
		if (viewRepresentation.usemin && value<min) {
			valid = false;
			errorMessage.text("The set double " + value + " is smaller than the required minimum " + min);
			errorMessage.css('display', 'inline');
		} else if (viewRepresentation.usemax && value>max) {
			valid = false;
			errorMessage.text("The set double " + value + " is bigger than the required maximum " + max);
			errorMessage.css('display', 'inline');
		} else {
			valid = true;
			errorMessage.css('display', 'none');
		}
		return valid;
	};

	doubleInput.value = function() {
		viewValue.double = input.val();
		return viewValue;
	};
	
	return doubleInput;
	
}();
