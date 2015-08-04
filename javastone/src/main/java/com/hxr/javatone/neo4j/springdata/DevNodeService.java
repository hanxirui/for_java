package com.hxr.javatone.neo4j.springdata;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("DevNodeService")
@Transactional
public class DevNodeService {

   @Autowired
   private DevNodeRepository devNodeRepository; 
   

   public DevNode create(final DevNode devNode) {
      return devNodeRepository.save(devNode);
   }

   public void delete(final DevNode devNode) {     
       DevNode t_devNode =  devNodeRepository.findBySchemaPropertyValue("name", devNode.getName());
       System.out.println("---"+t_devNode);
      devNodeRepository.delete(t_devNode);
   }

   public DevNode findById(final long id) {     
      return devNodeRepository.findOne(id);
   }

   public Result<DevNode> findAll() {     
       Result<DevNode> result = devNodeRepository.findAll();         
       Iterator<DevNode> iterator = result.iterator();
       

       while(iterator.hasNext()){
          System.out.println(iterator.next());
       }
      return result;
   }
}
