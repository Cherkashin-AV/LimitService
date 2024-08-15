package ru.vtb.jpro.limits.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.vtb.jpro.limits.entity.Setting;

@Repository
public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> getSettingByKey(String key);
}
