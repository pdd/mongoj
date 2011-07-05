/**
 * Copyright (c) 2011 Prashant Dighe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following conditions:
 * 	The above copyright notice and this permission notice shall be included 
 * 	in all copies or substantial portions of the Software. 
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS 
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER 
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
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

import org.mongoj.samples.model.Car;
import org.mongoj.samples.model.impl.CarImpl;

import org.mongoj.service.persistence.impl.BasePersistenceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Generated file - Do NOT edit. It will be overwritten at next ServiceBuilder iteration.
 */
public class CarPersistenceImpl extends BasePersistenceImpl<Car>
	implements CarPersistence {
	public Car create() {
		Car car = new CarImpl();

		car.setNew(true);

		return car;
	}

	public long countAll() throws SystemException {
		DBCollection collection = getDB()
									  .getCollection(CarImpl.COLLECTION_NAME);

		return collection.count();
	}

	public Car fetchByPrimaryKey(Serializable primaryKey)
		throws SystemException {
		DBCollection collection = getDB().getCollection(CarImpl.COLLECTION_NAME);

		DBObject criteria = new QueryBuilder().put("_id")
											  .is(new ObjectId(
					primaryKey.toString())).get();

		DBObject dbObject = collection.findOne(criteria);

		return getDocument(dbObject);
	}

	public Car findByVIN(String vIN) throws SystemException {
		String[] searchFields = { "VIN" };

		DBCollection collection = getDB().getCollection(CarImpl.COLLECTION_NAME);

		int i = 0;

		DBObject criteria = new QueryBuilder().put(searchFields[i++]).is(vIN)
											  .get();

		DBObject dbObject = collection.findOne(criteria);

		return getDocument(dbObject);
	}

	public List<Car> findByMake(String make) throws SystemException {
		return findByMake(make, 0, LIMIT);
	}

	public List<Car> findByMake(String make, int start, int end)
		throws SystemException {
		if (start < 0) {
			start = 0;
		}

		if ((end - start) < 0) {
			end = start + LIMIT;
		}

		String[] searchFields = { "make" };

		DBCollection collection = getDB().getCollection(CarImpl.COLLECTION_NAME);

		int i = 0;

		DBObject criteria = new QueryBuilder().put(searchFields[i++]).is(make)
											  .get();

		DBCursor dbCursor = collection.find(criteria).skip(start)
									  .limit(end - start);

		List<Car> list = new ArrayList<Car>();

		for (DBObject dbObject : dbCursor) {
			list.add(getDocument(dbObject));
		}

		return list;
	}

	public List<Car> findAll() throws SystemException {
		return findAll(0, LIMIT);
	}

	public List<Car> findAll(int start, int end) throws SystemException {
		if (start < 0) {
			start = 0;
		}

		if ((end - start) < 0) {
			end = start + LIMIT;
		}

		DBCollection collection = getDB().getCollection(CarImpl.COLLECTION_NAME);

		List<DBObject> dbObjects = collection.find().skip(start)
											 .limit(end - start).toArray();

		List<Car> documents = new ArrayList<Car>(dbObjects.size());

		for (DBObject dbObject : dbObjects) {
			documents.add(getDocument(dbObject));
		}

		return documents;
	}

	protected void removeImpl(String id)
		throws UpdateException, SystemException {
		DBObject criteria = new QueryBuilder().put("_id").is(id).get();

		DBCollection collection = getDB().getCollection(CarImpl.COLLECTION_NAME);

		WriteResult writeResult = collection.remove(criteria);

		String err = writeResult.getError();

		if (err != null) {
			throw new UpdateException(err);
		}
	}

	protected Car removeImpl(Car car) throws UpdateException, SystemException {
		DBCollection collection = getDB().getCollection(CarImpl.COLLECTION_NAME);

		WriteResult writeResult = collection.remove(getDBObject(car));

		String err = writeResult.getError();

		if (err != null) {
			throw new UpdateException(err);
		}

		return car;
	}

	protected Car updateImpl(org.mongoj.samples.model.Car car)
		throws UpdateException, SystemException {
		DBCollection collection = getDB().getCollection(CarImpl.COLLECTION_NAME);

		if (car.isNew()) {
			car.setNew(false);

			CarImpl carImpl = (CarImpl)car;

			carImpl.addMap.clear();
			carImpl.appendMap.clear();
			carImpl.removeMap.clear();
			carImpl.setMap.clear();

			WriteResult writeResult = collection.insert(getDBObject(car));

			String err = writeResult.getError();

			if (err != null) {
				throw new UpdateException(err);
			}
		}
		else {
			DBObject criteria = new QueryBuilder().put("_id")
												  .is(new ObjectId(car.getId()))
												  .get();

			CarImpl carImpl = (CarImpl)car;

			BasicDBObjectBuilder updateBuilder = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder setUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder pushUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder pushAllUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder addUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder removeUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder removeAllUpdates = BasicDBObjectBuilder.start();

			for (String field : carImpl.setMap.keySet()) {
				setUpdates = setUpdates.add(field, carImpl.setMap.get(field));
			}

			if (!setUpdates.isEmpty()) {
				updateBuilder.add(SET_OPERATOR, setUpdates.get());
			}

			for (String field : carImpl.appendMap.keySet()) {
				List<Object> list = (List<Object>)carImpl.appendMap.get(field);

				if (!list.isEmpty()) {
					if (list.size() == 1) {
						pushUpdates = pushUpdates.add(field,
								((List)carImpl.appendMap.get(field)).get(0));
					}
					else {
						pushAllUpdates = pushAllUpdates.add(field,
								carImpl.appendMap.get(field));
					}
				}
			}

			if (!pushUpdates.isEmpty()) {
				updateBuilder.add(PUSH_OPERATOR, pushUpdates.get());
			}

			if (!pushAllUpdates.isEmpty()) {
				updateBuilder.add(PUSH_ALL_OPERATOR, pushAllUpdates.get());
			}

			for (String field : carImpl.addMap.keySet()) {
				List<Object> list = (List<Object>)carImpl.addMap.get(field);

				if (!list.isEmpty()) {
					if (list.size() == 1) {
						addUpdates = addUpdates.add(field,
								((List)carImpl.addMap.get(field)).get(0));
					}
					else {
						DBObject each = BasicDBObjectBuilder.start()
															.add(EACH_OPERATOR,
								((List)carImpl.addMap.get(field)).toArray())
															.get();

						addUpdates = addUpdates.add(field, each);
					}
				}
			}

			if (!addUpdates.isEmpty()) {
				updateBuilder.add(ADD_TO_SET_OPERATOR, addUpdates.get());
			}

			for (String field : carImpl.removeMap.keySet()) {
				List<Object> list = (List<Object>)carImpl.removeMap.get(field);

				if (!list.isEmpty()) {
					if (list.size() == 1) {
						removeUpdates = removeUpdates.add(field,
								((List)carImpl.removeMap.get(field)).get(0));
					}
					else {
						removeAllUpdates = removeAllUpdates.add(field,
								carImpl.removeMap.get(field));
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

		return car;
	}

	protected Car getDocument(DBObject dbObject) throws SystemException {
		if (dbObject == null) {
			return null;
		}

		Car car = new CarImpl(dbObject.toMap());

		return car;
	}

	protected DBObject getDBObject(Car car) {
		DBObject dbObject = new BasicDBObject();

		if (car.getId() != null) {
			dbObject.put("_id", new ObjectId(car.getId()));
		}
		else {
			ObjectId id = new ObjectId();
			dbObject.put("_id", id);
			car.setId(id.toString());
		}

		dbObject.putAll(car.toMap());

		return dbObject;
	}

	Logger _log = LoggerFactory.getLogger(CarPersistenceImpl.class);
}