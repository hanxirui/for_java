package com.hxr.javatone.neo4j.springdata;

import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.GraphProperty;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

@RelationshipEntity(type="link")
public class LinkRelationship {
    @GraphId Long id;
    
    @GraphProperty
    String value;
    
    @StartNode
    DevNode startNode;
    
    @EndNode
    DevNode endNode;

    /**
     * @return id - {return content description}
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id - {parameter description}.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return value - {return content description}
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value - {parameter description}.
     */
    public void setValue(final String value) {
        this.value = value;
    }

    /**
     * @return startNode - {return content description}
     */
    public DevNode getStartNode() {
        return startNode;
    }

    /**
     * @param startNode - {parameter description}.
     */
    public void setStartNode(final DevNode startNode) {
        this.startNode = startNode;
    }

    /**
     * @return endNode - {return content description}
     */
    public DevNode getEndNode() {
        return endNode;
    }

    /**
     * @param endNode - {parameter description}.
     */
    public void setEndNode(final DevNode endNode) {
        this.endNode = endNode;
    }

}
