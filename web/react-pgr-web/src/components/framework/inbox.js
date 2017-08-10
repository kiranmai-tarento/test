import React, {Component} from 'react';
import {connect} from 'react-redux';
import RaisedButton from 'material-ui/RaisedButton';

import _ from "lodash";
import ShowFields from "./showFields";

import {translate} from '../common/common';
import Api from '../../api/api';
import jp from "jsonpath";
import UiButton from './components/UiButton';
import {fileUpload} from './utility/utility';
import UiTable from './components/UiTable';
import Workflow from './specs/pt/Workflow';

var specifications={};

let reqRequired = [];

class Inbox extends Component {
  constructor(props) {
    super(props);
	this.state = {
		searchResult : []
	}
  }

  setInitialUpdateChildData(form, children) {
    let _form = JSON.parse(JSON.stringify(form));
    for(var i=0; i<children.length; i++) {
      for(var j=0; j<children[i].groups.length; j++) {
        if(children[i].groups[j].multiple) {
          var arr = _.get(_form, children[i].groups[j].jsonPath);
          var ind = j;
          var _stringifiedGroup = JSON.stringify(children[i].groups[j]);
          var regex = new RegExp(children[i].groups[j].jsonPath.replace("[", "\[").replace("]", "\]") + "\\[\\d{1}\\]", 'g');
          for(var k=1; k < arr.length; k++) {
            j++;
            children[i].groups[j].groups.splice(ind+1, 0, JSON.parse(_stringifiedGroup.replace(regex, children[i].groups[ind].jsonPath + "[" + k + "]")));
            children[i].groups[j].groups[ind+1].index = ind+1;
          }
        }

        if(children[i].groups[j].children && children[i].groups[j].children.length) {
          this.setInitialUpdateChildData(form, children[i].groups[j].children);
        }
      }
    }
  }

  hideField(specs, moduleName, actionName, hideObject) {
    if(hideObject.isField) {
      for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
        for(let j=0; j<specs[moduleName + "." + actionName].groups[i].fields.length; j++) {
          if(hideObject.name == specs[moduleName + "." + actionName].groups[i].fields[j].name) {
            specs[moduleName + "." + actionName].groups[i].fields[j].hide = true;
            break;
          }
        }
      }
    } else {
      let flag = 0;
      for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
        if(hideObject.name == specs[moduleName + "." + actionName].groups[i].name) {
          flag = 1;
          specs[moduleName + "." + actionName].groups[i].hide = true;
          break;
        }
      }

