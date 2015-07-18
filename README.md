About
------
Project Homepage: www.theproduct.works/projects/current/smart-app

SMART is an open-source project with both an Android and Web application to help bring the
Community Midwives in the National Maternity Hospital in Holles Street Dublin into the digital age.
Along with the website Midwives will be better able to organize and manage their appointments for
patients and search for relevant patient information for patients being seen by the team in the
South Dublin and North Wicklow area.

##Getting Started

####Download

First download the project using either git clone or by downloading a zipped up copy of the project

Git
```
    git clone https://github.com/otormaigh/SMART.git
```

Zipped file
```
    https://github.com/otormaigh/SMART/archive/master.zip
```

####Import
To import the project use Android Studio or IntelliJ IDEA. Select 'open an existing project' 
from them main menu and navigate to where you downloaded the project.

####Prerequisite
Before being able to run the application fully you must first get an API-key,
 endpoint URL, username and password.

To request these details for testing purposes, email the [The Product Works]
(http://www.theproduct.works/contact-us) with your details and why you neede access to the API.

Once you have obtain the relevant details enter API-key and URL into a file called NotKeys.java located at

```
app/src/main/java/ie/teamchile/smartapp/util/NotKeys.java
```

Once done you should be good to go. Push over the application to an Android device with an API version between
16 and 22.
