package com.hxr.javatone.algorithms.toposearch;

public class Link {
private String Id;
private String srcId;
private String destId;
/**
 * @return id - {return content description}
 */
public String getId() {
    return Id;
}
/**
 * @param id - {parameter description}.
 */
public void setId(final String id) {
    Id = id;
}
/**
 * @return srcId - {return content description}
 */
public String getSrcId() {
    return srcId;
}
/**
 * @param srcId - {parameter description}.
 */
public void setSrcId(final String srcId) {
    this.srcId = srcId;
}
/**
 * @return destId - {return content description}
 */
public String getDestId() {
    return destId;
}
/**
 * @param destId - {parameter description}.
 */
public void setDestId(final String destId) {
    this.destId = destId;
}
}
