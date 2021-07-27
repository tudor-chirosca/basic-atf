DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.scheme-operator-settlement-dashboard');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.participant-settlement-dashboard');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.funding-only-participant-settlement-dashboard');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.funded-participant-settlement-dashboard');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.settlement-enquiry');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.direct-and-funding-settlement-dashboard');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.execute-audit-log');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.funding-participant-settlement-dashboard');