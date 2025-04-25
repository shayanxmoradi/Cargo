package org.example.cargo.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Table(name = User.TABLE_NAME)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "user_type",discriminatorType = DiscriminatorType.STRING)
public class User {
    public static final String TABLE_NAME = "user";
}
