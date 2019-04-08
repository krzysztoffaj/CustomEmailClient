package com.app.repository.dbrepository;

import com.app.common.User;
import com.app.repository.UserRepository;
import org.springframework.stereotype.Repository;

//@Repository("dbUserRepository")
public class DbUserRepository extends DbGenericRepository<User> implements UserRepository {

}
