org_knime_js_base_node_quickform_input_bool = function() {
	var booleanInput = {
			version: "1.0.0"
	};
	booleanInput.name = "Boolean input";
	var viewValue;
	var input;
	var viewValue;

	booleanInput.init = function(representation, value) {
		viewValue = value;
		input = $("body").append("<input></input>").find("input");
		input.attr("type", "checkbox");
		input.prop("checked", viewValue.boolean);
	};

	booleanInput.value = function() {
		viewValue.boolean = input.prop("checked");
		return viewValue;
	};
	
	return booleanInput;
	
}();