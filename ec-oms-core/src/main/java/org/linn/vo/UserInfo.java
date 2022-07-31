package org.linn.vo;

/**
 * 登陆用户信息
 */
public class UserInfo {

	/**
	 * 数据库中用户id
	 */
	private Long id;

	/**
	 * 数据库中用户名
	 */
	private String username;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
