package com.hxr.javatone.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class StartNeo4j {

    static GraphDatabaseService graphDb = null;
    static Node firstNode;
    static Node secondNode;
    static Relationship relationship;

    private static enum RelTypes implements RelationshipType {
        KNOWS
    }

    @SuppressWarnings("deprecation")
    public static void main(final String[] args) {
        String DB_PATH = "neo4j-sayhello";
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(DB_PATH);
        Transaction tx = graphDb.beginTx();

        try {
            firstNode = graphDb.createNode();
            firstNode.setProperty("message", "Hello, ");
            secondNode = graphDb.createNode();
            secondNode.setProperty("message", "World!");

            relationship = firstNode.createRelationshipTo(secondNode, RelTypes.KNOWS);
            relationship.setProperty("message", "brave Neo4j ");

            System.out.print(firstNode.getProperty("message"));
            System.out.print(relationship.getProperty("message"));
            System.out.print(secondNode.getProperty("message"));
            // Updating operations go here
            tx.success();
        } finally {
            registerShutdownHook(graphDb);
            tx.finish();
        }

    }

    private static void registerShutdownHook(final GraphDatabaseService graphDb) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

}
