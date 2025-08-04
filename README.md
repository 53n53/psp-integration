# psp-integration

This project uses the Quarkus Java framework to explore the integration of a Payment Service Provider (PSP).

Currently, it includes a basic integration with Stripe. Additional PSPs may be added as the project evolves.

## Roadmap
- [x] Retrieve the link to a checkout form hosted on Stripe
- [x] Implement a Spring Boot ExceptionHandler equivalent
- [x] Write Tests with Rest Assured
- [ ] Define an OpenAPI contract
- [ ] Containerize with Docker
- [ ] Use WireMock to mock Stripe API calls during testing
- [ ] Add a database to handle Stripe related data (priceId or productId for example)
- [ ] Build a front-end