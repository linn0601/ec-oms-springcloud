package org.linn.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "user_",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = { "username" })
	}
)
@EntityListeners({ AuditingEntityListener.class }) // 操作User时时启动监听
public class User implements Serializable {

	@Id
	@GeneratedValue(
		generator = "user_id_generator"
	)
	@SequenceGenerator(
		name = "user_id_generator",
		sequenceName = "public.user_sequence",
		allocationSize = 10
	)
	@Column(nullable = false)
	private Long id;

	private String username;

	private String password;

	private String extraInfo;

	@CreatedDate
	private LocalDateTime dateCreated;

	@LastModifiedDate
	private LocalDateTime dateUpdated;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getDateUpdated() {
		return dateUpdated;
	}

	public void setDateUpdated(LocalDateTime dateUpdated) {
		this.dateUpdated = dateUpdated;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
}
