/**
* Copyright (c) 2011 Prashant Dighe
* 
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
* 
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
* 
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/

package org.mongoj.samples.service.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteResult;

import org.bson.types.ObjectId;

import org.mongoj.exception.SystemException;
import org.mongoj.exception.UpdateException;

import org.mongoj.samples.model.User;
import org.mongoj.samples.model.impl.UserImpl;

import org.mongoj.service.persistence.impl.BasePersistenceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Prashant Dighe
 */
public class UserPersistenceImpl extends BasePersistenceImpl<User>
	implements UserPersistence {
	public User create() {
		User user = new UserImpl();

		user.setNew(true);

		return user;
	}

	public long countAll() throws SystemException {
		DBCollection collection = getDB()
									  .getCollection(UserImpl.COLLECTION_NAME);

		return collection.count();
	}

	public User fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		DBCollection collection = getDB().getCollection(UserImpl.COLLECTION_NAME);

		DBObject criteria = new QueryBuilder().put("_id")
											  .is(new ObjectId(
					primaryKey.toString())).get();

		DBObject dbObject = collection.findOne(criteria);

		return getDocument(dbObject);
	}

	public User findByUserId(String userId) throws SystemException {
		String[] searchFields = { "userId" };

		DBCollection collection = getDB().getCollection(UserImpl.COLLECTION_NAME);

		int i = 0;

		DBObject criteria = new QueryBuilder().put(searchFields[i++]).is(userId)
											  .get();

		DBObject dbObject = collection.findOne(criteria);

		return getDocument(dbObject);
	}

	public User findByLastName(String lastName) throws SystemException {
		String[] searchFields = { "lastName" };

		DBCollection collection = getDB().getCollection(UserImpl.COLLECTION_NAME);

		int i = 0;

		DBObject criteria = new QueryBuilder().put(searchFields[i++])
											  .is(lastName).get();

		DBObject dbObject = collection.findOne(criteria);

		return getDocument(dbObject);
	}

	public User findByFN_LN(String firstName, String lastName)
		throws SystemException {
		String[] searchFields = { "firstName", "lastName" };

		DBCollection collection = getDB().getCollection(UserImpl.COLLECTION_NAME);

		int i = 0;

		DBObject criteria = new QueryBuilder().put(searchFields[i++])
											  .is(firstName)
											  .put(searchFields[i++])
											  .is(lastName).get();

		DBObject dbObject = collection.findOne(criteria);

		return getDocument(dbObject);
	}

	public List<User> findByFN_DOB(String firstName, Date dob)
		throws SystemException {
		return findByFN_DOB(firstName, dob, 0, LIMIT);
	}

	public List<User> findByFN_DOB(String firstName, Date dob, int start,
		int end) throws SystemException {
		if (start < 0) {
			start = 0;
		}

		if ((end - start) < 0) {
			end = start + LIMIT;
		}

		String[] searchFields = { "firstName", "info.dob" };

		DBCollection collection = getDB().getCollection(UserImpl.COLLECTION_NAME);

		int i = 0;

		DBObject criteria = new QueryBuilder().put(searchFields[i++])
											  .is(firstName)
											  .put(searchFields[i++]).is(dob)
											  .get();

		DBCursor dbCursor = collection.find(criteria).skip(start)
									  .limit(end - start);

		List<User> list = new ArrayList<User>();

		for (DBObject dbObject : dbCursor) {
			list.add(getDocument(dbObject));
		}

		return list;
	}

	public List<User> findAll() throws SystemException {
		return findAll(0, LIMIT);
	}

	public List<User> findAll(int start, int end) throws SystemException {
		if (start < 0) {
			start = 0;
		}

		if ((end - start) < 0) {
			end = start + LIMIT;
		}

		DBCollection collection = getDB().getCollection(UserImpl.COLLECTION_NAME);

		List<DBObject> dbObjects = collection.find().skip(start)
											 .limit(end - start).toArray();

		List<User> documents = new ArrayList<User>(dbObjects.size());

		for (DBObject dbObject : dbObjects) {
			documents.add(getDocument(dbObject));
		}

		return documents;
	}

	protected void removeImpl(String id)
		throws UpdateException, SystemException {
		DBObject criteria = new QueryBuilder().put("_id").is(id).get();

		DBCollection collection = getDB().getCollection(UserImpl.COLLECTION_NAME);

		WriteResult writeResult = collection.remove(criteria);

		String err = writeResult.getError();

		if (err != null) {
			throw new UpdateException(err);
		}
	}

	protected User removeImpl(User user)
		throws UpdateException, SystemException {
		DBCollection collection = getDB().getCollection(UserImpl.COLLECTION_NAME);

		WriteResult writeResult = collection.remove(getDBObject(user));

		String err = writeResult.getError();

		if (err != null) {
			throw new UpdateException(err);
		}

		return user;
	}

	protected User updateImpl(org.mongoj.samples.model.User user)
		throws UpdateException, SystemException {
		DBCollection collection = getDB().getCollection(UserImpl.COLLECTION_NAME);

		if (user.isNew()) {
			user.setNew(false);

			WriteResult writeResult = collection.insert(getDBObject(user));

			String err = writeResult.getError();

			if (err != null) {
				throw new UpdateException(err);
			}
		}
		else {
			DBObject criteria = new QueryBuilder().put("_id")
												  .is(new ObjectId(user.getId()))
												  .get();

			UserImpl userImpl = (UserImpl)user;

			BasicDBObjectBuilder updateBuilder = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder setUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder pushUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder pushAllUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder addUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder removeUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder removeAllUpdates = BasicDBObjectBuilder.start();

			for (String field : userImpl.setMap.keySet()) {
				setUpdates = setUpdates.add(field, userImpl.setMap.get(field));
			}

			if (!setUpdates.isEmpty()) {
				updateBuilder.add(SET_OPERATOR, setUpdates.get());
			}

			for (String field : userImpl.appendMap.keySet()) {
				List<Object> list = (List<Object>)userImpl.appendMap.get(field);

				if (!list.isEmpty()) {
					if (list.size() == 1) {
						pushUpdates = pushUpdates.add(field,
								((List)userImpl.appendMap.get(field)).get(0));
					}
					else {
						pushAllUpdates = pushAllUpdates.add(field,
								userImpl.appendMap.get(field));
					}
				}
			}

			if (!pushUpdates.isEmpty()) {
				updateBuilder.add(PUSH_OPERATOR, pushUpdates.get());
			}

			if (!pushAllUpdates.isEmpty()) {
				updateBuilder.add(PUSH_ALL_OPERATOR, pushAllUpdates.get());
			}

			for (String field : userImpl.addMap.keySet()) {
				List<Object> list = (List<Object>)userImpl.addMap.get(field);

				if (!list.isEmpty()) {
					if (list.size() == 1) {
						addUpdates = addUpdates.add(field,
								((List)userImpl.addMap.get(field)).get(0));
					}
					else {
						DBObject each = BasicDBObjectBuilder.start()
															.add(EACH_OPERATOR,
								((List)userImpl.addMap.get(field)).toArray())
															.get();

						addUpdates = addUpdates.add(field, each);
					}
				}
			}

			if (!addUpdates.isEmpty()) {
				updateBuilder.add(ADD_TO_SET_OPERATOR, addUpdates.get());
			}

			for (String field : userImpl.removeMap.keySet()) {
				List<Object> list = (List<Object>)userImpl.removeMap.get(field);

				if (!list.isEmpty()) {
					if (list.size() == 1) {
						removeUpdates = removeUpdates.add(field,
								((List)userImpl.removeMap.get(field)).get(0));
					}
					else {
						removeAllUpdates = removeAllUpdates.add(field,
								userImpl.removeMap.get(field));
					}
				}
			}

			if (!removeUpdates.isEmpty()) {
				updateBuilder.add(PULL_OPERATOR, removeUpdates.get());
			}

			if (!removeAllUpdates.isEmpty()) {
				updateBuilder.add(PULL_ALL_OPERATOR, removeAllUpdates.get());
			}

			if (!updateBuilder.isEmpty()) {
				DBObject update = updateBuilder.get();

				_log.debug("Update query = {}", update);

				WriteResult writeResult = collection.update(criteria, update);

				String err = writeResult.getError();

				if (err != null) {
					throw new UpdateException(err);
				}
			}
		}

		return user;
	}

	protected User getDocument(DBObject dbObject) throws SystemException {
		if (dbObject == null) {
			return null;
		}

		User user = new UserImpl(dbObject.toMap());

		return user;
	}

	protected DBObject getDBObject(User user) {
		DBObject dbObject = new BasicDBObject();

		if (user.getId() != null) {
			dbObject.put("_id", new ObjectId(user.getId()));
		}
		else {
			ObjectId id = new ObjectId();
			dbObject.put("_id", id);
			user.setId(id.toString());
		}

		dbObject.putAll(user.toMap());

		return dbObject;
	}

	Logger _log = LoggerFactory.getLogger(UserPersistenceImpl.class);
}