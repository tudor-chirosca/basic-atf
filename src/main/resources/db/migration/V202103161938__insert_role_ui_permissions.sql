INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.direct-and-funding-settlement-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.scheme-operator-settlement-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-settlement-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.funding-participant-settlement-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.funded-participant-settlement-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.scheme-io-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-io-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.alerts-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.funding-only-participant-settlement-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-schedule', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-enquiry', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-management', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-config', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.files-batches-transactions', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_participant-name', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_settlement-account', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.broadcasts', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.approvals', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-routing-table', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.suspend-unsuspend-participant', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.approvals-configuration-change', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.suspend-unsuspend', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.reports', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.direct-and-funding-settlement-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.scheme-operator-settlement-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-settlement-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.funding-participant-settlement-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.funded-participant-settlement-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.scheme-io-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-io-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.alerts-dashboard', 'SCHEME');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.funding-only-participant-settlement-dashboard', 'SCHEME');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-io-dashboard', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-schedule', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'read.files-batches-transactions', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'update.batch-cancelation-action', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'update.approvals-configuration-change', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'read.reports', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-schedule', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-config', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.files-batches-transactions', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.approvals', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.approvals-filters-participants-section', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-routing-table', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_participant-name', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_output-file-flow', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.suspend-unsuspend-self-participant', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.batch-cancelation-action', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.approvals-configuration-change', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.reports', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.suspend-unsuspend', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-settlement-dashboard', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.funding-participant-settlement-dashboard', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-io-dashboard', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-config', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-schedule', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-routing-table', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-settlement-dashboard', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.funding-participant-settlement-dashboard', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-enquiry', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.batch-cancelation-action', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.approvals-configuration-change', 'DIRECT-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.reports', 'DIRECT-ONLY');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-schedule', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'read.reports', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-schedule', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-management', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-config', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_participant-name', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_debit-cap', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_debit-cap-threshold', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.approvals', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.approvals-filters-participants-section', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-routing-table', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.suspend-unsuspend-self-participant', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.suspend-unsuspend-funded-participant', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.reports', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.suspend-unsuspend', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.funding-only-participant-settlement-dashboard', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.funding-only-participant-settlement-dashboard-navigation-link', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.funding-participant-settlement-dashboard', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.funded-participant-settlement-dashboard', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.intraday-positions', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-management', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-config', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-schedule', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-routing-table', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.funding-only-participant-settlement-dashboard', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.funding-only-participant-settlement-dashboard-navigation-link', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.funding-participant-settlement-dashboard', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.funded-participant-settlement-dashboard', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.intraday-positions', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-enquiry', 'FUNDING-ONLY');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.reports', 'FUNDING-ONLY');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.funded-participant-settlement-dashboard', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-io-dashboard', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.intraday-positions', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-schedule', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-enquiry', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-config', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.files-batches-transactions', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_participant-name', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_debit-cap-threshold', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_output-file-flow', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.approvals', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.approvals-filters-participants-section', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-routing-table', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.suspend-unsuspend-self-participant', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.batch-cancelation-action', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.approvals-configuration-change', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.suspend-unsuspend', 'FUNDED');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.reports', 'FUNDED');

INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-io-dashboard', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-schedule', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'read.files-batches-transactions', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'update.batch-cancelation-action', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'update.approvals-configuration-change', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'CLEARING', SELECT id FROM UI_PERMISSION WHERE key = 'read.reports', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-schedule', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-management', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-config', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.files-batches-transactions', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_participant-name', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_debit-cap', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_debit-cap-threshold', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config_output-file-flow', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.participant-config', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.approvals', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.approvals-filters-participants-section', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-routing-table', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.suspend-unsuspend-self-participant', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.suspend-unsuspend-funded-participant', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.batch-cancelation-action', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.approvals-configuration-change', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.reports', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'MANAGEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'update.suspend-unsuspend', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.direct-and-funding-settlement-dashboard', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.direct-and-funding-settlement-dashboard-back-link', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.intraday-positions', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.funded-participant-settlement-dashboard', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-io-dashboard', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-management', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-config', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-schedule', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'READ_ONLY', SELECT id FROM UI_PERMISSION WHERE key = 'read.participant-routing-table', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.direct-and-funding-settlement-dashboard', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.direct-and-funding-settlement-dashboard-back-link', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.funded-participant-settlement-dashboard', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.intraday-positions', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.settlement-enquiry', 'DIRECT-FUNDING');
INSERT INTO ROLE_UI_PERMISSION(role_id, ui_permission_id, participant_type)
VALUES (SELECT id FROM ROLE WHERE function = 'SETTLEMENT', SELECT id FROM UI_PERMISSION WHERE key = 'read.reports', 'DIRECT-FUNDING');

