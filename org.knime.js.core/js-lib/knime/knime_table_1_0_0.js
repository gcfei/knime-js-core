// JavaScript Document
// KNIME Javascript Data Table Version 1.0.0
// Copyright by KNIME.com AG, Zurich Switzerland.
// All rights reserved.

kt = function() {
	var kt = { version: "1.0.0" };
	
	var dataTable = {};
	var extensions = [];
		
	kt.setDataTableFromJSON = function(jsonTable) {
		dataTable = JSON.parse(jsonTable);
	};
	
	kt.setDataTable = function(table) {
		dataTable = table;
	}
	
	kt.setDataTableSpecFromJSON = function(jsonTableSpec) {
		dataTable.spec = JSON.parse(jsonTableSpec);
	}
	
	kt.setDataTableSpec = function(dataTableSpec) {
		dataTable.spec = dataTableSpec;
	}
	
	kt.getData = function() {
		return dataTable.data;
	};
	
	kt.getColumn = function(columnID) {
		if (columnID < dataTable.spec.numColumns) {
			var col = [];
			
			for (var i = 0; i < kt.getNumRows(); i++) {
				col.push(dataTable.data[i][columnID]);
			}
			return col;
		}
	};
	
	kt.getColumnNames = function() {
		return dataTable.spec.colNames;
	};
	
	kt.getColumnTypes = function() {
		return dataTable.spec.colTypes;
	};
	
	kt.getNumRows = function() {
		return dataTable.spec.numRows;
	};
	
	kt.registerView = function(view) {
		for (var i = 0; i < view.extensionNames.length; i++) {
			kt_registerViewExtension(view, view.extensionNames[i]);
		}
	};
	
	kt.getExtension = function(extensionName) {
		var extension = {};
		for (var i = 0; i < extensions.length; i++) {
			if(extensions[i].name == extensionName) {
				extension = extensions[i];
				break;
			};
		};
		return extension;
	};
	
	kt_registerViewExtension = function(view, extensionName) {
		var extensionID;
		for (var i = 0; i < extensions.length; i++) {
			if (extensions[i].name === extensionName) {
				extensionID = i;
				break;
			}
		}
		if (typeof extensionID == 'undefined') {
			extensions.push(kt_createExtension(extensionName));
			extensionID = extensions.length-1;
		}
		extensions[extensionID].registerView(view);
	};
	
	kt_createExtension = function(name) {
		if (name === "hilite") {
			return kt_createHiliteExtension();
		}
	};
	
	kt_createHiliteExtension = function() {
		var defaultValue = false;
		var hiliteTable = [];
		var clearListeners = [];
		var changeListeners = [];
		var changeTable = [];
		var hCol = kt_getExtColumnID ("hilite");
		if (typeof hCol != 'undefined') {
			var pos = hiliteTable.push({name: "native", values: []}) - 1;
			for (var i = 0; i < dataTable.spec.numRows; i++) {
				hiliteTable[pos].values.push(dataTable.extensions[i][hCol]);
			};
		}
		return {
			name: "hilite",
			isHilited: function(rowID) {
				var hilited = false;
				for (var i = 0; i < hiliteTable.length; i++) {
					hilited |= hiliteTable[i].values[rowID];
				}
				return hilited;
			},
			setHilited: function(viewName, rowID, hilited) {
				var previousValue = this.isHilited(rowID);
				for (var i = 0; i < hiliteTable.length; i++) {
					if (hiliteTable[i].name === viewName) {
						hiliteTable[i].values[rowID] = hilited;
						break;
					};
				};
				if (this.isHilited(rowID) !== previousValue) {
					changeTable.push(rowID);
				}
			},
			registerView: function(view) {
				var pos = hiliteTable.push({name: view.name, values: []}) - 1;
				for (var i = 0; i < dataTable.spec.numRows; i++) {
					hiliteTable[pos].values.push(defaultValue);
				};
				changeListeners.push(view.hiliteChangeListener);
				clearListeners.push(view.hiliteClearListener);
			},
			fireHiliteChanged: function() {
				for (var i = 0; i < changeListeners.length; i++) {
					changeListeners[i](changeTable);
				}
				changeTable = [];
				pushData(JSON.stringify(this.exportExtension()));
			},
			fireClearHilite: function() {
				changeTable = [];
				for (var i = 0; i < hiliteTable.length; i++) {
					for (var j = 0; j < hiliteTable[i].values.length; j++) {
						hiliteTable[i].values[j] = false;
					}
				}
				for (var i = 0; i < clearListeners.length; i++) {
					clearListeners[i]();
				}
				pushData(JSON.stringify(this.exportExtension()));
			},
			exportExtension: function() {
				var exportHilite = new Array();
				for (var i = 0; i < dataTable.spec.numRows; i++) {
					exportHilite.push(this.isHilited(i));
				};
				return exportHilite;
			}
		};
	};
	
	kt_getDataColumnID = function(columnName) {
		var colID = null;
		for (var i = 0; i < dataTable.spec.numColumns; i++) {
			if (dataTable.spec.colNames[i] === columnName) {
				colID = i;
				break;
			};
		};
		return colID;
	};
	
	kt_getExtColumnID = function(columnName) {
		var colID = null;
		for (var i = 0; i < dataTable.spec.numExtensions; i++) {
			if (dataTable.spec.extensionNames[i] === columnName) {
				colID = i;
				break;
			};
		};
		return colID;
	};

	return kt;
};
