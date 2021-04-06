INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.send-broadcast-message'), 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.execute-audit-log'), 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-PRE_SETTLEMENT_ADVICE'), 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-PRE_SETTLEMENT_FUNDED_PARTICIPANT_ADVICE'), 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-PRE_SETTLEMENT_ADVICE_SCHEME_OPERATOR'), 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-CYCLE_SETTLEMENT_REPORT'), 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT'), 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT_MESSAGE'), 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_SUMMARY_REPORT_SCHEME_OPERATOR_P'), 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_SUMMARY_REPORT_SCHEME_OPERATOR_M'), 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-ROUTING_TABLE_REPORT'), 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.suspend-unsupend-multiple-participants'), 'SCHEME');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'CLEARING'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-ROUTING_TABLE_REPORT'), 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-PRE_SETTLEMENT_ADVICE'), 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-CYCLE_SETTLEMENT_REPORT'), 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT'), 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT_MESSAGE'), 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-ROUTING_TABLE_REPORT'), 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.execute-audit-log'), 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-PRE_SETTLEMENT_ADVICE'), 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-CYCLE_SETTLEMENT_REPORT'), 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT'), 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT_MESSAGE'), 'DIRECT-ONLY');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'CLEARING'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-ROUTING_TABLE_REPORT'), 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-PRE_SETTLEMENT_ADVICE'), 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-PRE_SETTLEMENT_FUNDED_PARTICIPANT_ADVICE'), 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-CYCLE_SETTLEMENT_REPORT'), 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT'), 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT_MESSAGE'), 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-ROUTING_TABLE_REPORT'), 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.execute-audit-log'), 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.suspend-unsupend-multiple-participants'), 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-PRE_SETTLEMENT_ADVICE'), 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-CYCLE_SETTLEMENT_REPORT'), 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT'), 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT_MESSAGE'), 'FUNDING-ONLY');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-PRE_SETTLEMENT_FUNDED_PARTICIPANT_ADVICE'), 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT_MESSAGE'), 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-ROUTING_TABLE_REPORT'), 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.execute-audit-log'), 'FUNDED');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'CLEARING'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-ROUTING_TABLE_REPORT'), 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-PRE_SETTLEMENT_ADVICE'), 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-PRE_SETTLEMENT_FUNDED_PARTICIPANT_ADVICE'), 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-CYCLE_SETTLEMENT_REPORT'), 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT'), 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT_MESSAGE'), 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-ROUTING_TABLE_REPORT'), 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.execute-audit-log'), 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'MANAGEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.suspend-unsupend-multiple-participants'), 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-PRE_SETTLEMENT_ADVICE'), 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-CYCLE_SETTLEMENT_REPORT'), 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT'), 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES ((SELECT id FROM ROLE WHERE function = 'SETTLEMENT'), (SELECT id FROM UI_PERMISSION WHERE key = 'read.download-report-DAILY_RECONCILIATION_REPORT_MESSAGE'), 'DIRECT-FUNDING');
