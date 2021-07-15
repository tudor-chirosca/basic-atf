ALTER TABLE user_audit_details ADD scheme VARCHAR(10) NOT NULL;

comment on column user_audit_details.scheme is 'The payment scheme.';
