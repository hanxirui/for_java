package com.ez.framework.core.dam.mybatis;

import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

import com.ez.framework.core.dam.IDam;

public class DamImpl implements IDam {

    /**
     * SQL Log constant.
     */
    private static final String S_DAM_UPDATE = "DamImpl.update( ";

    /**
     * SQL Log constant.
     */
    private static final String S_OBJECT_PARAMETER_END = ", Object parameter ) end , spend time : ";

    /**
     * SQL Log constant.
     */
    private static final String S_OBJECT_PARAMETER_START = ", Object parameter ) start..........";

    /**
     * SQL Log constant.
     */
    private static final String S_DAM_SELECT_LIST_STRING = "DamImpl.selectList(String ";

    /**
     * SQL Log constant.
     */
    private static final String S_DAM_SELECT_STRING = "DamImpl.select(String ";

    /**
     * the string of millisecond.
     */
    private static final String S_MILLISECOND = "ms";

    /**
     * <code>S_LOGGER</code> - Logger Object.
     */
    private static final Log S_LOGGER = LogFactory.getLog(DamImpl.class);

    /**
     * <code>sqlSession</code> - IBatis implements.
     */
    protected SqlSession m_sqlSession;

    /**
     * the ibatis mapper name space.
     */
    private String m_sqlMapperNameSpace;
    
    /**
     * the database type.
     */
    private String m_dbType;
    
    /**
     * the batch size for one transaction.
     */
    private int m_batchSize = DEFAULT_BATCH_SIZE;

    /**
     * 构造函数.
     * 
     * @param sqlSession - the instance of SqlSession.
     */
    public DamImpl(final SqlSession sqlSession) {

        this.m_sqlSession = sqlSession;
    }

    public String getDBType() {

        return m_dbType;
    }
    
    /**
     * the setter for database type.
     * @param dbType - type.
     */
    public void setDBType(final String dbType) {
        this.m_dbType = dbType;
    }

    /**
     * the setter for batch size.
     * @param batchSize - the value.
     */
    public void setBatchSize(final int batchSize) {
        m_batchSize = batchSize;
    }
    
	public int getBatchInsertMaxSize() {
		return m_batchSize;
	}

    public int delete(final String statement, final Object parameter) {

        String t_statement = buildSqlStatement(statement);

        long t_startTime = 0;

        int t_rtn = -1;

        if (S_LOGGER.isDebugEnabled()) {

            S_LOGGER.debug("DamImpl.delete(String " + t_statement + ", Object parameter) start..........");

            t_startTime = System.currentTimeMillis();

        }

            t_rtn = m_sqlSession.delete(t_statement, parameter);

        if (S_LOGGER.isDebugEnabled()) {

            S_LOGGER.debug("DamImpl.delete(String " + t_statement + ", Object parameter) end , spend time : "
                    + (System.currentTimeMillis() - t_startTime) + S_MILLISECOND);

        }
        return t_rtn;
    }

    public int insert(final String statement, final Object parameter) {

        String t_statement = buildSqlStatement(statement);

        long t_startTime = 0;

        int t_rtn = -1;

        if (S_LOGGER.isDebugEnabled()) {

            S_LOGGER.debug("DamImpl.insert(String " + t_statement + ", Object parameter) start..........");

            t_startTime = System.currentTimeMillis();

        }

            t_rtn = m_sqlSession.insert(t_statement, parameter);

        if (S_LOGGER.isDebugEnabled()) {

            S_LOGGER.debug("DamImpl.insert(String " + t_statement + ", Object parameter) end , spend time : "
                    + (System.currentTimeMillis() - t_startTime) + S_MILLISECOND);

        }

        return t_rtn;
    }


    public List<?> selectList(final String statement, final Object parameter){

        String t_statement = buildSqlStatement(statement);

        long t_startTime = 0;

        List<?> t_rtn = null;

        if (S_LOGGER.isDebugEnabled()) {

            S_LOGGER.debug(S_DAM_SELECT_LIST_STRING + t_statement + S_OBJECT_PARAMETER_START);

            t_startTime = System.currentTimeMillis();

        }

            t_rtn = m_sqlSession.selectList(t_statement, parameter);

        if (S_LOGGER.isDebugEnabled()) {

            S_LOGGER.debug(S_DAM_SELECT_LIST_STRING + t_statement + S_OBJECT_PARAMETER_END
                    + (System.currentTimeMillis() - t_startTime) + S_MILLISECOND);

        }

        return t_rtn;
    }

