package ${package}.service.persistence;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.io.Serializable;

import org.mongoj.exception.UpdateException;
import org.mongoj.exception.SystemException;
import org.mongoj.service.persistence.impl.BasePersistenceImpl;

import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCollection;
import com.mongodb.QueryBuilder;
import com.mongodb.WriteResult;
import org.bson.types.ObjectId;

import ${package}.model.${document.name};
import ${package}.model.impl.${document.name}Impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Generated file - Do NOT edit. It will be overwritten at next ServiceBuilder iteration.
 */
public class ${document.name}PersistenceImpl extends BasePersistenceImpl<${document.name}> implements ${document.name}Persistence {

	public ${document.name} create() {
		${document.name} ${document.variableName} = new ${document.name}Impl();
		
		${document.variableName}.setNew(true);

		return ${document.variableName};
	}

	public long countAll() throws SystemException {
		DBCollection collection = 
			getDB().getCollection(${document.name}Impl.COLLECTION_NAME);
			
		return collection.count(); 
	}

	public ${document.name} fetchByPrimaryKey(Serializable primaryKey) throws SystemException {
		DBCollection collection = 
			getDB().getCollection(${document.name}Impl.COLLECTION_NAME);
			
		DBObject criteria = new QueryBuilder()
			.put("_id")
			.is(new ObjectId(primaryKey.toString()))
			.get();
		
		DBObject dbObject = collection.findOne(criteria);
		
		return getDocument(dbObject);
	}

	<#list document.finders as finder>
		<#assign finderFields = finder.getFields()>
		
		<#if finder.collection>
			public List<${document.name}> findBy${finder.name}(
			<#list finderFields as finderField>
				${finderField.typeName} ${finderField.variableName}
				<#if finderField_has_next>
					,
				</#if>
			</#list>
			) throws SystemException {
				return findBy${finder.name}(
					<#list finderFields as finderField>
						${finderField.variableName}
						<#if finderField_has_next>
							,
						</#if>
					</#list>
					, 0 , LIMIT);
			}
			
			public List<${document.name}> findBy${finder.name}(
			<#list finderFields as finderField>
				${finderField.typeName} ${finderField.variableName}
				<#if finderField_has_next>
					,
				</#if>
			</#list>
			, int start, int end) throws SystemException {
				if (start < 0) {
					start = 0;
				} 
		
				if ((end - start) < 0) {
					end = start + LIMIT;
				}

				String[] searchFields = {
					<#list finder.finderFields as finderFieldName>
						"${finderFieldName}"
						<#if finderFieldName_has_next>
							,
						</#if>
					</#list>
				};
				
				DBCollection collection = 
					getDB().getCollection(${document.name}Impl.COLLECTION_NAME);
			
				int i = 0;
			
				DBObject criteria = new QueryBuilder()
					<#list finderFields as finderField>
						.put(searchFields[i++])
						.is(${finderField.variableName})
					</#list>
					.get();
				
				DBCursor dbCursor = collection.find(criteria)
					.skip(start).limit(end - start);
				
				List<${document.name}> list = new ArrayList<${document.name}>();
				
				for (DBObject dbObject : dbCursor) {
					list.add(getDocument(dbObject));
				}
			
				return list;
			}
		<#else>
			public ${document.name} findBy${finder.name}(
			<#list finderFields as finderField>
				${finderField.typeName} ${finderField.variableName}
				<#if finderField_has_next>
					,
				</#if>
			</#list>
			) throws SystemException {
				String[] searchFields = {
					<#list finder.finderFields as finderFieldName>
						"${finderFieldName}"
						<#if finderFieldName_has_next>
							,
						</#if>
					</#list>
				};
				
				DBCollection collection = 
					getDB().getCollection(${document.name}Impl.COLLECTION_NAME);
			
				int i = 0;
			
				DBObject criteria = new QueryBuilder()
					<#list finderFields as finderField>
						.put(searchFields[i++])
						.is(${finderField.variableName})
					</#list>
					.get();

				DBObject dbObject = collection.findOne(criteria);
				
				return getDocument(dbObject);
			}
		</#if>
	</#list>

	public List<${document.name}> findAll() throws SystemException {
		return findAll(0, LIMIT);
	}
	
	public List<${document.name}> findAll(int start, int end) 
		throws SystemException {
		if (start < 0) {
			start = 0;
		} 
		
		if (end - start < 0) {
			end = start + LIMIT;
		}
		 
		DBCollection collection = 
			getDB().getCollection(${document.name}Impl.COLLECTION_NAME);
		
		List<DBObject> dbObjects = collection.find().skip(start)
			.limit(end - start).toArray();
		
		List<${document.name}> documents = 
			new ArrayList<${document.name}>(dbObjects.size());
		
		for (DBObject dbObject : dbObjects) {
			documents.add(getDocument(dbObject));
		}
		
		return documents;
	}
	
	protected void removeImpl(String id) 
		throws UpdateException, SystemException {
		DBObject criteria = new QueryBuilder().put("_id").is(id).get();
	
		DBCollection collection = 
			getDB().getCollection(${document.name}Impl.COLLECTION_NAME);
		
		WriteResult writeResult = collection.remove(criteria);
		
		String err = writeResult.getError();
		
		if (err != null) {
			throw new UpdateException(err);
		}
	}

