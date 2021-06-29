INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.file-upload'), 'SCHEME_OPERATOR');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.file-download'), 'SCHEME_OPERATOR');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.approve-file-upload'), 'SCHEME_OPERATOR');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.file-upload'), 'DIRECT');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.file-download'), 'DIRECT');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.approve-file-upload'), 'DIRECT');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.file-upload'), 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.file-download'), 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.approve-file-upload'), 'FUNDED');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.file-upload'), 'DIRECT_FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.file-download'), 'DIRECT_FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'update.approve-file-upload'), 'DIRECT_FUNDING');