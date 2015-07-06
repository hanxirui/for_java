package com.hxr.bigdata.redis.springdata.dao;

import com.hxr.bigdata.redis.springdata.vo.UserVo;

public interface UserDao {
  /**
 * {method description}.
 * @param id
 * @return
 */
    UserVo getUserVo(final long id);
  /**
 * {method description}.
 * @param UserVo
 */
  void saveUserVo(final UserVo UserVo);
}
