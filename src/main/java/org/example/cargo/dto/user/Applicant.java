package org.example.cargo.dto.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = Applicant.TABLE_NAME)

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
@DiscriminatorColumn(name = "applicant_type",discriminatorType = DiscriminatorType.STRING)
public class Applicant extends BaseUser{
    public static final String TABLE_NAME = "applicant";
}
