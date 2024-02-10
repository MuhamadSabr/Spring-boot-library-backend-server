# Its General purpose
Communicates with the DB, and handles all transactional and non-transactional DB queries.
Exposes the DB entities as REST API endpoints to the client app, secured by Spring Security.
Generates a JWT Token when a successful request to /login is sent that can be used by the client app later instead of using the password with each request which is the standard way that stateless applications interact.
Serves a /refreshJWTToken endpoint that the client side can use to refresh their nonexpired token with. These are achieved using Spring Security filters.
Uses self-signed certificate to enable and accept only HTTPS communication
Uses Stripe payment APIs to provide the backend configuration such as creating paymentIntent, completing payment, etc... to the frontend app to accept payment from clients.
