package com.hxr.bigdata.redis.springdata.dao.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;

import com.hxr.bigdata.redis.springdata.dao.UserDao;
import com.hxr.bigdata.redis.springdata.vo.UserVo;

public class UserDaoImpl implements UserDao {
    @Autowired
    protected RedisTemplate<Serializable, Serializable> redisTemplate;


    public void saveUserVo(final UserVo UserVo) {
        redisTemplate.execute(new RedisCallback<Object>() {

 
            public Object doInRedis(final RedisConnection connection) throws DataAccessException {
                connection.set(redisTemplate.getStringSerializer().serialize("UserVo.uid." + UserVo.getId()),
                               redisTemplate.getStringSerializer().serialize(UserVo.getName()));
                return null;
            }
        });
    }


    public UserVo getUserVo(final long id) {
        return redisTemplate.execute(new RedisCallback<UserVo>() {
         
            public UserVo doInRedis(final RedisConnection connection) throws DataAccessException {
                byte[] key = redisTemplate.getStringSerializer().serialize("UserVo.uid." + id);
                if (connection.exists(key)) {
                    byte[] value = connection.get(key);
                    String name = redisTemplate.getStringSerializer().deserialize(value);
                    UserVo UserVo = new UserVo();
                    UserVo.setName(name);
                    UserVo.setId(id);
                    return UserVo;
                }
                return null;
            }
        });
    }
}
