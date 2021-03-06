var dat = {
	"asset.transaction": {
		"numCols": 12/3,
		"url": "/asset-services-maha/assets/_search",
    "tenantIdRequired": true,
    "objectName": "",
    "useTimestamp": true,
    "groups": [{
        "label": "ac.transaction.TA.title",
        "name": "createCategoryType",
        "fields": [
					{
						"name": "AssetSearchCode",
						"jsonPath": "assetCategoryType",
						"label": "ac.create.asset.asset.category.type",
						"pattern": "",
						"type": "singleValueList",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"defaultValue": [  {
						"key": "IMMOVABLE",
						"value": "IMMOVABLE",
					},
					{
						"key": "MOVABLE",
						"value": "MOVABLE"
					}]
					},
					{
						"name": "AssetCategory",
						"jsonPath": "assetParentCategory",
						"label": "ac.create.Asset.Category",
						"pattern": "",
						"type": "singleValueList",
						"url": "/egov-mdms-service/v1/_get?&moduleName=ASSET&masterName=AssetCategory&filter=%5B%3F(%20%40.isAssetAllow%20%3D%3D%20false%20%26%26%20%40.assetCategoryType%20%3D%3D%20%22MOVABLE%22)%5D|$.MdmsRes.ASSET.AssetCategory.*.id|$.MdmsRes.ASSET.AssetCategory.*.name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"depedants": [{
							"jsonPath": "assetCategory",
							"type": "dropDown",
							"pattern": "/egov-mdms-service/v1/_get?&moduleName=ASSET&masterName=AssetCategory&filter=%5B%3F(%20%40.isAssetAllow%20%3D%3D%20false%20%26%26%20%40.assetCategoryType%20%3D%3D%20%22MOVABLE%22)%5D|$.MdmsRes.ASSET.AssetCategory.*.id|$.MdmsRes.ASSET.AssetCategory.*.name"
						}]
					},
					{
						"name": "AssetSearchAssetSubCategory",
						"jsonPath": "assetCategory",
						"label": "ac.create.Asset.SubCategory.Name",
						"pattern": "",
						"type": "singleValueList",
						"url": "/egov-mdms-service/v1/_get?&moduleName=ASSET&masterName=AssetCategory&filter=%5B%3F(%20%40.isAssetAllow%20%3D%3D%20true%20%26%26%20%40.assetCategoryType%20%3D%3D%20%22IMMOVABLE%22)%5D|$.MdmsRes.ASSET.AssetCategory.*.id|$.MdmsRes.ASSET.AssetCategory.*.name",
						// "url": "/egov-mdms-service/v1/_get?&masterName=AssetCategory&moduleName=ASSET|$.MdmsRes.ASSET.AssetCategory.*.id|$.MdmsRes.ASSET.AssetCategory.*.name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"isStateLevel":true
					},
					{
						"name": "Department",
						"jsonPath": "department",
						"label": "ac.create.Department",
						"pattern": "",
						"type": "singleValueList",
						"url": "/egov-mdms-service/v1/_get?&masterName=Department&moduleName=common-masters|$..code|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"isStateLevel":true
					},
					{
						"name": "AssetSearchCode",
						"jsonPath": "code",
						"label": "ac.search.asset.code",
						"pattern": "",
						"type": "text",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "AssetSearchName",
						"jsonPath": "name",
						"label": "ac.search.asset.name",
						"pattern": "",
						"type": "text",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "fromOriginalValue",
						"jsonPath": "originalValueFrom",
						"label": "ac.create.OriginalFromDate",
						"pattern": "",
						"type": "number",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "toOriginalValue",
						"jsonPath": "originalValueTo",
						"label": "ac.create.OriginalToDate",
						"pattern": "",
						"type": "number",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "fromDate",
						"jsonPath": "assetCreatedFrom",
						"label": "ac.create.createFromDate",
						"pattern": "",
						"type": "datePicker",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "toDate",
						"jsonPath": "assetCreatedTo",
						"label": "ac.create.createToDate",
						"pattern": "",
						"type": "datePicker",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "transactionType",
						"jsonPath": "transaction",
						"label": "transaction",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"defaultValue": "DISPOSAL",
						"isHidden": true
					}
        ]
      }],
      "result": {
        "header": [
					{
						"name": "",
	          "jsonPath": "isRadio",
	          "label": "",
	          "pattern": "",
	          "type": "radio",
	          "isRequired": false,
	          "isDisabled": false,
	          "requiredErrMsg": "",
	          "patternErrMsg": "",
	    			"values": [{"label":"", "value":true}],
	    			"defaultValue":false
			},
					{
            "name": "AssetCode",
            "jsonPath": "code",
            "label": "ac.create.Asset.Code",
            "pattern": "",
            "type": "label",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"isLabel": false,
          },
          {
            "name": "NameAsset",
            "jsonPath": "name",
            "label": "ac.create.Name.of.Asset",
            "pattern": "",
            "type": "label",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"isLabel": false,
          },
          {
            "name": "DateofPurchase/Construction/Acquisition",
            "jsonPath": "acquisitionDate",
            "label": "ac.transaction.create.datePurchaseConstruction",
            "pattern": "",
            "type": "label",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"isDate":true,
						"isLabel": false,
          },
          {
            "name": "WdvValue",
            "jsonPath": "currentValue",
            "label": "ac.create.WDV.Value",
            "pattern": "",
            "type": "label",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"isLabel": false,
          }
        ],
        // "values": ["businessService", "consumerCode", "totalAmount","minimumAmount","bill"],
        "resultPath": "Assets",
        "tableResultPath": "Disposal.Assets",

        // "rowClickUrlUpdate": "/update/wc/pipeSize/{id}",
        // "rowClickUrlView": "/view/wc/pipeSize/{id}"
      },
			"transaction": [{
	      "label": "",
	      "name": "OtherDetails",
	      "children": [],
	      "fields": [
					{
            "name": "TypeofAsset",
            "jsonPath": "Disposal.transactionType",
            "label": "",
            "pattern": "",
            "type": "radio",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            //"values": [{"label":"ac.transaction.TA.TransferAsset", "value":true},{"label":"ac.transaction.TA.DisposalAsset", "value":false}],
						"values": [{"label":"Transfer of Asset", "value":"SALE"},{"label":"Disposal of Asset", "value":"DISPOSAL"}],
            "defaultValue":true
          },
	        {
	          "name": "Transfer/DisposalDate",
	          "jsonPath": "Disposal.disposalDate",
	          //"label": "ac.transaction.TA.TransferDisposalDate",
						"label": "Transfer / Disposal date",
	          "pattern": "",
	          "type": "datePicker",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "",
	          "patternErrMsg": "",
	        },
	        {
	          "name": "OrderNo",
	          "jsonPath": "Disposal.orderNumber",
	          //"label": "ac.transaction.create.order.no",
						"label": "Order No",
	          "pattern": "",
	          "type": "text",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": ""
	        },
	        {
	          "name": "OrderDate",
	          "jsonPath": "Disposal.orderDate",
	          //"label": "ac.transaction.create.order.date",
						"label": "Order Date",
	          "pattern": "",
	          "type": "datePicker",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": "",
	        },
					{
	          "name": "Purpose",
	          "jsonPath": "Disposal.disposalReason",
	          //"label": "ac.transaction.TA.purpose",
						"label": "Purpose",
	          "pattern": "",
	          "type": "text",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": ""
	        },
	        {
	          "name": "Amount",
	          "jsonPath": "Disposal.saleValue",
	          //"label": "ac.transaction.TA.amount",
						"label": "Amount",
	          "pattern": "",
	          "type": "number",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": ""
	        },
					{
	          "name": "AssetTransfer/SoldTo ",
	          "jsonPath": "Disposal.buyerName",
	          //"label": "ac.transaction.TA.assetTransferSold",
						"label": "Asset Transfer / Sold to",
	          "pattern": "",
	          "type": "text",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": ""
	        },
					{
	          "name": "Remarks",
	          "jsonPath": "Disposal.remarks",
	          //"label": "ac.transaction.TA.remarks",
						"label": "Remarks",
	          "pattern": "",
	          "type": "textarea",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": ""
	        },
	      ]
	    }]
	}
}

export default dat;