	protected ${document.name} removeImpl(${document.name} ${document.variableName}) 
		throws UpdateException, SystemException {
		DBCollection collection = 
			getDB().getCollection(${document.name}Impl.COLLECTION_NAME);
			
		WriteResult writeResult = collection.remove(
			getDBObject(${document.variableName}));
		
		String err = writeResult.getError();
		
		if (err != null) {
			throw new UpdateException(err);
		}
		
		return ${document.variableName};
	}

	protected ${document.name} updateImpl(${package}.model.${document.name} ${document.variableName}) 
		throws UpdateException, SystemException {
		DBCollection collection = 
			getDB().getCollection(${document.name}Impl.COLLECTION_NAME);
		
		if (${document.variableName}.isNew()) {
			${document.variableName}.setNew(false);
			
			${document.name}Impl ${document.variableName}Impl = (${document.name}Impl)${document.variableName};
			
			${document.variableName}Impl.addMap.clear();
			${document.variableName}Impl.appendMap.clear();
			${document.variableName}Impl.removeMap.clear();
			${document.variableName}Impl.setMap.clear();
			
			WriteResult writeResult = collection.insert(getDBObject(${document.variableName}));
			
			String err = writeResult.getError();
		
			if (err != null) {
				throw new UpdateException(err);
			}
		}
		else {			
			DBObject criteria = new QueryBuilder()
				.put("_id")
				.is(new ObjectId(${document.variableName}.getId()))
				.get();

			${document.name}Impl ${document.variableName}Impl = (${document.name}Impl)${document.variableName};
			
			BasicDBObjectBuilder updateBuilder = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder setUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder pushUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder pushAllUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder addUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder removeUpdates = BasicDBObjectBuilder.start();
			BasicDBObjectBuilder removeAllUpdates = BasicDBObjectBuilder.start();

			for (String field : ${document.variableName}Impl.setMap.keySet()) {
				setUpdates = setUpdates.add(field, ${document.variableName}Impl.setMap.get(field));
			}
			
			if (!setUpdates.isEmpty()) {
				updateBuilder.add(SET_OPERATOR, setUpdates.get());
			}
			
			for (String field : ${document.variableName}Impl.appendMap.keySet()) {
				List<Object> list = (List<Object>)${document.variableName}Impl.appendMap.get(field);
				
				if (!list.isEmpty()) {
					if (list.size() == 1) {
						pushUpdates = pushUpdates.add(field, 
							((List)${document.variableName}Impl.appendMap.get(field)).get(0));
					}
					else {
						pushAllUpdates = pushAllUpdates.add(
							field, ${document.variableName}Impl.appendMap.get(field));
					}
				}
			}
			
			if (!pushUpdates.isEmpty()) {
				updateBuilder.add(PUSH_OPERATOR, pushUpdates.get());
			}
	
			if (!pushAllUpdates.isEmpty()) {
				updateBuilder.add(PUSH_ALL_OPERATOR, pushAllUpdates.get());
			}

			for (String field : ${document.variableName}Impl.addMap.keySet()) {
				List<Object> list = (List<Object>)${document.variableName}Impl.addMap.get(field);
				
				if (!list.isEmpty()) {
					if (list.size() == 1) {
						addUpdates = addUpdates.add(field, 
							((List)${document.variableName}Impl.addMap.get(field)).get(0));
					}
					else {
						DBObject each = BasicDBObjectBuilder.start().add(EACH_OPERATOR, 
							((List)${document.variableName}Impl.addMap.get(field)).toArray()).get();
						
						addUpdates = addUpdates.add(field, each);
					}
				}
			}
			
			if (!addUpdates.isEmpty()) {
				updateBuilder.add(ADD_TO_SET_OPERATOR, addUpdates.get());
			}
			
			for (String field : ${document.variableName}Impl.removeMap.keySet()) {
				List<Object> list = (List<Object>)${document.variableName}Impl.removeMap.get(field);
				
				if (!list.isEmpty()) {
					if (list.size() == 1) {
						removeUpdates = removeUpdates.add(field, 
							((List)${document.variableName}Impl.removeMap.get(field)).get(0));
					}
					else {
						removeAllUpdates = removeAllUpdates.add(
							field, ${document.variableName}Impl.removeMap.get(field));
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
		
		return ${document.variableName};
	}
	
	protected ${document.name} getDocument(DBObject dbObject) 
		throws SystemException {
		if (dbObject == null) {
			return null;
		}
		
		${document.name} ${document.variableName} = new ${document.name}Impl(dbObject.toMap());

		return 	${document.variableName};
	}

	protected DBObject getDBObject(${document.name} ${document.variableName}) {
		DBObject dbObject = new BasicDBObject();
	
		if (${document.variableName}.getId() != null) {
			dbObject.put("_id", new ObjectId(${document.variableName}.getId()));
		}
		else {
			ObjectId id = new ObjectId();
			dbObject.put("_id", id);
			${document.variableName}.setId(id.toString());
		}
	
		dbObject.putAll(${document.variableName}.toMap());
			
		return dbObject;
	}

	Logger _log = LoggerFactory.getLogger(${document.name}PersistenceImpl.class);	

}