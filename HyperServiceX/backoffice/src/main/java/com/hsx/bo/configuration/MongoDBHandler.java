//package com.hsx.bo.configuration;
//
//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;
//
//import javax.annotation.PostConstruct;
//
///**
// * @author Nadith
// */
//public class MongoDBHandler {
//
//    private MongoClient mongoClient;
//    private MongoClientURI uri;
//    private MongoDatabase database;
//
//    @PostConstruct
//    public void initCacheConfig() {
//        uri = new MongoClientURI("mongodb+srv://nadith:HyperSX@123@cluster0-kkrir.mongodb.net/sample_airbnb?retryWrites=true&w=majority");
//        mongoClient = new MongoClient(uri);
//        database = mongoClient.getDatabase("sample_airbnb");
//    }
//
//    private MongoDatabase getInstance() {
//        return database;
//    }
//
//    public long getNextValOfSeq(String seq) {
//        return 0;
//    }
//
//    public void createCollection(String collectionName) {
//        database.createCollection(collectionName, null);
//    }
//
//    public <V> MongoCollection<V> getCollection(String collectionName, Class<V> requestType) {
//        return database.getCollection(collectionName, requestType);
//    }
//
//}
