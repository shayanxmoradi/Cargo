package org.example.cargo.dto.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.example.cargo.dto.BaseEntity;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Table(name = BaseUser.TABLE_NAME)
//@Data // dont use when you need custom getter setters
@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class BaseUser extends BaseEntity<Long> {
    public static final String TABLE_NAME = "base_user";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String IS_ACTIVE = "is_active";

    @Column(name = FIRST_NAME, nullable = false)
    @Length(min = 3, max = 30, message = "should be at least 3 and max 30 characters")
    private String firstName;

    @Column(name = LAST_NAME, nullable = false)
    @Length(min = 3, max = 30, message = "should be at least 3 and max 30 characters")
    private String lastName;
    @Column(nullable = false, name = EMAIL, unique = true)
    @Email
    private String email;

    @Column(nullable = false,name = PASSWORD)
    private String password;

    @Column(nullable = false,name= IS_ACTIVE)
    Boolean isActive = true;
    @Column
    private LocalDateTime registrationDate;

    @PrePersist
    public void prePersist() {
        if (this.registrationDate == null) {
            this.registrationDate = LocalDateTime.now();
        }

    }


}
