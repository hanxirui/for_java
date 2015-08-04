package com.hxr.javatone.neo4j.springdata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.conversion.Result;
import org.springframework.stereotype.Service;


@Service("DevNodeService")
//@Transactional
//@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
public class DevNodeService {

   @Autowired
   private DevNodeRepository devNodeRepository; 

   public DevNode create(final DevNode devNode) {
      return devNodeRepository.save(devNode);
   }

   public void delete(final DevNode devNode) {      
      devNodeRepository.delete(devNode);
   }

   public DevNode findById(final long id) {     
      return devNodeRepository.findOne(id);
   }

   public Result<DevNode> findAll() {     
      return devNodeRepository.findAll();
   }
}
