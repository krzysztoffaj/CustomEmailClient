package com.app.repositories.dbrepositories;

import com.app.models.User;
import com.app.repositories.UserRepository;

//@Repository("dbUserRepository")
public class DbUserRepository extends DbGenericRepository<User> implements UserRepository {

}
