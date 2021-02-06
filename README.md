# JsonDBOpenHelper
thouse are two classes made for creating and controlling a json "Database" for internal use in android jav development.
## using
simular to the SQliteOpenHelper you need to extend the JsonDBOpenHelper class, this is an abstract class and you will have
to implement 2 methods:
* onCreateDB
* onUpdateDB
and also create a constructor.
### constructor
using the super method you will use the JsonDBOpenHelper constructor.
the constructor takes in 3 varuables:
* name - String, the name of the file (without the ".json").
* context - Context, from the activiry class.
* version - Int, used for version controll and update
### onCreateDB
this method is called the first time the class is being called and in this method you should create the json
using the JSONObject,
using the "put" method.
and then calling the "writeJson" method to write the data into the json file.
### onUpdateDB
this method is called when you create a newer version of the class with the version int constructor.
