package com.ez.framework.core.dam.mybatis;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;

import com.ez.framework.core.dam.DBType;
import com.ez.framework.core.dam.DataSource;
import com.ez.framework.core.dam.IDam;
import com.ez.framework.core.service.ServiceContainer;
import com.ez.framework.core.service.ServiceContainerException;

public class DAMFactory {

    /**
     * the been name in Spring beans configuration.
     */
    public static final String S_NAME = "ibatisDAMFactory";
    
    private Map<DataSource, SqlSessionFactory> m_targetDataSources = new HashMap<DataSource, SqlSessionFactory>(2);
    
    /**
     * the database type.
     * @see DBType
     */
    private String dbType;
    /**
     * the batch size.
     */
    private int m_batchSize;
    
    /**
     * load ibatis mappers.
     * which been called when Spring first create instance.  
     * @throws IOException 
     */
    public void initialize() throws IOException {

		if (m_targetDataSources == null || m_targetDataSources.values() == null
				|| m_targetDataSources.values().size() == 0) {
			throw new RuntimeException("Can not find data sources in Spring Context.");
		}
        
        
        for (SqlSessionFactory t_sqlSessionFactory : m_targetDataSources.values()) {
        	SQLMapperXMLGetter.buildMapper(dbType, t_sqlSessionFactory.getConfiguration());
		}
    }

    public void setTargetDataSources(Map<DataSource, SqlSessionFactory> targetDataSources) {
		m_targetDataSources = targetDataSources;
	}


    public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	/**
     * the getter for batch size.
     * @return - database batch size.
     */
    public int getBatchSize() {
        return m_batchSize;
    }
    public void setBatchSize(int batchSize){
    	this.m_batchSize = batchSize;
    }
    /**
     * get DAM instance. 
     * @return - DAM instance.
     * @throws DBException when load ibatis mapper error. 
     */
    public IDam getDAM(final DataSource dataSource, final String clazz){

        return getDAM(dataSource, ExecutorType.SIMPLE, clazz);
    }

    /**
     * get DAM instance.
     * @param type - SIMPLE, REUSE, BATCH, @see ExecutorType
     * @param clazz DAO's name space.
     * @return - DAM instance.
     * @throws DBException when load ibatis mapper error. 
     */
    public IDam getDAM(final DataSource dataSource, final ExecutorType type, final String clazz){
        
		try {
			SqlSessionFactory t_factory = getDatasource(dataSource);
			
	        DamImpl t_ret = new DamImpl(new SqlSessionTemplate(t_factory, type)); 
	        t_ret.setSqlMapperNameSpace(clazz);
	        t_ret.setBatchSize(m_batchSize);
	        t_ret.setDBType(dbType);

	        return t_ret;
			
		} catch (ServiceContainerException t_e) {
			throw new RuntimeException(t_e.getMessage(), t_e);
		} catch (IOException t_e) {
			throw new RuntimeException(t_e.getMessage(), t_e);
		}
    }
    
    private synchronized SqlSessionFactory getDatasource(final DataSource dataSource) throws ServiceContainerException, IOException {
    	SqlSessionFactory t_factory = m_targetDataSources.get(dataSource);
    	if (t_factory == null) {
    		t_factory = ServiceContainer.getDatabaseComponent(dataSource.toString());
    		SQLMapperXMLGetter.buildMapper(dbType, t_factory.getConfiguration());
    		m_targetDataSources.put(dataSource, t_factory);
    	}

    	return t_factory;
    }

}
