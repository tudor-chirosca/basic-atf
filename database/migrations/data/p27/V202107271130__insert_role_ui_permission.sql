INSERT INTO UI_PERMISSION(id, key)
VALUES ('622b0586-1f87-48fd-9bc0-0aa2c2ad3c0f', 'read.output-info-section');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.output-info-section'), 'SCHEME_OPERATOR');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'READ_ONLY'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.output-info-section'), 'SCHEME_OPERATOR');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'CLEARING'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.output-info-section'), 'DIRECT');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'READ_ONLY'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.output-info-section'), 'DIRECT');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.output-info-section'), 'FUNDED');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'CLEARING'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.output-info-section'), 'DIRECT_FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'READ_ONLY'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.output-info-section'), 'DIRECT_FUNDING');