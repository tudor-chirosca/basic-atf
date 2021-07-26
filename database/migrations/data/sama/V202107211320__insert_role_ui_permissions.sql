INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_PAYMENT'), 'SCHEME_OPERATOR');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_MESSAGE'), 'SCHEME_OPERATOR');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_PAYMENT_OPERATOR'), 'SCHEME_OPERATOR');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_MESSAGE_OPERATOR'), 'SCHEME_OPERATOR');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_PAYMENT'), 'DIRECT');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_PAYMENT'), 'DIRECT');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_MESSAGE'), 'DIRECT');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_MESSAGE'), 'DIRECT');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_PAYMENT'), 'DIRECT_FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_PAYMENT'), 'DIRECT_FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_MESSAGE'), 'DIRECT_FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_MESSAGE'), 'DIRECT_FUNDING');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_PAYMENT'), 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-REALTIME_OUTPUT_MESSAGE'), 'FUNDED');