    public Object selectOne(final String statement, final Object parameter) {

        String t_statement = buildSqlStatement(statement);

        long t_startTime = 0;

        Object t_rtn = null;

        if (S_LOGGER.isDebugEnabled()) {

            S_LOGGER.debug("DamImpl.selectOne(String " + t_statement + S_OBJECT_PARAMETER_START);

            t_startTime = System.currentTimeMillis();

        }

            t_rtn = m_sqlSession.selectOne(t_statement, parameter);

        if (S_LOGGER.isDebugEnabled()) {

            S_LOGGER.debug("DamImpl.selectOne(String " + t_statement + S_OBJECT_PARAMETER_END
                    + (System.currentTimeMillis() - t_startTime) + S_MILLISECOND);

        }

        return t_rtn;
    }

    public int update(final String statement, final Object parameter) {

        String t_statement = buildSqlStatement(statement);

        long t_startTime = 0;

        int t_rtn = -1;

        if (S_LOGGER.isDebugEnabled()) {

            S_LOGGER.debug(S_DAM_UPDATE + t_statement + S_OBJECT_PARAMETER_START);

            t_startTime = System.currentTimeMillis();

        }

            t_rtn = m_sqlSession.update(t_statement, parameter);

        if (S_LOGGER.isDebugEnabled()) {
            S_LOGGER.debug(S_DAM_UPDATE + t_statement + S_OBJECT_PARAMETER_END
                    + (System.currentTimeMillis() - t_startTime) + S_MILLISECOND);
        }

        return t_rtn;
    }

    /**
     * get ibatis configuration. 
     * @return - config instance.
     */
    protected Configuration getConfiguration() {

        return m_sqlSession.getConfiguration();
    }

    @SuppressWarnings("rawtypes")
    public int batchInsert(final String statement, final Object parameter) {

        String t_statement = buildSqlStatement(statement);

        if (parameter instanceof Collection) {

            Collection t_params = (Collection) parameter;

            if (S_LOGGER.isDebugEnabled()) {
                S_LOGGER.debug(S_DAM_UPDATE + t_statement + ") start.");
            }
            int t_rtn = -1;

            if (t_params.size() > m_batchSize) {
                S_LOGGER.error("BatchInsert error: batch size is more than " + m_batchSize);
                throw new RuntimeException("Batch size is more than " + m_batchSize);
            }

				for (Object t_param : t_params) {
					t_rtn +=  m_sqlSession.insert(t_statement, t_param);
				}

            if (S_LOGGER.isDebugEnabled()) {
                S_LOGGER.debug(S_DAM_UPDATE + t_statement + ") end. Success insert:" + t_rtn);
            }

            return t_rtn;
        } else {
            throw new RuntimeException("Please make sure parameter [ids] is a collect object.");
        }
    }

    @SuppressWarnings("rawtypes")
    public int batchDeleteByIds(final String statement, final Object ids){

        String t_statement = buildSqlStatement(statement);

        if (ids instanceof Collection) {

            Collection t_ids = (Collection) ids;

            if (S_LOGGER.isDebugEnabled()) {
                S_LOGGER.debug("DamImpl.batchDeleteByIds( " + t_statement + ") start.");
            }
            int t_rtn = -1;

            if (t_ids.size() > m_batchSize) {
                S_LOGGER.error("BatchInsert error: batch size is more than " + m_batchSize);
                throw new RuntimeException("Batch size is more than " + m_batchSize);
            }

                t_rtn = m_sqlSession.delete(t_statement, ids);

            if (S_LOGGER.isDebugEnabled()) {
                S_LOGGER.debug("DamImpl.batchDeleteByIds( " + t_statement + ") end. Success delete:" + t_rtn);
            }

            return t_rtn;

        } else {
            throw new RuntimeException("Please make sure parameter [ids] is a collect object.");
        }

    }


    /**
     * the setter for ibatis's mapper name space.
     * @param sqlMapperNameSpace - name space.
     */
    public void setSqlMapperNameSpace(final String sqlMapperNameSpace) {
		m_sqlMapperNameSpace = sqlMapperNameSpace;
    }

    /**
     * add name space. 
     * @param statement - ibatis sql mapper  id.
     * @return sql.
     */
    private String buildSqlStatement(final String statement) {
        if (m_sqlMapperNameSpace != null && m_sqlMapperNameSpace.length() > 0) {
            return m_sqlMapperNameSpace + "." + statement;
        }
        return statement;
    }

}
