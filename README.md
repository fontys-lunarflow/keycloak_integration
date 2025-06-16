# Keycloak Integration for LunarFlow

This repository is for development of an integration between a Quarkus application and Keycloak for secure authentication and authorisation in the LunarFlow application.

## Overview

This implementation provides:

- User authentication via Keycloak OIDC
- Role-based access control (using Keycloak Groups)
- User information retrieval via Keycloak Admin API
- Secured REST endpoints with role-based protection
- Hierarchical permission model (Viewers < Editors < Admins)

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

1. Set up a Keycloak server (or use an existing one)
2. Create an `eclipse` realm (or update the realm in Lunarflow Configuration)
3. Create the following groups:
   - `LunarflowViewers` - Basic read access
   - `LunarflowEditors` - Can edit content
   - `LunarflowAdmins` - Full administrative access

4. Create a `lunarflow` client with:
   - Client ID: `lunarflow`
   - Enable `Client authentication`
   - Standard Flow: `Enabled`
   - Service Accounts Roles: `Enabled` (critical for Admin API access)
   - Root URL: Enter the full path URL the Lunarflow application will be served on
   - Home URL: Enter the full path URL the Lunarflow application will be served on
   - Valid Redirect URIs: Enter the full path URL the Lunarflow application will be served on + `/*` at the end. I.e. https://lunarflow.luxdomain.work/*

5. Configure Service Account Permissions:
   - Go to "Service Account Roles" tab
   - Press `Assign role` and add the below roles:
     - `view-users`
     - `query-users`
     - `view-realm`

6. Add Group Membership to Token:
   - Go to "Client Scopes" tab for your client
   - Select the dedicated scope (e.g., "lunarflow-dedicated")
   - Go to "Mappers" tab and configure a new one:
     - Name: `groups`
     - Mapper Type: `Group Membership`
     - Token Claim Name: `groups`
     - Full group path: `OFF`
     - Add to ID token: `ON`
     - Add to access token: `ON`
     - Add to userinfo: `ON`

7. Create test users and assign them to appropriate groups


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
