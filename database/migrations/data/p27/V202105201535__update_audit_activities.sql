UPDATE user_audit_details SET activity_name = 'VIEW_SETTLEMENT_DASHBOARD'
    WHERE activity_name = 'VIEW_SCHEME_SETTL_DASHBOARD';

UPDATE user_audit_details SET activity_name = 'VIEW_SETTLEMENT_DASHBOARD_BY_MESSAGE_TYPE'
    WHERE activity_name = 'VIEW_PTT_SETTL_DASHBOARD';
