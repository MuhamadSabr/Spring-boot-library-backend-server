# Its General purpose
Communicates with the DB, handles all transactional and non-transactional DB quries.
Exposes the DB entities as REST API endpoints to the client-app.
Generates a JWT Token when a successful request to /login was sent that can be used by the client app later instead of using the password with each request which is the standard way that stateless applications interact.
Serves a /refreshJWTToken endpoint that the client side can use to refresh their non expired token with. These are achieve using Spring Security filters.
