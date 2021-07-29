DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.execute-audit-log');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.funding-participant-settlement-dashboard');