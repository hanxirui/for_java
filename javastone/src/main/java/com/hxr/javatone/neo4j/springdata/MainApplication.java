package com.hxr.javatone.neo4j.springdata;

import java.util.Iterator;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.transaction.annotation.Transactional;


public class MainApplication {
    public static void main(final String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-data-context.xml");     
        DevNodeService service = (DevNodeService) context.getBean("DevNodeService");

        // Please uncomment one of the operation section 
        // and comment remaining section to test only one operation at a time
        // Here I've uncommented CREATE operation and 
        // commented other operations: FIND ONE, FIND ALL, DELETE
        
        
        // CREATE Operation
        DevNode devNode = createDevNode();
        createDevNode(service,devNode);       
        System.out.println("DevNode created successfully.");

        // FIND ONE 
        /*
        DevNode devNode = getOneDevNodeById(service,67515L);        
        System.out.println(devNode);
        */

        // FIND ALL
        
        getAllDevNodes(service);      
        

        // DELETE 
        /*
        DevNode devNode = createDevNode();
        deleteDevNode(service,devNode);       
        System.out.println("DevNode deleted successfully.");        
        */
     }
     
     private static DevNode createDevNode(final DevNodeService service, final DevNode devNode){
        return service.create(devNode);
     }    
     
     private static void deleteDevNode(final DevNodeService service,final DevNode devNode){
        service.delete(devNode);
     }    
     
     private static DevNode getOneDevNodeById(final DevNodeService service,final Long id){
        return service.findById(id);
     }    
     
     @Transactional
     private static void getAllDevNodes(final DevNodeService service){
        Result<DevNode> result = service.findAll();         
        Iterator<DevNode> iterator = result.iterator();
        
        while(iterator.hasNext()){
           System.out.println(iterator.next());
        }
     }
     
     private static DevNode createDevNode(){
        DevNode devNode = new DevNode();      
        devNode.setName("DevNode-2");
   
        return devNode;
     }
}
