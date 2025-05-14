# Keycloak Integration for LunarFlow

This repository is for development of an integration between a Quarkus application and Keycloak for secure authentication and authorisation in the LunarFlow application.

## Overview

This implementation provides:

- User authentication via Keycloak OIDC
- Group-based access control (using Keycloak Groups)
- Internal permission management within the application
- Permission-based authorisation
- Secured REST endpoints
- PostgreSQL data persistence

### Application Flow

1. Users access the application at http://localhost:5000
2. If not authenticated, they are redirected to Keycloak login page
3. After successful authentication, they are redirected back to the application
4. The application uses the SecurityUtils class to check if the user belongs to the allowed groups
5. Users in either `LunarFlowUsers` or `LunarFlowAdmins` groups can access the application
6. Additional permissions are managed internally by the application

## Getting Started

### Configuration

The Keycloak integration is configured in `/src/main/resources/application.properties`:

### Prerequisites

- Java 21
- Docker and Docker Compose
- A Keycloak server 

### Keycloak Setup

To use this integration, you'll need to:

1. Set up a Keycloak server
2. Create an `eclipse` realm (or update to the relm in application.properties)
3. Create a `lunarflow` client
   - Client ID: `lunarflow`
   - Access Type: `confidential`
   - Standard Flow Enabled: On
   - Valid Redirect URIs: include `http://localhost:5000/*`
   - Web Origins: include `+` to allow CORS from any Valid Redirect URI

4. Create 'LunarFlowUsers' and 'LunarFlowAdmins' groups
5. Assign users to these groups
6. Add Group Membership to the Token
   1. Select "Clients" and click on your "lunarflow" client
   2. Go to the "Client Scopes" tab
   3. Click on the default scope (usually "lunarflow-dedicated")
   4. Go to the "Mappers" tab
   5. Click "Create" and configure a new mapper:
       - Name: `groups`
       - Mapper Type: Group Membership
       - Token Claim Name: `groups`
       - Full group path: OFF
       - Add to ID token: ON
       - Add to access token: ON
       - Add to userinfo: ON

### Testing the Application

You can test the security integration by accessing these endpoints:

- / - Home page, requires authentication
- /hello - Sample endpoint that persists user info to database
- /test-security/debug - Shows what groups are included in your token
- /auth/login - Displays information about the authenticated user
- /auth/logout - Logs the user out

#### Development Mode

```bash
# Start the development environment with hot-reload
docker-compose up
```

The application will be available at http://localhost:5000

#### Production Mode

```bash
# Build the application
./mvnw package

# Build the Docker image
docker build -f src/main/docker/Dockerfile.jvm -t lunarflow/keycloak-integration .

# Run the container
docker run -i --rm -p 8080:8080 lunarflow/keycloak-integration
```

## Additional Resources

- [Quarkus Security Guide](https://quarkus.io/guides/security)
- [Quarkus Keycloak Guide](https://quarkus.io/guides/security-openid-connect)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
