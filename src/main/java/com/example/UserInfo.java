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
    protected boolean deleteFlg;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Timestamp getCreate_timestamp() {
		return create_timestamp;
	}

	public void setCreate_timestamp(Timestamp create_timestamp) {
		this.create_timestamp = create_timestamp;
	}

	public Timestamp getUpdate_timestamp() {
		return update_timestamp;
	}

	public void setUpdate_timestamp(Timestamp update_timestamp) {
		this.update_timestamp = update_timestamp;
	}

	public boolean isDeleteFlg() {
		return deleteFlg;
	}

	public void setDelete_flg(boolean deleteFlg) {
		this.deleteFlg = deleteFlg;
	}

    public UserInfo() {
    	super();
    }

      public UserInfo(String password,String name, Timestamp create_timestamp,Timestamp update_timestamp,boolean deleteFlg) {
        super();
        this.password = password;
        this.name = name;
        this.create_timestamp = create_timestamp;
        this.update_timestamp = update_timestamp;
        this.deleteFlg = deleteFlg;
      }

      // for debug
      public String toString() {
        return "[id:" + id + ", password:" + password + ", name:" + name + "]";
      }


}
