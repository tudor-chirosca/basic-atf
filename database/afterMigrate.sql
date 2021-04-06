-- Grants on all owner tables
BEGIN
    FOR t IN (SELECT OBJECT_NAME, OWNER
              FROM ALL_OBJECTS
              WHERE OBJECT_TYPE = 'TABLE'
              AND OWNER = UPPER('${schemaOwner}'))
    LOOP
        EXECUTE IMMEDIATE 'GRANT SELECT, INSERT, UPDATE, DELETE ON '|| t.OWNER ||'."'|| t.OBJECT_NAME ||'" TO ${schemaUser}';
    END LOOP;
END;

/

-- Synonyms on all owner tables
BEGIN
    FOR t IN (SELECT OBJECT_NAME, OWNER
              FROM ALL_OBJECTS
              WHERE OBJECT_TYPE = 'TABLE'
              AND OWNER = UPPER('${schemaOwner}'))
    LOOP
        EXECUTE IMMEDIATE 'CREATE OR REPLACE SYNONYM ${schemaUser}."'|| t.OBJECT_NAME ||'" FOR '|| t.OWNER ||'."'|| t.OBJECT_NAME ||'"';
    END LOOP;
END;

/

-- Grants on all owner views
BEGIN
    FOR v IN (SELECT OBJECT_NAME, OWNER
              FROM ALL_OBJECTS
              WHERE OBJECT_TYPE = 'VIEW'
              AND OWNER = UPPER('${schemaOwner}'))
    LOOP
        EXECUTE IMMEDIATE 'GRANT SELECT, INSERT, UPDATE, DELETE ON '|| v.OWNER ||'."'|| v.OBJECT_NAME ||'" TO ${schemaUser}';
    END LOOP;
END;

/

-- Synonyms on all owner views
BEGIN
    FOR v IN (SELECT OBJECT_NAME, OWNER
              FROM ALL_OBJECTS
              WHERE OBJECT_TYPE = 'VIEW'
              AND OWNER = UPPER('${schemaOwner}'))
    LOOP
        EXECUTE IMMEDIATE 'CREATE OR REPLACE SYNONYM ${schemaUser}."'|| v.OBJECT_NAME ||'" FOR '|| v.OWNER ||'."'|| v.OBJECT_NAME ||'"';
    END LOOP;
END;

/