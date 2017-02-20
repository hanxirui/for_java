package permission.service;

import org.durcframework.core.service.CrudService;
import org.springframework.stereotype.Service;

import permission.dao.RDataObjDao;
import permission.entity.RDataObj;

@Service
public class RDataObjService extends CrudService<RDataObj, RDataObjDao> {

}
