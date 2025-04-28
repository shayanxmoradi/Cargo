package org.example.cargo.domain.user;

import jakarta.persistence.*;
import lombok.*;
import org.example.cargo.domain.BaseEntity;
import org.hibernate.validator.constraints.Length;

@Table(name = User.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type",discriminatorType = DiscriminatorType.STRING)
@Entity
public class User extends BaseUser {
    public static final String TABLE_NAME = "\"user\"";
    private static final String USER_NAME = "user_name";

    @Column(nullable = false,name=USER_NAME)
    @Length(min = 3, max = 30, message = "should be at least 3 and max 30 characters")
    private String username;

}



