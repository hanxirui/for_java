package com.hxr.javatone.neo4j.springdata;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

@EnableTransactionManagement
public class MainApplication {
    private static int increase = 0;
    public static void main(final String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-data-context.xml");
        DevNodeService service = (DevNodeService) context.getBean("DevNodeService");

        // Please uncomment one of the operation section
        // and comment remaining section to test only one operation at a time
        // Here I've uncommented CREATE operation and
        // commented other operations: FIND ONE, FIND ALL, DELETE

        // CREATE Operation
        DevNode devNode = createDevNode();
        createDevNode(service, devNode);
        System.out.println("DevNode created successfully.");

        // FIND ONE

        // DevNode devNode1 = getOneDevNodeById(service,67515L);
        // System.out.println(devNode1);

        // DELETE

        DevNode devNode2 = createDevNode();
        devNode2.setName("DevNode-21");
        deleteDevNode(service, devNode2);
        System.out.println("DevNode deleted successfully.");

        // FIND ALL

        getAllDevNodes(service);
    }

    private static DevNode createDevNode(final DevNodeService service, final DevNode devNode) {
        return service.create(devNode);
    }

    private static void deleteDevNode(final DevNodeService service, final DevNode devNode) {
        service.delete(devNode);
    }

    private static DevNode getOneDevNodeById(final DevNodeService service, final Long id) {
        return service.findById(id);
    }

    @Transactional
    private static void getAllDevNodes(final DevNodeService service) {
        Result<DevNode> result = service.findAll();

    }

    private static DevNode createDevNode() {
        DevNode devNode = new DevNode();
        devNode.setName("DevNode-2"+ ++increase);

        return devNode;
    }
}
