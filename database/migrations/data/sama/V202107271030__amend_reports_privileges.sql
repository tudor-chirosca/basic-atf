DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.download-report-PRE_SETTLEMENT_ADVICE');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.download-report-PRE_SETTLEMENT_FUNDED_PARTICIPANT_ADVICE');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.download-report-PRE_SETTLEMENT_ADVICE_SCHEME_OPERATOR');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.download-report-CYCLE_SETTLEMENT_REPORT');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT_MESSAGE');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.download-report-DAILY_SUMMARY_REPORT_SCHEME_OPERATOR_P');

DELETE FROM role_ui_permission
WHERE ui_permission_id IN (SELECT id FROM ui_permission WHERE key = 'read.download-report-DAILY_SUMMARY_REPORT_SCHEME_OPERATOR_M');