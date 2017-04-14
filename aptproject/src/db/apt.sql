/* Create Tables */

CREATE TABLE aptuser
(
	aptuser_id varchar2(40) NOT NULL,
	aptuser_pw varchar2(40) DEFAULT '1234',
	aptuser_barcode varchar2(40) UNIQUE,
	aptuser_name varchar2(20),
	aptuser_phone varchar2(14),
	aptuser_regdate date DEFAULT sysdate,
	aptuser_live char(1) DEFAULT 'Y',
	aptuser_perm number DEFAULT 1,
	aptuser_ip varchar2(20),
	unit_id number NOT NULL,
	PRIMARY KEY (aptuser_id)
);


CREATE TABLE company
(
	company_id number NOT NULL,
	company_name varchar2(30),
	PRIMARY KEY (company_id)
);


CREATE TABLE complex
(
	complex_id number NOT NULL,
	complex_name varchar2(15),
	PRIMARY KEY (complex_id)
);


CREATE TABLE invoice
(
	invoice_id number NOT NULL,
	invoice_barcode varchar2(40) UNIQUE,
	invoice_arrtime date DEFAULT sysdate,
	invoice_taker varchar2(20),
	invoice_taketime date,
	invoice_takeflag char(1) DEFAULT 'N',
	aptuser_id varchar2(40) NOT NULL,
	company_id number NOT NULL,
	PRIMARY KEY (invoice_id)
);


CREATE TABLE message
(
	msg_id number,
	msg_sendtime date DEFAULT sysdate,
	msg_content varchar2(200),
	aptuser_id varchar2(40) NOT NULL
);


CREATE TABLE returninv
(
	returninv_id number NOT NULL,
	returninv_time date DEFAULT sysdate,
	returninv_date date,
	returninv_comment varchar2(200),
	returninv_arr date,
	returninv_dep date,
	returninv_barcode varchar2(40),
	invoiceinv_id number NOT NULL,
	PRIMARY KEY (returninv_id)
);


CREATE TABLE unit
(
	unit_id number NOT NULL,
	unit_name varchar2(15),
	complex_id number NOT NULL,
	PRIMARY KEY (unit_id)
);



/* Create Foreign Keys */

ALTER TABLE invoice
	ADD FOREIGN KEY (aptuser_id)
	REFERENCES aptuser (aptuser_id)
;


ALTER TABLE message
	ADD FOREIGN KEY (aptuser_id)
	REFERENCES aptuser (aptuser_id)
;


ALTER TABLE invoice
	ADD FOREIGN KEY (company_id)
	REFERENCES company (company_id)
;


ALTER TABLE unit
	ADD FOREIGN KEY (complex_id)
	REFERENCES complex (complex_id)
;


ALTER TABLE returninv
	ADD FOREIGN KEY (invoiceinv_id)
	REFERENCES invoice (invoice_id)
;


ALTER TABLE aptuser
	ADD FOREIGN KEY (unit_id)
	REFERENCES unit (unit_id)
;



