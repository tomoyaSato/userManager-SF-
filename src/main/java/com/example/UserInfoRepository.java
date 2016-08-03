package com.example;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo,Integer> {
	public List<UserInfo> findByIdAndPasswordAndDeleteFlg(int id ,String password,boolean deleteFlg);
	public List<UserInfo> findById(int id);
	public List<UserInfo> findByName(String name);
	public List<UserInfo> findByDeleteFlg(boolean deleteFlg);
}
