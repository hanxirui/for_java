package permission.service;

import org.durcframework.core.service.CrudService;
import org.springframework.stereotype.Service;

import permission.dao.RDataTypeDao;
import permission.entity.RDataType;

@Service
public class RDataTypeService extends CrudService<RDataType, RDataTypeDao> {

}
