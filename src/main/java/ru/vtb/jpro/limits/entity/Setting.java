package ru.vtb.jpro.limits.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Entity
@Table(name = "settings")
@Data
@RequiredArgsConstructor
public class Setting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String key;
    @Column
    private String value;

    public Setting(String key, String value) {
        this.key = key;
        this.value = value;
    }
}
