package ru.vtb.jpro.limits;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import ru.vtb.jpro.limits.dto.LimitDTO;
import ru.vtb.jpro.limits.dto.RequestDTO;
import ru.vtb.jpro.limits.dto.ResponseDTO;
import ru.vtb.jpro.limits.entity.Limit;
import ru.vtb.jpro.limits.repository.LimitRepository;
import ru.vtb.jpro.limits.service.LimitService;


@SpringBootTest
@AutoConfigureMockMvc
class RESTTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    LimitRepository limitRepository;
    @Autowired
    LimitService limitService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    EntityManager entityManager;

    @Test
    @Transactional
    @DisplayName("Проверка создания лимитов для нового пользователя")
    void testLimitsForNewClient() throws Exception {
        Long newClientId = 1 + limitRepository.getMaxClientId()
                                   .orElse(0L);
        BigDecimal dailyLimit = limitService.getDefaultDailyLimit();
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/limits/v1/" + newClientId))
                               .andExpect(MockMvcResultMatchers.status()
                                              .isOk())
                               .andReturn();
        LimitDTO limitDTO = objectMapper.readValue(result.getResponse()
                                                       .getContentAsString(), LimitDTO.class);
        Assertions.assertEquals(0, limitDTO.dailyLimit()
                                       .compareTo(dailyLimit));
        Assertions.assertEquals(0, limitDTO.currentLimit()
                                       .compareTo(dailyLimit));
        Assertions.assertEquals(new BigDecimal(0), limitDTO.reservedSum());
    }

    @Test
    @Transactional
    @DisplayName("Проверка получения лимитов существующего пользователя")
    void testLimitsForExistsClient() throws Exception {
        Long clientId = limitRepository.getMaxClientId()
                            .orElseThrow(() -> new RuntimeException("В БД лимитов нет записей"));
        Limit limit = limitRepository.findById(clientId)
                          .get();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/limits/v1/" + clientId))
                               .andExpect(MockMvcResultMatchers.status().isOk())
                               .andReturn();
        LimitDTO limitDTO = objectMapper.readValue(result.getResponse()
                                                       .getContentAsString(), LimitDTO.class);
        Assertions.assertEquals(limit.getDailyLimit(), limitDTO.dailyLimit());
        Assertions.assertEquals(limit.getCurrentLimit(), limitDTO.currentLimit());
        Assertions.assertEquals(limit.getReservedSum(), limitDTO.reservedSum());
    }

    @Test
    @Transactional
    @DisplayName("Проверка успешного резервирования")
    void checkSuccessReserveSum() throws Exception {
        Long clientId = limitRepository.getMaxClientId()
                            .orElseThrow(() -> new RuntimeException("В БД лимитов нет записей"));
        Limit limit = limitRepository.findById(clientId)
                          .get();
        if (limit.getCurrentLimit()
                .compareTo(new BigDecimal(0)) < 1) {
            throw new RuntimeException(
                "Нет доступного лимита для пользователя %s, проверка невозможна".formatted(clientId));
        }

        RequestDTO requestDTO = new RequestDTO(limit.getCurrentLimit());

        //Запрос на резервирование
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/limits/v1/" + clientId)
                                               .content(objectMapper.writeValueAsString(requestDTO))
                                               .contentType(MediaType.APPLICATION_JSON))
                               .andExpect(MockMvcResultMatchers.status().isOk())
                               .andReturn();

        ResponseDTO responseDTO = objectMapper.readValue(result.getResponse()
                                                             .getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals(HttpStatus.OK, responseDTO.status());
        Assertions.assertNull(responseDTO.message());
        Assertions.assertTrue(responseDTO.result());

        //Проверка установки резерва
        result = mockMvc.perform(MockMvcRequestBuilders.get("/limits/v1/" + clientId))
                     .andExpect(MockMvcResultMatchers.status()
                                    .isOk())
                     .andReturn();

        LimitDTO limitDTO = objectMapper.readValue(result.getResponse()
                                                       .getContentAsString(), LimitDTO.class);
        Assertions.assertEquals(limit.getDailyLimit(), limitDTO.dailyLimit());
        Assertions.assertEquals(0, limitDTO.currentLimit()
                                       .compareTo(new BigDecimal(0)));
        Assertions.assertEquals(limit.getDailyLimit(), limitDTO.reservedSum());
    }

    @Test
    @Transactional
    @DisplayName("Проверка резервирования суммы сверх лимита")
    void checkFailReserveSum() throws Exception {
        Long clientId = limitRepository.getMaxClientId()
                            .orElseThrow(() -> new RuntimeException("В БД лимитов нет записей"));
        Limit limit = limitRepository.findById(clientId).get();
        if (limit.getCurrentLimit()
                .compareTo(new BigDecimal(0)) < 1) {
            throw new RuntimeException(
                "Нет доступного лимита для пользователя %s, проверка невозможна".formatted(clientId));
        }

        RequestDTO requestDTO = new RequestDTO(limit.getCurrentLimit().add(new BigDecimal(1)));

        //Запрос на резервирование
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/limits/v1/" + clientId)
                                               .content(objectMapper.writeValueAsString(requestDTO))
                                               .contentType(MediaType.APPLICATION_JSON))
                               .andExpect(MockMvcResultMatchers.status().isOk())
                               .andReturn();
        ResponseDTO responseDTO = objectMapper.readValue(result.getResponse()
                                                             .getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseDTO.status());
        Assertions.assertNotNull(responseDTO.message());
        Assertions.assertFalse(responseDTO.result());
    }

    @Test
    @Transactional
    @DisplayName("Проверка списания зарезервированной суммы")
    void checkApplyReservedSum() throws Exception {
        //Резервируем сумму
        checkSuccessReserveSum();

        Long clientId = limitRepository.getMaxClientId()
                            .orElseThrow(() -> new RuntimeException("В БД лимитов нет записей"));
        Limit limit = limitRepository.findById(clientId).get();

        RequestDTO requestDTO = new RequestDTO(limit.getReservedSum());

        //Запрос на резервирование
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/limits/v1/" + clientId)
                                               .content(objectMapper.writeValueAsString(requestDTO))
                                               .contentType(MediaType.APPLICATION_JSON))
                               .andExpect(MockMvcResultMatchers.status().isOk())
                               .andReturn();

        ResponseDTO responseDTO = objectMapper.readValue(result.getResponse()
                                                             .getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals(HttpStatus.OK, responseDTO.status());
        Assertions.assertNull(responseDTO.message());
        Assertions.assertTrue(responseDTO.result());

        //Проверка установки резерва
        result = mockMvc.perform(MockMvcRequestBuilders.get("/limits/v1/" + clientId))
                     .andExpect(MockMvcResultMatchers.status().isOk())
                     .andReturn();

        LimitDTO limitDTO = objectMapper.readValue(result.getResponse().getContentAsString(), LimitDTO.class);
        Assertions.assertEquals(limit.getDailyLimit(), limitDTO.dailyLimit());
        Assertions.assertEquals(0, limitDTO.currentLimit()
                                       .compareTo(new BigDecimal(0)));
        Assertions.assertEquals(0, limitDTO.reservedSum()
                                       .compareTo(new BigDecimal(0)));
    }

    @Test
    @Transactional
    @DisplayName("Проверка отмены зарезервированной суммы")
    void checkRevertReservedSum() throws Exception {
        //Резервируем сумму
        checkSuccessReserveSum();

        Long clientId = limitRepository.getMaxClientId()
                            .orElseThrow(() -> new RuntimeException("В БД лимитов нет записей"));
        Limit limit = limitRepository.findById(clientId).get();

        RequestDTO requestDTO = new RequestDTO(limit.getReservedSum());

        //Запрос на отмену резервирования
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/limits/v1/" + clientId)
                                               .content(objectMapper.writeValueAsString(requestDTO))
                                               .contentType(MediaType.APPLICATION_JSON))
                               .andExpect(MockMvcResultMatchers.status().isOk())
                               .andReturn();

        ResponseDTO responseDTO = objectMapper.readValue(result.getResponse().getContentAsString(), ResponseDTO.class);
        Assertions.assertEquals(HttpStatus.OK, responseDTO.status());
        Assertions.assertNull(responseDTO.message());
        Assertions.assertTrue(responseDTO.result());

        //Проверка установки резерва
        result = mockMvc.perform(MockMvcRequestBuilders.get("/limits/v1/" + clientId))
                     .andExpect(MockMvcResultMatchers.status().isOk())
                     .andReturn();

        LimitDTO limitDTO = objectMapper.readValue(result.getResponse().getContentAsString(), LimitDTO.class);
        Assertions.assertEquals(limit.getDailyLimit(), limitDTO.dailyLimit());
        Assertions.assertEquals(limit.getDailyLimit(), limitDTO.currentLimit());
        Assertions.assertEquals(0, limitDTO.reservedSum()
                                       .compareTo(new BigDecimal(0)));
    }
}
