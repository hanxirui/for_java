package com.hxr.javatone.neo4j.springdata;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class DevNode {

    @GraphId
    Long id;

    @Indexed
    String name;

    @RelatedTo(type = "link", direction = Direction.INCOMING)
    @Fetch
    Set<DevNode> links = new HashSet<DevNode>();

    @RelatedTo(type = "link", direction = Direction.OUTGOING)
    @Fetch
    Set<DevNode> linked = new HashSet<DevNode>();

    /**
     * @return id - {return content description}
     */
    public Long getId() {
        return 123L;
    }

    /**
     * @param id - {parameter description}.
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return name - {return content description}
     */
    public String getName() {
        return name;
    }

    /**
     * @param name - {parameter description}.
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return links - {return content description}
     */
    public Set<DevNode> getLinks() {
        return links;
    }

    /**
     * @param links - {parameter description}.
     */
    public void setLinks(final Set<DevNode> links) {
        this.links = links;
    }

    /**
     * @return linked - {return content description}
     */
    public Set<DevNode> getLinked() {
        return linked;
    }

    /**
     * @param linked - {parameter description}.
     */
    public void setLinked(final Set<DevNode> linked) {
        this.linked = linked;
    }

}
