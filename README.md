# GoodPort.top


### Description
Web application – Social project which connects people in need with people who can offer help.


### Goal
* Make a meaningful difference and enable Ukrainians to receive help in this wartime
* Apply and sharpen my own technical knowledge by developing a web application from idea to deployment
* Build a portfolio to demonstrate my knowledge to future employers
  Project Scope

### Technology stack
* Java 11
* Spring boot, Spring MVC, SpringSecurity
* Thymeleaf, HTML, CSS, Jquery - AJAX
* Hibernate, SQL, MySQL
* Apache Tomcat
* Heroku cloud server
* Amazon AWS S3 bucket
* i18n and Google translator API
* IntelliJ IDEA
* Maven
* Git


### Registration and authorization
* Separate registration for user and administrator
* Blocking and / or displaying content depending on the user's role

### Main page:
* Selecting records from database and display via pagination

### Images
* A registered user can upload or delete photos to his page.
* Uploading files to AWS s3 cloud storage
* Reducing the size of the photo before uploading to the storage

### Details page
* Flipping photo (use AJAX-Jquery)
* Application Protection:
* SQL injection protection
* Preventing Reflected XSS Attacks 

### Internationalization (i18n)
* Translation of static text into 5 languages
* During the creation of a new custom page, the application automatically translates the custom text into 5 languages ​​and puts each text into the database
* The first time the user opens the app, the app shows content in the language the customer is using. Users can also change the language in the application themselves.

### Also
* Design adapted for different devices
* User facing form to create help request pages and photos (moving new records into database)
* User facing form to update and remove pages and photos
* Filter to display pages by various parameters
* User account to allow monitoring and management of the user’s database records
* Header and footer included in pages via Thymeleaf fragments
* Form validation (with Bean Validation)

