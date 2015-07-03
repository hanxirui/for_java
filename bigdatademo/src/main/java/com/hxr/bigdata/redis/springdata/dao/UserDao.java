package com.hxr.bigdata.redis.springdata.dao;

import com.hxr.bigdata.redis.springdata.vo.UserVo;

public interface UserDao {
  public  UserVo getUserVo(final long id);
  public  void saveUserVo(final UserVo UserVo);
}
