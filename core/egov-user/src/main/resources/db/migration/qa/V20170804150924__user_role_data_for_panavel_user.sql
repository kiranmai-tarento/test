insert into eg_userrole (roleid, userid, roleidtenantid, tenantid, lastmodifieddate) values( (select id from eg_role where code='SUPERUSER' and tenantid='panavel'), (select id from eg_user where username='ajay' and tenantid='panavel'), 'panavel','panavel', now());