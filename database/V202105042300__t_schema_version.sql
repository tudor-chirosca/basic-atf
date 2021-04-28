CREATE TABLE t_schema_version
(
    version       VARCHAR2(32 BYTE)   NOT NULL,
    release_date  TIMESTAMP WITH TIME ZONE  NOT NULL,
    applied_by    VARCHAR2(32 BYTE)
) NOLOGGING NOCACHE NOPARALLEL;
