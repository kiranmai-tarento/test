CREATE TABLE egasset_asset
(
  id bigint NOT NULL,
  name character varying(250) NOT NULL,
  code character varying(250) NOT NULL,
  oldcode character varying(250),
  departmentcode character varying(250) NOT NULL,
  assetcategory bigint NOT NULL,
  modeofacquisition character varying(250) NOT NULL,
  status character varying(250),
  grossvalue numeric,
  accumulateddepreciation numeric,
  description character varying(1024),
  dateofcreation bigint,
  remarks character varying(1024),
  version character varying(250),
  assetreference bigint,
  enableyearwisedepreciation boolean,
  assetattributes character varying(4000),
  depreciationrate numeric,
  anticipatedlife bigint,
  ordernumber character varying(250),
  orderdate bigint,
  wipreferenceno character varying(250),
  acquiredfrom character varying(250),
  warrantyavailable boolean,
  warrantyexpirydate bigint,
  defectliabilityyear bigint,
  defectliabilitymonth bigint,
  defectliabilityday bigint,
  securitydepositretained numeric,
  securitydepositrealized numeric,
  acquisitiondate bigint,
  originalvalue numeric,
  assetaccount character varying(250),
  accumulateddepreciationaccount character varying(250),
  revaluationreserveaccount character varying(250),
  depreciationexpenseaccount character varying(250),
  titledocumentsavalable character varying(1024),
  usage character varying(250),
  length numeric,
  width numeric,
  height numeric,
  totalarea numeric,
  plintharea numeric,
  address character varying(1024),
  longitude numeric,
  latitude numeric,
  floors bigint,
  landsurveyno character varying(250),
  cubiccontents character varying(250),
  quantity bigint,
  tenantid character varying(250) NOT NULL,
  zone bigint,
  revenueward bigint,
  street bigint,
  electionward bigint,
  doorno character varying(64),
  pincode bigint,
  locality bigint,
  block bigint,
  CONSTRAINT pk_egasset_asset PRIMARY KEY (id, tenantid),
  CONSTRAINT uk_egasset_asset_code UNIQUE (code, tenantid)
);

CREATE TABLE egasset_disposal
(
  id bigint NOT NULL,
  tenantid character varying(250) NOT NULL,
  assetid bigint NOT NULL,
  ordernumber character varying(250),
  orderdate bigint,
  buyername character varying(64) NOT NULL,
  buyeraddress character varying(64) NOT NULL,
  disposalreason character varying(500) NOT NULL,
  disposaldate bigint NOT NULL,
  pancardnumber character varying(32) NOT NULL,
  aadharcardnumber character varying(64) NOT NULL,
  assetcurrentvalue numeric(12,2),
  salevalue numeric(12,2),
  transactiontype character varying(64) NOT NULL,
  assetsaleaccount character varying(250),
  createdby character varying(64) NOT NULL,
  createddate bigint NOT NULL,
  lastmodifiedby character varying(64),
  lastmodifieddate bigint,
  profitlossvoucherreference character varying(250),
  remarks character varying(250),
  CONSTRAINT pk_egasset_disposal PRIMARY KEY (id, tenantid)
);

CREATE TABLE egasset_revaluation
(
  id bigint NOT NULL,
  tenantid character varying(250) NOT NULL,
  assetid bigint NOT NULL,
  currentcapitalizedvalue numeric(12,2),
  typeofchange character varying(64) NOT NULL,
  revaluationamount numeric(12,2) NOT NULL,
  valueafterrevaluation numeric(12,2),
  revaluationdate bigint NOT NULL,
  revaluatedby character varying(64) NOT NULL,
  reasonforrevaluation character varying(1024),
  fixedassetswrittenoffaccount character varying(250),
  function character varying(250),
  fund character varying(250),
  scheme character varying(250),
  subscheme character varying(250),
  comments character varying(250),
  status character varying(250),
  remarks character varying(1000),
  ordernumber character varying(250),
  orderdate bigint,
  createdby character varying(64) NOT NULL,
  createddate bigint NOT NULL,
  lastmodifiedby character varying(64),
  lastmodifieddate bigint,
  voucherreference character varying(250),
  CONSTRAINT pk_egasset_revalution PRIMARY KEY (id, tenantid)
);



CREATE SEQUENCE seq_egasset_asset;
CREATE SEQUENCE seq_egasset_assetcode;
CREATE SEQUENCE seq_egasset_revaluation;
CREATE SEQUENCE seq_egasset_disposal;
