import java.text.ParseException;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoPractice {

	public static void main(String args[]) throws ParseException {
		// Connect to a MongoDB instance running on local host at default port
		// 27017
		MongoClient mongoClient = new MongoClient(); // ("localhost", 27017);

		// Access "test" database (will create if it doesn't already exist)
		MongoDatabase db = mongoClient.getDatabase("test");

		// Insert a document into collection "restaurants" (will create if it
		// doesn't already exist) in "test" database
		// _id field is automatically added by driver if not included

//		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
//		MongoCollection<Document> collection = db.getCollection("restaurants");
//		collection.insertOne(new Document("address", new Document().append("street", "2 Avenue")
//				.append("zipcode", "10075").append("building", "1480")
//				.append("coord", asList(-73.9557413, 40.7720266)))
//				.append("borough", "Manhattan")
//				.append("cuisine", "Italian")
//				.append("grades",
//						asList(new Document().append("date", format.parse("2014-10-01T00:00:00Z"))
//								.append("grade", "A").append("score", 11),
//								new Document().append("date", format.parse("2014-01-16T00:00:00Z"))
//										.append("grade", "B").append("score", 17)))
//				.append("name", "Vella").append("restaurant_id", "41704620"));

		// Creating "practice" collection in "test" database
		MongoCollection<Document> coll = db.getCollection("practice");

		Document doc = new Document("name", "MongoDB").append("type", "database")
				.append("count", 1).append("info", new Document("x", 203).append("y", 102));

		Document doc2 = new Document("name", "MongoDB").append("type", "database")
				.append("count", 2).append("info", new Document("x", 103).append("y", 2));

		Document doc3 = new Document("name", "BDognoM").append("type", "database")
				.append("count", 2).append("info", new Document("x", 103).append("y", 2));

		coll.insertOne(doc);
		coll.insertOne(doc2);
		coll.insertOne(doc3);
		coll.findOneAndDelete(doc);
		coll.findOneAndDelete(doc2);
		coll.findOneAndDelete(doc3);
		
		//Update "name" in doc
		

		// Select all documents from the collection "restaurants"
		FindIterable<Document> iter = db.getCollection("practice").find(); // FindIterable<Document>
																			// instead
																			// of
																			// DBCursor

		iter.forEach(new Block<Document>() {
			public void apply(final Document document) {
				System.out.println(document);
			}
		});

		// Using Filter --the eq part-- to find a specific query
		// db.getCollection("restaurants").find(eq("borough", "Manhattan"));

		// Using regex to find results
		BasicDBObject regex = new BasicDBObject();
		regex.put("name", new BasicDBObject("$regex", "MongoDB").append("$options", "$i"));
		System.out.println(regex.toString());
		FindIterable<Document> iter2 = coll.find(regex);
		iter2.forEach(new Block<Document>() {
			public void apply(final Document document) {
				System.out.println(document);
			}
		});

		// Passing in instance of Java RegEx (java.util.regex.Pattern) to find results
		System.out.println("Regex with Java Pattern!");
		BasicDBObject regex2 = new BasicDBObject();
		regex2.put("name", java.util.regex.Pattern.compile("MongoDB"));
		FindIterable<Document> iter3 = coll.find(regex2);
		iter3.forEach(new Block<Document>() { 
			public void apply(final Document document) {
			System.out.println(document);
			}
		});
		
		//Trying to find nested array
		System.out.println("Finding nested array!");
		BasicDBObject regex3a = new BasicDBObject();
		regex3a.put("info.x", 203);
		FindIterable<Document> iter4 = coll.find(regex3a);
		iter4.forEach(new Block<Document>() { 
			public void apply(final Document document) {
			System.out.println(document);
			}
		});
		
		//Using for instead of FindIterable<Document> forEach
		System.out.println("Trying to print info.x");
		for (Document docObj : coll.find()) {//{'input.x' : '103'})) {
			long val = docObj.getInteger("info.1");
			System.out.println(val); //not returning the number; currently null
		}
	}
}
