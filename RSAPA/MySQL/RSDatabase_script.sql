Use RSDatabase;

drop table if exists RSPageAttributeAffiliation;
drop table if exists RSPageKeywordAffiliation;
drop table if exists RSPage;
drop table if exists RSDirectory;
drop table if exists RSAttribute;
drop table if exists RSKeyword;


create table RSDirectory (
	RSDirectoryId int Not Null Auto_increment,
	RSDirectoryName varchar(255) Not Null,
	absolutePath varchar(255),
	Primary Key (RSDirectoryId)
);

create table RSPage (
	RSPageId int Not Null Auto_increment,
	RSPageName varchar(255) Not Null,
	note varchar(255),
	url varchar(255),
	RSDirectoryId int,
	characterCount int,
	wordCount int,
	tagCount int,
	lineCount int,
	RSPageCreateDate datetime,
	RSPageModifiedDate datetime,
	RSPageLastAccessDate datetime,
	Primary Key (RSPageId),
	Foreign Key (RSDirectoryId) References RSDirectory (RSDirectoryId) on delete set Null
);

create table RSAttribute (
	RSAttributeId int Not Null Auto_increment,
	RSAttributeName varchar(255) Not Null,
	RSAttributeDescription varchar(255),
	RSAttributeCreateDate datetime,
	RSAttributeModifiedDate datetime,
	Primary Key (RSAttributeId)
);

create table RSPageAttributeAffiliation (
	RSPageAttributeAffiliationId int Not Null Auto_increment,
	RSPageId int,
	RSAttributeId int,
	RSPageAttributeAffiliationDescription text,
	RSPageAttributeAffiliationSector int,
	Primary Key (RSPageAttributeAffiliationId),
	Foreign Key (RSPageId) References RSPage (RSPageId) on delete set Null,
	Foreign Key (RSAttributeId) References RSAttribute (RSAttributeId) on delete set Null
);

create table RSKeyword (
	RSKeywordId int Not Null Auto_increment,
	RSKeywordName varchar(255) Not Null,
	RSKeywordDescription varchar(255),
	RSKeywordCreateDate datetime,
	RSKeywordModifiedDate datetime,
	Primary Key (RSKeywordId) 
);

create table RSPageKeywordAffiliation (
	RSPageKeywordAffiliationId int Not Null Auto_increment,
	RSPageId int,
	RSKeywordId int,
	RSPageKeywordAffiliationDescription varchar (255),
	RSPageKeywordAffiliationSector int,
	Primary Key (RSPageKeywordAffiliationId),
	Foreign Key (RSPageId) References RSPage (RSPageId) on delete set Null,
	Foreign Key (RSKeywordId) References RSKeyword (RSKeywordId) on delete set Null
);

