INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.execute-batch-cancellation'), 'SCHEME_OPERATOR');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.approve-batch-cancellation'), 'SCHEME_OPERATOR');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.execute-transaction-cancellation'), 'SCHEME_OPERATOR');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.approve-transaction-cancellation'), 'SCHEME_OPERATOR');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.execute-batch-cancellation'), 'DIRECT');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.approve-batch-cancellation'), 'DIRECT');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.execute-transaction-cancellation'), 'DIRECT');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.approve-transaction-cancellation'), 'DIRECT');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.execute-batch-cancellation'), 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.approve-batch-cancellation'), 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.execute-transaction-cancellation'), 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.approve-transaction-cancellation'), 'FUNDED');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.execute-batch-cancellation'), 'DIRECT_FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.approve-batch-cancellation'), 'DIRECT_FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.execute-transaction-cancellation'), 'DIRECT_FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.approve-transaction-cancellation'), 'DIRECT_FUNDING');