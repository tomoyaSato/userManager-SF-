package com.example;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="user_info")
public class UserInfo {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int id;

	@Size(max = 32)
	@Column(name="password")
    protected String password;

	@Size(max = 32)
	@Column(name="name")
    protected String name;


    @Column(name="create_timestamp")
    protected Timestamp create_timestamp;


    @Column(name="update_timestamp")
    protected Timestamp update_timestamp;

	@Column(name="delete_flg")
    protected boolean delete_flg;

    public UserInfo() {
    	super();
    }

      public UserInfo(String password,String name, Timestamp create_timestamp,Timestamp update_timestamp,boolean delete_flg) {
        super();
        this.password = password;
        this.name = name;
        this.create_timestamp = create_timestamp;
        this.update_timestamp = update_timestamp;
        this.delete_flg = delete_flg;
      }

      // for debug
      public String toString() {
        return "[id:" + id + ", password:" + password + ", name:" + name + "]";
      }
}
