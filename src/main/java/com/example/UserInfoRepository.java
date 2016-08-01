package com.example;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {
	public List<UserInfo> findByIdAndPassword(int id ,String password);
	public List<UserInfo> findById(int id);
}
