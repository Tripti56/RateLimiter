# Rate Limiter Application

## Steps to configure the application 

### Softwares required
		IDE - Eclipse or intelliJ
		Java 1.8
		Maven 3.6.0
		Redis 2.4.5
		Postman or any client to test
### Import the project in the IDE
### Run the Redis server on the local system 
### Run the application 
	Go to main application ,click on run as Java application or click  on application and select on run as java application.
### Run postman and use following URL for operation's
	URL's
	#### To configure the rate limits for user with respect to API
		URL : <host>:<port>/api/v1/configure 
		Type: Post
		Body: Example - 
		{
		"user":"user2",
		"api":"orgs",
		"maxRequestsInWindow":5
		}
	#### To see all the rate limits wrt user,api,rateLimit
		URL : <host>:<port>/api/v1/getAllConfigurations
		Type : Get
	#### To see if a request is allowed for a user with respect to an API (Needs to be hit multiple times to reach rate limit)
		URL : <host>:<port>/api/v1/isRateLimitingAllowed/{userid}/{api}
		Type : Get
	#### To update an existing configuration
		URL : <host>:<port>/update/{api}/{user}/{rateLimit}
		Type : Post
		
## Design

### Rate limiting 
	The pattern for Rate Limitation focuses on controlling incoming requests to protect the application in case of peak loads. Here we have used sliding window 	algorithm which resolves the problem of burst inflow of request precisely and handles the rate limiting in a dynamic fashion. It also employs better memory 	consumption (~timestamp * maximum allowable requests). 
### Flow
	When a user request registers with the application. Admin user can configure the rate limits for the user wrt API's. This data gets stored in the redis 	cache. Requests to the application should be redirected to rate limiter using the filter/interceptor/API gateway/load balancer etc. When a request reached 	rate limiter , redis configuration is checked against the number of request already served in the last (60s) time window. If the request served is less 	than the maximum allowed , the incoming request is served else rejected with a prompt for later trial. The limit gets refreshed at the next 60s window. 


### Assumptions and limitations 
	- Users are limited in number.
	- User information and API called is taken in the URL.
	- Uses one instance of Redis . The connection configuration requires enhancement to work in distributed environment.

### Further steps
	- Better handling of the responses in case of request rejection , error codes, redirects etc. 
	- Unit testing for coverage and static checks. 

### Other approaches explored
	- Rate limiting can be part of a solution with Spring filters. Bucket4J library gives a out of the box implementation of leaky bucket algorithm.
	- If we have a load balancer like cloud controller which can intercept all the incoming calls, then create an independent application. Register it as a 	route service which can be integrated with multiple user interfacing applications. Easily maintainable approach but requires autoscaling on the part of 	rate limiter. 
	- Use API Manager which has rate limiting and throttling involved. 
	



