ALTER TABLE user_audit_details ADD scheme VARCHAR(10);
UPDATE user_audit_details SET scheme = '-';
ALTER TABLE user_audit_details MODIFY (scheme NOT NULL);
COMMENT ON COLUMN user_audit_details.scheme IS 'The payment scheme.';
