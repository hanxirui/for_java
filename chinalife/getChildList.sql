DELIMITER $$ 
DROP FUNCTION IF EXISTS getChildList$$
CREATE FUNCTION `getChildList` (parentcode varchar(30))
RETURNS VARCHAR(2000)
BEGIN
DECLARE sTemp VARCHAR(2000);
DECLARE sTempChd VARCHAR(2000);

SET sTemp = '$';
SET sTempChd = parentcode;

WHILE sTempChd is not NULL DO
SET sTemp = CONCAT(sTemp,',',sTempChd);
SELECT group_concat(code) INTO sTempChd FROM employee where FIND_IN_SET(managercode,sTempChd)>0;
END WHILE;
return sTemp;
END$$