      if(flag == 0) {
        for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
          if(specs[moduleName + "." + actionName].groups[i].children && specs[moduleName + "." + actionName].groups[i].children.length) {
            for(let j=0; j<specs[moduleName + "." + actionName].groups[i].children.length; j++) {
              for(let k=0; k<specs[moduleName + "." + actionName].groups[i].children[j].groups.length; k++) {
                if(hideObject.name == specs[moduleName + "." + actionName].groups[i].children[j].groups[k].name) {
                  specs[moduleName + "." + actionName].groups[i].children[j].groups[k].hide = true;
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  showField(specs, moduleName, actionName, showObject) {
    if(showObject.isField) {
      for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
        for(let j=0; j<specs[moduleName + "." + actionName].groups[i].fields.length; j++) {
          if(showObject.name == specs[moduleName + "." + actionName].groups[i].fields[j].name) {
            specs[moduleName + "." + actionName].groups[i].fields[j].hide = false;
            break;
          }
        }
      }
    } else {
      let flag = 0;
      for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
        if(showObject.name == specs[moduleName + "." + actionName].groups[i].name) {
          flag = 1;
          specs[moduleName + "." + actionName].groups[i].hide = false;
          break;
        }
      }

      if(flag == 0) {
        for(let i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
          if(specs[moduleName + "." + actionName].groups[i].children && specs[moduleName + "." + actionName].groups[i].children.length) {
            for(let j=0; j<specs[moduleName + "." + actionName].groups[i].children.length; j++) {
              for(let k=0; k<specs[moduleName + "." + actionName].groups[i].children[j].groups.length; k++) {
                if(showObject.name == specs[moduleName + "." + actionName].groups[i].children[j].groups[k].name) {
                  specs[moduleName + "." + actionName].groups[i].children[j].groups[k].hide = false;
                  break;
                }
              }
            }
          }
        }
      }
    }
  }

  setInitialUpdateData(form, specs, moduleName, actionName, objectName) {
    let {setMockData} = this.props;
    let _form = JSON.parse(JSON.stringify(form));
    var ind;
    for(var i=0; i<specs[moduleName + "." + actionName].groups.length; i++) {
      if(specs[moduleName + "." + actionName].groups[i].multiple) {
        var arr = _.get(_form, specs[moduleName + "." + actionName].groups[i].jsonPath);
        ind = i;
        var _stringifiedGroup = JSON.stringify(specs[moduleName + "." + actionName].groups[i]);
        var regex = new RegExp(specs[moduleName + "." + actionName].groups[i].jsonPath.replace(/\[/g, "\\[").replace(/\]/g, "\\]") + "\\[\\d{1}\\]", 'g');
        for(var j=1; j < arr.length; j++) {
          i++;
          specs[moduleName + "." + actionName].groups.splice(ind+1, 0, JSON.parse(_stringifiedGroup.replace(regex, specs[moduleName + "." + actionName].groups[ind].jsonPath + "[" + j + "]")));
          specs[moduleName + "." + actionName].groups[ind+1].index = ind+1;
        }
      }

      for(var j=0; j<specs[moduleName + "." + actionName].groups[i].fields.length; j++) {
        if(specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields && specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields.length) {
          for(var k=0; k<specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields.length; k++) {
            if(specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].ifValue == _.get(form, specs[moduleName + "." + actionName].groups[i].fields[j].jsonPath)) {
              if(specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide && specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide.length) {
                for(var a=0; a<specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide.length; a++) {
                  this.hideField(specs, moduleName, actionName, specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].hide[a]);
                }
              }

              if(specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show && specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show.length) {
                for(var a=0; a<specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show.length; a++) {
                  this.showField(specs, moduleName, actionName, specs[moduleName + "." + actionName].groups[i].fields[j].showHideFields[k].show[a]);
                }
              }
            }
          }
        }
      }

      if(specs[moduleName + "." + actionName].groups[ind || i].children && specs[moduleName + "." + actionName].groups[ind || i].children.length) {
        this.setInitialUpdateChildData(form, specs[moduleName + "." + actionName].groups[ind || i].children);
      }
    }

    setMockData(specs);
  }

  initData() {
	  
	  var current = this;
	  
    try {
      var hash = window.location.hash.split("/");
      if(hash.length == 4) {
        specifications = require(`./specs/${hash[2]}/${hash[2]}`).default;
      } else {
        specifications = require(`./specs/${hash[2]}/master/${hash[3]}`).default;
      }
    } catch(e) {
      
    }

    let { setMetaData, setModuleName, setActionName, setMockData, workflow} = this.props;
    let hashLocation = window.location.hash;
    let self = this;
    let obj = specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`];
    setMetaData(specifications);
    setMockData(JSON.parse(JSON.stringify(specifications)));
    setModuleName(hashLocation.split("/")[2]);
    setActionName(hashLocation.split("/")[1]);
    //Get view form data
    var url = specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].url.split("?")[0];
    var hash = window.location.hash.split("/");
    var value = (hash.length == 4) ? hash[3] : hash[4];
    var query = {
      [specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].url.split("?")[1].split("=")[0]]: value
    };

    Api.commonApiPost(url, query, {}, false, specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].useTimestamp).then(function(res){
		current.setState({
			searchResult: res.properties
		})
		var workflowDetails = res.properties[0].propertyDetail.workFlowDetails;
		if(workflowDetails) {
			workflow.workflowDepartment = workflowDetails.department || null;
			workflow.workflowDesignation = workflowDetails.designation || null;
			workflow.approver =workflowDetails.assignee || null;
		}
		
		console.log(workflowDetails);
      self.props.setFormData(res);
      self.setInitialUpdateData(res, JSON.parse(JSON.stringify(specifications)), hashLocation.split("/")[2], hashLocation.split("/")[1], specifications[`${hashLocation.split("/")[2]}.${hashLocation.split("/")[1]}`].objectName);
    }, function(err){

    })
  }

  componentDidMount() {
      this.initData();
	  this.props.initForm();
  }
  
  updateInbox = () => {
	  
	  var currentThis = this;
	  
	  let {workflow, setLoadingStatus, toggleSnackbarAndSetText} = this.props;
	 
	  var data = this.state.searchResult;
	 
	  var workFlowDetails = {
		"department": workflow.workflowDepartment || null,
		"designation":workflow.workflowDesignation || null,
		"assignee": workflow.approver || null,
		"action": "Forward",
		"status": "Assitant approved"
	  }
	  
		data[0].owners[0].tenantId = "default";
		data[0].vltUpicNumber = null;
		data[0].gisRefNo = null;
		data[0].oldUpicNumber = null;

				  
	  data[0].propertyDetail.workFlowDetails = workFlowDetails;
	  
	   setLoadingStatus('loading');
	   
	   var body = {
		   "properties": data
	   } 
	  
	     Api.commonApiPost('pt-property/properties/_update', {},body, false, true).then((res)=>{
			currentThis.setState({
				ack: res.properties.applicationNo
			});
			localStorage.setItem('ack', res.properties[0].propertyDetail.applicationNo);
			this.props.history.push('acknowledgement');
			setLoadingStatus('hide');
		  }).catch((err)=> {
			console.log(err)
			setLoadingStatus('hide');
			toggleSnackbarAndSetText(true, err.message);
		  })
  }

  getVal = (path) => {
    var val = _.get(this.props.formData, path);
    return  typeof val != "undefined" && (typeof val == "string" || typeof val == "number" || typeof val == "boolean") ? (val + "") : "";
  }

  printer = () => {
    window.print();
  }

  render() {
    let {mockData, moduleName, actionName, formData, fieldErrors, workflow} = this.props;
    let {handleChange, getVal, addNewCard, removeCard, printer} = this;

    const renderTable = function() {
      if(moduleName && actionName && formData && formData[objectName]) {
        var objectName = mockData[`${moduleName}.${actionName}`].objectName;
        if(formData[objectName].documents && formData[objectName].documents.length) {
          var dataList = {
            resultHeader: ["#", "Name", "File"],
            resultValues: []
          };

          for(var i=0; i<formData[objectName].documents.length; i++) {
            dataList.resultValues.push([i+1, formData[objectName].documents[i].name || "File", "<a href=/filestore/v1/files/id?tenantId=" + localStorage.getItem("tenantId") + "&fileStoreId=" + formData[objectName].documents[i].fileStoreId + ">Download</a>"]);
          }

          return (
            <UiTable resultList={dataList}/>
          );
        }
      }
    }

    return (
      <div className="Inbox">
        <form id="printable">
        {!_.isEmpty(mockData) && <ShowFields groups={mockData[`${moduleName}.${actionName}`].groups} noCols={mockData[`${moduleName}.${actionName}`].numCols} ui="google" handler={""} getVal={getVal} fieldErrors={fieldErrors} useTimestamp={mockData[`${moduleName}.${actionName}`].useTimestamp || false} addNewCard={""} removeCard={""} screen="view"/>}
          <br/>
          {renderTable()}
		  <Workflow/>
		  <br/>
        </form>
        <div style={{"textAlign": "center"}}>
			<RaisedButton type="button" primary={true} label="Update" style={{margin:'0 5px'}} style={{display:'inline-block'}} onClick={()=> {
				this.updateInbox();
			}}/>
        </div>
      </div>
    );
  }
}

const mapStateToProps = state => ({
	workflow:state.form.form,
  metaData:state.framework.metaData,
  mockData: state.framework.mockData,
  moduleName:state.framework.moduleName,
  actionName:state.framework.actionName,
  formData:state.frameworkForm.form,
  fieldErrors: state.frameworkForm.fieldErrors
});

const mapDispatchToProps = dispatch => ({
  initForm: () => {
    dispatch({
      type: "RESET_STATE",
      validationData: {
        required: {
          current: [],
          required: ['approver', 'workflowDesignation', 'workflowDepartment']
        },
        pattern: {
          current: [],
          required: []
        }
      }
    });
  },
  handleChange: (e, property, isRequired, pattern) => {
    dispatch({type: "HANDLE_CHANGE", property, value: e.target.value, isRequired, pattern});
  },
  setFormData: (data) => {
    dispatch({type: "SET_FORM_DATA", data});
  },
  setMetaData: (metaData) => {
    dispatch({type:"SET_META_DATA", metaData})
  },
  setMockData: (mockData) => {
    dispatch({type: "SET_MOCK_DATA", mockData});
  },
  setModuleName: (moduleName) => {
    dispatch({type:"SET_MODULE_NAME", moduleName})
  },
  setActionName: (actionName) => {
    dispatch({type:"SET_ACTION_NAME", actionName})
  },
  setLoadingStatus: (loadingStatus) => {
    dispatch({type: "SET_LOADING_STATUS", loadingStatus});
  },
  toggleSnackbarAndSetText: (snackbarState, toastMsg, isSuccess, isError) => {
    dispatch({type: "TOGGLE_SNACKBAR_AND_SET_TEXT", snackbarState, toastMsg, isSuccess, isError});
  }
});
export default connect(mapStateToProps, mapDispatchToProps)(Inbox);