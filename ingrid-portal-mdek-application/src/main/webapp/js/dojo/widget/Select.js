dojo.provide("ingrid.widget.Select");

dojo.require("dojo.widget.Select");

/**
 * Select
 */
dojo.widget.defineWidget(
	"ingrid.widget.Select",
	dojo.widget.Select,
    function() {
      // The listId used for populating this select box with values from sys_list
      this.listId = "";
    },
{
  templateCssPath: dojo.uri.moduleUri("ingrid", "widget/templates/ComboBox.css"),
  buttonSrc: dojo.uri.moduleUri("ingrid", "widget/templates/images/combo_box_arrow.png"),
  dropdownToggle: "none",

  // NOTE: we use dojo.widget.basicComboBoxDataProvider for simplicity. Drawback is that it loads
  // all data on initialization. So if there is much data in the comboboxes, we should implement 
  // an own provider, that allows to search for a value (like the default one) AND for a key 
  // (not supported by default implementations).
  // another improvement would be to have a method that loads the first value only (should be used
  // in setFirstValue)
  dataProviderClass: "dojo.widget.basicComboBoxDataProvider",

  /*
   * Overidden to disable text field
   */
  postCreate: function() {
    ingrid.widget.Select.superclass.postCreate.apply(this, arguments);
    //this.textInputNode.setAttribute("disabled",true);
  },

  /*
   * Get the display value (=label) of the selection
   */ 
  getDisplayValue: function() {
    return this._getDisplayValueForValue(this.getValue());
  },

  /*
   * Checks if the value is valid and sets it if valid. Sets the first value found otherwise.
   */
  setValue: function(value) {
    if (this.dataProvider) {
//  	  this.dataProvider.startSearch(this._getDisplayValueForValue(value), dojo.lang.hitch(this, "_setValueOrFirst"));
    	var dispVal = this.getDisplayValueForValue(value);
    	if (dispVal != null) {
    		dojo.widget.Select.prototype.setLabel.apply(this, [dispVal]);
	    	dojo.widget.Select.prototype.setValue.apply(this, [value]);
	    } else {
    		dojo.widget.Select.prototype.setLabel.apply(this, [""]);
	    	dojo.widget.Select.prototype.setValue.apply(this, [""]);	    
	    }
    } else {
      dojo.debug("ingrid.widget.Select.setValue: initialization not finished.");
	}
  },

  _setValueOrFirst: function(/*Array*/ results){
    if (results.length) {
      this._forceValue(results[0][0], results[0][1]);
    }
    else {
      this.setFirstValue();
    }
  },

  /*
   * Set the first value found.
   */
  setFirstValue: function(){
    if (this.dataProvider)
    	this.dataProvider.startSearch("", dojo.lang.hitch(this, "_setFirst"));
    else
      dojo.debug("ingrid.widget.Select.setFirstValue: initialization not finished.");
  },

  _setFirst: function(/*Array*/ results){
    if (results.length) {
      this._forceValue(results[0][0], results[0][1]);
    }
  },

  /*
   * Set a value without validity check
   * value1 is the display value (=label) and value2 is the value (=key)
   */
  _forceValue: function(/*String*/ value1, /*String*/ value2){
    dojo.widget.Select.prototype.setLabel.apply(this, [value1]);
    dojo.widget.Select.prototype.setValue.apply(this, [value2]);
  },

  /*
   * Get the display value (=label) for a value (=key)
   */
  _getDisplayValueForValue: function(value){
    var displayValue = '';
    if (this.dataProviderClass == "dojo.widget.basicComboBoxDataProvider" && this.dataProvider) {
      for (var i=0; i<this.dataProvider._data.length; i++) {
        if (this.dataProvider._data[i][1] == value) {
          displayValue = this.dataProvider._data[i][0];
          break;
        }
      }
    }

    return displayValue;
  },


	getDisplayValueForValue: function(value) {
		for (var i=0; i<this.dataProvider._data.length; i++) {
			if (this.dataProvider._data[i][1] == value) {
				return this.dataProvider._data[i][0];
			}
		}
		return null;
	},
	
	getValueForDisplayValue: function(dispValue) {
		for (var i=0; i<this.dataProvider._data.length; i++) {
			if (this.dataProvider._data[i][0] == dispValue) {
				return this.dataProvider._data[i][1];
			}
		}
		return null;
	},

  enable: function() {
    dojo.widget.Select.superclass.enable.apply(this, arguments);
    dojo.html.removeClass(this.textInputNode, 'noEdit');
  },

  disable: function() {
    dojo.widget.Select.superclass.disable.apply(this, arguments);
    dojo.html.addClass(this.textInputNode, 'noEdit');
  }

});
