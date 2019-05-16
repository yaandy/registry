Tech stack:
* spring boot mvc
* timeleaf
* mysql
* jquery 
* bootstrap
* mdb lib
* datatables

HOW to Start
* setup docker
* setup  mysql on host machine (not containarized currently)
* create user and empty schema
* specify user credentials and schema in application.properties config file
* set spring.datasource.initialization-mode to always only if schema is empty and need to be initialized with initial data
* take a look at the jenkins pipeline script there is a script that up everything and redeploy 

Important configuration:
* application port configuration
* path to folder where all attachments are stored 


link storage to local linux folder
sudo mount -t nfs 192.168.0.107:/nfs/storage ~/storage


https://datatables.net/manual/installation
http://jqueryui.com/controlgroup/ - jquery snippets
https://habr.com/ru/post/351304/ - timeleaf


telegram
https://github.com/pengrad/java-telegram-bot-api
https://www.youtube.com/watch?v=m-5RSSZg0Os


//village into customer
//if zyomka then show for heodezyst
