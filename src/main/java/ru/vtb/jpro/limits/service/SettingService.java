package ru.vtb.jpro.limits.service;


import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.jpro.limits.entity.Setting;
import ru.vtb.jpro.limits.repository.SettingRepository;


@Service
public class SettingService {

    public static final String DEFAULT_DAILY_LIMIT = "DEFAULT_DAILY_LIMIT";

    private final SettingRepository settingRepository;

    @Value("${limits.defaultDailyLimit:100000}")
    private BigDecimal defaultDailyLimit;

    public SettingService(SettingRepository settingRepository) {
        this.settingRepository = settingRepository;
    }

    @Transactional
    public BigDecimal getDefaultDailyLimit() {
        Setting setting = settingRepository.getSettingByKey(DEFAULT_DAILY_LIMIT)
                              .orElseGet(() -> {
                                  Setting newSetting = new Setting(DEFAULT_DAILY_LIMIT, defaultDailyLimit.toString());
                                  settingRepository.save(newSetting);
                                  return newSetting;
                              });
        return new BigDecimal(setting.getValue());
    }

    @Transactional
    public void setDefaultDailyLimit(BigDecimal newLimit) {
        Setting newSetting = settingRepository.getSettingByKey(DEFAULT_DAILY_LIMIT)
                                 .orElseGet(() -> new Setting(DEFAULT_DAILY_LIMIT, newLimit.toString()));
        newSetting.setValue(newLimit.toString());
        settingRepository.save(newSetting);
    }
}
