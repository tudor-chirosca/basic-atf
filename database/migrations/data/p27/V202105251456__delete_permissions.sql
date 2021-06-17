DELETE FROM role_ui_permission
WHERE participant_type IN ('DIRECT', 'DIRECT_FUNDING', 'FUNDED', 'FUNDING')
  AND ui_permission_id IN (
    SELECT ID FROM ui_permission WHERE key='read.approvals-filters-participants-section'
);
