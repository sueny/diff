package com.ssm.diff;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssm.diff.domain.EncodedData;
import com.ssm.diff.domain.LeftData;
import com.ssm.diff.domain.RightData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = DiffApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
public class DiffDataControllerIT {

    private static final String V1_DIFF_LEFT = "/v1/diff/{id}/left";
    private static final String V1_DIFF_RIGHT = "/v1/diff/{id}/right";
    private static final String V1_DIFF = "/v1/diff/{id}";
    private static final String ENCODED_DATA1 = "ABC";
    private static final String ENCODED_DATA2 = "ABCD";
    private static final String ENCODED_DATA_LEFT = "XXBCDX";
    private static final String ENCODED_DATA_RIGHT = "WBCDWW";
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenValidLeftData_thenSaveIt() throws Exception {
        LeftData validLeftData = new LeftData(null, ENCODED_DATA1);

        mvc.perform(post(V1_DIFF_LEFT, 1).contentType(APPLICATION_JSON)
                .content(toJson(validLeftData))
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenInvalidLeftData_thenThrowBadRequest() throws Exception {
        LeftData invalidLeftData = new LeftData();

        mvc.perform(post(V1_DIFF_LEFT, 2).contentType(APPLICATION_JSON)
                .content(toJson(invalidLeftData))
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenValidRightData_thenSaveIt() throws Exception {
        RightData validRightData = new RightData(null, ENCODED_DATA1);

        mvc.perform(post(V1_DIFF_RIGHT, 1).contentType(APPLICATION_JSON)
                .content(toJson(validRightData))
                .accept(APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void whenInvalidRightData_thenThrowBadRequest() throws Exception {
        RightData invalidRightData = new RightData();

        mvc.perform(post(V1_DIFF_RIGHT, 2).contentType(APPLICATION_JSON)
                .content(toJson(invalidRightData))
                .accept(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenLeftAndRightData_thenGetDiff() throws Exception {
        long id = 3;
        LeftData leftData = new LeftData(id, ENCODED_DATA1);
        RightData rightData = new RightData(id, ENCODED_DATA1);
        mvc.perform(post(V1_DIFF_LEFT, id).contentType(APPLICATION_JSON).content(toJson(leftData)));
        mvc.perform(post(V1_DIFF_RIGHT, id).contentType(APPLICATION_JSON).content(toJson(rightData)));

        mvc.perform(get(V1_DIFF, id).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.equal").value(Boolean.TRUE));
    }

    @Test
    public void whenLeftAndRightDataWithDifferentSize_thenGetDiff() throws Exception {
        long id = 4;
        LeftData leftData = new LeftData(id, ENCODED_DATA1);
        RightData rightData = new RightData(id, ENCODED_DATA2);
        mvc.perform(post(V1_DIFF_LEFT, id).contentType(APPLICATION_JSON).content(toJson(leftData)));
        mvc.perform(post(V1_DIFF_RIGHT, id).contentType(APPLICATION_JSON).content(toJson(rightData)));

        mvc.perform(get(V1_DIFF, id).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.leftDataSize").value(leftData.getData().length()))
                .andExpect(jsonPath("$.rightDataSize").value(rightData.getData().length()));
    }

    @Test
    public void whenLeftAndRightDataWithSameSizeButDiffData_thenGetDiff() throws Exception {
        long id = 5;
        LeftData leftData = new LeftData(id, ENCODED_DATA_LEFT);
        RightData rightData = new RightData(id, ENCODED_DATA_RIGHT);
        mvc.perform(post(V1_DIFF_LEFT, id).contentType(APPLICATION_JSON).content(toJson(leftData)));
        mvc.perform(post(V1_DIFF_RIGHT, id).contentType(APPLICATION_JSON).content(toJson(rightData)));

        mvc.perform(get(V1_DIFF, id).accept(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.matches.length()").value(1))
                .andExpect(jsonPath("$.leftMismatches.length()").value(2))
                .andExpect(jsonPath("$.rightMismatches.length()").value(2));
    }

    @Test
    public void whenLeftDataIsSaveButRightIsNot_thenGetDiff_expectNotFound() throws Exception {
        long id = 6;
        LeftData leftData = new LeftData(id, ENCODED_DATA1);
        mvc.perform(post(V1_DIFF_LEFT, id).contentType(APPLICATION_JSON).content(toJson(leftData)));

        mvc.perform(get(V1_DIFF, id).accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenRightDataIsSaveButLeftIsNot_thenGetDiff_expectNotFound() throws Exception {
        long id = 7;
        RightData rightData = new RightData(id, ENCODED_DATA1);
        mvc.perform(post(V1_DIFF_RIGHT, id).contentType(APPLICATION_JSON).content(toJson(rightData)));

        mvc.perform(get(V1_DIFF, id).accept(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private String toJson(EncodedData invalidLeftData) throws JsonProcessingException {
        return objectMapper.writeValueAsString(invalidLeftData);
    }
}
