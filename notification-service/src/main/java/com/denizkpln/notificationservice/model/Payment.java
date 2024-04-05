package com.denizkpln.notificationservice.model;

import jakarta.persistence.*;
import lombok.*;


@Builder
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    private Long id;
    private String userName;
    private Integer money;
}