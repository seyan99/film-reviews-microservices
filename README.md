### Film reviews microservices

This project is inspired by Letterboxd, a social platform where you can share your taste in film.  
Currently in active development.

Basically, all service functionality the same as Letterboxd.

Architecture consists of config server, discovery, gateway and 6 main microservices.

**1. Activity service** represents user's activity on film:  
- like
- rate
- add to watchlist
- mark as watched

**2. Review service** allows users to share their reviews on films. User activity on particular film reflects in reviews to this film.

**3. List service** allows users to create custom film lists.

**4. Comment service** allows users to comment lists and reviews.

**5. Film service** allows to interact with information about films and films crew.

**6. User service** responsible for user info and networking.

In active development: fault tolerance, jwt-based authentication\authorization, notification and message queuing, db\server-side filtering with pagination and other improvements.