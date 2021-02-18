# Crating the JsonDBController
Now we will create the JsonDBController,
this JsonDBController will extends the JsonDBOpenHelper.

### Constructor
This constructor takes in the following items:
* dbName - String - your database name (with no ".json")
* dbContext - Context - from your Activity
* dbVersion - int - the version of the database

### onCreateDB
Then we will have to create the onCreateDB method. in this method we will specify
the jsonObject and its items, and it will be called the first time the database is created.
In the end we will write the jsonObject into the jsonfile using the writeJson method.

### onUpdateDB
Also we will have to create the onUpdateDB method, this method will be called when
you will create a newer version of the json database.

### other methods
we also have the writeJson method that takes in a jsonObject and writes in into the file.
and the readJson that reads the json file and returns a jsonObject.

Never forget to write you jsonObject into the file every time you change somthing in the jsonObject 