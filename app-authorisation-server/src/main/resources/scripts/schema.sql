CREATE TABLE oauth2_registered_client (
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    client_id VARCHAR(100) NOT NULL,
    client_id_issued_at DATETIME2 DEFAULT GETDATE() NOT NULL,
    client_secret VARCHAR(200) NULL,
    client_secret_expires_at DATETIME2 NULL,
    client_name VARCHAR(200) NOT NULL,
    client_authentication_methods VARCHAR(1000) NOT NULL,
    authorization_grant_types VARCHAR(1000) NOT NULL,
    redirect_uris VARCHAR(1000) NULL,
    post_logout_redirect_uris VARCHAR(1000) NULL,
    scopes VARCHAR(1000) NOT NULL,
    client_settings VARCHAR(2000) NOT NULL,
    token_settings VARCHAR(2000) NOT NULL
);


CREATE TABLE oauth2_authorization (
    id VARCHAR(100) NOT NULL PRIMARY KEY,
    registered_client_id VARCHAR(100) NOT NULL,
    principal_name VARCHAR(200) NOT NULL,
    authorization_grant_type VARCHAR(100) NOT NULL,
    authorized_scopes VARCHAR(1000) NULL,
    attributes VARCHAR(MAX) NULL,
    state VARCHAR(500) NULL,
    authorization_code_value VARCHAR(MAX) NULL,
    authorization_code_issued_at DATETIME2 NULL,
    authorization_code_expires_at DATETIME2 NULL,
    authorization_code_metadata VARCHAR(MAX) NULL,
    access_token_value VARCHAR(MAX) NULL,
    access_token_issued_at DATETIME2 NULL,
    access_token_expires_at DATETIME2 NULL,
    access_token_metadata VARCHAR(MAX) NULL,
    access_token_type VARCHAR(100) NULL,
    access_token_scopes VARCHAR(1000) NULL,
    oidc_id_token_value VARCHAR(MAX) NULL,
    oidc_id_token_issued_at DATETIME2 NULL,
    oidc_id_token_expires_at DATETIME2 NULL,
    oidc_id_token_metadata VARCHAR(MAX) NULL,
    refresh_token_value VARCHAR(MAX) NULL,
    refresh_token_issued_at DATETIME2 NULL,
    refresh_token_expires_at DATETIME2 NULL,
    refresh_token_metadata VARCHAR(MAX) NULL,
    user_code_value VARCHAR(MAX) NULL,
    user_code_issued_at DATETIME2 NULL,
    user_code_expires_at DATETIME2 NULL,
    user_code_metadata VARCHAR(MAX) NULL,
    device_code_value VARCHAR(MAX) NULL,
    device_code_issued_at DATETIME2 NULL,
    device_code_expires_at DATETIME2 NULL,
    device_code_metadata VARCHAR(MAX) NULL
);

CREATE TABLE oauth2_authorization_consent (
    registered_client_id varchar(100) NOT NULL,
    principal_name varchar(200) NOT NULL,
    authorities varchar(1000) NOT NULL,
    PRIMARY KEY (registered_client_id, principal_name)
);

INSERT INTO oauth2_registered_client
(id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings)
VALUES(N'7bc06160-6a85-4823-9a19-662752683a71', N'emandate-switch', '2025-10-12 15:21:25.912', N'{noop}secret', NULL, N'7bc06160-6a85-4823-9a19-662752683a71', N'client_secret_basic', N'refresh_token,client_credentials,authorization_code', N'http://127.0.0.1:8080/authorized', N'', N'account-enquiry:read', N'{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":false}', N'{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",720.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",43200.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');




INSERT INTO oauth2_registered_client
(id, client_id, client_id_issued_at, client_secret, client_secret_expires_at, client_name, client_authentication_methods, authorization_grant_types, redirect_uris, post_logout_redirect_uris, scopes, client_settings, token_settings)
VALUES(N'6499bc69-6a6a-4ff8-90d9-64db79deb7b0', N'public-client', '2025-10-12 14:45:13.778', NULL, NULL, N'6499bc69-6a6a-4ff8-90d9-64db79deb7b0', N'none', N'authorization_code', N'http://127.0.0.1:8080/authorized', N'', N'openid,iblogin-scope,profile', N'{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":true,"settings.client.require-authorization-consent":true}', N'{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":true,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",300.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",3600.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",300.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}');

