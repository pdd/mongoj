
db.cars.ensureIndex({"make" : 1});
db.testuser.ensureIndex({"lastName" : 1});
db.testuser.ensureIndex({"firstName" : 1, "lastName" : 1});
db.testuser.ensureIndex({"firstName" : 1, "info.dob" : 1});

db.cars.ensureIndex({"VIN" : 1}, {"unique" : true});
db.testuser.ensureIndex({"userId" : 1}, {"unique" : true});
db.testuser.ensureIndex({"firstName" : 1, "info.phone" : 1}, {"unique" : true});
